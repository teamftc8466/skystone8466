package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TestColor", group="FS")
// @Disabled
public class TestColor extends RobotHardware {

    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start test color program");
        telemetry.update();

        waitForStart();

        initializeWhenStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                color_.colorDebug();
                telemetry.update();
            }
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        createColor();
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        // TBD
    }
}
