package ca.mcgill.ecse321.eventregistration.dto;

import java.util.Collections;
import java.util.List;

public class OrganizerDto {
	
	private String name;
	private List<EventDto> eventsAttended;
	private List<EventDto> organizes;

	
	public OrganizerDto() {
		
	}
	
	@SuppressWarnings("unchecked")
	public OrganizerDto(String name) {
		this(name, Collections.EMPTY_LIST);
	}
	
	public OrganizerDto(String name, List<EventDto> events) {
		this.name = name;
		this.eventsAttended = events;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	public List<EventDto> getEventsAttended() {
		return eventsAttended;
	}

	public void setEventsAttended(List<EventDto> eventsAttended) {
		this.eventsAttended = eventsAttended;
	}

	public List<EventDto> getOrganizes() {
		return organizes;
	}

	public void setOrganizes(List<EventDto> organizes) {
		this.organizes = organizes;
	}
	

}
