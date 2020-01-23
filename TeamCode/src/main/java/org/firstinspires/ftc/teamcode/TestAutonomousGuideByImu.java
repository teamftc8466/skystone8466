package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

///This program is for testing the encoder to meter ratio.

@Autonomous(name = "TestAutonomousGuideByImu", group = "FS")
// @Disabled
public class TestAutonomousGuideByImu extends AutonomousCommon {

    AutoOperation [] opEncoderTest_ = {
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_RESET_ENCODER, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_LEFT, 90),
            new AutoOperation(AutoOperation.OpCode.OP_WAIT, 3),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_RIGHT, 90),
            new AutoOperation(AutoOperation.OpCode.OP_WAIT, 3),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 1.5),
            new AutoOperation(AutoOperation.OpCode.OP_WAIT, 3),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_BACKWARD, 1.5),
            new AutoOperation(AutoOperation.OpCode.OP_WAIT, 3),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_RIGHT, 180),
            new AutoOperation(AutoOperation.OpCode.OP_WAIT, 3),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_TURN_LEFT, 180),
            new AutoOperation(AutoOperation.OpCode.OP_STOP, 0.1)
    };

    @Override
    public void runOpMode() {
        useImu_ = true;
	    autoCorrectHeadingDuringDriving_ = true;
        isRedTeam_ = true;

        opList_ = opEncoderTest_;
        super.runOpMode();
    }
}
