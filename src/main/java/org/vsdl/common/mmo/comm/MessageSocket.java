package org.vsdl.common.mmo.comm;

import org.vsdl.common.log.VLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

import static org.vsdl.common.mmo.comm.Message.*;

public class MessageSocket extends Thread {

    private static int nextId = 0;

    private final int ID;

    private final BufferedReader READER;

    private final ConnectionHandler HANDLER;

    private final Socket SOCK;

    private boolean isActive;

    public MessageSocket(Socket socket, ConnectionHandler handler) throws IOException {
        ID = nextId++;
        SOCK = socket;
        READER = new BufferedReader(new InputStreamReader(SOCK.getInputStream()));
        HANDLER = handler;
    }

    public void receive() {
        HANDLER.handleNewConnection(ID);
        char[] readBuffer;
        int readCount;
        String message;
        try {
            do {
                readBuffer = new char[8192];
                Thread.sleep(10);
                if (SOCK.getInputStream().available() <= 0) continue;
                readCount = READER.read(readBuffer);
                message = new String(Arrays.copyOf(readBuffer, readCount));
                VLogger.log("Receiving message [" + message + "] on " + this + ".", VLogger.Level.TRACE);
                HANDLER.handleMessage(unwrap(message), ID);
            } while (isActive);
            READER.close();
        } catch (InterruptedException | IOException e) {
            HANDLER.handleExceptionWithConnection(e, ID);
        }
        HANDLER.handleConnectionClosure(ID);
    }

    public void transmit(Message message) throws IOException {
        VLogger.log("Transmitting message [Type: " + message.getMessageType() + ", Content: " + message.getMessageContent() + "] on " + this + ".", VLogger.Level.TRACE);
        SOCK.getOutputStream().write(wrap(message));
    }

    @Override
    public void run() {
        isActive = true;
        receive();
    }

    public void close() {
        isActive = false;
    }

    public int getID() {
        return ID;
    }

    public int getPort() {
        return SOCK.getPort();
    }

    public boolean isActive() {
        return SOCK.isConnected() && isActive;
    }

    @Override
    public String toString() {
        return "MessageSocket [ID=" + ID + ", Port=" + SOCK.getPort() + ", isActive=" + isActive + "]";
    }
}
