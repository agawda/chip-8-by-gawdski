package com.gawdski.chip8bygawdski.game;

import com.gawdski.chip8bygawdski.CPU;
import com.gawdski.chip8bygawdski.Memory;

/**
 * @author Anna Gawda
 * 10.03.2018
 */
public class GameHandler {
    private final GameData gameData;
    private final CPU cpu;
    private final Memory memory;

    public GameHandler(GameData gameData, CPU cpu, Memory memory) {
        this.gameData = gameData;
        this.cpu = cpu;
        this.memory = memory;
    }
}
