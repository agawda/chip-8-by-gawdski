package opcodes;

public class ADDVxVy implements OpCode {
    @Override
    public void processOpCode() {
        //8xy4
        //set Vx = Vx + Vy, set VF = carry
    }
}
