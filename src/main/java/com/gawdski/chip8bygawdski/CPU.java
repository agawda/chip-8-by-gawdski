package com.gawdski.chip8bygawdski;

import opcodes.OpCode;

public class CPU {
    //chip-8 programs don't access memory below this point
    private final int PC_START = 0x200;

    //program counter
    private int pc;

    private Keyboard keyboard;
    private Memory memory;

    private OpCode[] opCodes;


}
