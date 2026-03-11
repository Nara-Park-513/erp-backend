package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.JournalLineResponseDTO;
import port.sm.erp.dto.JournalLineSaveRequest;
import port.sm.erp.dto.JournalResponseDTO;
import port.sm.erp.dto.JournalSaveRequest;
import port.sm.erp.entity.Customer;
import port.sm.erp.entity.DcType;
import port.sm.erp.entity.Journal;
import port.sm.erp.entity.JournalLine;
import port.sm.erp.entity.JournalStatus;
import port.sm.erp.repository.CustomerRepository;
import port.sm.erp.repository.JournalRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JournalService {

    private final JournalRepository journalRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public Page<JournalResponseDTO> list(int page, int size, String q) {
        Page<Journal> result;

        if (q == null || q.trim().isEmpty()) {
            result = journalRepository.findAll(PageRequest.of(page, size));
        } else {
            result = journalRepository.findByJournalNoContainingOrRemarkContaining(
                    q.trim(), q.trim(), PageRequest.of(page, size)
            );
        }

        return result.map(this::toDto);
    }

    @Transactional(readOnly = true)
    public JournalResponseDTO get(Long id) {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("전표가 없습니다. id=" + id));
        return toDto(journal);
    }

    public JournalResponseDTO create(JournalSaveRequest request) {
        Journal journal = new Journal();
        applyRequest(journal, request);
        Journal saved = journalRepository.save(journal);
        return toDto(saved);
    }

    public JournalResponseDTO update(Long id, JournalSaveRequest request) {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("전표가 없습니다. id=" + id));

        journal.getLines().clear();
        applyRequest(journal, request);

        Journal saved = journalRepository.save(journal);
        return toDto(saved);
    }

    public void delete(Long id) {
        journalRepository.deleteById(id);
    }

    private void applyRequest(Journal journal, JournalSaveRequest request) {
        journal.setJournalNo(request.getJournalNo());
        journal.setJournalDate(request.getJournalDate());
        journal.setRemark(request.getRemark());

        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            journal.setStatus(JournalStatus.valueOf(request.getStatus()));
        } else {
            journal.setStatus(JournalStatus.DRAFT);
        }

        if (request.getCustomerId() != null) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("거래처가 없습니다. id=" + request.getCustomerId()));
            journal.setCustomer(customer);
        } else {
            journal.setCustomer(null);
        }

        if (request.getLines() != null) {
            for (JournalLineSaveRequest reqLine : request.getLines()) {
                JournalLine line = new JournalLine();
                line.setAccountCode(reqLine.getAccountCode());
                line.setAccountName(reqLine.getAccountName());
                line.setDcType(DcType.valueOf(reqLine.getDcType()));
                line.setAmount(reqLine.getAmount());
                line.setLineRemark(reqLine.getLineRemark());
                journal.addLine(line);
            }
        }
    }

    private JournalResponseDTO toDto(Journal journal) {
        List<JournalLineResponseDTO> lineDtos = new ArrayList<>();

        if (journal.getLines() != null) {
            for (JournalLine line : journal.getLines()) {
                lineDtos.add(
                        JournalLineResponseDTO.builder()
                                .id(line.getId())
                                .accountCode(line.getAccountCode())
                                .accountName(line.getAccountName())
                                .dcType(line.getDcType() != null ? line.getDcType().name() : null)
                                .amount(line.getAmount())
                                .lineRemark(line.getLineRemark())
                                .build()
                );
            }
        }

        return JournalResponseDTO.builder()
                .id(journal.getId())
                .journalNo(journal.getJournalNo())
                .journalDate(journal.getJournalDate())
                .customerId(journal.getCustomer() != null ? journal.getCustomer().getId() : null)
                .customerName(journal.getCustomer() != null ? journal.getCustomer().getCustomerName() : null)
                .remark(journal.getRemark())
                .status(journal.getStatus() != null ? journal.getStatus().name() : null)
                .lines(lineDtos)
                .build();
    }
}