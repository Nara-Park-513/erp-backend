package port.sm.erp.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "material_order_line")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "material_order_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MaterialOrder materialOrder;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "price")
    private Integer price;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "remark", length = 300)
    private String remark;
}