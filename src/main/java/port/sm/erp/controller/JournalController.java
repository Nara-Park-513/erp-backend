package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.JournalResponseDTO;
import port.sm.erp.dto.JournalSaveRequest;
import port.sm.erp.service.JournalService;

@RestController
@RequestMapping("/api/acc/journals")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JournalController {

    private final JournalService journalService;

    @GetMapping
    public Page<JournalResponseDTO> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String q
    ) {
        return journalService.list(page, size, q);
    }

    @GetMapping("/{id}")
    public JournalResponseDTO get(@PathVariable Long id) {
        return journalService.get(id);
    }

    @PostMapping
    public JournalResponseDTO create(@RequestBody JournalSaveRequest request) {
        return journalService.create(request);
    }

    @PutMapping("/{id}")
    public JournalResponseDTO update(@PathVariable Long id,
                                     @RequestBody JournalSaveRequest request) {
        return journalService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        journalService.delete(id);
    }
}