package org.vsdl.common.mmo.comm;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

public class MessageSocketManager {

    private final ConnectionHandler connectionHandler;
    private final HashMap<Integer, MessageSocket> messageSockets = new HashMap<>();

    private static MessageSocketManager instance;

    public static void initialize(final ConnectionHandler connectionHandler) {
        instance = new MessageSocketManager(connectionHandler);
    }

    public static MessageSocketManager getInstance() {
        if (isNull(instance)) {
            throw new IllegalStateException("MessageSocketManager has not been initialized");
        }
        return instance;
    }

    private MessageSocketManager(final ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public void registerConnection(Socket socket) throws IOException {
        MessageSocket messageSocket = new MessageSocket(socket, connectionHandler);
        messageSockets.put(messageSocket.getID(), messageSocket);
        messageSocket.start();
    }

    public List<MessageSocket> getMessageSockets() {
        return new ArrayList<>(messageSockets.values());
    }

    public MessageSocket getMessageSocketById(int id) {
        return Optional.ofNullable(messageSockets.get(id)).orElseThrow(() -> new IllegalArgumentException("No such socket: " + id));
    }

    public void reportError(Exception e) {
        this.connectionHandler.handleExceptionWithoutConnection(e);
    }
}
