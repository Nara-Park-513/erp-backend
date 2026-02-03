package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.JournalLineResponse;
import port.sm.erp.dto.JournalResponse;
import port.sm.erp.dto.JournalSearchRequest;
import port.sm.erp.entity.Journal;
import port.sm.erp.repository.JournalRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JournalService {
    private final JournalRepository journalRepository;
    public Page<JournalResponse> list(JournalSearchRequest req, Pageable pageable
    ) {
        Page<Journal> page = journalRepository.findAll(pageable);
        return page.map(this::toResponse);
    }

    private JournalResponse toResponse(Journal j){
        List<JournalLineResponse> lines = j.getLines().stream()
                .map(l -> new JournalLineResponse(
                        l.getAccountCode(),
                        l.getAccountName(),
                        l.getDcType(),
                        l.getAmount().longValue()
                )).toList();
                return new JournalResponse(
                        j.getId(), j.getJournalDate(), lines
                );
    }
}
