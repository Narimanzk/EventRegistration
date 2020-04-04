package ca.mcgill.ecse321.eventregistration.model;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


@Entity
@DiscriminatorValue("Organizer")
public class Organizer extends Person {
	
	@OneToMany(mappedBy = "name")
	private Set<Organizer> organizes;

	public Set<Organizer> getOrganizes() {
		return organizes;
	}

	public void setOrganizes(Set<Organizer> organizes) {
		this.organizes = organizes;
	}



}
