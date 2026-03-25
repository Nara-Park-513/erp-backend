package port.sm.erp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import port.sm.erp.dto.SearchResultDto;
import port.sm.erp.service.SearchService;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public Map<String, Object> search(@RequestParam("q") String keyword) {
        List<SearchResultDto> results = searchService.searchAll(keyword);

        Map<String, Object> response = new HashMap<>();
        response.put("results", results);
        return response;
    }
}