package hospital_management_system;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hospital_Management_System 
{
	final static String rootFolder = "..\\Hospital-System\\src\\main\\resources\\";
	protected static List<Doctor> doctors;
	protected static List<Patient> patients;
	protected static List<MedicalRecord> mRecords;
	
    public static void main(String[] args) 
    {
    	doctors = readDoctorsFromCsv(rootFolder + "doctor.csv");
    	patients = readPatientsFromCsv(rootFolder + "patient.csv");
        mRecords = readMedicalRecordsFromCSV(rootFolder + "hospitalMedicalRecords.csv");
        
    }
    
    // CSV Handling Files
    public static List<Doctor> readDoctorsFromCsv(String csvFilePath) {
        List<Doctor> doctors = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");  // assuming comma-separated values
                String name = data[0];
                if (name.equals("dr_name"))
                	continue;
                String workingTime = data[1];
                String workingDates = data[2];
                String department = data[3];
                List<Integer> appointmentIds = parseIds(data[4]);
                List<String> availableTimes = Arrays.asList(data[5].split(";"));

                Doctor doctor = new Doctor(name, workingTime, workingDates, department, appointmentIds, availableTimes);
                doctors.add(doctor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return doctors;
    }
    public static List<Patient> readPatientsFromCsv(String csvFilePath) {
        List<Patient> patients = new ArrayList<>();

        int lineNumber = 0;  // to keep track of the current line number

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineNumber++;

                if (lineNumber == 1) {  // skip the first line
                    continue;
                }

                String[] data = line.split(",");  // assuming comma-separated values

                int patient_id = Integer.parseInt(data[0]);
                String patient_name = data[1];
                int age = Integer.parseInt(data[2]);
                String gender = data[3];
                String address = data[4];
                String phone = data[5];
                String emergency_contact = data[6];
                String emergency_contact_phone = data[7];
                String medical_history = data[8];
                String current_conditions = data[9];

                Patient patient = new Patient(patient_id, patient_name, age, gender, address, 
                                              phone, emergency_contact, emergency_contact_phone, 
                                              medical_history, current_conditions);
                patients.add(patient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return patients;
    }
    public static List<MedicalRecord> readMedicalRecordsFromCSV(String filePath) {
        List<MedicalRecord> medicalRecords = new ArrayList<>();

        int lineNumber = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	lineNumber++;

                if (lineNumber == 1) {  // skip the first line
                    continue;
                }
                
                String[] data = line.split(",");

                int recordId = Integer.parseInt(data[0]);
                int patientId = Integer.parseInt(data[1]);
                String medicalHistory = data[2].replace("\"", "").replace(",", ";"); // Remove quotes and replace commas with ;
                List<String> medications = splitAndTrim(data[3], ";");
                List<String> allergies = splitAndTrim(data[4], ";");
                List<String> treatments = splitAndTrim(data[5], ";");
                List<String> diagnoses = splitAndTrim(data[6], ";");

                MedicalRecord medicalRecord = new MedicalRecord(recordId, patientId, medicalHistory, 
                                                                medications, allergies, treatments, diagnoses);
                medicalRecords.add(medicalRecord);
            }
        } catch (IOException e) {
            System.err.println("Error reading medical records from CSV: " + e.getMessage());
        }

        return medicalRecords;
    }
    private static List<String> splitAndTrim(String input, String delimiter) {
        List<String> list = new ArrayList<>();
        if (input != null && !input.isEmpty()) {
            String[] items = input.split(delimiter);
            for (String item : items) {
                list.add(item.trim());
            }
        }
        return list;
    }
    private static List<Integer> parseIds(String idsString) {
        String[] idsArray = idsString.split(";");
        List<Integer> ids = new ArrayList<>();
        for (String id : idsArray) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }
    
    //Create new Doctor and Patient Functions
    public static void createNewDoctor(String name, String workingTime, String workingDates, String department, List<Integer> appointmentIds, List<String> availableTimes) {
    	Doctor doc = new Doctor(name, workingTime, workingDates, department, appointmentIds, availableTimes);
    	doctors.add(doc);
    	doc.writeToCSV();
    }
    public static void createNewPatient(int patient_id, String patient_name, int age, String gender, String address, String phone, String emergency_contact, String emergency_contact_phone, String medical_history, String current_conditions) {
		Patient patient = new Patient(patient_id, patient_name, age, gender, address, phone, emergency_contact, emergency_contact_phone, medical_history, current_conditions);
		patients.add(patient);
		patient.writeToCSV();
	}
    
    // Login Functions
    public static Doctor doctorLogin(String Name, String Department) {
    	
    	for (Doctor doc : doctors) {
    		if (doc.name.equals(Name) && doc.department.equals(Department))
    			return doc;
    	}
    	return null;
    }
	public static Patient patientLogin(int PID, String pName) {
	    	
	    for (Patient patient: patients) {
	    	if (patient.patient_id == PID && patient.patient_name.equals(pName))
	    		return patient;
	    	}
	    return null;
	}

}
