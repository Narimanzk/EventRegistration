package ca.mcgill.ecse321.eventregistration.dto;

public class RegistrationDto {

	private PersonDto person;
	private EventDto event;
	private BitcoinDto bitcoin;
	private Integer id;
	private OrganizerDto organizer;

	public RegistrationDto() {
	}
	
	

	public RegistrationDto(Integer id) {
		this.id = id;
	}



	public RegistrationDto(PersonDto person, EventDto event) {
		this.person = person;
		this.event = event;
	}

	public RegistrationDto(PersonDto person, EventDto event, BitcoinDto bitcoin) {
		this.person = person;
		this.event = event;
		this.bitcoin = bitcoin;
	}
	
	

	public RegistrationDto(OrganizerDto organizer, EventDto event) {
		this.event = event;
		this.organizer = organizer;
	}



	public EventDto getEvent() {
		return event;
	}

	public void setEvent(EventDto event) {
		this.event = event;
	}

	public PersonDto getPerson() {
		return person;
	}

	public void setPerson(PersonDto person) {
		this.person = person;
	}

	public BitcoinDto getBitcoin() {
		return bitcoin;
	}

	public void setBitcoin(BitcoinDto bitcoin) {
		this.bitcoin = bitcoin;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public OrganizerDto getOrganizer() {
		return organizer;
	}



	public void setOrganizer(OrganizerDto organizer) {
		this.organizer = organizer;
	}
	
	
	

}
