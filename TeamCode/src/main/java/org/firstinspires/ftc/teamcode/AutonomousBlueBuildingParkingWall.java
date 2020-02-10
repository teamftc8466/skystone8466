package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/*
Notes

As of 2/5/2020, this autonomous program:
1. Begins in the building zone
2. Parks underneath the bridge

SOFTWARE TEAM, PLEASE READ!
Units are either in degrees or in METERS!
Please remember this so that the robot doesn't try to move 50 meters during testing :)
*/

// TODO: This program has not been tested nor debugged.

@Autonomous(name = "BlueBuildingParkingWall", group = "FS")
// @Disabled
public class AutonomousBlueBuildingParkingWall extends AutonomousCommon {
    AutoOperation [] opBlueBuilding= {
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_RESET_ENCODER, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_WAIT, 25),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 0.05),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_RIGHT, 0.3),
            new AutoOperation(AutoOperation.OpCode.OP_STOP, 0)
    };

    @Override
    public void runOpMode() {
        useImu_ = true;
        controlTurnDegreeByEncoderCnt_ = true;
        autoCorrectHeadingDuringDriving_ = true;
        // useShiftToDeliverStoneToFoundation_ = true;
        initLiftGrabberToCatchPosition_ = false;

        opList_ = opBlueBuilding;
        isRedTeam_ = true;         // Remember to set this variable to false for all blue side programs!
        // grabFirstSkystone_ = blueGrabSecondSkystone_;

        super.runOpMode();
    }
}
