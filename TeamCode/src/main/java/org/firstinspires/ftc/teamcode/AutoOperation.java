package org.firstinspires.ftc.teamcode;

public class AutoOperation {
    enum OpCode {
        OP_STOP,
        OP_WAIT,                                       // One operand: Waiting time (>=0)
        OP_DRIVE_TRAIN_RESET_ENCODER,                  // One operand: time spent to reset encoder (>=0)
        OP_DRIVE_TRAIN_SHIFT_GEAR,                     // One operand: multiple factor for driving train power (>0)
        OP_DRIVE_TRAIN_FORWARD,                        // Two operands: 1. driving distance in meter (>=0); 2. Optional power factor.
        OP_DRIVE_TRAIN_BACKWARD,                       // Two operands: 1. driving distance in meter (>=0); 2. Optional power factor.
        OP_DRIVE_TRAIN_TURN_LEFT,                      // Two operands: 1. turning degree (>=0); 2. Optional power factor.
        OP_DRIVE_TRAIN_TURN_RIGHT,                     // Two operands: 1. turning degree (>=0); 2. Optional power factor.
        OP_DRIVE_TRAIN_SHIFT_LEFT,                     // Two operands: 1. shifting distance in meter (>=0); 2. Optional power factor.
        OP_DRIVE_TRAIN_SHIFT_RIGHT,                    // Two operands: 1. shifting distance in meter (>=0); 2. Optional power factor.
        OP_DRIVE_TRAIN_CORRECT_HEADING,                // Three operands: 1. Max. tolerated error in degree;
                                                       //                 2. Min. reduce power factor;
                                                       //                 3. Optional power factor.
        OP_MOVE_HOOK,                                  // One operand: Hook position.
        OP_DRIVE_TO_FIRST_SKYSTONE,                    // No operand needed
        OP_GRAB_STONE,                                 // One operand: maximum allowed time spent by this operation
        OP_DRIVE_FROM_FIRST_SKYSTONE_TO_FOUNDATION,    // No operand needed
        OP_GRABBER_CRANE_FULL_DRAW_BACK,               // No operand needed
        OP_LIFT_MOVE_TO_BOTTOM_POSITION,               // No operand needed
        OP_GRAB_STONE_READY_POSITION,                  // No operand needed
        OP_DROP_SKYSTONE_TO_FOUNDATION,                // One operand: time spent to drop skystone
        OP_DROP_STONE_TO_GROUND,                        // One operand: time spent to drop stone
        OP_DETECT_SKYSTONE,                             // One operand: time limit for detection of stone
        OP_DRIVE_SKYSTONE_CLOSE_TO_WALL                    // no operand
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
