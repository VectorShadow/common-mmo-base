package org.vsdl.common.mmo.comm;

import org.vsdl.common.log.VLogger;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
        VLogger.log("Registered connection for new socket with ID " + messageSocket.getID() + " on port " + messageSocket.getPort(), VLogger.Level.INFO);
        messageSocket.start();
    }

    public List<MessageSocket> getMessageSockets() {
        return new ArrayList<>(messageSockets.values());
    }

    public MessageSocket getMessageSocketById(int id) {
        return Optional.ofNullable(messageSockets.get(id)).orElseThrow(() -> new IllegalArgumentException("No such socket: " + id));
    }

    public void removeMessageSocketById(int id) {
        if (nonNull(messageSockets.remove(id))) {
            VLogger.log("Removed Message Socket with ID " + id, VLogger.Level.TRACE);
        } else {
            VLogger.log("Tried to remove Message Socket with ID " + id + " but no such socket was found.", VLogger.Level.WARN);
        }
    }

    public void reportError(Exception e) {
        this.connectionHandler.handleExceptionWithoutConnection(e);
    }
}
