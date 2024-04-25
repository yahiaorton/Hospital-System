package hospital_management_system;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

class AppointmentTest {

	private Appointment appointment;
    private Patient patient;
    
    
    @BeforeAll
    public static void setUpAll() throws IOException {
        Appointment.rootFolder = "..\\Hospital-System\\src\\test\\resources\\";
     // Mocking doctor.csv content without available emergency doctors
        String mockDoctorCsv = "Dr. Johnson,Imaging,,Cardiology,,10:00:00;11:00:00";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Appointment.rootFolder + "doctor.csv"))) {
            writer.write(mockDoctorCsv);
        }
    	
    }
    

    @BeforeEach
    public void setUp() {
        appointment = new Appointment();
        patient = new Patient(404, "John Doe", 30, "M", "123 Elm Street", "555-1234",
                "Jane Doe", "555-5678", "Asthma, Hypertension", "High blood pressure");
    }
    
    @Test
    public void testEmergencyAppointment() {

        String result = Appointment.emergencyAppointment(patient);
        
        assertTrue((result.trim()).contains("Emergency appointment registered with: Dr. Jones at "));

    }
    @Test
    public void testNoAvailableEmergencyDoctor() throws IOException {
        // Mocking doctor.csv content without available emergency doctors
        String mockDoctorCsv = "Dr. Johnson,Imaging,,Cardiology,,10:00:00;11:00:00";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Appointment.rootFolder + "doctor.csv"))) {
            writer.write(mockDoctorCsv);
        }

        // Mocking LocalDateTime to a fixed value
        LocalDateTime mockCurrentDate = LocalDateTime.of(2024, 4, 24, 8, 30); // Before the first available time

        String result = Appointment.emergencyAppointment(patient);

        assertEquals("No emergency doctors are available at this moment.", result.trim());

    }
    
    
    @Test
    
    public void testupdateDoctorAvailableTimes() {
    	//Test Data
    	String doctorName = "Dr. Johns";
        String registeredTime = "09:00";
        Appointment.updateDoctorAvailableTimes(doctorName, registeredTime);
        	
        
        List<String> Data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Appointment.rootFolder + "doctor.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Data.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading doctor's data from CSV: " + e.getMessage());
        }
        
        
     // Find the line corresponding to the doctor with the updated times
        String updatedDoctor = Data.stream()
                                      .filter(line -> line.startsWith(doctorName))
                                      .findFirst()
                                      .orElse("");

        // Check if the registeredTime is removed from Dr. John's available times
        assertFalse(updatedDoctor.contains(registeredTime));
        
    }
    
    
    @AfterAll
    public static void cleanAll() {
    	File doctorCsvFile = new File(Appointment.rootFolder + "doctor.csv");
        if (doctorCsvFile.exists()) {
            doctorCsvFile.delete();
        }
        Appointment.rootFolder = "..\\Hospital-System\\src\\main\\resources\\";
    }
    
    	
    

}