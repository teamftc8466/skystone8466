package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TestRevImu", group="FS")
// @Disabled
public class TestRevImu extends LinearOpMode {

    RevImu imu_ = null;

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
            double heading = imu_.getHeading();

            double heading_diff = imu_.getHeadingDifference(270);
            telemetry.addData("Imu", "heading="+ String.valueOf(heading) + " heading_diff=" + String.valueOf(heading_diff));
            telemetry.update();

            // imu_.showHeading(true);
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        imu_ = new RevImu(hardwareMap.get(BNO055IMU.class, "imu"),
                          telemetry);
    }

    void initializeWhenStart() {
        // timer_.reset();
        // currTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        // TBD
    }
}
