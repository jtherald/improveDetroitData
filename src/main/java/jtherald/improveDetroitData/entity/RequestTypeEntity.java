package jtherald.improveDetroitData.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
