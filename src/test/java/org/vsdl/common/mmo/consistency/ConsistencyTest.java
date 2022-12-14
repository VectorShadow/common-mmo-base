package org.vsdl.common.mmo.consistency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vsdl.common.mmo.exceptions.MismatchedUUIDMaintenanceException;
import org.vsdl.common.mmo.exceptions.StaleVersionMaintenanceException;
import org.vsdl.common.mmo.exceptions.UnrecognizedClassMaintenanceException;
import org.vsdl.common.mmo.fixtures.ConsistencyTestObject;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ConsistencyTest {

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
