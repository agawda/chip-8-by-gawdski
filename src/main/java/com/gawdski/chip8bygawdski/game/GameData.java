package com.gawdski.chip8bygawdski.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anna Gawda
 * 10.03.2018
 */
public class GameData {
    private static final Logger LOG = LoggerFactory.getLogger(GameData.class);
    private static final int MAX_SIZE = 0x1000; //4096
    private static final String ERROR_MESSAGE = "Index out of bounds at index: {}";

    private final byte[] storage;

    public GameData(byte[] loadedData) {
        this.storage = loadedData;
    }

    public short read(int index) {
        if (index > MAX_SIZE || index < 0) {
            LOG.error(ERROR_MESSAGE, index);
            throw new IllegalArgumentException();
        }

        LOG.info(String.valueOf(storage[index] << 8 | storage[index + 1]));
        return (short) (storage[index] << 8 | storage[index + 1]);
    }
}
