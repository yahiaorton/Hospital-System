package hospital_management_system;

import java.util.ArrayList;
import java.util.Date;

public class Staff 
{
    protected int staffId;
    protected String name;
    protected String role; // e.g., "nurse", "receptionist"
    
    // Constructor
    public Staff(int staffId, String name, String role) {
        this.staffId = staffId;
        this.name = name;
        this.role = role;
    }
    
    // Method for staff to add an inventory item
    public String addInventoryItem(InventoryControl inventoryControl, int inventoryId, String name, Date dateOfPurchase, Date repairDate) {
        InventoryControl.InventoryItem newItem = new InventoryControl.InventoryItem(inventoryId, name, dateOfPurchase, repairDate);
        inventoryControl.addInventoryItem(newItem);
        return("Inventory item " + name + " added by staff.");
    }

    // Method for staff to remove an inventory item
    public String removeInventoryItem(InventoryControl inventoryControl, int inventoryId) {
        inventoryControl.removeInventoryItem(inventoryId);
        return("Inventory item with ID " + inventoryId + " removed by staff.");
    }

    // Method for staff to list all inventory items
    public String listAllInventoryItems(InventoryControl inventoryControl) {
    	String output = "";
        output += ("Listing all inventory items:");
        for (InventoryControl.InventoryItem item : inventoryControl.listAllInventoryItems().values()) {
        	output += ("ID: " + item.inventoryId + ", Name: " + item.name + ", Date of Purchase: " + item.dateOfPurchase + ", Repair Date: " + item.repairDate);
        }
        return output;
    }

}
