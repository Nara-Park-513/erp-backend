package port.sm.erp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EstimateCreateRequest {
    private String estimateNo;
    private List<EstimateCreateRequest> lines;
}
