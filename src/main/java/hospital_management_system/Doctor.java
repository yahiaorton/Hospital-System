package hospital_management_system;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


class Doctor {
	protected String name;
	protected String workingTime;
    protected String workingDates;
    protected String department;
    protected List<Integer> appointmentIds;
    protected List<String> availableTimes;
	final static String rootFolder = "..\\Hospital-System\\src\\main\\resources\\";
	
	public Doctor(String name) {
		this.name = name;
	}
	
    public Doctor(String name, String workingTime, String workingDates, String department, List<Integer> appointmentIds, List<String> availableTimes) {
        this.name = name;
        this.workingTime = workingTime;
        this.workingDates = workingDates;
        this.department = department;
        this.appointmentIds = appointmentIds;
        this.availableTimes = availableTimes;
    }

	public String notifyUpdatedSchedule(LocalDate currentDate) {
		String output = "";
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		output += "Doctor " + name + " has a new schedule for " + currentDate.format(dateFormatter) + "\n";
		// Fetch the updated schedule from listOfAppointment.csv
		try {
			List<String> updatedSchedule = new ArrayList<>();
			try (BufferedReader reader = new BufferedReader(new FileReader(rootFolder + "listOfAppointments.csv"))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(",");
					if (parts.length >= 6 && parts[6].equals(name)) {
						String appointmentDate = parts[4].split(" ")[0];
						if ((currentDate.format(dateFormatter).contains(appointmentDate))) {
							updatedSchedule.add(line);
						}
					}
				}
			}

			if (!updatedSchedule.isEmpty()) {
				output += "Doctor " + name + "'s updated schedule for today:\n";
				for (String appointment : updatedSchedule) {
					String[] parts = appointment.split(",");
					output += "Patient ID: " + parts[1] + ", " + "Patient Name: " + parts[2] + ", "
							+ "Registration Type: " + parts[3] + ", " + "Appointment Date: " + parts[4] + "\n";
				}
			} else {
				output += "No appointments for " + name + " today.\n";
			}
			

		} catch (IOException e) {
			System.err.println("Error fetching doctor's schedule: " + e.getMessage());
		}
		
		return output;
	}
	
	public void writeToCSV() {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(rootFolder + "doctor.csv", true))) {
	        StringBuilder sb = new StringBuilder();
	        
	        sb.append(this.name).append(",").append(this.workingTime).append(",")
	          .append(this.workingDates).append(",").append(this.department).append(",")
	          .append(String.join(";", this.appointmentIds.toString())).append(",")
	          .append(String.join(";", this.availableTimes));

	        writer.write(sb.toString());
	        writer.newLine();
	    } catch (IOException e) {
	        System.err.println("Error writing to doctors CSV: " + e.getMessage());
	    }
	}
	
	@Override
    public String toString() {
        return "Doctor{" +
                "drName='" + name + '\'' +
                ", workingTime='" + workingTime + '\'' +
                ", workingDates='" + workingDates + '\'' +
                ", department='" + department + '\'' +
                ", appointmentIds=" + appointmentIds +
                ", availableTimes=" + availableTimes +
                '}';
    }
}
