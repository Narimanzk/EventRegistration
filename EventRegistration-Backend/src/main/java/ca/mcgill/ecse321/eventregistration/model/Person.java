package ca.mcgill.ecse321.eventregistration.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
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
    
    
//	private Set<Bitcoin> bitcoins;
//
//	@OneToMany(targetEntity=Bitcoin.class, mappedBy="person", fetch=FetchType.EAGER)
//	public Set<Bitcoin> getBitcoins() {
//		return bitcoins;
//	}
//	public void setBitcoins(Set<Bitcoin> bitcoins) {
//		this.bitcoins = bitcoins;
//	}
}
