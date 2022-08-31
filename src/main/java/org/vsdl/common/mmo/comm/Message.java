package org.vsdl.common.mmo.comm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Message {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final String messageType;

    private final Object messageContent;

    public Message(String messageType, Object messageContent) {
        this.messageType = messageType;
        this.messageContent = messageContent;
    }

    public String getMessageType() {
        return messageType;
    }

    public Object getMessageContent() {
        return messageContent;
    }

    public static byte[] wrap(Message message) {
        return gson.toJson(message).getBytes();
    }

    public static Message unwrap(String message) {
        return gson.fromJson(message, Message.class);
    }
}
