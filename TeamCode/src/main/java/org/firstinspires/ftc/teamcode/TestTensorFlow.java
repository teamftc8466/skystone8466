package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TestTensorFlow", group = "FS")
// @Disabled
public class TestTensorFlow extends RobotHardware {

    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start test color program");
        telemetry.update();

        detectSkystone_.setupTfod();

        waitForStart();

        initializeWhenStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                detectSkystone_.detectSkystone();
                telemetry.update();
            }
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        createDetectSkystone();
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        // TBD
    }
}
