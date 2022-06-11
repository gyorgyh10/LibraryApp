package hu.progmasters.library.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "borrowing")
public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borowing_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "exemplar_id")
    private Exemplar exemplar;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "borowing_from_date")
    private LocalDate fromDate;

    @Column(name = "borowing_to_date")
    private LocalDate toDate;

    @Column(name = "borowing_active")
    private Boolean active;
}
