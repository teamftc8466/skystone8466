package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name = "AutonomousExample", group = "FS")
// @Disabled
public class AutonomousExample extends AutonomousCommon {

    AutoOperation [] opExample_ = {
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_RESET_ENCODER, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_LEFT, 0.15),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 1.0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.15),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_RIGHT, 90),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 2.95),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_LEFT, 90),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 0.15),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_RIGHT, 90),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.5),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_LEFT, 180),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.2),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_RIGHT, 180),
            new AutoOperation(AutoOperation.OpCode.OP_STOP, 0.1)
    };

    @Override
    public void runOpMode() {
        useImu_ = true;
        opList_ = opExample_;
        super.runOpMode();
    }
}
