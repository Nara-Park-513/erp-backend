package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.StockHistoryDto;
import port.sm.erp.service.StockHistoryService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stock-history")
@CrossOrigin(origins = "http://localhost:5173")
public class StockHistoryController {

    private final StockHistoryService stockHistoryService;

    @GetMapping
    public ResponseEntity<List<StockHistoryDto>> findAll(
            @RequestParam(required = false) String q
    ) {
        return ResponseEntity.ok(stockHistoryService.findAll(q));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockHistoryDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(stockHistoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<StockHistoryDto> create(@RequestBody StockHistoryDto dto) {
        StockHistoryDto saved = stockHistoryService.create(dto);
        return ResponseEntity
                .created(URI.create("/api/stock-history/" + saved.getId()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockHistoryDto> update(
            @PathVariable Long id,
            @RequestBody StockHistoryDto dto
    ) {
        return ResponseEntity.ok(stockHistoryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockHistoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}