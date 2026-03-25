package port.sm.erp.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "material_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false, unique = true, length = 100)
    private String orderNo;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Column(name = "remark", length = 500)
    private String remark;

    /**
     * 발주요청 / 발주완료 / 입고대기 / 부분입고 / 입고완료 / 발주취소 / DELETED
     */
    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @OneToMany(mappedBy = "materialOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MaterialOrderLine> lines = new ArrayList<>();

    public void addLine(MaterialOrderLine line) {
        this.lines.add(line);
        line.setMaterialOrder(this);
    }

    public void clearLines() {
        for (MaterialOrderLine line : this.lines) {
            line.setMaterialOrder(null);
        }
        this.lines.clear();
    }
}