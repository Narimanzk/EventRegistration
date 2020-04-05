package ca.mcgill.ecse321.eventregistration.service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.eventregistration.dao.*;
import ca.mcgill.ecse321.eventregistration.model.*;

@Service
public class EventRegistrationService {

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private RegistrationRepository registrationRepository;
	@Autowired
	private OrganizerRepository organizerRepository;
	@Autowired
	private CarShowRepository carShowRepository;
	@Autowired
	private BitcoinRepository bitcoinRepository;

	@Transactional
	public Person createPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		} else if (personRepository.existsById(name)) {
			throw new IllegalArgumentException("Person has already been created!");
		}
		Person person = new Person();
		person.setName(name);
		personRepository.save(person);
		return person;
	}


	@Transactional
	public Person getPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		Person person = personRepository.findByName(name);
		return person;
	}

	@Transactional
	public List<Person> getAllPersons() {
		return toList(personRepository.findAll());
	}

	@Transactional
	public Event buildEvent(Event event, String name, Date date, Time startTime, Time endTime) {
		// Input validation
		String error = "";
		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty! ";
		} else if (eventRepository.existsById(name)) {
			throw new IllegalArgumentException("Event has already been created!");
		}
		if (date == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (startTime == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error = error + "Event end time cannot be before event start time!";
		}
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		event.setName(name);
		event.setDate(date);
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		return event;
	}

	@Transactional
	public Event createEvent(String name, Date date, Time startTime, Time endTime) {
		Event event = new Event();
		buildEvent(event, name, date, startTime, endTime);
		eventRepository.save(event);
		return event;
	}

	@Transactional
	public Event getEvent(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Event name cannot be empty!");
		}
		Event event = eventRepository.findByName(name);
		return event;
	}

	// This returns all objects of instance "Event" (Subclasses are filtered out)
	@Transactional
	public List<Event> getAllEvents() {
		return toList(eventRepository.findAll()).stream().filter(e -> e.getClass().equals(Event.class)).collect(Collectors.toList());
	}

	@Transactional
	public Registration register(Person person, Event event) {
		String error = "";
		if (person == null) {
			error = error + "Person needs to be selected for registration! ";
		} else if (!personRepository.existsById(person.getName())) {
			error = error + "Person does not exist! ";
		}
		if (event == null) {
			error = error + "Event needs to be selected for registration!";
		} else if (!eventRepository.existsById(event.getName())) {
			error = error + "Event does not exist!";
		}
		if (registrationRepository.existsByPersonAndEvent(person, event)) {
			error = error + "Person is already registered to this event!";
		}

		error = error.trim();

		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		Registration registration = new Registration();
		registration.setId(person.getName().hashCode() * event.getName().hashCode());
		registration.setPerson(person);
		registration.setEvent(event);

		registrationRepository.save(registration);

		return registration;
	}

	@Transactional
	public List<Registration> getAllRegistrations() {
		return toList(registrationRepository.findAll());
	}

	@Transactional
	public Registration getRegistrationByPersonAndEvent(Person person, Event event) {
		if (person == null || event == null) {
			throw new IllegalArgumentException("Person or Event cannot be null!");
		}

		return registrationRepository.findByPersonAndEvent(person, event);
	}
	@Transactional
	public List<Registration> getRegistrationsForPerson(Person person){
		if(person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		return registrationRepository.findByPerson(person);
	}

	@Transactional
	public List<Registration> getRegistrationsByPerson(Person person) {
		return toList(registrationRepository.findByPerson(person));
	}

	@Transactional
	public List<Event> getEventsAttendedByPerson(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		List<Event> eventsAttendedByPerson = new ArrayList<>();
		for (Registration r : registrationRepository.findByPerson(person)) {
			eventsAttendedByPerson.add(r.getEvent());
		}
		return eventsAttendedByPerson;
	}

	private <T> List<T> toList(Iterable<T> iterable) {
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}

	@Transactional
	public Organizer createOrganizer(String name) {
		String error = "";
		List<Organizer> organizers = getAllOrganizers();
		for(int i=0; i<organizers.size();i++) {
			if((organizers.get(i).getName()).equals(name)) {
				error += "Organizer has already been created!";
				break;
			}
		}
		if(name == null || name.trim().length() == 0) {
			error += "Organizer name cannot be empty!";
		}
		error = error.trim();
		if(error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		Organizer organizer = new Organizer();
		organizer.setName(name);
		organizerRepository.save(organizer);
		return organizer;
	}

	@Transactional
	public List<Organizer> getAllOrganizers() {
		return toList(organizerRepository.findAll());
	}

	@Transactional
	public void organizesEvent(Organizer organizer, Event event) {
		String error = "";
		if(organizer == null) {
			error += "Organizer needs to be selected for organizes!";
		}
		if(eventRepository.findByName(event.getName()) == null) {
			error += "Event does not exist!";
		}
		error = error.trim();
		if(error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		event.setOrganizer(organizer);
		organizer.getOrganizes().add(event);
		organizerRepository.save(organizer);
		eventRepository.save(event);

		
	}

	@Transactional
	public Organizer getOrganizer(String name) {
		String error = "";
		if(name == null || name.trim().length() == 0) {
			error += "Person name cannot be empty!";
		}
		error = error.trim();
		if(error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		return organizerRepository.findByName(name);
		
	}


	public List<CarShow> getAllCarShows() {
		return toList(carShowRepository.findAll());
	}


	public void createCarShow(String name, Date carShowDate, Time startTime, Time endTime, String make) {
		CarShow carShow = new CarShow();
		buildEvent(carShow, name, carShowDate, startTime, endTime);
		String error = "";
		if(make == null || make.trim().length() == 0) {
			error += "CarShow make cannot be empty!";
		}
		error = error.trim();
		if(error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		carShow.setMake(make);
		carShowRepository.save(carShow);
		
	}


	public Bitcoin createBitcoinPay(String userID, int amount) {
		Pattern BITCOIN_PATTERN = Pattern.compile("^\\w{4}-\\d{4}$");
		String error = "";
		if(userID == null || userID.trim().length() == 0 || !BITCOIN_PATTERN.matcher(userID).matches()) {
			error += "User id is null or has wrong format!";
		}
		if (amount < 0) {
			error += "Payment amount cannot be negative!";
		}
		error = error.trim();
		if(error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		
		Bitcoin bitcoin = new Bitcoin();
		bitcoin.setUserID(userID);
		bitcoin.setAmount(amount);
		bitcoinRepository.save(bitcoin);
		return bitcoin;
		
	}


	public void pay(Registration registration, Bitcoin bitcoin) {
		String error = "";
		if(registration == null || bitcoin == null) {
			error += "Registration and payment cannot be null!";
		}
		error = error.trim();
		if(error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		List<Registration> regitrations = getAllRegistrations();
		for(int i=0; i<regitrations.size();i++) {
			if(regitrations.get(i).getId() == registration.getId()) {
				registration.setBitcoin(bitcoin);
				registrationRepository.save(registration);
			}
		}
	}
}