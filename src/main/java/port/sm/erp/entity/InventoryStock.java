package port.sm.erp.entity;

import javax.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "INV_STOCK", uniqueConstraints = @UniqueConstraint(name = "UK_INV_STOCK_ITEM", columnNames = "ITEM_ID"), indexes = @Index(name = "IDX_INV_STOCK_ITEM", columnList = "ITEM_ID"))
//ITEM_ID 중복금지 조회/조인 할 때 속도가 빨라지기 위해서

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStock {
    @Id //PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    //버전 (동시성 제어)
    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    //아이템 연관관계
    @ManyToOne(fetch = FetchType.LAZY) //재고 n개 아이템 1개
    //기본적으로 성능을 위해 지연 로딩, 반대는 즉시 로딩(FetchType.EAGER)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    //수량 컬럼
    @Min(0)
    @Column(name = "ON_HAND_QTY", nullable = false)
    private Long onHandQty = 0L; //현재 보유 수량

    @Min(0)
    @Column(name = "RESERVED_QTY", nullable = false)
    private Long reservedQty = 0L;

    @Min(0)
    @Column(name = "AVAILABLE_QTY", nullable = false)
    private Long availableQty = 0L;

    @Min(0)
    @Column(name = "SAFETY_QTY", nullable = false)
    private Long safetyQty = 0L;

    //이동일자
    @Column(name = "LAST_MOVED_AT")
    private java.time.LocalDate lastMovedAt;

    //검사필드
    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    //마지막 수정 시간 자동 갱신
    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updateAt;

    //자동계산 메서드
    @PrePersist
    @PreUpdate
    private void syncAvailableQty(){
        long onHand = (onHandQty == null) ? 0L : onHandQty;
        long reserved = (reservedQty == null) ? 0L : reservedQty;
        long v = onHand - reserved;
        this.availableQty = Math.max(0L, v);
    }
}
