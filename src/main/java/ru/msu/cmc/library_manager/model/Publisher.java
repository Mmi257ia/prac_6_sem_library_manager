package ru.msu.cmc.library_manager.model;

import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "publisher")
public class Publisher implements GenericEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id", nullable = false)
    private Integer id;

    @NonNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Publisher other))
            return false;
        return this.id.equals(other.id) && this.name.equals(other.name);
    }
}
