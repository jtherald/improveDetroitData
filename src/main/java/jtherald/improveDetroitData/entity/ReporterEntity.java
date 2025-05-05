package jtherald.improveDetroitData.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.Random;

@Entity
@Data
@Table(name = "reporter")
public class ReporterEntity {
    @Id
    @Column(name = "id")
    Integer id;
    String name;
    String role;

    /**
     * there is at least one reporter with a null id
     * we need the id to be a valid int because it is the primary key
     * ID 0 and larger are valid ids, so generate a random negative number
     */
    @PrePersist
    protected void onCreate() {
        if (Objects.isNull(this.id)) {
            Random r = new Random();
            this.id = r.nextInt(-9999999, -1);
        }
    }
}
