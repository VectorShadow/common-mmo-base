package org.vsdl.common.mmo.consistency;

import java.io.Serializable;
import java.util.UUID;

public interface Maintainable extends Serializable {
    UUID getUUID();
    long getVersion();
    void incrementVersion();
}

