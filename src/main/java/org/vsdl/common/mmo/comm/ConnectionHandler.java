package org.vsdl.common.mmo.comm;

public interface ConnectionHandler {
    void handleNewConnection(int connectionId);
    void handleMessage(Message message, int connectionId);
    void handleExceptionWithConnection(Exception e, int connectionId);
    void handleExceptionWithoutConnection(Exception e);
    void handleConnectionClosure(int connectionId);
}
