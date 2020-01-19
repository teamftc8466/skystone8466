package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp (name = "TouchSensorTestKyle", group = "Lift")
public class TestTouchSensors extends RobotHardware {

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();

        while (opModeIsActive())  {
            boolean is_touched = gobildaTouchSensors_.touching();
            telemetry.addData("Touch", String.valueOf(is_touched));
            telemetry.update();
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() { createGobildaTouchSensors(); }

    void cleanUpAtEndOfRun() {
        // TBD
    }
}
