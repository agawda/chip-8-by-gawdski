package com.gawdski.chip8bygawdski;

/**
 * @author Anna Gawda
 * 10.02.2018
 */
public interface OpCode {
    default String convertToHexString(int n) {
        return Integer.toHexString(n);
    }

    default int convertToInt(String n) {
        return Integer.valueOf(n, 16);
    }

    void processOpCode();
}
