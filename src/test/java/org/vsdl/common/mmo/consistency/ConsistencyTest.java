package org.vsdl.common.mmo.consistency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vsdl.common.mmo.fixtures.ConsistencyTestObject;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ConsistencyTest {


    @Test
    public void testArgMismatch() {
        assertThrows(IllegalArgumentException.class, () -> new MaintenanceTransaction("", "", new Class[]{}, new Object[]{new ConsistencyTestObject(UUID.randomUUID())}));
    }

    @Test
    public void testSingleMaintenanceTransactionApplication() {
        UUID uuid = UUID.randomUUID();
        ConsistencyTestObject test = new ConsistencyTestObject(uuid);
        MaintenanceTransactionRecord record = MaintenanceTransactionRecord.initializeRecord(uuid, 0L);
        record.record(new MaintenanceTransaction(test.getClass().getCanonicalName(), "setFlagToTrue", new Class[]{}, new Object[]{}));
        try {
            record.applyTo(test);
        } catch (Exception e) {
            fail();
        }
        assertTrue(test.getFlag());
        assertEquals(1, test.getVersion());
    }

    //todo - more tests!
}
