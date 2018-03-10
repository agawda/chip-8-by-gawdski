package com.gawdski.chip8bygawdski;

import com.gawdski.chip8bygawdski.graphics.Screen;

import java.util.Map;
import java.util.Random;

public class CPU {
    //chip-8 programs don't access memory below this point
    private static final int PC_START = 0x200;
    private static final int REGISTERS_NUM = 16;
    private static final long DEFAULT_CPU_CYCLE_TIME = 2;
    private static final short DEFAULT_STACK_SIZE = 16;

    private int pc; //program counter
    private int I; //index register
    private int sp; //stack pointer

    private int soundTimer;
    private int delayTimer;

    private short opc; //current opcode read from memory

    private Keyboard keyboard;
    private Memory memory;
    private Screen screen;

    private final Map<OpCode, Operation> operations;
    private short[] V; //CPU registers
    private short[] stack;

    private Random random;

    public CPU(Screen screen, Map<OpCode, Operation> operations) {
        this.screen = screen;
        this.operations = operations;

        random = new Random();
        memory = new Memory();

        stack = new short[DEFAULT_STACK_SIZE];

        V = new short[REGISTERS_NUM];

        pc = PC_START;
    }



    private class ADDIVx implements Operation {
        @Override
        public void processOpCode() {
            //Fx1E
            //set I = I + Vx
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            I += V[vx];
        }
    }

    class ADDVx implements Operation {
        @Override
        public void processOpCode() {
            //7xkk
            //set Vx = Vx + kk
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            int kk = convertToInt(command.substring(2, 4));
            V[vx] = (short) (V[vx] + kk);
        }
    }

    class ADDVxVy implements Operation {
        @Override
        public void processOpCode() {
            //8xy4
            //set Vx = Vx + Vy, set VF = carry
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));
            int sum = V[vx] + V[vy];
            if(sum > 255) {
                V[vx] = (short) (sum - 256);
                V[0xF] = 1;
            } else {
                V[vx] = (short) sum;
                V[0xF] = 0;
            }
        }
    }

    class ANDVxVy implements Operation {
        @Override
        public void processOpCode() {
            //8xy2
            //set Vx = Vx AND Vy
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));

            V[vx] = (short) (V[vx] & V[vy]);
        }
    }

    class CALLAddr implements Operation {
        @Override
        public void processOpCode() {
            //2nnn
            //call subroutine at nnn
            String command = convertToHexString(opc);
            int nnn = convertToInt(command.substring(1));

            stack[sp++] = (short) pc;
            pc = nnn;
        }
    }

    class CLS implements Operation {
        @Override
        public void processOpCode() {
            //clear display
            screen.clearDisplay();
        }
    }

    class DRWVxVy implements Operation {
        //TODO:
        @Override
        public void processOpCode() {
            //Dxyn
            //display n-byte sprite starting at memory location I at (Vx, Vy), set Vf = collision
        }
    }

    class JPAddr implements Operation {
        @Override
        public void processOpCode() {
            //1nnn
            //jump to location nnn
            String command = convertToHexString(opc);
            int address = convertToInt(command.substring(1));
            pc = address;
        }
    }

    class JPV0Addr implements Operation {
        @Override
        public void processOpCode() {
            //Bnnn
            //jump to location nnn + V0
            String command = convertToHexString(opc);
            int nnn = convertToInt(command.substring(1));
            pc = nnn + V[0];
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

    class LDDTVx implements Operation {
        @Override
        public void processOpCode() {
            //Fx15
            //set delay timer = Vx
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            delayTimer = V[vx];
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

    class LDIAddr implements Operation {
        @Override
        public void processOpCode() {
            //Annn
            //set I = nnn
            String command = convertToHexString(opc);
            int n = convertToInt(command.substring(1));
            I = n;
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

    class LDSTVx implements Operation {
        @Override
        public void processOpCode() {
            //Fx18
            //set sound timer = Vx
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            soundTimer = V[vx];
        }
    }

    class LDVx implements Operation {
        @Override
        public void processOpCode() {
            //6xkk
            //set Vx = kk
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            int kk = convertToInt(command.substring(2, 4));
            V[vx] = (short) kk;
        }
    }

    class LDVxDT implements Operation {
        @Override
        public void processOpCode() {
            //Fx07
            //set Vx = delay timer value
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            V[vx] = (short) delayTimer;
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

    class LDVxVy implements Operation {
        @Override
        public void processOpCode() {
            //8xy0
            //set Vx = Vy
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));
            V[vx] = V[vy];
        }
    }

    class ORVxVy implements Operation {
        @Override
        public void processOpCode() {
            //8xy1
            //set Vx = Vx OR Vy
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));

            V[vx] = (short) (V[vx] | V[vy]);
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

    class RNDVx implements Operation {
        @Override
        public void processOpCode() {
            //Cxkk
            //set Vx = random byte AND kk
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            int kk = convertToInt(command.substring(2, 4));
            int rnd = random.nextInt(256);
            V[vx] = (short) (kk & rnd);
        }
    }

    class SEVx implements Operation {
        @Override
        public void processOpCode() {
            //3xkk
            //skip next instruction if Vx = kk
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            int kk = convertToInt(command.substring(2, 4));

            if (V[vx] == kk) {
                pc += 2;
            }
        }
    }

    class SEVxVy implements Operation {
        @Override
        public void processOpCode() {
            //5xy0
            //skip next instruction if Vx = Vy
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));

            if(V[vx] == V[vy]) {
                pc += 2;
            }
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

    class SHRVx implements Operation {
        @Override
        public void processOpCode() {
            //8xy6
            //set Vx = Vx SHR 1
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            V[0xF] = (short) (V[vx] & 0x1);
            V[vx] = (short) (V[vx] >> 1);
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
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
//            if(keyboard.isPressed())
        }
    }

    class SNEVx implements Operation {
        @Override
        public void processOpCode() {
            //4xkk
            //skip next instruction if Vx != kk
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            int kk = convertToInt(command.substring(2, 4));

            if(V[vx] != kk) {
                pc += 2;
            }
        }
    }

    class SNEVxVy implements Operation {
        @Override
        public void processOpCode() {
            //9xy0
            //skip next instruction if Vx != Vy
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));
            if(V[vx] != V[vy]) {
                pc += 2;
            }
        }
    }

    class SUBNVxVy implements Operation {
        @Override
        public void processOpCode() {
            //8xy7
            //set Vx = Vy - Vx, set VF = NOT borrow
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));

            if(V[vy] > V[vx]) {
                V[vx] = (short) (V[vy] - V[vx]);
                V[0xF] = 1;
            } else {
                V[vx] = (short) (256 + V[vy] - V[vx]);
                V[0xF] = 0;
            }
        }
    }

    class SUBVxVy implements Operation {
        @Override
        public void processOpCode() {
            //8xy5
            //set Vx = Vx - Vy, set VF = NOT borrow
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));
            if(V[vx] > V[vy]) {
                V[vx] = (short) (V[vx] - V[vy]);
                V[0xF] = 1;
            } else {
                V[vx] = (short) (256 + V[vx] - V[vy]);
                V[0xF] = 0;
            }
        }
    }

    class XORVxVy implements Operation {
        @Override
        public void processOpCode() {
            //8xy3
            //set Vx = Vx XOR Vy
            String command = convertToHexString(opc);
            int vx = convertToInt(command.substring(1, 2));
            int vy = convertToInt(command.substring(2, 3));

            V[vx] = (short) (V[vx] ^ V[vy]);
        }
    }

}