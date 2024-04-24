package hospital_management_system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


class Appointment {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String TIME_FORMAT = "HH:mm";
    protected static String rootFolder = "..\\Hospital-System\\src\\main\\resources\\";
    private static int nextAppointmentId = 122; // Assuming you have existing IDs up to 121

    // Emergency appointment registration
    public static String emergencyAppointment(Patient patient) {
    	String output = "";
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);

        // Find the emergency doctor with the closest available time
        try (BufferedReader reader = new BufferedReader(new FileReader(rootFolder + "doctor.csv"))) {
            String line;
            LocalDateTime closestTime = null;
            String closestDoctor = null;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && "Emergency".equals(parts[3])) { // Assuming Emergency is the department
                    String[] availableTimes = parts[5].split(";");
                    for (String time : availableTimes) {
                        LocalDateTime doctorTime = LocalDateTime.parse(currentDate.toLocalDate() + " " + time.split("-")[0], dateFormatter);
                        if (closestTime == null || doctorTime.isAfter(currentDate)) {
                            closestTime = doctorTime;
                            closestDoctor = parts[0]; // Doctor's name
                        }
                    }
                }
            }

            if (closestDoctor != null) {
                output += ("Emergency appointment registered with: " + closestDoctor + " at " + closestTime.format(dateFormatter));

                // Add the patient to the list of appointments CSV
                try (BufferedWriter appointmentWriter = new BufferedWriter(new FileWriter(rootFolder + "listOfAppointments.csv", true))) {
                    appointmentWriter.write(
                        nextAppointmentId + "," + 
                        patient.patient_id + "," +
                        patient.patient_name + "," +
                        "Emergency," +
                        currentDate.format(dateFormatter) + "," +
                        "Emergency," +
                        closestDoctor
                    );
                    appointmentWriter.newLine();
                }

                // Update the available times in the doctors CSV
                updateDoctorAvailableTimes(closestDoctor, closestTime.format(timeFormatter));
                
                // Notify the doctor with the updated schedule
                Doctor doctor = new Doctor(closestDoctor);
                doctor.notifyUpdatedSchedule(currentDate.toLocalDate());

                nextAppointmentId++; // Increment the appointment ID
            } else {
                output += ("No emergency doctors are available at this moment.");
            }

        } catch (IOException e) {
            System.err.println("Error reading doctors CSV: " + e.getMessage());
        }
        
        return output;
    }

    // Update doctor's available times
    private static void updateDoctorAvailableTimes(String doctorName, String registeredTime) {
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(rootFolder + "doctor.csv"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(doctorName)) {
                        String[] parts = line.split(",");
                        String[] availableTimes = parts[5].split(";");
                        List<String> updatedTimes = new ArrayList<>(Arrays.asList(availableTimes));
                        updatedTimes.remove(registeredTime);
                        parts[5] = String.join(";", updatedTimes);

                        lines.add(String.join(",", parts));
                    } else {
                        lines.add(line);
                    }
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(rootFolder + "doctor.csv"))) {
                for (String l : lines) {
                    writer.write(l);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error updating doctor's available times: " + e.getMessage());
        }
    }

    //Registering an appointment
    public static void registerAppointment(Patient patient) {
        Scanner scanner = new Scanner(System.in); // For user input
        String doctorName = patient.getPreferredDoctor(); 

        if (doctorName != null && !doctorName.isEmpty()) {
            // Case 1: Patient specified a doctor
            try (BufferedReader reader = new BufferedReader(new FileReader(rootFolder + "doctor.csv"))) {
                List<String> availableTimes = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(doctorName)) {
                        String[] parts = line.split(",");
                        if (parts.length >= 7) {
                            availableTimes = Arrays.asList(parts[6].split(";"));
                            System.out.println("Available times for " + doctorName + ": " + availableTimes);
                            break;
                        }
                    }
                }

                if (!availableTimes.isEmpty()) {
                    System.out.print("Select a time (enter the time in the format HH:mm): ");
                    String selectedTime = scanner.nextLine();

                    if (availableTimes.contains(selectedTime)) {
                        // Register the appointment
                        LocalDateTime appointmentDate = LocalDateTime.now().withHour(Integer.parseInt(selectedTime.split(":")[0]))
                                                                                  .withMinute(Integer.parseInt(selectedTime.split(":")[1]));

                        // Register the appointment in listOfAppointments.csv
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rootFolder + "listOfAppointments.csv", true))) {
                            writer.write(nextAppointmentId + "," + 
                                         patient.patient_id + "," +
                                         patient.patient_name + "," +
                                         "Scheduled," +
                                         appointmentDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)) + "," +
                                         "Regular Checkup," +
                                         doctorName);
                            writer.newLine();
                        }

                        // Update the doctor's available times
                        updateDoctorAvailableTimes(doctorName, selectedTime);

                        // Notify the doctor about the updated schedule
                        Doctor doctor = new Doctor(doctorName);
                        doctor.notifyUpdatedSchedule(LocalDate.now());

                        nextAppointmentId++; // Increment the appointment ID
                        System.out.println("Appointment registered successfully with " + doctorName + " at " + selectedTime);
                        return;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading doctor's CSV: " + e.getMessage());
            }
        }

        // Case 2: Patient didn't agree with available times of the specified doctor
        System.out.print("Enter your preferred appointment time (HH:mm): ");
        String preferredTime = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(rootFolder + "doctor.csv"))) {
            String line;
            List<String> doctorsInDepartment = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (line.contains(patient.getDepartment())) {
                    doctorsInDepartment.add(line.split(",")[0]);
                }
            }

            boolean appointmentRegistered = false;

            // Check all doctors in the department for the preferred time
            for (String doctor : doctorsInDepartment) {
                List<String> availableTimes = new ArrayList<>();
                try (BufferedReader doctorReader = new BufferedReader(new FileReader(rootFolder + "doctor.csv"))) {
                    while ((line = doctorReader.readLine()) != null) {
                        if (line.startsWith(doctor)) {
                            availableTimes = Arrays.asList(line.split(",")[5].split(";"));
                            if (availableTimes.contains(preferredTime)) {
                                // Register the appointment
                                LocalDateTime appointmentDate = LocalDateTime.now().withHour(Integer.parseInt(preferredTime.split(":")[0]))
                                                                              .withMinute(Integer.parseInt(preferredTime.split(":")[1]));

                                try (BufferedWriter writer = new BufferedWriter(new FileWriter("listOfAppointments.csv", true))) {
                                    writer.write(nextAppointmentId + "," + 
                                                 patient.patient_id + "," +
                                                 patient.patient_name + "," +
                                                 "Scheduled," +
                                                 appointmentDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)) + "," +
                                                 "Regular Checkup," +
                                                 doctor);
                                    writer.newLine();
                                }

                                // Update the doctor's available times
                                updateDoctorAvailableTimes(doctor, preferredTime);

                                // Notify the doctor about the updated schedule
                                Doctor doc = new Doctor(doctor);
                                doc.notifyUpdatedSchedule(LocalDate.now());

                                appointmentRegistered = true;
                                nextAppointmentId++; // Increment the appointment ID
                                System.out.println("Appointment registered successfully with " + doctor + " at " + preferredTime);
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading doctor's CSV: " + e.getMessage());
                }
                if (appointmentRegistered) {
                    break;
                }
            }

            if (!appointmentRegistered) {
                // Case 3: No doctor is available at the preferred time
                LocalDateTime closestAvailableTime = null;
                String closestDoctor = null;

                for (String doctor : doctorsInDepartment) {
                    List<String> availableTimes = new ArrayList<>();
                    try (BufferedReader doctorReader = new BufferedReader(new FileReader(rootFolder + "doctor.csv"))) {
                        while ((line = doctorReader.readLine()) != null) {
                            if (line.startsWith(doctor)) {
                                availableTimes = Arrays.asList(line.split(",")[5].split(";"));
                                for (String time : availableTimes) {
                                    LocalDateTime availableTime = LocalDateTime.now().withHour(Integer.parseInt(time.split("-")[0].split(":")[0]))
                                                                                 .withMinute(Integer.parseInt(time.split("-")[0].split(":")[1]));

                                    if (closestAvailableTime == null || availableTime.isAfter(LocalDateTime.now())) {
                                        closestAvailableTime = availableTime;
                                        closestDoctor = doctor;
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.err.println("Error reading doctor's CSV: " + e.getMessage());
                    }
                }

                if (closestAvailableTime != null && closestDoctor != null) {
                    // Register the appointment with the closest available time
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(rootFolder + "listOfAppointments.csv", true))) {
                        writer.write(nextAppointmentId + "," + 
                                     patient.patient_id + "," +
                                     patient.patient_name + "," +
                                     "Scheduled," +
                                     closestAvailableTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT)) + "," +
                                     "Regular Checkup," +
                                     closestDoctor);
                        writer.newLine();
                    }

                    // Update the doctor's available times
                    updateDoctorAvailableTimes(closestDoctor, closestAvailableTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT)));

                    // Notify the doctor about the updated schedule
                    Doctor doc = new Doctor(closestDoctor);
                    doc.notifyUpdatedSchedule(LocalDate.now());

                    nextAppointmentId++; // Increment the appointment ID
                    System.out.println("Appointment registered successfully with " + closestDoctor + " at " + closestAvailableTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading doctor's CSV: " + e.getMessage());
        }
    }

}
