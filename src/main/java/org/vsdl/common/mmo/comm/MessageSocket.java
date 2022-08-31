package org.vsdl.common.mmo.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.vsdl.common.mmo.comm.Message.*;

public class MessageSocket extends Thread {

    private static int nextId = 0;

    private final int ID;

    private final BufferedReader READER;

    private final MessageHandler HANDLER;

    private final Socket SOCK;

    private boolean isActive;

    public MessageSocket(Socket socket, MessageHandler handler) throws IOException {
        ID = nextId++;
        SOCK = socket;
        READER = new BufferedReader(new InputStreamReader(SOCK.getInputStream()));
        HANDLER = handler;
    }

    public void receive() {
        try {
            do {
                Thread.sleep(10);
                if (SOCK.getInputStream().available() <= 0) continue;
                HANDLER.handleMessage(unwrap(READER.readLine()), ID);
            } while (isActive);
            READER.close();
        } catch (InterruptedException | IOException e) {
            HANDLER.handleException(e, ID);
        }
        HANDLER.handleClosure(ID);
    }

    public void transmit(Message message) throws IOException {
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

    public boolean isActive() {
        return isActive;
    }
}
