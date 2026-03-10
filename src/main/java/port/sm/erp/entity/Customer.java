package port.sm.erp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String customerCode;

    @Column(length = 200, nullable = false)
    private String customerName;

    @Column(length = 200)
    private String ceoName;

    @Column(length = 200)
    private String phone;

    @Column(length = 200)
    private String email;

    @Column(length = 200)
    private String address;

    @Column(length = 200)
    private String detailAddress;

    @Column(length = 200)
    private String remark;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerType customerType;
}