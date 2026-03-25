package port.sm.erp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import port.sm.erp.dto.SearchResultDto;

@Service
public class SearchService {

    public List<SearchResultDto> searchAll(String keyword) {
        List<SearchResultDto> results = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return results;
        }

        String q = keyword.trim();

        // TODO:
        // 여기서 공지사항, 거래처, 전자결재, 오더, 견적서 등
        // 각 테이블 검색 결과를 results에 추가하면 됨

        // 예시
        // results.add(new SearchResultDto("공지사항", 1L, "3월 시스템 점검 안내", "/notice/1"));

        return results;
    }
}