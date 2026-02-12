package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.ApprovalDocRequestDTO;
import port.sm.erp.dto.ApprovalDocResponseDTO;
import port.sm.erp.entity.ApprovalDoc;
import port.sm.erp.entity.ApprovalStatus;
import port.sm.erp.entity.Member;
import port.sm.erp.repository.ApprovalDocRepository;
import port.sm.erp.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ApprovalDocService {
    private final ApprovalDocRepository approvalDocRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<ApprovalDocResponseDTO> list(){
        return approvalDocRepository.findList().stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ApprovalDocResponseDTO> myDrafts(Long drafterId){
        return approvalDocRepository.findMyDrafts(drafterId).stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ApprovalDocResponseDTO detail(Long id){
        ApprovalDoc d = approvalDocRepository.findDetail(id);
        if (d == null) throw new IllegalArgumentException("기안 없음: " +id);
        return toResponse(d);
    }

    public ApprovalDocResponseDTO create(ApprovalDocRequestDTO dto){
        ApprovalDoc d = new ApprovalDoc();
        applyDto(d, dto);
        ApprovalDoc saved = approvalDocRepository.save(d);
        return toResponse(
                approvalDocRepository.findById(saved.getId())
                        .orElseThrow(() -> new IllegalArgumentException("기안 없음"))
        );
    }

    public ApprovalDocResponseDTO update(Long id, ApprovalDocRequestDTO dto){
        ApprovalDoc d = approvalDocRepository.findDetail(id);
        if (d == null) throw new IllegalArgumentException("기안없음:" +id);
        applyDto(d, dto);
        ApprovalDoc saved = approvalDocRepository.save(d);
        return toResponse(
                approvalDocRepository.findById(saved.getId())
                        .orElseThrow(() -> new IllegalArgumentException("기안 없음"))
        );
    }

    public void delete(Long id){
        approvalDocRepository.deleteById(id);
    }

    private void applyDto(ApprovalDoc d, ApprovalDocRequestDTO dto){
        d.setTitle(requiredText(dto.getTitle(), "title"));
        d.setContent(dto.getContent());
        // drafter/approver
        if (dto.getDrafterId() == null) throw new IllegalArgumentException("drafterId 필수");
        if (dto.getApproverId() == null) throw new IllegalArgumentException("approverId 필수");

        Member drafter = memberRepository.findById(dto.getDrafterId())
                .orElseThrow(() -> new IllegalArgumentException("기안자(Member) 없음: " + dto.getDrafterId()));
        Member approver = memberRepository.findById(dto.getApproverId())
                .orElseThrow(() -> new IllegalArgumentException("결재자(Member) 없음: " + dto.getApproverId()));

        d.setDrafter(drafter);
        d.setApprover(approver);

        // createdAt/updatedAt 안전 보정 (엔티티 @PrePersist/@PreUpdate 있어도 OK)
        LocalDateTime now = LocalDateTime.now();
        if (d.getCreatedAt() == null) d.setCreatedAt(now);
        d.setUpdatedAt(now);
    }

    private ApprovalDocResponseDTO toResponse(ApprovalDoc d){
        return ApprovalDocResponseDTO.builder()
                .id(d.getId())
                .draftDate(d.getDraftDate() != null ? d.getDraftDate().toString() : null)
                .title(d.getTitle())
                .content(d.getContent())
                .status(d.getStatus() != null ? d.getStatus().name() : null)

                .drafterId(d.getDrafter() != null ? d.getDrafter().getId() : null)
                .drafterName(d.getDrafter() != null ? safeMemberName(d.getDrafter()) : null)

                .approverId(d.getApprover() != null ? d.getApprover().getId() : null)
                .approverName(d.getApprover() != null ? safeMemberName(d.getApprover()) : null)

                .createdAt(d.getCreatedAt() != null ? d.getCreatedAt().toString() : null)
                .updatedAt(d.getUpdatedAt() != null ? d.getUpdatedAt().toString() : null)
                .build();
    }

    private String safeMemberName(Member m){
        try { return (String) Member.class.getMethod("getName").invoke(m); } catch (Exception ignored) {}
        try { return (String) Member.class.getMethod("getUsername").invoke(m); } catch (Exception ignored) {}
        try { return (String) Member.class.getMethod("getMemberName").invoke(m); } catch (Exception ignored) {}
        return String.valueOf(m.getId());
    }

    private static String requiredText(String v, String field){
        if (v == null) throw new IllegalArgumentException(field + " 필수");
        String s = v.trim();
        if (s.isEmpty()) throw new IllegalArgumentException(field + " 필수");
        return s;
    }

    private static ApprovalStatus parseStatus(String v){
        if (v == null || v.trim().isEmpty()) return null;
        return ApprovalStatus.valueOf(v.trim());
    }
}
