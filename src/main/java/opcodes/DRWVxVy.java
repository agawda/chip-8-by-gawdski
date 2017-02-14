package opcodes;

public class DRWVxVy implements OpCode {
    @Override
    public void processOpCode() {
        //Dxyn
        //display n-byte sprite starting at memory location I at (Vx, Vy), set Vf = collision
    }
}
