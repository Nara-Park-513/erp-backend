package port.sm.erp.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EstimateLineRequest {

    private String itemName;
    private Integer qty;
    private BigDecimal price;


}