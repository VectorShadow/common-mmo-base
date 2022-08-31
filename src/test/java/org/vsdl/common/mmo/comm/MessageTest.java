package org.vsdl.common.mmo.comm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MessageTest {

    @Test
    void testMessageWrapAndUnwrap() {
        Message message = new Message("Test", "Content");
        byte[] wrappedMessage = Message.wrap(message);
        assertEquals("Content", Message.unwrap(new String(wrappedMessage)).getMessageContent());
    }
}
