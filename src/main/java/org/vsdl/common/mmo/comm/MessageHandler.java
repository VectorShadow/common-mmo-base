package org.vsdl.common.mmo.comm;

public interface MessageHandler {
    void handleMessage(Message message, int linkId);
    void handleException(Exception e, int linkId);
    void handleClosure(int linkId);
}
