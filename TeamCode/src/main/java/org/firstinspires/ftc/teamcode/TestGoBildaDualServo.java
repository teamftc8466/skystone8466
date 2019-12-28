package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="TestGoBuildaDualServo", group="FS")
// @Disabled
public class TestGoBildaDualServo extends RobotHardware {

    /// Degree positions
    final double INIT_LEFT_HOOK_DEGREE = 280;    // Initial positions for the left and right servos are on opposite ends of the position spectrum
    final double INIT_RIGHT_HOOK_DEGREE = 0;     // (0 degrees to 280 degrees) because they are facing opposite directions when mounted on the robot

    final double LEFT_HOOK_PULL_DEGREE = 80;
    final double RIGHT_HOOK_PULL_DEGREE = 200;

    final double LEFT_HOOK_RELEASE_DEGREE = 240;
    final double RIGHT_HOOK_RELEASE_DEGREE = 40;

    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start test GoBuilda servo program");
        telemetry.update();

        waitForStart();

        initializeWhenStart();

        leftHookServo_.showPosition(false);
        rightHookServo_.showPosition(true);

        sleep(3000);

        for (int i=0; i<3; ++i) {
            leftHookServo_.setServoModePositionInDegree(LEFT_HOOK_PULL_DEGREE, false);
            rightHookServo_.setServoModePositionInDegree(RIGHT_HOOK_PULL_DEGREE, false);

            leftHookServo_.showPosition(false);
            rightHookServo_.showPosition(true);

            sleep(3000);

            leftHookServo_.setServoModePositionInDegree(LEFT_HOOK_RELEASE_DEGREE, false);
            rightHookServo_.setServoModePositionInDegree(RIGHT_HOOK_RELEASE_DEGREE, false);

            leftHookServo_.showPosition(false);
            rightHookServo_.showPosition(true);

            sleep(3000);
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        createHookServoSystem(INIT_LEFT_HOOK_DEGREE, INIT_RIGHT_HOOK_DEGREE);
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        // TBD
    }
}
