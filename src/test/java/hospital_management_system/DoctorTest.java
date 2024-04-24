package hospital_management_system;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.time.LocalDate;
import java.util.Arrays;

class DoctorTest {
	private Doctor doc;
	
	@AfterEach
	public void cleanUp() {
        doc = null;
    }

	@Test
    public void testNotifyUpdatedScheduleWithAppointments() {
		doc = new Doctor("Dr. Davis");
        String res = doc.notifyUpdatedSchedule(LocalDate.of(2024, 4, 29));

        // Validate printed messages
        assertTrue(res.contains("Doctor Dr. Davis's updated schedule for today:"));
        assertTrue(res.contains("Patient ID: 6, Patient Name: Susan Clark, Registration Type: Inpatient Admission, Appointment Date: 4/29/2024"));
    }
	
	@Test
    public void testNotifyUpdatedScheduleWithNoAppointments() {
		doc = new Doctor("Dr. John");
		String res = doc.notifyUpdatedSchedule(LocalDate.of(2024, 4, 24));

        assertTrue(res.contains("No appointments for Dr. John today."));
	}
	
	@Test
    public void testToString() {
		doc = new Doctor("Dr. Smith", "09:00-17:00", "Mon-Fri", "Cardiology",
                Arrays.asList(101, 102, 103), Arrays.asList("09:00-10:00", "10:00-11:00", "11:00-12:00"));
		
        String expected = "Doctor{drName='Dr. Smith', workingTime='09:00-17:00', workingDates='Mon-Fri', "
                + "department='Cardiology', appointmentIds=[101, 102, 103], availableTimes=[09:00-10:00, 10:00-11:00, 11:00-12:00]}";
        
        assertEquals(expected, doc.toString());
    }

}
