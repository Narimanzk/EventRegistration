package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "CarShow")
@Table(name = "carShow")
@DiscriminatorValue("CarShow")
public class CarShow extends Event {
	
	private String make;

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}
	
	

}
