package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/*
Notes

As of 2/9/2020, this autonomous program:
1. Begins in the building zone
2. Moves the foundation
3. Drives into the loading zone
4. Grabs the stone second closest to the wall
5. Drops the stone in the building zone
6. Parks underneath the bridge
*/

// TODO: This programhas not been debugged nor tested.
// DO NOT USE THIS PROGRAM!

@Autonomous(name = "BlueBuildingFull", group = "FS")
@Disabled
public class AutonomousBlueBuildingFull extends AutonomousCommon {
    AutoOperation [] opBlueLoading= {
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_RESET_ENCODER, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_LEFT, 0.2),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 0.743),
            new AutoOperation(AutoOperation.OpCode.OP_MOVE_HOOK, (double)(Hooks.Position.PULL.getValue()), 1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_RIGHT, 0.3),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.4),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_GEAR, 2.0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_LEFT, 90),
            new AutoOperation(AutoOperation.OpCode.OP_MOVE_HOOK, (double)(Hooks.Position.RELEASE.getValue()), 0.5),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_GEAR, 1.0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 0.45),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.45),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_LEFT, 0.45),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_RIGHT, 0.03),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_GEAR, 2.0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 1.45),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_GEAR, 1.0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_RIGHT, 0.2),
            new AutoOperation(AutoOperation.OpCode.OP_GRAB_STONE_READY_POSITION, 0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_RIGHT, 90),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 0.55),
            new AutoOperation(AutoOperation.OpCode.OP_GRAB_STONE, 2),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.55),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_LEFT, 90),
            // new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_RIGHT, 0.2),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_GEAR, 2.0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 1.85),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_GEAR, 1.0),
            new AutoOperation(AutoOperation.OpCode.OP_DROP_STONE_TO_GROUND, 1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_LIFT_MOVE_TO_BOTTOM_POSITION, 0),
            new AutoOperation(AutoOperation.OpCode.OP_GRABBER_CRANE_FULL_DRAW_BACK, 0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.3),
            // new AutoOperation(AutoOperation.OpCode.OP_WAIT, 3),
            new AutoOperation(AutoOperation.OpCode.OP_STOP, 0)
    };

    @Override
    public void runOpMode() {
        useImu_ = true;
        controlTurnDegreeByEncoderCnt_ = true;
        autoCorrectHeadingDuringDriving_ = true;
        // useShiftToDeliverStoneToFoundation_ = true;
        initLiftGrabberToCatchPosition_ = true;

        opList_ = opBlueLoading;
        isRedTeam_ = false;         // Remember to set this variable to false for all blue side programs!

        super.runOpMode();
    }
}
