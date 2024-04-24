package hospital_management_system;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventoryControlTest {

private InventoryControl inventoryControl;
    
    @BeforeEach
    public void setUp() {
        inventoryControl = new InventoryControl();
    }
    
    @Test
    public void testAddInventoryItem() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfPurchase = sdf.parse("2022-01-15");
        Date repairDate = sdf.parse("2023-01-15");
        
        InventoryControl.InventoryItem item = new InventoryControl.InventoryItem(10, "Item10", dateOfPurchase, repairDate);
        inventoryControl.addInventoryItem(item);
        
        InventoryControl.InventoryItem retrievedItem = inventoryControl.getInventoryItemById(10);
        
        assertNotNull(retrievedItem);
        assertEquals(item.inventoryId, retrievedItem.inventoryId);
        assertEquals(item.name, retrievedItem.name);
        assertEquals(sdf.format(item.dateOfPurchase), sdf.format(retrievedItem.dateOfPurchase));
        assertEquals(sdf.format(item.repairDate), sdf.format(retrievedItem.repairDate));
    }
    
    @Test
    public void testRemoveInventoryItem() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfPurchase = sdf.parse("2022-01-15");
        Date repairDate = sdf.parse("2023-01-15");
        
        InventoryControl.InventoryItem item = new InventoryControl.InventoryItem(10, "Item10", dateOfPurchase, repairDate);
        inventoryControl.addInventoryItem(item);
        
        inventoryControl.removeInventoryItem(10);
        
        InventoryControl.InventoryItem retrievedItem = inventoryControl.getInventoryItemById(10);
        
        assertNull(retrievedItem);
    }
    
    @Test
    public void testListAllInventoryItems() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfPurchase1 = sdf.parse("2022-01-15");
        Date repairDate1 = sdf.parse("2023-01-15");
        
        InventoryControl.InventoryItem item1 = new InventoryControl.InventoryItem(1, "Item1", dateOfPurchase1, repairDate1);
        inventoryControl.addInventoryItem(item1);
        
        Date dateOfPurchase2 = sdf.parse("2022-02-15");
        Date repairDate2 = sdf.parse("2023-02-15");
        
        InventoryControl.InventoryItem item2 = new InventoryControl.InventoryItem(2, "Item2", dateOfPurchase2, repairDate2);
        inventoryControl.addInventoryItem(item2);
        
        Map<Integer, InventoryControl.InventoryItem> inventoryItems = inventoryControl.listAllInventoryItems();
        
        assertEquals(2, inventoryItems.size());
        assertTrue(inventoryItems.containsKey(1));
        assertTrue(inventoryItems.containsKey(2));
    }
    
    @Test
    public void testLoadCaseTypeDepartment() {
        assertEquals("Cardiology", inventoryControl.caseTypeToDepartment.get("Specialist Consultation"));
        assertEquals("General Surgery", inventoryControl.caseTypeToDepartment.get("Surgery"));
        assertEquals("Emergency", inventoryControl.caseTypeToDepartment.get("Emergency"));
    }


}
