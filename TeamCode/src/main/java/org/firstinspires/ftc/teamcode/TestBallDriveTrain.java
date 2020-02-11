package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Autonomous.RobotHardware;

@TeleOp(name="TestBallDriveTrain", group="FS")
@Disabled
public class TestBallDriveTrain extends RobotHardware {

    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start drive train test program");
        telemetry.update();

        waitForStart();

        initializeWhenStart();

        while (opModeIsActive()) {
            driveTrain_.driveByGamePad(gamepad1);
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        driveTrain_.createBallDriveTrain(hardwareMap);
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        // TBD
    }
}
