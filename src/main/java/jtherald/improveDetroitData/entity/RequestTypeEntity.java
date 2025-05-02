package jtherald.improveDetroitData.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "request_type")
public class RequestTypeEntity {

    @Id
    Integer id;
    String title;
    String organization;
}
