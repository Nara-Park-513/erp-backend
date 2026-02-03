package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import port.sm.erp.dto.JournalResponse;
import port.sm.erp.dto.JournalSearchRequest;
import port.sm.erp.service.JournalService;

@RestController
@RequestMapping("/api/acc/journals")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JournalController {

    private final JournalService journalService;

    @GetMapping
    public Page<JournalResponse> list (

            JournalSearchRequest req,
            @PageableDefault(size = 20, sort = "journalDate", direction = Sort.Direction.DESC)
    Pageable pageable
    ){
    return journalService.list(req, pageable);
    }
}
