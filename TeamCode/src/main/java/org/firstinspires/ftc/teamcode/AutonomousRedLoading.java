package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "AutonomousRedLoading", group = "FS")
// @Disabled
public class AutonomousRedLoading extends AutonomousCommon {
    AutoOperation [] opRedLoading_ = {
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_RESET_ENCODER, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TO_FIRST_SKYSTONE, 0),
            new AutoOperation(AutoOperation.OpCode.OP_GRAB_FIRST_SKYSTONE, 0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_FROM_FIRST_SKYSTONE_TO_FOUNDATION, 0),
            new AutoOperation(AutoOperation.OpCode.OP_MOVE_HOOK, LEFT_HOOK_PULL_DEGREE, RIGHT_HOOK_PULL_DEGREE, 1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_LEFT, 0.3),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 1.1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_RIGHT, 120),   // Overestimating is for compensating for the extra drag the foundation creates
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 0.8),
            new AutoOperation(AutoOperation.OpCode.OP_MOVE_HOOK, LEFT_HOOK_RELEASE_DEGREE, RIGHT_HOOK_RELEASE_DEGREE, 0.5),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.5),
            new AutoOperation(AutoOperation.OpCode.OP_STOP, 0)
    };

    @Override
    public void runOpMode() {
        opList_ = opRedLoading_;
        isRedTeam_ = true;
        super.runOpMode();
    }
}
