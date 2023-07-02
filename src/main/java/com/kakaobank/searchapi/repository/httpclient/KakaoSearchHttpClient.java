package com.kakaobank.searchapi.repository.httpclient;

import com.kakaobank.searchapi.config.CustomFeignConfig;
import com.kakaobank.searchapi.dto.KakaoSearchResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakao-api", url = "https://dapi.kakao.com/v2/searc11h", configuration = CustomFeignConfig.class)
public interface KakaoSearchHttpClient{

    @GetMapping(value = "/blog", headers = "Authorization=KakaoAK 299471cbf59110f557b8a296b70c56f7")
    KakaoSearchResult searchBlogByQuery(@RequestParam(name = "query") String query,
                                        @RequestParam(name = "sort", required = false) String sort,
                                        @RequestParam(name = "page", required = false) int page,
                                        @RequestParam(name = "page", required = false) int size);
}
