package com.kakaobank.searchapi.service;

import com.kakaobank.searchapi.SortTypeEnum;
import com.kakaobank.searchapi.dto.KakaoSearchResult;
import com.kakaobank.searchapi.dto.NaverSearchResult;
import com.kakaobank.searchapi.dto.SearchResult;
import com.kakaobank.searchapi.dto.SearchSummaryResult;
import com.kakaobank.searchapi.entity.SearchSummary;
import com.kakaobank.searchapi.repository.SearchSummaryRepository;
import com.kakaobank.searchapi.repository.httpclient.KakaoSearchHttpClient;
import com.kakaobank.searchapi.repository.httpclient.NaverSearchHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final KakaoSearchHttpClient kakaoSearchHttpClient;
    private final NaverSearchHttpClient naverSearchHttpClient;
    private final SearchSummaryRepository searchSummaryRepository;
    private final CacheManager cacheManager;

    private final Map<String, Long> concurrentHashMap = new ConcurrentHashMap<>();
    private final Map<String, Long> map = new HashMap<>();

    @Cacheable(value = "keyword", key = "#keyword")
    public Page<SearchResult> searchBlogByQuery(String keyword, String blogUrl, SortTypeEnum sortType, Pageable pageable) {
        String specificBlogTargetQuery = Strings.EMPTY;

        if (!ObjectUtils.isEmpty(blogUrl)) {
            specificBlogTargetQuery = String.format("%s %s", blogUrl, keyword);
        }

        storeDefaultCountInCache(keyword);
        long count = storeSearchKeywordCountInCache(keyword);
        storeSearchKeyWordCountInDB(keyword, count);

        List<SearchResult> searchResults = new ArrayList<>();

        String sort =  (sortType == null) ? SortTypeEnum.ACCURACY.getKakaoType() : sortType.getKakaoType();
        try {
            KakaoSearchResult kakaoSearchResult = kakaoSearchHttpClient.searchBlogByQuery(keyword, sort, pageable.getPageNumber(), pageable.getPageSize());
            for (KakaoSearchResult.Document document : kakaoSearchResult.getDocuments()) {
                searchResults.add(kakaoSearchResult.convertSearchResult(document));
            }

            return new PageImpl<>(searchResults, pageable, kakaoSearchResult.getMeta().getTotalCount());

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            sort =  (sortType == null) ? SortTypeEnum.ACCURACY.getNaverType() : sortType.getNaverType();
            NaverSearchResult naverSearchResult = naverSearchHttpClient.searchBlogByQuery(keyword, sort, pageable.getPageNumber(), pageable.getPageSize());

            for (NaverSearchResult.Item item : naverSearchResult.getItems()) {
                searchResults.add(naverSearchResult.convertSearchResult(item));
            }

            return new PageImpl<>(searchResults, pageable, naverSearchResult.getTotal());
        }
    }

    private void storeDefaultCountInCache(String keyword) {
        if (ObjectUtils.isEmpty(concurrentHashMap.get(keyword))) {
            SearchSummary summary = searchSummaryRepository.findById(keyword).orElse(null);

            if (ObjectUtils.isEmpty(summary)) {
                concurrentHashMap.putIfAbsent(keyword, 0L);
            } else {
                concurrentHashMap.putIfAbsent(keyword, summary.getCount());
            }
        }
    }

    private Long storeSearchKeywordCountInCache(String keyword) {
        return concurrentHashMap.merge(keyword, 1L, Long::sum);
    }

    @Transactional
    public void storeSearchKeyWordCountInDB(String keyword, long count) {
        SearchSummary summary = new SearchSummary();
        summary.setKeyword(keyword);
        summary.setCount(count);
        searchSummaryRepository.save(summary);
    }


    @Cacheable(cacheNames = "trends")
    @Transactional(readOnly = true)
    public List<SearchSummaryResult> findSearchTrends() {

        List<SearchSummary> trends = searchSummaryRepository.findTop10ByOrderByCountDesc();

        return trends.stream().map(summary -> SearchSummaryResult.builder()
                .keyword(summary.getKeyword())
                .count(summary.getCount())
                .build()).collect(Collectors.toList());
    }
}
