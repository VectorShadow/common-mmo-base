package org.vsdl.common.mmo.comm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class MessageSocketManagerTest {

    @Mock
    private static ConnectionHandler connectionHandler;
    @Mock
    private static InputStream inputStream;
    @Mock
    private static Socket socket;
    private static MessageSocketManager manager;

    @BeforeEach
    public void setUp() {
        connectionHandler = mock(ConnectionHandler.class);
        inputStream = mock(InputStream.class);
        socket = mock(Socket.class);
        manager = new MessageSocketManager(connectionHandler);
    }

    @Test
    void testRegisterConnection() throws IOException {
        when(socket.isConnected()).thenReturn(true);
        when(socket.getInputStream()).thenReturn(inputStream);
        manager.registerConnection(socket);
        verify(connectionHandler, times(1)).handleNewConnection(anyInt());
        assertFalse(manager.getMessageSockets().isEmpty());
    }

    @Test
    void testReportError() {
        manager.reportError(new IOException());
        verify(connectionHandler, times(1)).handleExceptionWithoutConnection(any(Exception.class));
        assertTrue(manager.getMessageSockets().isEmpty());
    }

    @Test
    void testGetMessageSocketByIdById_Success() throws IOException {
        when(socket.isConnected()).thenReturn(true);
        when(socket.getInputStream()).thenReturn(inputStream);
        manager.registerConnection(socket);
        assertDoesNotThrow(() -> manager.getMessageSocketById(manager.getMessageSockets().stream().map(MessageSocket::getID).findFirst().get()));
    }

    @Test
    void testGetMessageSocketByIdById_Failure_NotFound() throws IOException {
        when(socket.isConnected()).thenReturn(true);
        when(socket.getInputStream()).thenReturn(inputStream);
        manager.registerConnection(socket);
        assertThrows(IllegalArgumentException.class, () -> manager.getMessageSocketById(1));
    }

    @Test
    void testGetMessageSocketByIdById_Failure_Empty() {
        assertThrows(IllegalArgumentException.class, () -> manager.getMessageSocketById(0));
    }
}
