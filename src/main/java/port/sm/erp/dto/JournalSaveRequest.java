package port.sm.erp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class JournalSaveRequest {
    private String journalNo;
    private LocalDate journalDate;
    private Long customerId;
    private String remark;
    private String status;
    private List<JournalLineSaveRequest> lines;
}