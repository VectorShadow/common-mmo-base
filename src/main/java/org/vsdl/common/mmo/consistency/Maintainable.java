package org.vsdl.common.mmo.consistency;

import java.util.UUID;

public interface Maintainable {
    UUID getUUID();
    long getVersion();
    void incrementVersion();
}

