package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TestLift", group="FS")
@Disabled
public class TestLift extends RobotHardware {

    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start test lift program");
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
        createLift();
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        // TBD
    }
}
