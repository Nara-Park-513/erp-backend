package port.sm.erp.entity;

import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "INV_STOCK_MOVEMENT",
        indexes = {
                @Index(name = "IDX_STK_MV_ITEM", columnList = "ITEM_ID"),
                @Index(name = "IDX_STK_MV_AT", columnList = "MOVED_AT"),
                @Index(name = "IDX_STK_MV_REF", columnList = "REF_TYPE, REF_ID")
        }
)
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    @Enumerated(EnumType.STRING)
    @Column(name = "MOVE_TYPE", nullable = false, length = 20)
    private StockTxnType moveType;

    @Column(name = "QTY", nullable = false)
    private Long qty;

    @Column(name = "BEFORE_QTY", nullable = false, columnDefinition = "BIGINT" )
    private Long beforeQty = 0L;

    @Column(name = "AFTER_QTY", nullable = false, columnDefinition = "BIGINT")
    private Long afterQty = 0L;

    @Column(name = "MOVED_AT", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime movedAt;

    @Column(name = "REF_TYPE", length = 30)
    private String refType;

    @Column(name = "REF_ID", columnDefinition = "BIGINT")
    private Long refId;

    //작업자 기존 멤버 FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "MEMBER_ID")
    private Member user;

    @Column(name = "REMARK", length = 500)
    private String remark;
}
