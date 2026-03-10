package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.MaterialOrderRequestDto;
import port.sm.erp.dto.MaterialOrderResponseDto;
import port.sm.erp.service.MaterialOrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/material-orders")
public class MaterialOrderController {

    private final MaterialOrderService materialOrderService;

    @GetMapping
    public ResponseEntity<List<MaterialOrderResponseDto>> findAll() {
        return ResponseEntity.ok(materialOrderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialOrderResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(materialOrderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MaterialOrderResponseDto> create(@RequestBody MaterialOrderRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(materialOrderService.create(requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialOrderResponseDto> update(
            @PathVariable Long id,
            @RequestBody MaterialOrderRequestDto requestDto
    ) {
        return ResponseEntity.ok(materialOrderService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}