package ru.msu.cmc.library_manager.model;

import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "author")
public class Author implements GenericEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id", nullable = false)
    private Integer id;

    @NonNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;
}
