package hospital_management_system;
import java.util.ArrayList;
import java.util.List;
public class MedicalRecordManager 
{
    // Global list to store all medical records
    protected static List<MedicalRecord> medicalRecords = new ArrayList<>();

    // Method to add a new medical record
    public static void addMedicalRecord(MedicalRecord record) {
        medicalRecords.add(record);
    }

    // Method to find a medical record by patient ID
    public static MedicalRecord findRecordByPatientId(int patientId) {
        for (MedicalRecord record : medicalRecords) {
            if (record.patientId== patientId) {
                return record;
            }
        }
        return null; // No record found for the given patient ID
    }    
}
