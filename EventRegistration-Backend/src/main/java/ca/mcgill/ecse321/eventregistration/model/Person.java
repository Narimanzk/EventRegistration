package ca.mcgill.ecse321.eventregistration.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Person{
private String name;

    public void setName(String value) {
        this.name = value;
    }
    @Id
    public String getName() {
        return this.name;
}
    
    @OneToMany(mappedBy = "person")
	private Set<Bitcoin> bitcoins;

	public Set<Bitcoin> getBitcoins() {
		return bitcoins;
	}
	public void setBitcoins(Set<Bitcoin> bitcoins) {
		this.bitcoins = bitcoins;
	}
    
}
