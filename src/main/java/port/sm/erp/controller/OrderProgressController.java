package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.OrderProgressRequest;
import port.sm.erp.dto.OrderProgressResponse;
import port.sm.erp.service.OrderProgressService;

import java.util.List;

@RestController
@RequestMapping("/api/orders/progress")
@RequiredArgsConstructor
public class OrderProgressController {

    private final OrderProgressService orderProgressService;

    /**
     * 등록
     */
    @PostMapping
    public ResponseEntity<OrderProgressResponse> create(@RequestBody OrderProgressRequest request) {
        return ResponseEntity.ok(orderProgressService.create(request));
    }

    /**
     * ID로 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderProgressResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderProgressService.getById(id));
    }

    /**
     * 오더번호로 조회 (단건)
     */
    @GetMapping("/order-no/{orderNo}")
    public ResponseEntity<OrderProgressResponse> getByOrderNo(@PathVariable String orderNo) {
        return ResponseEntity.ok(orderProgressService.getByOrderNo(orderNo));
    }

    /**
     * 오더명 LIKE 검색 (리스트)
     */
    @GetMapping("/search")
    public ResponseEntity<List<OrderProgressResponse>> searchByOrderName(
            @RequestParam String keyword) {
        return ResponseEntity.ok(orderProgressService.searchByOrderName(keyword));
    }

    /**
     * 오더명 LIKE 검색 (페이징)
     */
    @GetMapping("/search/page")
    public ResponseEntity<Page<OrderProgressResponse>> searchByOrderNamePage(
            @RequestParam String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(orderProgressService.searchByOrderName(keyword, pageable));
    }

    /**
     * 업무 진행상태 조회
     * 예: 접수완료, 재고확인중, 출고대기 ...
     */
    @GetMapping("/progress/{progressText}")
    public ResponseEntity<List<OrderProgressResponse>> getByProgressText(
            @PathVariable String progressText) {
        return ResponseEntity.ok(orderProgressService.getByProgressText(progressText));
    }

    /**
     * 작성자 기준 조회
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<OrderProgressResponse>> getByMemberId(
            @PathVariable Long memberId) {
        return ResponseEntity.ok(orderProgressService.getByMemberId(memberId));
    }

    /**
     * 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody OrderProgressRequest request) {

        orderProgressService.update(id, request);
        return ResponseEntity.ok().build();
    }

    /**
     * 삭제 (Soft Delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderProgressService.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 전체 조회 (ACTIVE만)
     */
    @GetMapping
    public ResponseEntity<List<OrderProgressResponse>> getAll() {
        return ResponseEntity.ok(orderProgressService.getActiveList());
    }
}