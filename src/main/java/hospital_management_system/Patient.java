package hospital_management_system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class Patient {
    protected int patient_id;
    protected String patient_name;
    protected int age;
    protected String gender;
    protected String address;
    protected String phone;
    protected String emergency_contact;
    protected String emergency_contact_phone;
    protected String medical_history;
    protected String current_conditions;
    final static String rootFolder = "..\\Hospital-System\\src\\main\\resources\\"; // Default CSV folder path

    public Patient(int patient_id, String patient_name, int age, String gender, String address, 
		            String phone, String emergency_contact, String emergency_contact_phone, 
		            String medical_history, String current_conditions) {
		 this.patient_id = patient_id;
		 this.patient_name = patient_name;
		 this.age = age;
		 this.gender = gender;
		 this.address = address;
		 this.phone = phone;
		 this.emergency_contact = emergency_contact;
		 this.emergency_contact_phone = emergency_contact_phone;
		 this.medical_history = medical_history;
		 this.current_conditions = current_conditions;
		 this.writeToCSV();
	}

    public void updateCurrent_conditions(String current_conditions) {
		this.current_conditions = current_conditions;
	}

	// Method to register a new application
    public String registerApplication(boolean emergency) {
    	String output = "";
        if (!emergency) {
            // Non-emergency case, check CSV for corresponding department
            boolean foundDepartment = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(rootFolder + "patientCaseType.csv"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) { // We expect case type, department, and condition
                        String caseType = parts[0];
                        String department = parts[1];
                        String condition = parts[2];

                        // If current condition has a corresponding department, set foundDepartment to true
                        if (condition.contains(this.current_conditions)) {
                            foundDepartment = true;
                            Appointment.registerAppointment(this); // Call the registerAppointment method
                            output += "Appliction is registerd for current condition."; 
                            break;
                        }
                    }
                }

                if (!foundDepartment) {
                	output += "This department is not available at the moment.";
                }

            } catch (IOException e) {
                System.err.println("Error reading patientCaseType CSV: " + e.getMessage());
            }

        } else {
            // Register emergency appointment
            Appointment.emergencyAppointment(this);
            output += "Emergency Appointment has been registred for currenct condition.";
        }
        
        return output;
    }
  
    // New method to get the preferred doctor based on the CSV file
    public String getPreferredDoctor() {
        try (BufferedReader reader = new BufferedReader(new FileReader(rootFolder + "patient_preferred_doctor.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String patientNameFromCSV = parts[0].trim();
                    String preferredDoctor = parts[1].trim();
                    // If the patient_name matches, return the preferred doctor
                    if (patientNameFromCSV.equalsIgnoreCase(this.patient_name)) {
                        return preferredDoctor;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
        // Return a default value if no preferred doctor is found
        return "No preferred doctor found";
    }
    
    // Method to get the department from the CSV file based on current conditions
    public String getDepartment() {
        String department = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(rootFolder + "patientCaseType.csv"))) {
            String line;

            // Loop through the CSV file to find the appropriate department based on current conditions
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split the line into components
                if (parts.length >= 3) {
                    String csvCurrentConditions = parts[2].trim(); // The case type from the CSV
                    String csvDepartment = parts[1].trim(); // The associated department

                    // If current_conditions contains this case type, assign the department and exit the loop
                    if (csvCurrentConditions.contains(current_conditions)) {
                        department = csvDepartment;
                        break; // Exit the loop once a match is found
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading case type CSV file: " + e.getMessage());
        }

        return department; // Return the matching department or null if none was found
    }
    
    private void writeToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rootFolder + "patient.csv", true))) {
            writer.write(this.patient_id + "," + this.patient_name + "," + this.age + "," + this.gender + "," +
                         this.address + "," + this.phone + "," + this.emergency_contact + "," +
                         this.emergency_contact_phone + "," + this.medical_history + "," + this.current_conditions);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to patients CSV: " + e.getMessage());
        }

    }
    
    @Override
    public String toString() {
        return "Patient{" +
                "patient_id=" + patient_id +
                ", patient_name='" + patient_name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", emergency_contact='" + emergency_contact + '\'' +
                ", emergency_contact_phone='" + emergency_contact_phone + '\'' +
                ", medical_history='" + medical_history + '\'' +
                ", current_conditions='" + current_conditions + '\'' +
                '}';
    }
}


 