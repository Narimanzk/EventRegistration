package ca.mcgill.ecse321.eventregistration.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity(name = "Organizer")
@Table(name = "organizer")
public class Organizer extends Person {
	
	@OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Organizer> organizers;

	public Set<Organizer> getOrganizers() {
		return organizers;
	}

	public void setOrganizers(Set<Organizer> organizers) {
		this.organizers = organizers;
	}


}
