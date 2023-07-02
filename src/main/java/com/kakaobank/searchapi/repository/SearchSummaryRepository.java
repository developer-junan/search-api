package com.kakaobank.searchapi.repository;

import com.kakaobank.searchapi.entity.SearchSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchSummaryRepository extends JpaRepository<SearchSummary, String> {
    List<SearchSummary> findTop10ByOrderByCountDesc();
}
