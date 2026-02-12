package port.sm.erp.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "PL_ACCOUNT_MAP",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_PL_ACCT", columnNames = {"ACCOUNT_CODE"})
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PlAccountMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCOUNT_CODE", nullable = false, length = 50)
    private String accountCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "PL_ITEM", nullable = false, length = 30)
    private PlItem plItem;

    @Column(name = "REMARK", length = 200)
    private String remark;
}