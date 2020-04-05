package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Bitcoin {
	
	private String userID;

	@Id
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	private int amount;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
//	private Person person;
//
//	@ManyToOne(optional = false)
//	public Person getPerson() {
//		return person;
//	}
//
//	public void setPerson(Person person) {
//		this.person = person;
//	}
	
//	private Registration registration;
//
//	@OneToOne
//	public Registration getRegistration() {
//		return registration;
//	}
//
//	public void setRegistration(Registration registration) {
//		this.registration = registration;
//	}
	
	
	

}
