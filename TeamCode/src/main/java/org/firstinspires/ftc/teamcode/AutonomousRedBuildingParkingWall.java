package org.firstinspires.ftc.teamcode;

// TODO: This program has not been tested nor debugged.

/* Note to the software team: Units are either in degrees or in METERS!
Please remember this so that the robot doesn't try to move 50 meters during testing :) */

public class AutonomousRedBuildingParkingWall extends AutonomousCommon {
    AutoOperation [] opRedBuilding= {
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_RESET_ENCODER, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_WAIT, 25),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 0.05),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_LEFT, 0.5),
            new AutoOperation(AutoOperation.OpCode.OP_STOP, 0)
    };

    @Override
    public void runOpMode() {
        useImu_ = true;
        controlTurnDegreeByEncoderCnt_ = true;
        autoCorrectHeadingDuringDriving_ = true;
        // useShiftToDeliverStoneToFoundation_ = true;
        initLiftGrabberToCatchPosition_ = false;

        opList_ = opRedBuilding;
        isRedTeam_ = true;         // Remember to set this variable to false for all blue side programs!
        // grabFirstSkystone_ = blueGrabSecondSkystone_;

        super.runOpMode();
    }
}
