package com.gawdski.chip8bygawdski;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class Memory {
    private static final Logger LOG = LoggerFactory.getLogger(Memory.class);

    private static final int MAX_SIZE = 0x1000; //4096
    private static final String ERROR_MESSAGE = "Index out of bounds at index: {}";

    private short[] storage;
    private int size;

    public Memory() {
        storage = new short[MAX_SIZE];
    }

    public Memory(int size) {
        this.size = size;
        storage = new short[size];
    }

    public short read(int index) {
        if (index > MAX_SIZE || index < 0) {
            LOG.error(ERROR_MESSAGE, index);
            throw new IllegalArgumentException();
        }

        return (short) (storage[index] << 8 | storage[index + 1]);
    }

    public short readOneByte(int index) {
        if (index > MAX_SIZE || index < 0) {
            LOG.error(ERROR_MESSAGE, index);
            throw new IllegalArgumentException();
        }

        return (short) (storage[index] & 0xFF);
    }

    public void write(int index, int value) {
        if (index > MAX_SIZE || index < 0) {
            LOG.error(ERROR_MESSAGE, index);
            throw new IllegalArgumentException();
        }

        storage[index] = (short) (value & 0xFF);
    }

    public boolean loadProgram(InputStream inputStream) {
        try {
            byte[] programData = IOUtils.toByteArray(inputStream);
            return true;
        } catch (IOException e) {
            LOG.error("Exception thrown during program loading: {}", e.getMessage());
            return false;
        }
    }
}
