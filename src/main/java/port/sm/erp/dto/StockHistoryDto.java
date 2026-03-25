package port.sm.erp.dto;

import lombok.*;
import port.sm.erp.entity.StockHistory;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockHistoryDto {

    private Long id;
    private Long itemId;
    private String itemCode;
    private String itemName;
    private String changeType;
    private Integer changeQty;
    private Integer beforeQty;
    private Integer afterQty;
    private String refNo;
    private String remark;
    private LocalDateTime changedAt;

    public static StockHistoryDto fromEntity(StockHistory entity) {
        return StockHistoryDto.builder()
                .id(entity.getId())
                .itemId(entity.getItemId())
                .itemCode(entity.getItemCode())
                .itemName(entity.getItemName())
                .changeType(entity.getChangeType())
                .changeQty(entity.getChangeQty())
                .beforeQty(entity.getBeforeQty())
                .afterQty(entity.getAfterQty())
                .refNo(entity.getRefNo())
                .remark(entity.getRemark())
                .changedAt(entity.getChangedAt())
                .build();
    }

    public StockHistory toEntity() {
        return StockHistory.builder()
                .id(this.id)
                .itemId(this.itemId)
                .itemCode(this.itemCode)
                .itemName(this.itemName)
                .changeType(this.changeType)
                .changeQty(this.changeQty)
                .beforeQty(this.beforeQty)
                .afterQty(this.afterQty)
                .refNo(this.refNo)
                .remark(this.remark)
                .changedAt(this.changedAt)
                .build();
    }
}