package hospital_management_system;

import java.time.LocalDate;

public class Hospital_Management_System 
{
    public static void main(String[] args) 
    {
    	Doctor doc = new Doctor("Dr. Davis");
    	doc.notifyUpdatedSchedule(LocalDate.of(2024, 4, 29));
    }
}
