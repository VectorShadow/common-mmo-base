package org.vsdl.common.mmo.comm;

import java.io.Serializable;

import static org.vsdl.common.mmo.utils.GsonUtils.*;

public class Message {

    private final String messageType;

    private final String messageContent;

    public Message(final String messageType) {
        this(messageType, messageType);
    }

    public Message(String messageType, Serializable messageContent) {
        if (messageType.isEmpty()) throw new IllegalArgumentException("Message type cannot be empty");
        this.messageType = messageType;
        this.messageContent = convertObjectToJSONString(messageContent);
    }

    public String getMessageType() {
        return messageType;
    }


    public Object getMessageContent(Class contentClass) {
        return convertJSONStringToObject(messageContent, contentClass);
    }

    public Object getMessageContent() {
        return convertJSONStringToString(messageContent);
    }

    public static byte[] wrap(Message message) {
        return convertObjectToJSONString(message).getBytes();
    }

    public static Message unwrap(String message) {
        return convertJSONStringToObject(message, Message.class);
    }

    public static Message unwrap(byte[] messageBytes) {
        return unwrap(new String(messageBytes));
    }
}
