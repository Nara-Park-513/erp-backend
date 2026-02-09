package port.sm.erp.entity;

import javax.persistence.*;

@Entity
@Table(name = "INV_STOCK", uniqueConstraints = @UniqueConstraint(name = "UK_INV_STOCK_ITEM", columnNames = "ITEM_ID"), indexes = @Index(name = "IDX_INV_STOCK_ITEM", columnList = "ITEM_ID"))
//ITEM_ID 중복금지 조회/조인 할 때 속도가 빨라지기 위해서
public class InventoryStock {
    @Id //PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //재고 n개 아이템 1개
    //기본적으로 성능을 위해 지연 로딩, 반대는 즉시 로딩(FetchType.EAGER)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    //수량 컬럼
    @Column(name = "ON_HAND_QTY", nullable = false)
    private Long onHandQty; //현재 보유 수량

    @Column(name = "RESERVED_QTY", nullable = false)
    private Long reservedQty;

    @Column(name = "AVAILABLE_QTY", nullable = false)
    private Long availableQty;

    @Column(name = "SAFETY_QTY", nullable = false)
    private Long safetyQty;

    @Column(name = "LAST_MOVED_AT")
    private java.time.LocalDate lastMovedAt;

}
