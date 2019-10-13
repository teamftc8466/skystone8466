package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="AutonomousCommon", group="FS")
// @Disabled
public class AutonomousCommon extends RobotHardware {

    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start autonomous");
        telemetry.update();

        waitForStart();

        initializeWhenStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                getDetectSkystone().detectSkystone();
            }
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        initializeAutonomous();

        // Activate Tfod for detecting skystone
        getDetectSkystone().setupTfod();
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        getDetectSkystone().shutdownTfod();
    }
}
