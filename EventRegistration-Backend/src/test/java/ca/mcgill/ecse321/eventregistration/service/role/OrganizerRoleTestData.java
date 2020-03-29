package ca.mcgill.ecse321.eventregistration.service.role;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;

import ca.mcgill.ecse321.eventregistration.model.*;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

public class OrganizerRoleTestData {
	public static Event setupEvent(EventRegistrationService service, String name) throws IllegalArgumentException{
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date eventDate = new Date(c.getTimeInMillis());
		LocalTime startTime = LocalTime.parse("09:00");
		c.set(2017, Calendar.MARCH, 16, 10, 30, 0);
		LocalTime endTime = LocalTime.parse("10:30");
		return service.createEvent(name, eventDate, Time.valueOf(startTime) , Time.valueOf(endTime));
	}
}
