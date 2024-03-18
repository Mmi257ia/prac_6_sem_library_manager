package ru.msu.cmc.library_manager.model;

import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reader")
public class Reader implements GenericEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @OneToMany(mappedBy = "reader_id")
    @Column(name = "reader_id", nullable = false, length = 100)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private Long phone;

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Reader other))
            return false;

        boolean result = this.id.equals(other.id);
        if (this.name == null)
            result = result && other.name == null;
        else
            result = result && this.name.equals(other.name);
        if (this.address == null)
            result = result && other.address == null;
        else
            result = result && this.address.equals(other.address);
        if (this.phone == null)
            result = result && other.phone == null;
        else
            result = result && this.phone.equals(other.phone);
        return result;
    }
}
