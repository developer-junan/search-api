package com.kakaobank.searchapi.repository.httpclient;

import com.kakaobank.searchapi.config.CustomFeignConfig;
import com.kakaobank.searchapi.dto.NaverSearchResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naver-api", url = "https://openapi.naver.com/v1/search/blog.json", configuration = CustomFeignConfig.class)
public interface NaverSearchHttpClient{

    @GetMapping(headers = {"X-Naver-Client-Id=T2xNh26GTwFoPe0bid9h", "X-Naver-Client-Secret=0SAwDmVHPaㄴㅁㅇㅁㄴㅇㄴㅁㅇㄴ"})
    NaverSearchResult searchBlogByQuery(@RequestParam(name = "query") String query,
                                        @RequestParam(name = "sort", required = false) String sort,
                                        @RequestParam(name = "display", required = false) int display,
                                        @RequestParam(name = "start", required = false) int start);
}
