package hospital_management_system;
import java.util.ArrayList;
import java.util.Date;
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
    protected Date lastUpdated;

    // Constructor
    public MedicalRecord(int recordId, int patientId, String medicalHistory, List<String> medications, List<String> allergies, List<String> treatments, List<String> diagnoses, Date lastUpdated) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.medicalHistory = medicalHistory;
        this.medications = medications;
        this.allergies = allergies;
        this.treatments = treatments;
        this.diagnoses = diagnoses;
        this.lastUpdated = lastUpdated;
    }

    
    // Method to add a medical record to the global list
    public void addToMedicalRecord() {
        MedicalRecordManager.addMedicalRecord(this);
    }

    // Methods to add data to the record's lists
    public void addMedication(String medication) {
        this.medications.add(medication);
        updateLastUpdated();
    }

    public void addAllergy(String allergy) {
        this.allergies.add(allergy);
        updateLastUpdated();
    }

    public void addTreatment(String treatment) {
        this.treatments.add(treatment);
        updateLastUpdated();
    }

    public void addDiagnosis(String diagnosis) {
        this.diagnoses.add(diagnosis);
        updateLastUpdated();
    }

    // Method to update the "lastUpdated" timestamp
    public void updateLastUpdated() {
        this.lastUpdated = new Date();
    }
}
