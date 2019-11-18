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
        OP_DRIVE_TRAIN_SHIFT_RIGHT
};
    private OpCode opcode_ = OpCode.OP_STOP;
    private double operand_;

    public AutoOperation (OpCode opcode,
                          double operand) {
        opcode_ = opcode;
        operand_ = operand;
    }

    OpCode opcode() { return opcode_; }

    double operand() { return operand_; }
}
