package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "AutonomousBlueLoading", group = "FS")
// @Disabled
public class AutonomousBlueLoading extends AutonomousCommon {
    AutoOperation [] opBlueLoading= {
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_RESET_ENCODER, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 0.15),
            new AutoOperation(AutoOperation.OpCode.OP_MOVE_HOOK, LEFT_HOOK_PULL_DEGREE, RIGHT_HOOK_PULL_DEGREE, 1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_RIGHT, 0.3),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 1.1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_LEFT, 120),   // Overestimating is for compensating for the extra drag the foundation creates
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 0.8),
            new AutoOperation(AutoOperation.OpCode.OP_MOVE_HOOK, LEFT_HOOK_RELEASE_DEGREE, RIGHT_HOOK_RELEASE_DEGREE, 0.5),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.5),
            new AutoOperation(AutoOperation.OpCode.OP_STOP, 0.1)
    };

    @Override
    public void runOpMode() {
        opList_ = opBlueLoading;
        isRedTeam_ = false;         // Remember to set this variable to false for all blue side programs!
        super.runOpMode();
    }
}
