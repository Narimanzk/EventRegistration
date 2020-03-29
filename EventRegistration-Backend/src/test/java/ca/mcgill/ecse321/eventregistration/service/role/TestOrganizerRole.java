package ca.mcgill.ecse321.eventregistration.service.role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.eventregistration.dao.*;
import ca.mcgill.ecse321.eventregistration.model.*;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOrganizerRole {
    @Autowired
    private EventRegistrationService service;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private OrganizerRepository organizerRepository;

    @After
    public void clearDatabase() {
        // First, we clear registrations to avoid exceptions due to inconsistencies
        registrationRepository.deleteAll();
        // Then we can clear the other tables
        personRepository.deleteAll();
        eventRepository.deleteAll();
        organizerRepository.deleteAll();
    }

    @Test
    public void test_01_CreateOrganizer() {
        try {
            String name = "validname";
            service.createOrganizer(name);
            List<Organizer> organizers = service.getAllOrganizers();
            assertEquals(organizers.size(), 1);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_02_CreateOrganizerWithEmptyName() {
        try {
            String name = "";
            service.createOrganizer(name);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Organizer name cannot be empty!", e.getMessage());
            List<Organizer> organizers = service.getAllOrganizers();
            assertEquals(organizers.size(), 0);
        }
    }

    @Test
    public void test_04_CreateOrganizerDuplicate() {
        try {
            String name = "validname";
            service.createOrganizer(name);
            List<Organizer> organizers = service.getAllOrganizers();
            assertEquals(organizers.size(), 1);
        } catch (IllegalArgumentException e) {
            fail();
        }

        try {
            String name = "validname";
            service.createOrganizer(name);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Organizer has already been created!", e.getMessage());
            List<Organizer> organizers = service.getAllOrganizers();
            assertEquals(organizers.size(), 1);
        }
    }

    @Test
    public void test_05_OrganizesEvent() {
        try {
            Organizer organizer = service.createOrganizer("validname");
            Event event = OrganizerRoleTestData.setupEvent(service, "eventname");
            service.organizesEvent(organizer, event);
            assertEquals(organizer.getOrganizes().size(), 1);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_06_OrganizesEventWithNullOrganizer() {
        try {
            Organizer organizer = null;
            Event event = OrganizerRoleTestData.setupEvent(service, "eventname");
            service.organizesEvent(organizer, event);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Organizer needs to be selected for organizes!", e.getMessage());
        }
    }

    @Test
    public void test_09_OrganizesEventWithNonExsistantEvent() {
        try {
            Organizer organizer = service.createOrganizer("validname");
            Event event = new Event();
            event.setName("concert");
            service.organizesEvent(organizer, event);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Event does not exist!", e.getMessage());
        }
    }

    @Test
    public void test_10_GetAllOrganizers() {
        try {
            service.createOrganizer("validname1");
            service.createOrganizer("validname2");
            List<Organizer> organizers = service.getAllOrganizers();
            assertEquals(organizers.size(), 2);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_11_GetOrganizer() {
        try {
            service.createOrganizer("organizer");
            service.getOrganizer("organizer");
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_12_GetOrganizerWithNullName() {
        try {
            service.getOrganizer(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Person name cannot be empty!", e.getMessage());
        }
    }
}
