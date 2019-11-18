package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name = "AutonomousExample", group = "FS")
@Disabled
public class AutonomousExample extends AutonomousCommon {

    AutoOperation [] opExample_ = {
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_RESET_ENCODER, 0.1),
            new AutoOperation(AutoOperation.OpCode.OP_DRIVE_TRAIN_FORWARD, 0.5),
            new AutoOperation(AutoOperation.OpCode.OP_STOP, 0.1)
    };

    AutonomousExample () {
        opList_ = opExample_;
    }

    @Override
    public void runOpMode() {
        super.runOpMode();
    }
}
