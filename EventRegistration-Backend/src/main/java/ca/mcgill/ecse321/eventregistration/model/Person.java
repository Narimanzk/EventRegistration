package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name="person")
@Inheritance(strategy=InheritanceType.JOINED)
public class Person{
private String name;

    public void setName(String value) {
        this.name = value;
    }
    @Id
    public String getName() {
        return this.name;
}
}
