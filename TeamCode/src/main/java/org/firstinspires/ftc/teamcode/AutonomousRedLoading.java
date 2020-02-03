package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "AutonomousRedLoading", group = "FS")
// @Disabled
public class AutonomousRedLoading extends AutonomousCommon {
    AutoOperation [] opRedLoading_ = {
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_RESET_ENCODER, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TO_FIRST_SKYSTONE, 0),
            new AutoOperation(AutoOperation.OpCode.OP_GRAB_FIRST_SKYSTONE, 2),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_FROM_FIRST_SKYSTONE_TO_FOUNDATION, 0),
            new AutoOperation(AutoOperation.OpCode.OP_DROP_SKYSTONE_TO_FOUNDATION, 1),
            new AutoOperation(AutoOperation.OpCode.OP_MOVE_HOOK, (double)(Hooks.Position.PULL.getValue()), 1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_LEFT, 0.3),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.6),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_GEAR, 3.0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_RIGHT, 90, 0.5),  // With load of foundation, should not reduce the driving power too much during correcting heading
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_GEAR, 1.0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 0.65),
            new AutoOperation(AutoOperation.OpCode.OP_MOVE_HOOK, (double)(Hooks.Position.RELEASE.getValue()), 0.5),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 0.45),
            new AutoOperation(AutoOperation.OpCode.OP_LIFT_MOVE_TO_BOTTOM_POSITION, 0),
            new AutoOperation(AutoOperation.OpCode.OP_GRABBER_CRANE_FULL_DRAW_BACK, 0),
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
    ///   - First parameter: Distance to shift in order to align with the Skystone
    ///   - Second parameter: Distance forward to Skystone
    ///   - Third parameter: Distance to foundation after collecting Skystone (the turning angle towards the foundation is constant for all three positions of the Skystones, so we do not need a fourth parameter)
    double [][] redGrabFirstSkystone_ = {{ 0.05, 0.72, 1.95},           // TODO: Determine actual measurements
                                         {-0.15, 0.72, 2.15},
                                         {-0.35, 0.72, 2.35}};

    @Override
    public void runOpMode() {
        useImu_ = true;
        autoCorrectHeadingDuringDriving_ = true;
        // useShiftToDeliverStoneToFoundation_ = true;
        initLiftGrabberToCatchPosition_ = true;

        opList_ = opRedLoading_;
        isRedTeam_ = true;
        grabFirstSkystone_ = redGrabFirstSkystone_;

        super.runOpMode();
    }
}