package org.vsdl.common.mmo.comm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vsdl.common.mmo.fixtures.ConsistencyTestObject;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MessageTest {

    @Test
    void testMessageWrapAndUnwrap() {
        Message message = new Message("Test", "Content");
        byte[] wrappedMessage = Message.wrap(message);
        assertEquals("Content", Message.unwrap(wrappedMessage).getMessageContent());
    }

    @Test
    void testMessageWrapAndUnwrapObject() {
        UUID uuid = UUID.randomUUID();
        Message message = new Message("Test", new ConsistencyTestObject(uuid));
        byte[] wrappedMessage = Message.wrap(message);
        assertEquals(uuid, ((ConsistencyTestObject)Message.unwrap(new String(wrappedMessage)).getMessageContent(ConsistencyTestObject.class)).getUUID());
    }
}
