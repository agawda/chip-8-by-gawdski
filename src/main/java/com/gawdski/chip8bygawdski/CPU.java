package com.gawdski.chip8bygawdski;

import com.gawdski.chip8bygawdski.game.GameData;
import com.gawdski.chip8bygawdski.graphics.Screen;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.gawdski.chip8bygawdski.Operation.convertToInt;

public class CPU {
    //chip-8 programs don't access memory below this point
    private static final int PC_START = 0x200;
    private static final int REGISTERS_NUM = 16;
    private static final long DEFAULT_CPU_CYCLE_TIME = 2;
    private static final short DEFAULT_STACK_SIZE = 16;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private int pc; //program counter
    private int I; //index register
    private int sp; //stack pointer

    private int soundTimer;
    private int delayTimer;

    private short opc; //current opcode read from memory
    private String command;

    private Keyboard keyboard;
    private Memory memory;
    private Screen screen;
    private GameData gameData;

    private final Map<OpCode, Operation> operations;
    private short[] V; //CPU registers
    private short[] stack;

    private Random random;

    public CPU(Screen screen, GameData gameData) {
        this.screen = screen;
        this.gameData = gameData;
        this.operations = new HashMap<>();

        random = new Random();
        memory = new Memory();

        stack = new short[DEFAULT_STACK_SIZE];

        V = new short[REGISTERS_NUM];

        pc = PC_START;

        setOpcodes();
    }

    public void run() {
        executor.scheduleAtFixedRate(() -> cycle(), 0, 1, TimeUnit.SECONDS);
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void cycle() {
        //fetch
        int operation = gameData.read(pc);
        System.out.println(operation);
        //decode
        System.out.println(Operation.convertToHexString(0x0250));
        operations.get(OpCode.valueOf(Operation.convertToHexString(operation))).processOpCode();
        //execute
    }

    private void setOpcodes() {
        operations.put(OpCode.ADDIVx, () -> {
            //Fx1E
            //set I = I + Vx
            int vx = convertToInt(command.substring(1, 2));
            I += V[vx];
        });

        operations.put(OpCode.ADDVx, () -> {
            //7xkk
            //set Vx = Vx + kk
            int vx = convertToInt(command.substring(1, 2));
            int kk = convertToInt(command.substring(2, 4));
            V[vx] = (short) (V[vx] + kk);
        });

        operations.put(OpCode.ADDVxVy, () -> {
            //8xy4
            //set Vx = Vx + Vy, set VF = carry
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));
            int sum = V[vx] + V[vy];
            if (sum > 255) {
                V[vx] = (short) (sum - 256);
                V[0xF] = 1;
            } else {
                V[vx] = (short) sum;
                V[0xF] = 0;
            }
        });

        operations.put(OpCode.ANDVxVy, () -> {
            //8xy2
            //set Vx = Vx AND Vy
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));

            V[vx] = (short) (V[vx] & V[vy]);
        });

        operations.put(OpCode.CALLAddr, () -> {
            //2nnn
            //call subroutine at nnn
            int nnn = convertToInt(command.substring(1));

            stack[sp++] = (short) pc;
            pc = nnn;
        });

        operations.put(OpCode.CLS, () -> screen.clearDisplay());

//        operations.put(OpCode.DRWVxVy, () -> )

        operations.put(OpCode.JPAddr, () -> {
            //1nnn
            //jump to location nnn
            int address = convertToInt(command.substring(1));
            pc = address;
        });

        operations.put(OpCode.JPV0Addr, () -> {
            //Bnnn
            //jump to location nnn + V0
            int nnn = convertToInt(command.substring(1));
            pc = nnn + V[0];
        });

//        operations.put(OpCode.LDBVx)

        operations.put(OpCode.LDDTVx, () -> {
            //Fx15
            //set delay timer = Vx
            int vx = convertToInt(command.substring(1, 2));
            delayTimer = V[vx];
        });

//        operations.put(OpCode.LDFVx, () -> {})

        operations.put(OpCode.LDIAddr, () -> {
            //Annn
            //set I = nnn
            int n = convertToInt(command.substring(1));
            I = n;
        });

//        operations.put(OpCode.LDIVx, () -> )

        operations.put(OpCode.LDSTVx, () -> {
            //Fx18
            //set sound timer = Vx
            int vx = convertToInt(command.substring(1, 2));
            soundTimer = V[vx];
        });

        operations.put(OpCode.LDVx, () -> {
            //6xkk
            //set Vx = kk
            int vx = convertToInt(command.substring(1, 2));
            int kk = convertToInt(command.substring(2, 4));
            V[vx] = (short) kk;
        });

        operations.put(OpCode.LDVxDT, () -> {
            //Fx07
            //set Vx = delay timer value
            int vx = convertToInt(command.substring(1, 2));
            V[vx] = (short) delayTimer;
        });

//        operations.put(OpCode.LDVxI, () ->)

//        operations.put(OpCode.LDVxI, () ->)

//        operations.put(OpCode.LDVxK, () ->)

        operations.put(OpCode.LDVxVy, () -> {
            //8xy0
            //set Vx = Vy
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));
            V[vx] = V[vy];
        });

        operations.put(OpCode.ORVxVy, () -> {
            //8xy1
            //set Vx = Vx OR Vy
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));

            V[vx] = (short) (V[vx] | V[vy]);
        });

//        operations.put(OpCode.RET, () -> {})

        operations.put(OpCode.RNDVx, () -> {
            //Cxkk
            //set Vx = random byte AND kk
            int vx = convertToInt(command.substring(1, 2));
            int kk = convertToInt(command.substring(2, 4));
            int rnd = random.nextInt(256);
            V[vx] = (short) (kk & rnd);
        });

        operations.put(OpCode.SEVx, () -> {
            //3xkk
            //skip next instruction if Vx = kk
            int vx = convertToInt(command.substring(1, 2));
            int kk = convertToInt(command.substring(2, 4));

            if (V[vx] == kk) {
                pc += 2;
            }
        });

        operations.put(OpCode.SEVxVy, () -> {
            //5xy0
            //skip next instruction if Vx = Vy
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));

            if (V[vx] == V[vy]) {
                pc += 2;
            }
        });

//        operations.put(OpCode.SHLVx, () ->)

        operations.put(OpCode.SHRVx, () -> {
            //8xy6
            //set Vx = Vx SHR 1
            int vx = convertToInt(command.substring(1, 2));
            V[0xF] = (short) (V[vx] & 0x1);
            V[vx] = (short) (V[vx] >> 1);
        });

//        operations.put(OpCode.SKNPVx, () ->)

        operations.put(OpCode.SKPVx, () -> {
            //Ex9E
            //skip next instruction if key with the value of Vx is pressed
            int vx = convertToInt(command.substring(1, 2));
//            if(keyboard.isPressed())
        });

        operations.put(OpCode.SNEVx, () -> {
            //4xkk
            //skip next instruction if Vx != kk
            int vx = convertToInt(command.substring(1, 2));
            int kk = convertToInt(command.substring(2, 4));

            if (V[vx] != kk) {
                pc += 2;
            }
        });

        operations.put(OpCode.SNEVxVy, () -> {
            //9xy0
            //skip next instruction if Vx != Vy
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));
            if (V[vx] != V[vy]) {
                pc += 2;
            }
        });

        operations.put(OpCode.SUBNVxVy, () -> {
            //8xy7
            //set Vx = Vy - Vx, set VF = NOT borrow
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));

            if (V[vy] > V[vx]) {
                V[vx] = (short) (V[vy] - V[vx]);
                V[0xF] = 1;
            } else {
                V[vx] = (short) (256 + V[vy] - V[vx]);
                V[0xF] = 0;
            }
        });

        operations.put(OpCode.SUBVxVy, () -> {
            //8xy5
            //set Vx = Vx - Vy, set VF = NOT borrow
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));
            if (V[vx] > V[vy]) {
                V[vx] = (short) (V[vx] - V[vy]);
                V[0xF] = 1;
            } else {
                V[vx] = (short) (256 + V[vx] - V[vy]);
                V[0xF] = 0;
            }
        });

        operations.put(OpCode.XORVxVy, () -> {
            //8xy3
            //set Vx = Vx XOR Vy
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));

            V[vx] = (short) (V[vx] ^ V[vy]);
        });
    }

    class DRWVxVy implements Operation {
        //TODO:
        @Override
        public void processOpCode() {
            //Dxyn
            //display n-byte sprite starting at memory location I at (Vx, Vy), set Vf = collision
        }
    }

    class LDBVx implements Operation {
        //TODO:
        @Override
        public void processOpCode() {
            //Fx33
            //store BCD representation of Vx in memory locations I, I+1, and I+2
        }
    }

    class LDFVx implements Operation {
        //TODO:
        @Override
        public void processOpCode() {
            //Fx29
            //set I = location of sprite for digit Vx
        }
    }

    class LDIVx implements Operation {
        //TODO:
        @Override
        public void processOpCode() {
            //Fx55
            //store registers V0 through Vx in memory starting at location I
        }
    }

    class LDVxI implements Operation {
        //TODO:
        @Override
        public void processOpCode() {
            //Fx65
            //read registers V0 through Vx from memory starting at location I
        }
    }

    class LDVxK implements Operation {
        //TODO:
        @Override
        public void processOpCode() {
            //Fx0A
            //wait for a key press, store the value of the key in Vx
        }
    }

    class RET implements Operation {
        //TODO:
        @Override
        public void processOpCode() {
            //00EE
            //return from a subroutine
        }
    }

    class SHLVx implements Operation {
        //TODO:
        @Override
        public void processOpCode() {
            //8xyE
            //set Vx = Vx SHL 1
        }
    }

    class SKNPVx implements Operation {
        //TODO:
        @Override
        public void processOpCode() {
            //ExA1
            //skip next instruction if key with the value Vx is not pressed
        }
    }

    class SKPVx implements Operation {
        //TODO:
        @Override
        public void processOpCode() {
            //Ex9E
            //skip next instruction if key with the value of Vx is pressed
            String command = Operation.convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
//            if(keyboard.isPressed())
        }
    }
}