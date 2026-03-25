package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.MaterialReceiptDto;
import port.sm.erp.service.MaterialReceiptService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/material-receipts")
@CrossOrigin(origins = "http://localhost:5173")
public class MaterialReceiptController {

    private final MaterialReceiptService materialReceiptService;

    @GetMapping
    public ResponseEntity<List<MaterialReceiptDto>> findAll() {
        return ResponseEntity.ok(materialReceiptService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialReceiptDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(materialReceiptService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MaterialReceiptDto> create(@RequestBody MaterialReceiptDto dto) {
        MaterialReceiptDto saved = materialReceiptService.create(dto);
        return ResponseEntity
                .created(URI.create("/api/material-receipts/" + saved.getId()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialReceiptDto> update(
            @PathVariable Long id,
            @RequestBody MaterialReceiptDto dto
    ) {
        return ResponseEntity.ok(materialReceiptService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialReceiptService.delete(id);
        return ResponseEntity.noContent().build();
    }
}