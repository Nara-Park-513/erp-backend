package port.sm.erp.entity;

import javax.persistence.*;

import lombok.*;

import java.math.*;

@Entity
@Table(
    name = "inv_item", // MariaDB는 소문자 권장
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_inv_item_code", columnNames = "item_code")
    },
    indexes = {
        @Index(name = "idx_inv_item_name", columnList = "item_name"),
        @Index(name = "idx_inv_item_group", columnList = "item_group"),
        @Index(name = "idx_inv_item_barcode", columnList = "item_barcode"), // 컬럼명 일치시킴
        @Index(name = "idx_inv_item_use_yn", columnList = "use_yn")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MariaDB는 IDENTITY(Auto_Increment)가 효율적입니다.
    @Column(name="id")
    private Long id;
    
    @Column(name="item_code", nullable = false, length=50)
    private String itemCode;
    
    @Column(name="item_name", nullable = false, length=200)
    private String itemName;
    
    @Column(name="item_group", length=100)
    private String itemGroup;
    
    @Column(name="spec", length=200)
    private String spec;
    
    @Column(name="spec_mode", length=30)
    private String specMode;
    
    @Column(name="unit", length=30)
    private String unit;
    
    // ★ 수정된 부분: BARCORD -> ITEM_BARCODE (인덱스 설정과 이름 맞춤)
    @Column(name="item_barcode", length=100) 
    private String barcode;
    
    @Column(name="process", length=100)
    private String process;
    
    @Enumerated(EnumType.STRING)
    @Column(name="item_type", nullable=false, length=30)
    private ItemType itemType;
    
    @Column(name="is_set_yn", nullable = false, length=1)
    @Builder.Default
    private String isSetYn = "N";
    
    @Column(name="in_price", precision=18, scale=2)
    private BigDecimal inPrice;
    
    @Column(name="in_vat_included_yn", nullable = false, length=1)
    @Builder.Default
    private String inVatIncludedYn = "N";
    
    @Column(name="image_url", length=500)
    private String imageUrl;
    
    @Column(name="use_yn", nullable=false, length=1)
    @Builder.Default
    private String useYn = "Y";
    
    @Column(name="out_price", precision=18, scale=2)
    private BigDecimal outPrice;

    @Column(name="out_vat_included_yn", nullable=false, length=1)
    @Builder.Default
    private String outVatIncludedYn = "N";

}

/*@Entity
@Table(
name = "INV_ITEM",
uniqueConstraints = {
@UniqueConstraint(name = "UK_INV_ITEM_CODE", columnNames = "ITEM_CODE")
},indexes = {
@Index(name = "IDX_INV_ITEM_NAME", columnList = "ITEM_NAME"),
@Index(name = "IDX_INV_ITEM_GROUP", columnList = "ITEM_GROUP"),
@Index(name = "IDX_INV_ITEM_BARCODE", columnList = "ITEM_BARCODE"),
@Index(name = "IDX_INV_ITEM_USE_YN", columnList = "USE_YN")
}
)
@Getter
@Setter
@NoArgsConstructor //파라미터가 없는 생성자
@AllArgsConstructor //밑에 항목을 한줄이라도 안 적으면 빨간 줄이 뜸
@Builder
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INV_ITEM")
	@SequenceGenerator(
			name = "SEQ_INV_ITEM",
			sequenceName = "SEQ_INV_ITEM",
			allocationSize = 1
			)
	@Column(name="ID")
	private Long id;
	
	@Column(name="ITEM_CODE", nullable = false, length=50)
	private String itemCode;
	
	@Column(name="ITEM_NAME", nullable = false, length=200)
	private String itemName;
	
	@Column(name="ITEM_GROUP", length=100)
	private String itemGroup;
	
	@Column(name="SPEC", length=200)
	private String spec;
	
	@Column(name="SPEC_MODE", length=30)
	private String specMode;
	
	@Column(name="UNIT", length=30)
	private String unit;
	
	@Column(name="BARCORD", length=100)
	private String barcode;
	
	@Column(name="PROCESS", length=100)
	private String process;
	
	@Enumerated(EnumType.STRING)
	@Column(name="ITEM_TYPE", nullable=false, length=30)
	private ItemType itemType;
	
	//오라클에서는 boolean 컬럼이 없어서 보통 CHAR(1) Y/N으로 저장한다
	//오라클 Y/N
	@Column(name="IS_SET_YN", nullable = false, length=1)
	@Builder.Default
	private String isSetYn = "N";
	
	@Column(name="IN_PRICE", precision=18, scale=2)
	private BigDecimal inPrice;
	
	@Column(name="IN_VAT_INCLUDED_YN", nullable = false, length=1)
	@Builder.Default
	private String outVatIncludedYn = "N";
	
	@Column(name="IMAGE_URL", length=500)
	private String imageUrl;
	
	//사용여부
	@Column(name="USE_YN", nullable=false, length=1)
	@Builder.Default
	private String useYN = "Y";
}*/
