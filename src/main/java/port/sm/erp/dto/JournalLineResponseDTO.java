package port.sm.erp.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalLineResponseDTO {
    private Long id;
    private String accountCode;
    private String accountName; // 계정명 (추가)
    private String dcType;
    private Long amount;
    private String lineRemark;

    // 핵심: 거래처 정보를 담을 필드를 추가합니다!
    private Long customerId;      // 거래처 ID
    private String customerName;  // 거래처명
}

/*package port.sm.erp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalLineResponseDTO {

    private Long id;
    private String accountCode;
    private String dcType;
    private Long amount;
    private String lineRemark;
}*/