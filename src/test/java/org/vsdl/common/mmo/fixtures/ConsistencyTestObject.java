package org.vsdl.common.mmo.fixtures;

import org.vsdl.common.mmo.consistency.Maintainable;

import java.util.UUID;

public class ConsistencyTestObject implements Maintainable {

    private final UUID uuid;
    private long version;

    private boolean flag = false;
    private int sum = 0;

    public ConsistencyTestObject(UUID uuid) {
        this.uuid = uuid;
        version = 0;
    }

    public void setFlagToTrue() {
        flag = true;
    }

    public boolean getFlag() {
        return flag;
    }

    public void add(int a, int b) {
        sum = a + b;
    }

    public int getSum() {
        return sum;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void incrementVersion() {
        version++;
    }
}
