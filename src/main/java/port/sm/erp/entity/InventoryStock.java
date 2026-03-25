package port.sm.erp.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "INV_STOCK",
        uniqueConstraints = @UniqueConstraint(name = "UK_INV_STOCK_ITEM", columnNames = "ITEM_ID"),
        indexes = @Index(name = "IDX_INV_STOCK_ITEM", columnList = "ITEM_ID")
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    @Min(0)
    @Column(name = "ON_HAND_QTY", nullable = false)
    @Builder.Default
    private Long onHandQty = 0L;

    @Min(0)
    @Column(name = "RESERVED_QTY", nullable = false)
    @Builder.Default
    private Long reservedQty = 0L;

    @Min(0)
    @Column(name = "AVAILABLE_QTY", nullable = false)
    @Builder.Default
    private Long availableQty = 0L;

    @Min(0)
    @Column(name = "SAFETY_QTY", nullable = false)
    @Builder.Default
    private Long safetyQty = 0L;

    @Column(name = "LAST_MOVED_AT")
    private LocalDate lastMovedAt;

    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    private void syncAvailableQty() {
        long onHand = (onHandQty == null) ? 0L : onHandQty;
        long reserved = (reservedQty == null) ? 0L : reservedQty;
        long value = onHand - reserved;
        this.availableQty = Math.max(0L, value);
    }
}