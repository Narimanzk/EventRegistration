package ca.mcgill.ecse321.eventregistration.dto;

import java.sql.Date;
import java.sql.Time;

public class EventDto {

	private String name;
	private Date date;
	private Time startTime;
	private Time endTime;
	private String make;

	public EventDto() {
	}

	public EventDto(String name) {
		this(name, Date.valueOf("1971-01-01"), Time.valueOf("00:00:00"), Time.valueOf("23:59:59"), "--");
	}
	
	public EventDto(String name, String make) {
		this(name, Date.valueOf("1971-01-01"), Time.valueOf("00:00:00"), Time.valueOf("23:59:59"), make);
	}
	
	public EventDto(String name, Date date, Time startTime, Time endTime) {
		this(name, date, startTime, endTime, "");
	}

	public EventDto(String name, Date date, Time startTime, Time endTime, String make) {
		this.name = name;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		if (make==null||make.trim().length() == 0) {
			this.make="--";
			}else {
			this.make = make;
			}
	}
	
	

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getName() {
		return name;
	}

	public Date getDate() {
		return date;
	}

	public Time getStartTime() {
		return startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

}
