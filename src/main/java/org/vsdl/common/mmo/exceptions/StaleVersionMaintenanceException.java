package org.vsdl.common.mmo.exceptions;

public class StaleVersionMaintenanceException extends MaintenanceException {
    public StaleVersionMaintenanceException(String expected, String actual) {
        super("Transaction record version does not match target object version - expected " + expected + " but was " + actual);
    }
}
