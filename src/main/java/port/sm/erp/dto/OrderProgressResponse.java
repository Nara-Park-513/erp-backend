package port.sm.erp.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

@Getter
@Builder
public class OrderProgressResponse {

    private Long id;
    private String orderNo;
    private String orderName;
    private String progressText;
    private String status;
    private Long memberId;
    private Date createdAt;
    private Date updatedAt;
}