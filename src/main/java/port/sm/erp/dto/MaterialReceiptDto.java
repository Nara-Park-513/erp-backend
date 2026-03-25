package port.sm.erp.dto;

import lombok.*;
import port.sm.erp.entity.MaterialReceipt;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialReceiptDto {

    private Long id;
    private String receiptNo;
    private LocalDate receiptDate;
    private String orderNo;
    private String itemName;
    private Integer qty;
    private String status;
    private Long supplierId;
    private String supplierName;
    private String warehouse;
    private String remark;

    public static MaterialReceiptDto fromEntity(MaterialReceipt entity) {
        return MaterialReceiptDto.builder()
                .id(entity.getId())
                .receiptNo(entity.getReceiptNo())
                .receiptDate(entity.getReceiptDate())
                .orderNo(entity.getOrderNo())
                .itemName(entity.getItemName())
                .qty(entity.getQty())
                .status(entity.getStatus())
                .supplierId(entity.getSupplierId())
                .supplierName(entity.getSupplierName())
                .warehouse(entity.getWarehouse())
                .remark(entity.getRemark())
                .build();
    }

    public MaterialReceipt toEntity() {
        return MaterialReceipt.builder()
                .id(this.id)
                .receiptNo(this.receiptNo)
                .receiptDate(this.receiptDate)
                .orderNo(this.orderNo)
                .itemName(this.itemName)
                .qty(this.qty)
                .status(this.status)
                .supplierId(this.supplierId)
                .supplierName(this.supplierName)
                .warehouse(this.warehouse)
                .remark(this.remark)
                .build();
    }
}