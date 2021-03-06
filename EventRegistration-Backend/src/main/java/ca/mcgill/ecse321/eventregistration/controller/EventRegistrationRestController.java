package ca.mcgill.ecse321.eventregistration.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime, @RequestParam String make)
					throws IllegalArgumentException {
		// @formatter:on
		if (make != null && !make.isEmpty()) {
			CarShow carShow = service.createCarShow(name, date, Time.valueOf(startTime), Time.valueOf(endTime), make);
			return convertToDto(carShow);
		} else {
			Event event = service.createEvent(name, date, Time.valueOf(startTime), Time.valueOf(endTime));
			return convertToDto(event);
		}
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


	// GET Mappings

	@GetMapping(value = { "/events", "/events/" })
	public List<EventDto> getAllEvents() {
		List<EventDto> eventDtos = new ArrayList<>();
		for (Event event : service.getAllEvents()) {
			eventDtos.add(convertToDto(event));
		}
		for (CarShow carShow : service.getAllCarShows()) {
			eventDtos.add(convertToDto(carShow));
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
	public PersonDto createOrganizer(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Organizer organizer = service.createOrganizer(name);
		return convertToDto(organizer);
	}

	@GetMapping(value = { "/organizers", "/organizers/" })
	public List<PersonDto> getAllOrganizers() {
		List<PersonDto> persons = new ArrayList<>();
		for (Organizer organizer : service.getAllOrganizers()) {
			persons.add(convertToDto(organizer));
		}
		return persons;
	}

	@PostMapping(value = { "/events/{name}/{make}", "/events/{name}/{make}" })
	public EventDto createCarShow(@PathVariable("name") String name, @PathVariable("make") String make, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
					throws IllegalArgumentException {
		// @formatter:on
		CarShow carShow = service.createCarShow(name, date, Time.valueOf(startTime), Time.valueOf(endTime), make);
		return convertToDto(carShow);
	}
	
	@PostMapping(value = { "/pay", "/pay/" })
	public BitcoinDto pay(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto, @RequestParam String bitcoin,
			@RequestParam int amount) throws IllegalArgumentException {
		
		if (amount < 0) {
			throw new IllegalArgumentException();
		}
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());
		Registration r;
		if (!service.isRegisteredForEvent(p, e))
			r = service.register(p, e);
		else
			r = service.getRegistrationByPersonAndEvent(p, e);
		
		Bitcoin b;
		if (!service.isBitcoinExist(bitcoin)) {
			b = service.createBitcoinPay(bitcoin, amount);
			service.pay(r, b);
		} else {
			b = service.getBitcoinPay(bitcoin);
			b.setAmount(b.getAmount() + amount);
		}
		
		
		return convertToDto(b);
	}
	
	@GetMapping(value = { "/bitcoins", "/bitcoins/" })
	public List<BitcoinDto> getAllBitcoins() {
		List<BitcoinDto> bitcoinDtos = new ArrayList<>();
		for (Bitcoin bitcoin : service.getAllBitcoins()) {
			bitcoinDtos.add(convertToDto(bitcoin));
		}
		return bitcoinDtos;
	}


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
		personDto.setBitcoins(createBitcoinDtosForPerson(p));
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


	private EventDto convertToDto(CarShow c) {
		if (c == null) {
			throw new IllegalArgumentException("There is no such CarShow!");
		}
		EventDto carShowDto = new EventDto(c.getName(), c.getDate(), c.getStartTime(), c.getEndTime(), c.getMake());
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

	
	private List<BitcoinDto> createBitcoinDtosForPerson(Person p) {
		List<Bitcoin> bitcoinsForPerson = service.getBitcoinsByPerson(p);
		List<BitcoinDto> bitcoins = new ArrayList<BitcoinDto>();
		for (Bitcoin b : bitcoinsForPerson) {
			bitcoins.add(convertToDto(b));
		}
		return bitcoins;
	}


}
