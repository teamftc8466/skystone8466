package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="TestGoBuildaServo", group="FS")
// @Disabled
public class TestGoBuildaServo extends RobotHardware {

    /// Degree positions
    double hookGrabPosDegree_ = 280;
    double hookInitPosDegree_ = 0;

    /// Cont. positions
    final double LEFT_FULL_SPEED = 0;
    final double RIGHT_FULL_SPEED = 1;

    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start test GoBuilda servo program");
        telemetry.update();

        waitForStart();

        initializeWhenStart();


        while (opModeIsActive()) {
            leftHookServo_.setServoModePositionInDegree(hookGrabPosDegree_, true);
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        Servo left_servo = hardwareMap.get(Servo.class,"leftHookServo");

        leftHookServo_ = new GoBuildDualServo(left_servo,
                               false,
                                              hookInitPosDegree_,
                                              telemetry);
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        // TBD
    }
}
