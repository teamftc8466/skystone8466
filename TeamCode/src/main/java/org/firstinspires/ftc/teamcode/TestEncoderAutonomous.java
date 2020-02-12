package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

///This program is for testing the encoder to meter ratio.

@Autonomous(name = "TestAutonomousEncoder", group = "FS")
 @Disabled
public class TestEncoderAutonomous extends AutonomousCommon {

    AutoOperation [] opEncoderTest_ = {
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_RESET_ENCODER, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 1.0),
            new AutoOperation(AutoOperation.OpCode.OP_WAIT, 5),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 1.0),
            new AutoOperation(AutoOperation.OpCode.OP_WAIT, 5),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_LEFT, 90),
            new AutoOperation(AutoOperation.OpCode.OP_WAIT, 5),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_RIGHT, 1.0),
            new AutoOperation(AutoOperation.OpCode.OP_WAIT, 5),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_RIGHT, 90),
            new AutoOperation(AutoOperation.OpCode.OP_WAIT, 5),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_SHIFT_LEFT, 1.0),
            new AutoOperation(AutoOperation.OpCode.OP_STOP, 0.1)
    };

    @Override
    public void runOpMode() {
        useImu_ = false;
        controlTurnDegreeByEncoderCnt_ = true;
        autoCorrectHeadingDuringDriving_ = false;
        initLiftGrabberToCatchPosition_ = false;

        isRedTeam_ = true;

        opList_ = opEncoderTest_;
        super.runOpMode();
    }
}
