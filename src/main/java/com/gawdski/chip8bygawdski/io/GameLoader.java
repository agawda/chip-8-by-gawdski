package com.gawdski.chip8bygawdski.io;

import com.gawdski.chip8bygawdski.exception.GameNotLoadedException;
import com.gawdski.chip8bygawdski.game.GameData;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Anna Gawda
 * 10.03.2018
 */
public class GameLoader {
    private static final Logger LOG = LoggerFactory.getLogger(GameLoader.class);
    private static final int MAX_SIZE = 0x1000; //4096
    private static final String ERROR_MESSAGE = "Index out of bounds at index: {}";

    private int size;

    private static byte[] storage;

    public static GameData loadGame(File gameFile) throws GameNotLoadedException {
        try {
            storage = IOUtils.toByteArray(new FileInputStream(gameFile));
            return new GameData(IOUtils.toByteArray(new FileInputStream(gameFile)));
        } catch (IOException e) {
            LOG.error("Exception during program loading: {}", e.getMessage());
            throw new GameNotLoadedException("Error occurred during game loading");
        }
    }


}
