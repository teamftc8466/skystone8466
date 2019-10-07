package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TeleOpCommon", group="FS")
// @Disabled
public class TeleOpCommon extends RobotHardware {

    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start TeleOp");
        telemetry.update();

        waitForStart();

        initializeWhenStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                //TBD
            }
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        initializeTeleOp();
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        // TBD
    }
}
