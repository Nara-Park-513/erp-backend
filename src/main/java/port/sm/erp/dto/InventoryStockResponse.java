package port.sm.erp.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class InventoryStockResponse {

    private Long id;
    private Long itemId;
    private Long onHandQty;
    private Long reservedQty;
    private Long availableQty;
    private Long safetyQty;

    private String itemCode;
    private String itemName;

    private LocalDate lastMovedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}