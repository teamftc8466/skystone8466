package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/*
Notes

As of 2/8/2020, this autonomous program:
1. Begins in the loading zone
2. Detects and grabs the Skystone furthest from the wall
3. Drives into the building zone
4. Drops the Skystone on the foundation
5. Pushes the foundation
6. Parks underneath the bridge

This program has finished testing and debugging.
*/

@Autonomous(name = "RedLoadingFull", group = "FS")
// @Disabled
public class AutonomousRedLoadingFull extends AutonomousCommon {
    AutoOperation [] opRedLoading_ = {
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_RESET_ENCODER, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_GEAR, 0.8),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TO_FIRST_SKYSTONE, 0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_GEAR, 1.0),
            new AutoOperation(AutoOperation.OpCode.OP_GRAB_STONE, 2),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_FROM_FIRST_SKYSTONE_TO_FOUNDATION, 0),
            new AutoOperation(AutoOperation.OpCode.OP_DROP_SKYSTONE_TO_FOUNDATION, 1),
            new AutoOperation(AutoOperation.OpCode.OP_MOVE_HOOK, (double)(Hooks.Position.PULL.getValue()), 1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_LEFT, 0.3),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.4),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_GEAR, 2.0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_RIGHT, 90),
            // new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_CORRECT_HEADING, 3, 0.6),
            new AutoOperation(AutoOperation.OpCode.OP_MOVE_HOOK, (double)(Hooks.Position.RELEASE.getValue()), 0.5),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_GEAR, 1.0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 0.45),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.45),
            new AutoOperation(AutoOperation.OpCode.OP_LIFT_MOVE_TO_BOTTOM_POSITION, 0),
            new AutoOperation(AutoOperation.OpCode.OP_GRABBER_CRANE_FULL_DRAW_BACK, 0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_LEFT, 0.20),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.6),
            // new AutoOperation(AutoOperation.OpCode.OP_GRABBER_CRANE_FULL_DRAW_BACK, 0),
            // new AutoOperation(AutoOperation.OpCode.OP_WAIT, 3),
            new AutoOperation(AutoOperation.OpCode.OP_STOP, 0)
    };

    /// The cetner position of robot is placed at the middle of second stone farthest from the wall
    //  in order to catch the first two stone farthest from the wall in the frame when the webcam
    //  is mounted at the left side of robot.
    ///
    /// Each row is associated with skystone position:
    ///   - Row 0 : skystone is the first farthest from the wall.
    ///   - Row 1 : skystone is the second farthest from the wall.
    ///   - Row 2 : skystone is the third farthest from the wall.
    ///
    //  At each row:
    ///   - First parameter: Distance to shift in order to align with the Skystone (neg. = left, pos. = right)
    ///   - Second parameter: Distance forward to Skystone
    ///   - Third parameter: Distance to foundation after collecting Skystone (the turning angle towards the foundation is constant for all three positions of the Skystones, so we do not need a fourth parameter)
    double [][] redGrabFirstSkystone_ = {{ 0.05, 0.745, 1.95},
                                         {-0.1, 0.745, 2.1},
                                         {-0.3, 0.75, 2.35}};

    @Override
    public void runOpMode() {
        useTensorFlowToDetectSkystone_ = true;
        useImu_ = true;
        controlTurnDegreeByEncoderCnt_ = false;
        autoCorrectHeadingDuringDriving_ = true;
        // useShiftToDeliverStoneToFoundation_ = true;
        initLiftGrabberToCatchPosition_ = true;

        opList_ = opRedLoading_;
        isRedTeam_ = true;
        grabFirstSkystone_ = redGrabFirstSkystone_;

        super.runOpMode();
    }
}