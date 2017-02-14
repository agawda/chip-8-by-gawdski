package opcodes;

public class SUBVxVy implements OpCode {
    @Override
    public void processOpCode() {
        //8xy5
        //set Vx = Vx - Vy, set VF = NOT borrow
    }
}
