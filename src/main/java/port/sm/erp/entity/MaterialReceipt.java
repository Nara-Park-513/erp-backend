package port.sm.erp.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "material_receipt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String receiptNo;

    @Column(nullable = false)
    private LocalDate receiptDate;

    @Column(length = 50)
    private String orderNo;

    @Column(nullable = false, length = 100)
    private String itemName;

    @Column(nullable = false)
    private Integer qty;

    @Column(length = 30)
    private String status;

    private Long supplierId;

    @Column(length = 100)
    private String supplierName;

    @Column(length = 100)
    private String warehouse;

    @Column(length = 500)
    private String remark;
}