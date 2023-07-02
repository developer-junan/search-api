package com.kakaobank.searchapi.repository;

import com.kakaobank.searchapi.entity.SearchSummary;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SearchSummaryRepositoryTest {

    @Autowired
    private SearchSummaryRepository searchSummaryRepository;

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 10; i ++) {
            SearchSummary summary = new SearchSummary();

            summary.setKeyword(String.format("%d", i));
            summary.setCount(i);
            searchSummaryRepository.save(summary);
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("상위 10개 검색어 조회")
    void findTop10ByOrderByCountDesc() {
        List<SearchSummary> summaries = searchSummaryRepository.findTop10ByOrderByCountDesc();
        Assertions.assertEquals(10, summaries.size());
    }
}