package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.InventoryStockRequest;
import port.sm.erp.dto.InventoryStockResponse;
import port.sm.erp.service.InventoryStockService;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class InventoryStockController {

    private final InventoryStockService stockService;

    @GetMapping
    public ResponseEntity<Page<InventoryStockResponse>> list(
            Pageable pageable,
            @RequestParam(required = false) String q
    ) {
        return ResponseEntity.ok(stockService.list(pageable, q));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryStockResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.get(id));
    }

    @PostMapping
    public ResponseEntity<InventoryStockResponse> create(@RequestBody InventoryStockRequest req) {
        return ResponseEntity.ok(stockService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryStockResponse> update(
            @PathVariable Long id,
            @RequestBody InventoryStockRequest req
    ) {
        return ResponseEntity.ok(stockService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockService.delete(id);
        return ResponseEntity.noContent().build();
    }
}