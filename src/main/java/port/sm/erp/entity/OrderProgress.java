package port.sm.erp.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "ORDER_PROGRESS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_PROGRESS_ID")
    private Long id;

    @Column(name = "ORDER_NO", nullable = false, length = 50)
    private String orderNo;

    @Column(name = "ORDER_NAME", nullable = false, length = 200)
    private String orderName;

    @Column(name = "PROGRESS_TEXT", length = 500)
    private String progressText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    /**
     * ACTIVE / DELETED
     */
    @Column(name = "STATUS", length = 30)
    private String status;

    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    private Date createdAt;

    @Column(name = "UPDATED_AT", insertable = false, updatable = false)
    private Date updatedAt;
}