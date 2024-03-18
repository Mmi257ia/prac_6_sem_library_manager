package ru.msu.cmc.library_manager.model;

import lombok.*;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product implements GenericEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @OneToMany(mappedBy = "product_id")
    @Column(name = "product_id", nullable = false)
    private Integer id;

    @Column(name = "isbn", length = 13)
    private String isbn;

    @NonNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "authors")
    private List<Integer> authors;

    @ManyToOne
    @JoinColumn(name = "publisher_id", referencedColumnName = "publisher_id")
    private Publisher publisher;

    @Column(name = "year_of_publishing")
    private Integer yearOfPublishing;

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Product other))
            return false;

        boolean result = this.id.equals(other.id);
        if (this.isbn == null)
            result = result && other.isbn == null;
        else
            result = result && this.isbn.equals(other.isbn);
        result = result && this.name.equals(other.name);
        if (this.authors == null || other.authors == null)
            result = result && this.authors == null && other.authors == null;
        else
            result = result && new HashSet<>(this.authors).containsAll(other.authors) &&
                    new HashSet<>(other.authors).containsAll(this.authors);
        if (this.yearOfPublishing == null)
            result = result && other.yearOfPublishing == null;
        else
            result = result && this.yearOfPublishing.equals(other.yearOfPublishing);
        return result;
    }
}
