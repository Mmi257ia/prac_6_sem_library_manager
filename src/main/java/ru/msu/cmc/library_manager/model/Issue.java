package ru.msu.cmc.library_manager.model;

import lombok.*;

import jakarta.persistence.*;
import java.sql.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "issue")
public class Issue implements GenericEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id", nullable = false)
    private Integer id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "book_id", nullable = false)
    private Book book;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "reader_id", referencedColumnName = "reader_id", nullable = false)
    private Reader reader;

    @NonNull
    @Column(name = "issued", nullable = false)
    private Date issued;

    @Column(name = "returned")
    private Date returned;

    @Column(name = "deadline")
    private Date deadline;
}
