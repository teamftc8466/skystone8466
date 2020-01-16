package org.firstinspires.ftc.teamcode;

public class AutonomousBlueBuildingParkingWall extends AutonomousCommon {
    AutoOperation [] opBlueBuilding= {
            new AutoOperation(AutoOperation.OpCode.OP_WAIT, 25),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 15),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_LEFT, 50),
            new AutoOperation(AutoOperation.OpCode.OP_STOP, 0)
    };

    @Override
    public void runOpMode() {
        // useImu_ = true;
        opList_ = opBlueBuilding;
        isRedTeam_ = false;         // Remember to set this variable to false for all blue side programs!
        // grabFirstSkystone_ = blueGrabSecondSkystone_;

        super.runOpMode();
    }
}
