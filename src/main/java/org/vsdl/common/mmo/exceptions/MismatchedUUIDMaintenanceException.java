package org.vsdl.common.mmo.exceptions;

public class MismatchedUUIDMaintenanceException extends MaintenanceException {

    public MismatchedUUIDMaintenanceException(String expected, String actual) {
        super("Transaction record ID does not match target object UUID - expected " + expected + " but was " + actual);
    }
}
