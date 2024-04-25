package snippet;

public class Snippet {
	File doctorCsvFile = new File(Appointment.rootFolder + "doctor.csv");
	        if (doctorCsvFile.exists()) {
	            doctorCsvFile.delete();
	        }
}

