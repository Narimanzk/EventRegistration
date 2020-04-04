package ca.mcgill.ecse321.eventregistration.model;

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity(name = "Organizer")
@Table(name = "organizer")
@Access(AccessType.PROPERTY)
public class Organizer extends Person {
	
	
	private Set<Organizer> organizers;
	
	@OneToMany(mappedBy = "organizers", cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<Organizer> getOrganizers() {
		return organizers;
	}

	public void setOrganizers(Set<Organizer> organizers) {
		this.organizers = organizers;
	}


}
