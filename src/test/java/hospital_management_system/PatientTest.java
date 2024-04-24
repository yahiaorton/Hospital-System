package hospital_management_system;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatientTest {
	final static String rootFolder = "..\\Hospital-System\\src\\main\\resources\\";
	Patient patient;
	static InputStream sysInBackup;
	
	@BeforeAll
	public static void setUpAll() {
		sysInBackup = System.in; // backup System.in to restore it later
		ByteArrayInputStream in = new ByteArrayInputStream("08:15".getBytes());
		System.setIn(in);
	}
	@AfterAll
	public static void cleanUpAll() {
		System.setIn(sysInBackup);
	}
	
	@BeforeEach
    public void setUp() {
        patient = new Patient(404, "John Doe", 30, "M", "123 Elm Street", "555-1234",
                              "Jane Doe", "555-5678", "Asthma, Hypertension", "High blood pressure");
    }
	@AfterEach
	public void cleanUp() {
		deleteFromCSV(patient.patient_id);
		patient = null;
	}
	
	@Test
    public void testRegisterApplicationNonEmergency() {
        String expectedOutput = "Appliction is registerd for current condition.";

        String output = patient.registerApplication(false);

        assertEquals(expectedOutput, output);
    }
    @Test
    public void testRegisterApplicationEmergency() {
        String expectedOutput = "Emergency Appointment has been registred for currenct condition.";

        String output = patient.registerApplication(true);

        assertEquals(expectedOutput, output);
    }
    @Test
    public void testRegisterApplicationNoDepartment() {
        // Set a current_condition that doesn't match any department in the patientCaseType.csv
        patient.current_conditions = "NoMatch";

        String expectedOutput = "This department is not available at the moment.";

        String output = patient.registerApplication(false);

        assertEquals(expectedOutput, output);
    }
	@Test
    public void testGetPreferredDoctor() {
        String expectedPreferredDoctor = "Dr. Smith";

        String preferredDoctor = patient.getPreferredDoctor();

        assertEquals(expectedPreferredDoctor, preferredDoctor);
    }
    @Test
    public void testGetPreferredDoctorNoMatch() {
        // Set a patient_name that doesn't match any entry in the patient_preferred_doctor.csv
        patient.patient_name = "NoMatch";

        String preferredDoctor = patient.getPreferredDoctor();

        assertEquals("No preferred doctor found", preferredDoctor);
    }
	@Test
	public void testGetDepartment() {
        String expectedDepartment = "Cardiology";

        String department = patient.getDepartment();

        assertEquals(expectedDepartment, department);
    }
	@Test
    public void testGetDepartmentNoMatch() {
		patient.current_conditions = "NoMatch";
		
        String department = patient.getDepartment();

        assertNull(department);
    }
	@Test
    public void testToString() {

        String expected = "Patient{patient_id=404, patient_name='John Doe', age=30, gender='M', "
                + "address='123 Elm Street', phone='555-1234', emergency_contact='Jane Doe', "
                + "emergency_contact_phone='555-5678', medical_history='Asthma, Hypertension', "
                + "current_conditions='High blood pressure'}";

        assertEquals(expected, patient.toString());
    }

	private void deleteFromCSV(int patientIdToDelete) {
	    File inputFile = new File(rootFolder + "patient.csv");
	    List<String> linesToKeep = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(",");
	            if (parts[0].equals("patient_id")) {
	            	linesToKeep.add(line);
	            	continue;
	            }
	            if (parts.length > 0 && Integer.parseInt(parts[0]) != patientIdToDelete) {
	                linesToKeep.add(line);
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("Error reading CSV: " + e.getMessage());
	    }

	    // Write the lines to keep back to the original file
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
	        for (String line : linesToKeep) {
	            writer.write(line);
	            writer.newLine();
	        }
	    } catch (IOException e) {
	        System.err.println("Error writing to CSV: " + e.getMessage());
	    }
	}



}
