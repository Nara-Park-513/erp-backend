package port.sm.erp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JournalLineSaveRequest {
    private String accountCode;
    private String accountName;
    private String dcType;
    private Long amount;
    private String lineRemark;
}