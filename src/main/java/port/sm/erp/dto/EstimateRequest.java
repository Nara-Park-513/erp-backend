package port.sm.erp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EstimateRequest {
    private String estimateNo;
    private LocalDate estimateDate;
    private String customerName;
    private String remark;
    private List<EstimateLineRequest> lines;
}
