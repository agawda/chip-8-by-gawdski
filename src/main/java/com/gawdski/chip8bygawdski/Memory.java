package com.gawdski.chip8bygawdski;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class Memory {
    public static final int MAX_SIZE = 0x1000; //4096

    private short[] memory;
    private int size;

    public Memory() {
        memory = new short[MAX_SIZE];
    }

    public Memory(int size) {
        this.size = size;
        memory = new short[size];
    }

    public short read(int index) {
        if(index > size || index < 0) {
            throw new IllegalArgumentException("Index out of bounds.");
        }

        return (short)(memory[index] << 8 | memory[index + 1]);
    }

    public short readOneByte(int index) {
        if(index > size || index < 0) {
            throw new IllegalArgumentException("Index out of bounds.");
        }

        return (short)(memory[index] & 0xFF);
    }

    public void write(int index, int value) {
        if(index > size || index < 0) {
            throw new IllegalArgumentException("Index out of bounds.");
        }

        memory[index] = (short)(value & 0xFF);
    }

    public boolean loadProgram(InputStream inputStream) {
        try {
            byte[] programData = IOUtils.toByteArray(inputStream);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
