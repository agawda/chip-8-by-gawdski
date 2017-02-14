package opcodes;

public class SUBNVxVy implements OpCode {
    @Override
    public void processOpCode() {
        //8xy7
        //set Vx = Vy - Vx, set VF = NOT borrow
    }
}
