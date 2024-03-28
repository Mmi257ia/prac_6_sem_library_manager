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
@Table(name = "book")
public class Book implements GenericEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false)
    private Integer id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;

    @Column(name = "receiving_date")
    private Date receivingDate;

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Book other))
            return false;

        boolean result = this.id.equals(other.id) && this.product.equals(other.product);
        if (this.receivingDate == null)
            result = result && other.receivingDate == null;
        else
            result = result && this.receivingDate.equals(other.receivingDate);
        return result;
    }
}
