package org.firstinspires.ftc.teamcode;

public class AutoOperation {
    private int opcode_ = 0;
    private double operand_;

    public AutoOperation (int opcode,
                          double operand) {
        opcode_ = opcode;
        operand_ = operand;
    }

    int opcode() { return opcode_; }

    double operand() { return operand_; }
}
