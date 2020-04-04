package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;

@Entity
public class CarShow extends Event {
	
	private String make;

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}
	
	

}
