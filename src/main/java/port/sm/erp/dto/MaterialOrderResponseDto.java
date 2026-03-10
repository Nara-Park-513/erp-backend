package port.sm.erp.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialOrderResponseDto {

    private Long id;
    private String orderNo;
    private LocalDate orderDate;
    private Long customerId;
    private String customerName;
    private String remark;
    private String status;
    private Integer totalAmount;
    private List<MaterialOrderLineDto> lines;
}