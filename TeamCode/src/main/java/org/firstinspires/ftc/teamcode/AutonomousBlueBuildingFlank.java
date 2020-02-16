package org.firstinspires.ftc.teamcode;


//1. Start at build zone
//2. Shift to corner (player base corner)
//3.
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "BlueBuildingBackDoorStone", group = "FS")
// @Disabled
public class AutonomousBlueBuildingFlank extends AutonomousCommon {
    AutoOperation [] opBlueBuildingFlank = {
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_RESET_ENCODER, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_RIGHT, 2.37),
            new AutoOperation(AutoOperation.OpCode.OP_DETECT_SKYSTONE, 0.5),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_SKYSTONE_CLOSE_TO_WALL, 0),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_LEFT,1.78)
    };


    double [][] blueFlankGrabSkystone = {{-0.5, 0.75, 1.2},
                                        {-0.25, 0.75, 1.0},
                                        {-0.25, 0.75, 1.0}};

    @Override
    public void runOpMode() {
        useTensorFlowToDetectSkystone_ = true;
        useImu_ = true;
        controlTurnDegreeByEncoderCnt_ = true;
        autoCorrectHeadingDuringDriving_ = true;
        // useShiftToDeliverStoneToFoundation_ = true;
        initLiftGrabberToCatchPosition_ = true;

        opList_ = opBlueBuildingFlank;
        isRedTeam_ = false;         // Remember to set this variable to false for all blue side programs!
        grabFirstSkystone_ = blueFlankGrabSkystone;

        super.runOpMode();
    }
}
