package ca.mcgill.ecse321.eventregistration.dto;

import java.sql.Date;
import java.sql.Time;

import ca.mcgill.ecse321.eventregistration.model.Organizer;

public class CarShowDto {
	
	private String name;
	private Date date;
	private Time startTime;
	private Time endTime;
	private String make;
	private Organizer organizer;
	
	
	public CarShowDto() {
		
	}


	public CarShowDto(String name) {
		this(name, Date.valueOf("1971-01-01"), Time.valueOf("00:00:00"), Time.valueOf("23:59:59"), "Various Companies");
	}
	
	public CarShowDto(String name, String make) {
		this(name, Date.valueOf("1971-01-01"), Time.valueOf("00:00:00"), Time.valueOf("23:59:59"),make);
		this.make = make;
	}



	public CarShowDto(String name, Date date, Time startTime, Time endTime, String make) {
		this.name = name;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.make = make;
	}
	

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Time getStartTime() {
		return startTime;
	}


	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}


	public Time getEndTime() {
		return endTime;
	}


	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}


	public String getMake() {
		return make;
	}


	public void setMake(String make) {
		this.make = make;
	}


	public Organizer getOrganizer() {
		return organizer;
	}


	public void setOrganizer(Organizer organizer) {
		this.organizer = organizer;
	}
	
	
	
	

}
