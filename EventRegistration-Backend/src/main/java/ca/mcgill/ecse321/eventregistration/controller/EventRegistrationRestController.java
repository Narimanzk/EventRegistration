package ca.mcgill.ecse321.eventregistration.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.eventregistration.model.*;
import ca.mcgill.ecse321.eventregistration.dto.*;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@CrossOrigin(origins = "*")
@RestController
public class EventRegistrationRestController {

	@Autowired
	private EventRegistrationService service;

	// POST Mappings

	// @formatter:off
	// Turning off formatter here to ease comprehension of the sample code by
	// keeping the linebreaks
	// Example REST call:
	// http://localhost:8088/persons/John
	@PostMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto createPerson(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Person person = service.createPerson(name);
		return convertToDto(person);
	}


	// @formatter:off
	// Example REST call:
	// http://localhost:8080/events/testevent?date=2013-10-23&startTime=00:00&endTime=23:59
	@PostMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto createEvent(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
					throws IllegalArgumentException {
		// @formatter:on
		Event event = service.createEvent(name, date, Time.valueOf(startTime), Time.valueOf(endTime));
		return convertToDto(event);
	}

	// @formatter:off
	@PostMapping(value = { "/register", "/register/" })
	public RegistrationDto registerPersonForEvent(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// @formatter:on

		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.register(p, e);
		return convertToDto(r, p, e);
	}

	@PostMapping(value = { "/pay", "/pay/" })
	public RegistrationDto pay(@RequestParam(name = "bitcoin") BitcoinDto bDto,
			@RequestParam(name = "personName") PersonDto pDto,
			@RequestParam(name = "eventName") EventDto eDto) throws IllegalArgumentException {
		// @formatter:on
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		return convertToDto(r);
	}


	// GET Mappings

	@GetMapping(value = { "/events", "/events/" })
	public List<EventDto> getAllEvents() {
		List<EventDto> eventDtos = new ArrayList<>();
		for (Event event : service.getAllEvents()) {
			eventDtos.add(convertToDto(event));
		}
		return eventDtos;
	}


	// Example REST call:
	// http://localhost:8088/events/person/JohnDoe
	@GetMapping(value = { "/events/person/{name}", "/events/person/{name}/" })
	public List<EventDto> getEventsOfPerson(@PathVariable("name") PersonDto pDto) {
		Person p = convertToDomainObject(pDto);
		return createAttendedEventDtosForPerson(p);
	}


	@GetMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto getPersonByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getPerson(name));
	}


	@GetMapping(value = { "/registrations", "/registrations/" })
	public RegistrationDto getRegistration(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		return convertToDtoWithoutPerson(r);
	}

	@GetMapping(value = { "/registrations/person/{name}", "/registrations/person/{name}/" })
	public List<RegistrationDto> getRegistrationsForPerson(@PathVariable("name") PersonDto pDto)
			throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());

		return createRegistrationDtosForPerson(p);
	}

	@GetMapping(value = { "/persons", "/persons/" })
	public List<PersonDto> getAllPersons() {
		List<PersonDto> persons = new ArrayList<>();
		for (Person person : service.getAllPersons()) {
			persons.add(convertToDto(person));
		}
		return persons;
	}

	@GetMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto getEventByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getEvent(name));
	}


	@PostMapping(value = { "/organizers/{name}", "/organizers/{name}/" })
	public OrganizerDto createOrganizer(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Organizer organizer = service.createOrganizer(name);
		return convertToDto(organizer);
	}
	
	@PostMapping(value = { "/organizes", "/organizes/" })
	public OrganizerDto organizesEvent(@RequestParam(name = "organizer") OrganizerDto oDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// @formatter:on

		// Both the organizer and the event are identified by their names
		Organizer o = service.getOrganizer(oDto.getName());
		Event e = service.getEvent(eDto.getName());

		o = service.organizesEvent(o, e);
		return convertToDto(o);
	}

	@GetMapping(value = { "/events/organizer/{name}", "/events/organizer/{name}/" })
	public List<EventDto> getEventsOfOrganizer(@PathVariable("name") OrganizerDto oDto) {
		Organizer o = convertToDomainObject(oDto);
		return createOrganizedEventDtosForOrganizer(o);
	}

	@GetMapping(value = { "/organizers/{name}", "/organizers/{name}/" })
	public OrganizerDto getOrganizerByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getOrganizer(name));
	}

	@GetMapping(value = { "/organizers", "/organizers/" })
	public List<OrganizerDto> getAllOrganizers() {
		List<OrganizerDto> organizers = new ArrayList<>();
		for (Organizer organizer : service.getAllOrganizers()) {
			organizers.add(convertToDto(organizer));
		}
		return organizers;
	}

//	@DeleteMapping(value = { "/organizers/{name}" })
//	public void deleteOrganizer(@PathVariable("name") String name) throws IllegalArgumentException{
//		service.deleteOrganizer(name);
//	}

	@PostMapping(value = { "/carShows/{name}", "/carShows/{name}/" })
	public CarShowDto createCarShow(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime, String make)
					throws IllegalArgumentException {
		// @formatter:on
		CarShow carShow = service.createCarShow(name, date, Time.valueOf(startTime), Time.valueOf(endTime), make);
		return convertToDto(carShow);
	}

	@GetMapping(value = { "/carShows", "/carShows/" })
	public List<CarShowDto> getAllCarShows() {
		List<CarShowDto> carShowDtos = new ArrayList<>();
		for (CarShow carShow : service.getAllCarShows()) {
			carShowDtos.add(convertToDto(carShow));
		}
		return carShowDtos;
	}

//	@GetMapping(value = { "/carShows/{name}", "/carShows/{name}/" })
//	public CarShowDto getCarShowByName(@PathVariable("name") String name) throws IllegalArgumentException {
//		return convertToDto(service.getCarShow(name));
//	}

//	@PutMapping(value = { "/carShows"}, consumes = "application/json", produces = "application/json")
//	public CarShowDto updateCarShow(@RequestBody CarShowDto carShow) {
//		CarShow domainCarShow = service.updateCarShow(carShow.getName(), carShow.getDate(),carShow.getStartTime(), carShow.getEndTime(), carShow.getMake());
//		CarShowDto carShowDto = convertToDto(domainCarShow);
//		return carShowDto;
//	}

//	@DeleteMapping(value = { "/carShows/{name}" })
//	public void deleteCarShow(@PathVariable("name") String name) throws IllegalArgumentException{
//		service.deleteCarShow(name);
//	}

	@PostMapping(value = { "/bitcoins/{userID}", "/bitcoins/{userID}/" })
	public BitcoinDto createBitcoinPay(@PathVariable("userID") String userID, @RequestParam int amount) throws IllegalArgumentException {
		// @formatter:on
		Bitcoin bitcoin = service.createBitcoinPay(userID, amount);
		return convertToDto(bitcoin);
	}

//	@PutMapping(value = { "/bitcoins"}, consumes = "application/json", produces = "application/json")
//	public BitcoinDto updateBitcoinPay(@RequestBody BitcoinDto bitcoin) {
//		Bitcoin domainBitcoin = service.updateBitcoinPay(bitcoin.getUserID(), bitcoin.getAmount());
//		BitcoinDto bitcoinDto = convertToDto(domainBitcoin);
//		return bitcoinDto;
//	}

	@GetMapping(value = { "/bitcoins", "/bitcoins/" })
	public List<BitcoinDto> getAllBitcoinPays() {
		List<BitcoinDto> bitcoinDtos = new ArrayList<>();
		for (Bitcoin bitcoin : service.getAllBitcoinPays()) {
			bitcoinDtos.add(convertToDto(bitcoin));
		}
		return bitcoinDtos;
	}

//	@GetMapping(value = { "/bitcoins/{userID}", "/bitcoins/{userID}/" })
//	public BitcoinDto getBitcoinPayByUserID(@PathVariable("userID") String userID) throws IllegalArgumentException {
//		return convertToDto(service.getBitcoinPay(userID));
//	}

//	@DeleteMapping(value = { "/bitcoins/{userID}" })
//	public void deleteBitcoinPay(@PathVariable("userID") String userID) throws IllegalArgumentException{
//		service.deleteBitcoinPay(userID);
//	}

	// Model - DTO conversion methods (not part of the API)

	private EventDto convertToDto(Event e) {
		if (e == null) {
			throw new IllegalArgumentException("There is no such Event!");
		}
		EventDto eventDto = new EventDto(e.getName(), e.getDate(), e.getStartTime(), e.getEndTime());
		//eventDto.setOrganizer(convertToDto(e.getOrganizer()));
		return eventDto;
	}


	private PersonDto convertToDto(Person p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Person!");
		}
		PersonDto personDto = new PersonDto(p.getName());
		personDto.setEventsAttended(createAttendedEventDtosForPerson(p));
		return personDto;
	}


	// DTOs for registrations
	private RegistrationDto convertToDto(Registration r, Person p, Event e) {
		EventDto eDto = convertToDto(e);
		PersonDto pDto = convertToDto(p);
		return new RegistrationDto(pDto, eDto);
	}

	private RegistrationDto convertToDto(Registration r) {
		EventDto eDto = convertToDto(r.getEvent());
		PersonDto pDto = convertToDto(r.getPerson());
		RegistrationDto rDto = new RegistrationDto(pDto, eDto);
		return rDto;
	}

	private OrganizerDto convertToDto(Organizer o) {
		if (o == null) {
			throw new IllegalArgumentException("There is no such Organizer!");
		}
		OrganizerDto organizerDto = new OrganizerDto(o.getName());
		organizerDto.setOrganizes(createOrganizedEventDtosForOrganizer(o));
		organizerDto.setEventsAttended(createAttendedEventDtosForPerson(o));
		return organizerDto;
	}


	private CarShowDto convertToDto(CarShow c) {
		if (c == null) {
			throw new IllegalArgumentException("There is no such CarShow!");
		}
		CarShowDto carShowDto = new CarShowDto(c.getName(), c.getDate(), c.getStartTime(), c.getEndTime(), c.getMake());
		return carShowDto;
	}

	private BitcoinDto convertToDto(Bitcoin b) {
		if (b == null) {
			throw new IllegalArgumentException("There is no such Bitcoin!");
		}
		BitcoinDto bitcoinDto = new BitcoinDto(b.getUserID(), b.getAmount());
		return bitcoinDto;
	}

	// return registration dto without peron object so that we are not repeating
	// data
	private RegistrationDto convertToDtoWithoutPerson(Registration r) {
		RegistrationDto rDto = convertToDto(r);
		rDto.setPerson(null);
		return rDto;
	}

	private Person convertToDomainObject(PersonDto pDto) {
		List<Person> allPersons = service.getAllPersons();
		for (Person person : allPersons) {
			if (person.getName().equals(pDto.getName())) {
				return person;
			}
		}
		return null;
	}

	private Organizer convertToDomainObject(OrganizerDto oDto) {
		List<Organizer> allOrganizers = service.getAllOrganizers();
		for (Organizer organizer : allOrganizers) {
			if (organizer.getName().equals(oDto.getName())) {
				return organizer;
			}
		}
		return null;
	}
	
	private Registration convertToDomainObject(RegistrationDto rDto) {
		List<Registration> allregistrations = service.getAllRegistrations();
		for (Registration registration : allregistrations) {
			if (registration.getId() == rDto.getId()) {
				return registration;
			}
		}
		return null;
	}

	// Other extracted methods (not part of the API)

	private List<EventDto> createAttendedEventDtosForPerson(Person p) {
		List<Event> eventsForPerson = service.getEventsAttendedByPerson(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForPerson) {
			events.add(convertToDto(event));
		}
		return events;
	}


	private List<RegistrationDto> createRegistrationDtosForPerson(Person p) {
		List<Registration> registrationsForPerson = service.getRegistrationsForPerson(p);
		List<RegistrationDto> registrations = new ArrayList<RegistrationDto>();
		for (Registration r : registrationsForPerson) {
			registrations.add(convertToDtoWithoutPerson(r));
		}
		return registrations;
	}


	private List<EventDto> createOrganizedEventDtosForOrganizer(Organizer o) {
		List<Event> eventsForOrganizer = service.getOrganizes(o);
		List<EventDto> events = new ArrayList<>();
		if(o.getOrganizes() != null) {
			for (Event event : eventsForOrganizer) {
				events.add(convertToDto(event));
			}
		}
		return events;
	}


}
