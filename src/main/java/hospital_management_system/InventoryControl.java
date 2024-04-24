package hospital_management_system;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class InventoryControl {
	final static String rootFolder = "..\\Hospital-System\\src\\main\\resources\\";
    // A map to store inventory items
    protected Map<Integer, InventoryItem> inventory;
    protected Map<String, String> caseTypeToDepartment;

    // Constructor
    public InventoryControl() {
        this.inventory = new HashMap<>();
        this.caseTypeToDepartment = new HashMap<>();
        loadCaseTypeDepartment(); // Load the case type-department mappings
    }
    // Inner class to represent inventory items
    public static class InventoryItem {
        public int inventoryId;
        public String name;
        public Date dateOfPurchase;
        public Date repairDate;

        // Constructor
        public InventoryItem(int inventoryId, String name, Date dateOfPurchase, Date repairDate) {
            this.inventoryId = inventoryId;
            this.name = name;
            this.dateOfPurchase = dateOfPurchase;
            this.repairDate = repairDate;
        }

        @Override
        public String toString() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return inventoryId + "," + name + "," + sdf.format(dateOfPurchase) + "," + sdf.format(repairDate);
        }
    }

    // Method to add an item to the inventory
    public void addInventoryItem(InventoryItem item) {
        inventory.put(item.inventoryId, item);
        writeInventoryToCSV(); // Save to CSV after adding
    }

    // Method to remove an item from the inventory
    public void removeInventoryItem(int inventoryId) {
        inventory.remove(inventoryId);
        writeInventoryToCSV(); // Save to CSV after removing
    }

    // Method to get an inventory item by ID
    public InventoryItem getInventoryItemById(int inventoryId) {
        return inventory.get(inventoryId);
    }

    // Method to list all inventory items
    public Map<Integer, InventoryItem> listAllInventoryItems() {
        return inventory;
    }

    // Method to load the case_type to department mapping from CSV
    private void loadCaseTypeDepartment() {
        String csvFilePath = rootFolder + "Cases.csv"; // Path to the CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split the line by comma
                if (parts.length >= 2) { // At least two parts (case_type, department)
                    String caseType = parts[0].trim();
                    String department = parts[1].trim();
                    caseTypeToDepartment.put(caseType, department); // Store in the map
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Cases CSV: " + e.getMessage());
        }
    }

    // Method to write the current inventory to a CSV file
    private void writeInventoryToCSV() {
        String inventoryFilePath = rootFolder + "Inventory.csv"; // Path to the inventory CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inventoryFilePath))) {
            for (InventoryItem item : inventory.values()) {
                writer.write(item.toString()); // Write the inventory item to CSV
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to inventory CSV: " + e.getMessage());
        }
    }
}
