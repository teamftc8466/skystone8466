package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TestRevImu", group="FS")
@Disabled
public class TestRevImu extends RobotHardware {

    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData("initial Imu Heading", String.valueOf(imu_.getHeading()));
        telemetry.addData(">", "Press Play to start test IMU program");
        telemetry.update();

        waitForStart();

        initializeWhenStart();

        while (opModeIsActive()) {
            double heading = getImu().getHeading();

            double heading_diff = getImu().getHeadingDifference(-90);
            telemetry.addData("Imu", "heading="+ String.valueOf(heading) + " heading_diff=" + String.valueOf(heading_diff));
            telemetry.update();

            // imu_.showHeading(true);
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        createImu();
    }

    void initializeWhenStart() {
        // timer_.reset();
        // currTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        // TBD
    }
}
