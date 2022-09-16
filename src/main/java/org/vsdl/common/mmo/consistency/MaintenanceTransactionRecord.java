package org.vsdl.common.mmo.consistency;

import org.vsdl.common.mmo.exceptions.MismatchedUUIDMaintenanceException;
import org.vsdl.common.mmo.exceptions.StaleVersionMaintenanceException;
import org.vsdl.common.mmo.exceptions.UnrecognizedClassMaintenanceException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MaintenanceTransactionRecord {
    private final UUID targetUUID;
    private final long initialVersion;
    private final List<MaintenanceTransaction> MaintenanceTransactionList;

    private MaintenanceTransactionRecord(UUID targetUUID, long initialVersion) {
        this.targetUUID = targetUUID;
        this.initialVersion = initialVersion;
        MaintenanceTransactionList = new ArrayList<>();
    }

    public static MaintenanceTransactionRecord initializeRecord(UUID targetUUID, long initialVersion) {
        return new MaintenanceTransactionRecord(targetUUID, initialVersion);
    }

    public MaintenanceTransactionRecord record(MaintenanceTransaction MaintenanceTransaction) {
        MaintenanceTransactionList.add(MaintenanceTransaction);
        return this;
    }

    public void applyTo(Maintainable target) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IllegalStateException, IllegalArgumentException, UnrecognizedClassMaintenanceException, MismatchedUUIDMaintenanceException, StaleVersionMaintenanceException {
        if (!targetUUID.equals(target.getUUID())) {
            throw new MismatchedUUIDMaintenanceException(targetUUID.toString(), target.getUUID().toString());
        }
        if (initialVersion != target.getVersion()) {
            throw new StaleVersionMaintenanceException("" + initialVersion, "" + target.getVersion() + "[Before Updates]");
        }
        for(MaintenanceTransaction t : MaintenanceTransactionList) {
            t.applyTo(target);
        }
        long finalVersion = initialVersion + MaintenanceTransactionList.size();
        if (finalVersion != target.getVersion()) {
            throw new StaleVersionMaintenanceException("" + finalVersion, target.getVersion() + "[After Updates]");
        }
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }
}
