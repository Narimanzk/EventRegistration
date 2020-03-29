package ca.mcgill.ecse321.eventregistration.service.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.eventregistration.dao.*;
import ca.mcgill.ecse321.eventregistration.service.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCarShow {

    @Autowired
    private EventRegistrationService service;

    @Autowired
    private CarShowRepository carShowRepository;

    @After
    public void clearDatabase() {
        carShowRepository.deleteAll();
    }

    @Test
    public void test_01_CreateCarShow() {
        assertEquals(0, service.getAllCarShows().size());

        String name = "Montreal Auto Show";
        String make = "Ferrari";

        Calendar c = Calendar.getInstance();
        c.set(2019, Calendar.JANUARY, 18);
        Date carShowDate = new Date(c.getTimeInMillis());

        LocalTime startTime = LocalTime.parse("09:00");
        LocalTime endTime = LocalTime.parse("18:00");

        try {
            service.createCarShow(name, carShowDate, Time.valueOf(startTime) , Time.valueOf(endTime), make);
        } catch (IllegalArgumentException e) {
            fail();
        }

        checkResultCarShow(name, carShowDate, startTime, endTime, make);
    }

    private void checkResultCarShow(String name, Date carShowDate, LocalTime startTime, LocalTime endTime, String make) {
        assertEquals(0, service.getAllPersons().size());
        assertEquals(1, service.getAllCarShows().size());
        assertEquals(name, service.getAllCarShows().get(0).getName());
        assertEquals(carShowDate.toString(), service.getAllCarShows().get(0).getDate().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        assertEquals(startTime.format(formatter).toString(), service.getAllCarShows().get(0).getStartTime().toString());
        assertEquals(endTime.format(formatter).toString(), service.getAllCarShows().get(0).getEndTime().toString());
        assertEquals(make, service.getAllCarShows().get(0).getMake());
        assertEquals(0, service.getAllRegistrations().size());
    }

    @Test
    public void test_02_CreateCarShowNull() {
        assertEquals(0, service.getAllRegistrations().size());

        String name = null;
        String make = null;
        Date carShowDate = null;
        Time startTime = null;
        Time endTime = null;

        String error = null;
        try {
            service.createCarShow(name, carShowDate, startTime, endTime, make);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // Check error
        System.out.println(error);
        assertTrue(error.contains("Event name cannot be empty!"));
        assertTrue(error.contains("Event date cannot be empty!"));
        assertTrue(error.contains("Event start time cannot be empty!"));
        assertTrue(error.contains("Event end time cannot be empty!"));
        // Check model in memory
        assertEquals(0, service.getAllCarShows().size());
    }

    @Test
    public void test_03_CreateCarShowNameEmpty() {
        assertEquals(0, service.getAllCarShows().size());

        String name = "";
        String make = "Porsche";

        Calendar c = Calendar.getInstance();
        c.set(2019, Calendar.JANUARY, 18);
        Date carShowDate = new Date(c.getTimeInMillis());

        LocalTime startTime = LocalTime.parse("09:00");
        LocalTime endTime = LocalTime.parse("18:00");

        String error = null;
        try {
            service.createCarShow(name, carShowDate, Time.valueOf(startTime), Time.valueOf(endTime), make);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // Check error
        assertEquals("Event name cannot be empty!", error);
        // Check model in memory
        assertEquals(0, service.getAllCarShows().size());
    }

    @Test
    public void test_05_CreateCarShowEndTimeBeforeStartTime() {
        assertEquals(0, service.getAllCarShows().size());

        String name = "Geneva Motor Show";
        String make = "Koenigsegg";

        Calendar c = Calendar.getInstance();
        c.set(2019, Calendar.MARCH, 7);
        Date carShowDate = new Date(c.getTimeInMillis());

        LocalTime startTime = LocalTime.parse("18:00");
        LocalTime endTime = LocalTime.parse("09:00");

        String error = null;
        try {
            service.createCarShow(name, carShowDate, Time.valueOf(startTime), Time.valueOf(endTime), make);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // Check error
        assertEquals("Event end time cannot be before event start time!", error);

        // Check model in memory
        assertEquals(0, service.getAllCarShows().size());
    }

    @Test
    public void test_07_CreateCarShowMakeSpaces() {
        assertEquals(0, service.getAllCarShows().size());

        String name = "LA Auto Show";
        String make = " ";

        Calendar c = Calendar.getInstance();
        c.set(2019, Calendar.NOVEMBER, 22);
        Date carShowDate = new Date(c.getTimeInMillis());

        LocalTime startTime = LocalTime.parse("09:00");
        LocalTime endTime = LocalTime.parse("18:00");

        String error = null;
        try {
            service.createCarShow(name, carShowDate, Time.valueOf(startTime), Time.valueOf(endTime), make);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // Check error
        assertEquals("CarShow make cannot be empty!", error);
        // Check model in memory
        assertEquals(0, service.getAllCarShows().size());
    }
}
