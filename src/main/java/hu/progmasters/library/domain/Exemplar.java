package hu.progmasters.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exemplar")
public class Exemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exemplar_id")
    private Integer id;

    @Column(name = "exemplar_inventory_number")
    private Integer inventoryNumber;

    @Column(name = "exemplar_condition")
    @Enumerated(EnumType.STRING)
    private Condition condition;

    @Column(name = "exemplar_borrowable")
    private Boolean borrowable;

    @ManyToOne
    @JoinColumn(name = "of_book_id")
    private Book ofBook;

    @OneToMany(mappedBy = "exemplar")
    private List<Borrowing> borrowings;

    @Column(name="exemplar_deleted")
    private Boolean deleted;
}
