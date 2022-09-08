package org.vsdl.common.mmo.exceptions;

public class UnrecognizedClassMaintenanceException extends MaintenanceException {

    public UnrecognizedClassMaintenanceException(String expected, String actual) {
        super("Unrecognized class - expected " + expected + " but was " + actual);
    }

}
