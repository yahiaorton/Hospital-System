package hospital_management_system;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventoryControlTest {

private InventoryControl inventoryControl;
private static InventoryControl.InventoryItem item;
private static SimpleDateFormat sdf;
    
    @BeforeAll
    public static void setUpAll() throws ParseException {
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfPurchase1 = sdf.parse("2022-01-15");
        Date repairDate1 = sdf.parse("2023-01-15");
        
        item = new InventoryControl.InventoryItem(10, "Item10", dateOfPurchase1, repairDate1);
        
    }
    
    @BeforeEach
    public void setUp() {
        inventoryControl = new InventoryControl();
        inventoryControl.addInventoryItem(item);
    }
    @AfterEach
    public void cleanUp() {
    	inventoryControl.removeInventoryItem(10);
    	inventoryControl = null;
    }
    
    @Test
    public void testAddInventoryItem() throws ParseException {       
        InventoryControl.InventoryItem retrievedItem = inventoryControl.getInventoryItemById(10);
        
        assertNotNull(retrievedItem);
        assertEquals(item.inventoryId, retrievedItem.inventoryId);
        assertEquals(item.name, retrievedItem.name);
        assertEquals(sdf.format(item.dateOfPurchase), sdf.format(retrievedItem.dateOfPurchase));
        assertEquals(sdf.format(item.repairDate), sdf.format(retrievedItem.repairDate));
    }
    
    @Test
    public void testRemoveInventoryItem() throws ParseException {  
        inventoryControl.removeInventoryItem(10);
        
        InventoryControl.InventoryItem retrievedItem = inventoryControl.getInventoryItemById(10);
        
        assertNull(retrievedItem);
    }
    
    @Test
    public void testListAllInventoryItems() throws ParseException {
        Date dateOfPurchase20 = sdf.parse("2022-02-15");
        Date repairDate20 = sdf.parse("2023-02-15");
        
        InventoryControl.InventoryItem item20 = new InventoryControl.InventoryItem(20, "Item20", dateOfPurchase20, repairDate20);
        inventoryControl.addInventoryItem(item20);
        
        Map<Integer, InventoryControl.InventoryItem> inventoryItems = inventoryControl.listAllInventoryItems();
        
        assertEquals(2, inventoryItems.size());
        assertTrue(inventoryItems.containsKey(10));
        assertTrue(inventoryItems.containsKey(20));
    }
    
    @Test
    public void testLoadCaseTypeDepartment() {
        assertEquals("Cardiology", inventoryControl.caseTypeToDepartment.get("Specialist Consultation"));
        assertEquals("General Surgery", inventoryControl.caseTypeToDepartment.get("Surgery"));
        assertEquals("Emergency", inventoryControl.caseTypeToDepartment.get("Emergency"));
    }


}
