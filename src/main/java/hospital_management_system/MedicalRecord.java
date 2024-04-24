package hospital_management_system;

import java.io.*;
import java.util.List;

public class MedicalRecord 
{
    protected int recordId;
    protected int patientId;
    protected String medicalHistory;
    protected List<String> medications;
    protected List<String> allergies;
    protected List<String> treatments;
    protected List<String> diagnoses;
    final static String rootFolder = "..\\Hospital-System\\src\\main\\resources\\"; // Default CSV folder path

    // Constructor
    public MedicalRecord(int recordId, int patientId, String medicalHistory, List<String> medications, List<String> allergies, List<String> treatments, List<String> diagnoses) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.medicalHistory = medicalHistory;
        this.medications = medications;
        this.allergies = allergies;
        this.treatments = treatments;
        this.diagnoses = diagnoses;
    }

    // Methods to add data to the record's lists
    public void addMedication(String medication) {
        this.medications.add(medication);
    }

    public void addAllergy(String allergy) {
        this.allergies.add(allergy);
    }

    public void addTreatment(String treatment) {
        this.treatments.add(treatment);
    }

    public void addDiagnosis(String diagnosis) {
        this.diagnoses.add(diagnosis);
    }
    
    public void writeToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rootFolder + "hospitalMedicalRecords.csv", true))) {
            StringBuilder sb = new StringBuilder();
            
            sb.append(this.recordId).append(",")
              .append(this.patientId).append(",")
              .append("\"").append(this.medicalHistory.replace(";", ",")).append("\"").append(",")
              .append("\"").append(String.join(";", this.medications)).append("\"").append(",")
              .append("\"").append(String.join(";", this.allergies)).append("\"").append(",")
              .append("\"").append(String.join(";", this.treatments)).append("\"").append(",")
              .append("\"").append(String.join(";", this.diagnoses)).append("\"");
            
            writer.write(sb.toString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to medical records CSV: " + e.getMessage());
        }
    }
    
}
