package com.gawdski.chip8bygawdski;

public class CPU {
    //chip-8 programs don't access memory below this point
    private final int PC_START = 0x200;
    private final int REGISTERS_NUM = 16;

    private int pc; //program counter
    private int I; //index register
    private int sp; //stack pointer

    private int soundTimer;
    private int delayTimer;

    private Keyboard keyboard;
    private Memory memory;
    private Screen screen;

    private OpCode[] opCodes;
    private short[] V; //CPU registers
    private short[] stack;




    private interface OpCode {
        static String convert(int n) {
            return Integer.toHexString(n);
        }

        void processOpCode();
    }

    private class ADDIVx implements OpCode {
        @Override
        public void processOpCode() {
            //Fx1E
            //set I = I + Vx
        }
    }

    class ADDVx implements OpCode {
        @Override
        public void processOpCode() {
            //7xkk
            //set Vx = Vx + kk
        }
    }

    class ADDVxVy implements OpCode {
        @Override
        public void processOpCode() {
            //8xy4
            //set Vx = Vx + Vy, set VF = carry
        }
    }

    class ANDVxVy implements OpCode {
        @Override
        public void processOpCode() {
            //8xy2
            //set Vx = Vx AND Vy
        }
    }

    class CALLAddr implements OpCode {
        @Override
        public void processOpCode() {
            //2nnn
            //call subroutine at nnn
        }
    }

    class CLS implements OpCode {
        @Override
        public void processOpCode() {
            //clear display
            screen.clearDisplay();
        }
    }

    class DRWVxVy implements OpCode {
        @Override
        public void processOpCode() {
            //Dxyn
            //display n-byte sprite starting at memory location I at (Vx, Vy), set Vf = collision
        }
    }

    class JPAddr implements OpCode {
        @Override
        public void processOpCode() {
            //1nnn
            //jump to location nnn
        }
    }

    class JPV0Addr implements OpCode {
        @Override
        public void processOpCode() {
            //Bnnn
            //jump to location nnn + V0
        }
    }

    class LDBVx implements OpCode {
        @Override
        public void processOpCode() {
            //Fx33
            //store BCD representation of Vx in memory locations I, I+1, and I+2
        }
    }

    class LDDTVx implements OpCode {
        @Override
        public void processOpCode() {
            //Fx15
            //set delay timer = Vx
        }
    }

    class LDFVx implements OpCode {
        @Override
        public void processOpCode() {
            //Fx29
            //set I = location of sprite for digit Vx
        }
    }

    class LDIAddr implements OpCode {
        @Override
        public void processOpCode() {
            //Annn
            //set I = nnn
        }
    }

    class LDIVx implements OpCode {
        @Override
        public void processOpCode() {
            //Fx55
            //store registers V0 through Vx in memory starting at location I
        }
    }

    class LDSTVx implements OpCode {
        @Override
        public void processOpCode() {
            //Fx18
            //set sound timer = Vx
        }
    }

    class LDVx implements OpCode {
        @Override
        public void processOpCode() {
            //6xkk
            //set Vx = kk
        }
    }

    class LDVxDT implements OpCode {
        @Override
        public void processOpCode() {
            //Fx07
            //set Vx = delay timer value
        }
    }

    class LDVxI implements OpCode {
        @Override
        public void processOpCode() {
            //Fx65
            //read registers V0 through Vx from memory starting at location I
        }
    }

    class LDVxK implements OpCode {
        @Override
        public void processOpCode() {
            //Fx0A
            //wait for a key press, store the value of the key in Vx
        }
    }

    class LDVxVy implements OpCode {
        @Override
        public void processOpCode() {
            //8xy0
            //set Vx = Vy
        }
    }

    class ORVxVy implements OpCode {
        @Override
        public void processOpCode() {
            //8xy1
            //set Vx = Vx OR Vy
        }
    }

    class RET implements OpCode {
        @Override
        public void processOpCode() {
            //00EE
            //return from a subroutine
        }
    }

    class RNDVx implements OpCode {
        @Override
        public void processOpCode() {
            //Cxkk
            //set Vx = random byte AND kk
        }
    }

    class SEVx implements OpCode {
        @Override
        public void processOpCode() {
            //3xkk
            //skip next instruction if Vx = kk
        }
    }

    class SEVxVy implements OpCode {
        @Override
        public void processOpCode() {
            //5xy0
            //skip next instruction if Vx = Vy
        }
    }

    class SHLVx implements OpCode {
        @Override
        public void processOpCode() {
            //8xyE
            //set Vx = Vx SHL 1
        }
    }

    class SHRVx implements OpCode {
        @Override
        public void processOpCode() {
            //8xy6
            //set Vx = Vx SHR 1
        }
    }

    class SKNPVx implements OpCode {
        @Override
        public void processOpCode() {
            //ExA1
            //skip next instruction if key with the value Vx is not pressed
        }
    }

    class SKPVx implements OpCode {
        @Override
        public void processOpCode() {
            //Ex9E
            //skip next instruction if key with the value of Vx is pressed
        }
    }

    class SNEVx implements OpCode {
        @Override
        public void processOpCode() {
            //4xkk
            //skip next instruction if Vx != kk
        }
    }

    class SNEVxVy implements OpCode {
        @Override
        public void processOpCode() {
            //9xy0
            //skip next instruction if Vx != Vy
        }
    }

    class SUBNVxVy implements OpCode {
        @Override
        public void processOpCode() {
            //8xy7
            //set Vx = Vy - Vx, set VF = NOT borrow
        }
    }

    class SUBVxVy implements OpCode {
        @Override
        public void processOpCode() {
            //8xy5
            //set Vx = Vx - Vy, set VF = NOT borrow
        }
    }

    class XORVxVy implements OpCode {
        @Override
        public void processOpCode() {
            //8xy3
            //set Vx = Vx XOR Vy
        }
    }

}