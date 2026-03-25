package port.sm.erp.entity;

import javax.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    @Column(length = 50)
    private String itemCode;

    @Column(length = 100)
    private String itemName;

    @Column(length = 30, nullable = false)
    private String changeType;   // 입고 / 출고 / 조정

    @Column(nullable = false)
    private Integer changeQty;

    @Column(nullable = false)
    private Integer beforeQty;

    @Column(nullable = false)
    private Integer afterQty;

    @Column(length = 50)
    private String refNo;

    @Column(length = 500)
    private String remark;

    @Column(nullable = false)
    private LocalDateTime changedAt;
}