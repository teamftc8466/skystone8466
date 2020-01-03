package org.firstinspires.ftc.teamcode;

public class AutoOperation {
    enum OpCode {
        OP_STOP,
        OP_WAIT,
        OP_DRIVE_TRAIN_RESET_ENCODER,
        OP_DRIVE_TRAIN_RESET_HEADING,
        OP_DRIVE_TRAIN_SHIFT_GEAR,
        OP_DRIVE_TRAIN_FORWARD,
        OP_DRIVE_TRAIN_BACKWARD,
        OP_DRIVE_TRAIN_TURN_LEFT,
        OP_DRIVE_TRAIN_TURN_RIGHT,
        OP_DRIVE_TRAIN_SHIFT_LEFT,
        OP_DRIVE_TRAIN_SHIFT_RIGHT,
        OP_MOVE_HOOK
};
    private OpCode opcode_ = OpCode.OP_STOP;
    private double [] operand_ = {0, 0, 0};

    public AutoOperation(OpCode opcode,
                         double operand) {
        opcode_ = opcode;
        operand_[0] = operand;
    }

    public AutoOperation(OpCode opcode,
                         double operand1,
                         double operand2) {
        opcode_ = opcode;
        operand_[0] = operand1;
        operand_[1] = operand2;
    }

    public AutoOperation(OpCode opcode,
                         double operand1,
                         double operand2,
                         double operand3) {
        opcode_ = opcode;
        operand_[0] = operand1;
        operand_[1] = operand2;
        operand_[2] = operand3;
    }

    OpCode opcode() { return opcode_; }

    double operand(int index) { return operand_[index]; }
}
