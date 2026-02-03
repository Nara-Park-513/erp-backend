package port.sm.erp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "journals")
@Getter
@Setter
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate journalDate;
    @OneToMany(mappedBy = "journal", cascade = CascadeType.ALL)
    private List<JournalLine> lines = new ArrayList<>();
}
