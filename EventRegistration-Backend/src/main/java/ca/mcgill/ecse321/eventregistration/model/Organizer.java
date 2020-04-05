package ca.mcgill.ecse321.eventregistration.model;

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@DiscriminatorValue("Organizer")
public class Organizer extends Person {
	
	
	private Set<Event> organizes;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToMany(mappedBy="organizer")
	public Set<Event> getOrganizes() {
		return organizes;
	}

	public void setOrganizes(Set<Event> organizes) {
		this.organizes = organizes;
	}


}
