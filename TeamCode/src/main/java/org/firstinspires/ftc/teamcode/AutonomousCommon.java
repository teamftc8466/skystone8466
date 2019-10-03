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
        telemetry.addData(">", "Press Play to start automous");
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

    @Override
    public void initialize() {
        super.initialize();

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
