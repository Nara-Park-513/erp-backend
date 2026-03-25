package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.OrderProgressRequest;
import port.sm.erp.dto.OrderProgressResponse;
import port.sm.erp.entity.Member;
import port.sm.erp.entity.OrderProgress;
import port.sm.erp.repository.MemberRepository;
import port.sm.erp.repository.OrderProgressRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderProgressService {

    private static final String ACTIVE = "ACTIVE";
    private static final String DELETED = "DELETED";

    private final OrderProgressRepository orderProgressRepository;
    private final MemberRepository memberRepository;

    /**
     * 등록
     */
    public OrderProgressResponse create(OrderProgressRequest request) {
        OrderProgress entity = new OrderProgress();

        if (request.getOrderNo() == null || request.getOrderNo().isBlank()) {
            entity.setOrderNo(generateOrderNo());
        } else {
            entity.setOrderNo(request.getOrderNo().trim());
        }

        entity.setOrderName(request.getOrderName());
        entity.setProgressText(request.getProgressText());
        entity.setStatus(ACTIVE);

        if (request.getMemberId() != null) {
            Member member = memberRepository.findById(request.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 작성자가 없습니다. memberId=" + request.getMemberId()));
            entity.setMember(member);
        }

        OrderProgress saved = orderProgressRepository.save(entity);
        return toResponse(saved);
    }

    /**
     * 오더번호 생성
     */
    private String generateOrderNo() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        long millis = System.currentTimeMillis() % 100000;
        return String.format("ORD-%s-%05d", date, millis);
    }

    /**
     * 전체 조회 (ACTIVE만)
     */
    @Transactional(readOnly = true)
    public List<OrderProgressResponse> getActiveList() {
        return orderProgressRepository.findByStatus(ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * ID로 단건 조회 (ACTIVE만)
     */
    @Transactional(readOnly = true)
    public OrderProgressResponse getById(Long id) {
        return toResponse(getActiveEntityById(id));
    }

    /**
     * 오더번호로 단건 조회 (ACTIVE만)
     */
    @Transactional(readOnly = true)
    public OrderProgressResponse getByOrderNo(String orderNo) {
        OrderProgress entity = orderProgressRepository.findByOrderNoAndStatus(orderNo, ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("해당 오더번호가 없습니다. orderNo=" + orderNo));
        return toResponse(entity);
    }

    /**
     * 오더명 LIKE 검색 (List)
     */
    @Transactional(readOnly = true)
    public List<OrderProgressResponse> searchByOrderName(String keyword) {
        return orderProgressRepository.findByOrderNameContainingAndStatus(keyword, ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * 오더명 LIKE 검색 (Page)
     */
    @Transactional(readOnly = true)
    public Page<OrderProgressResponse> searchByOrderName(String keyword, Pageable pageable) {
        return orderProgressRepository
                .findByOrderNameContainingAndStatus(keyword, ACTIVE, pageable)
                .map(this::toResponse);
    }

    /**
     * 진행상태 조회
     */
    @Transactional(readOnly = true)
    public List<OrderProgressResponse> getByProgressText(String progressText) {
        return orderProgressRepository.findByProgressTextAndStatus(progressText, ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * 작성자 기준 조회
     */
    @Transactional(readOnly = true)
    public List<OrderProgressResponse> getByMemberId(Long memberId) {
        return orderProgressRepository.findByMember_IdAndStatus(memberId, ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * 수정
     */
    public void update(Long id, OrderProgressRequest request) {
        OrderProgress entity = getActiveEntityById(id);

        if (request.getOrderNo() != null && !request.getOrderNo().isBlank()) {
            entity.setOrderNo(request.getOrderNo().trim());
        }

        if (request.getOrderName() != null) {
            entity.setOrderName(request.getOrderName().trim());
        }

        if (request.getProgressText() != null) {
            entity.setProgressText(request.getProgressText().trim());
        }

        if (request.getMemberId() != null) {
            Member member = memberRepository.findById(request.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 작성자가 없습니다. memberId=" + request.getMemberId()));
            entity.setMember(member);
        }
    }

    /**
     * 삭제 (Soft Delete)
     */
    public void delete(Long id) {
        OrderProgress entity = getActiveEntityById(id);
        entity.setStatus(DELETED);
    }

    @Transactional(readOnly = true)
    protected OrderProgress getActiveEntityById(Long id) {
        return orderProgressRepository.findByIdAndStatus(id, ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("해당 진행정보가 없습니다. id=" + id));
    }

    private OrderProgressResponse toResponse(OrderProgress entity) {
        return OrderProgressResponse.builder()
                .id(entity.getId())
                .orderNo(entity.getOrderNo())
                .orderName(entity.getOrderName())
                .progressText(entity.getProgressText())
                .status(entity.getStatus())
                .memberId(entity.getMember() != null ? entity.getMember().getId() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}