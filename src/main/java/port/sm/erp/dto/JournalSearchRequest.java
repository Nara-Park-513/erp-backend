package port.sm.erp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JournalSearchRequest {
    private LocalDate fromDate;
    private LocalDate toDate;

}
