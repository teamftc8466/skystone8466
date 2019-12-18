package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="TestGoBuildaDualServo", group="FS")
// @Disabled
public class TestGoBildaDualServo extends RobotHardware {

    /// Degree positions
    final double INIT_LEFT_HOOK_DEGREE = 0;
    final double INIT_RIGHT_HOOK_DEGREE = 280;

    final double LEFT_HOOK_MOVE_STEP = 90;
    final double RIGHT_HOOK_MOVE_STEP = -90;

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

        double left_degree = INIT_LEFT_HOOK_DEGREE;
        double right_degree = INIT_RIGHT_HOOK_DEGREE;
        for (int i=0; i<4; ++i) {
            left_degree += LEFT_HOOK_MOVE_STEP;
            right_degree -= RIGHT_HOOK_MOVE_STEP;

            leftHookServo_.setServoModePositionInDegree(left_degree, false);
            rightHookServo_.setServoModePositionInDegree(right_degree, true);

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
