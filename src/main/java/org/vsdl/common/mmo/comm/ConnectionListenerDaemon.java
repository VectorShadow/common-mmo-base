package org.vsdl.common.mmo.comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListenerDaemon extends Thread {

    private static boolean isAlive = true;
    private static MessageSocketManager messageSocketManager;
    private static ServerSocket serverSocket;

    private static final ConnectionListenerDaemon instance = new ConnectionListenerDaemon();

    private ConnectionListenerDaemon() {}

    public static void startDaemon(int hostPort, MessageSocketManager messageSocketManager) {
        try {
            ConnectionListenerDaemon.messageSocketManager = messageSocketManager;
            ConnectionListenerDaemon.serverSocket = new ServerSocket(hostPort);
            ConnectionListenerDaemon.instance.start();
        } catch (IOException e) {
            throw new IllegalStateException("IOException on server socket creation: " + e.getMessage());
        }
    }

    public void kill() {
        isAlive = false;
    }

    public void run() {
        while (isAlive) {
            try {
                Socket s = serverSocket.accept();
                messageSocketManager.registerConnection(s);
            } catch (IOException e) { //no need to kill the server here, log the error and continue
                messageSocketManager.reportError(e);
            }
        }
    }
}
