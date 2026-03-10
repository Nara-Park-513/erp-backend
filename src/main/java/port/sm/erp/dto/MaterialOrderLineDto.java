package port.sm.erp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialOrderLineDto {

    private Long id;
    private Long itemId;
    private String itemName;
    private Integer qty;
    private Integer price;
    private Integer amount;
    private String remark;
}