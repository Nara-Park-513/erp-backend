package port.sm.erp.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "PL_STATEMENT_LINE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_PL_LINE", columnNames = {"STATEMENT_ID", "PL_ITEM"})
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlStatementLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATEMENT_ID", nullable = false)
    private PlStatement statement;

    @Enumerated(EnumType.STRING)
    @Column(name = "PL_ITEM", nullable = false, length = 30)
    private PlItem plItem;

    @Column(name = "AMOUNT", nullable = false)
    private Long amount;
}