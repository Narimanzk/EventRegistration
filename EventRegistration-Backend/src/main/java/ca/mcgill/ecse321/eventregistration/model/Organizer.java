package ca.mcgill.ecse321.eventregistration.model;

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity(name = "Organizer")
@Table(name = "organizer")
@Access(AccessType.PROPERTY)
public class Organizer extends Person {
	
	
	private Set<Event> organizes;
	

	@OneToMany(mappedBy="organizer")
	public Set<Event> getOrganizes() {
		return organizes;
	}

	public void setOrganizes(Set<Event> organizes) {
		this.organizes = organizes;
	}


}
