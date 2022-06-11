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
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer id;

    @Column(name = "book_ISBN")
    private String ISBN;

    @Column(name = "book_title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @Column(name = "number_of_pages")
    private int numberOfPages;

    @Column(name = "book_publisher")
    private String publisher;

    @Column(name = "book_publishing_year")
    private int publishingYear;

    @Column(name = "book_genre")
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @OneToMany(mappedBy = "ofBook")
    private List<Exemplar> exemplars;

    @Column(name="book_deleted")
    private Boolean deleted;
}

