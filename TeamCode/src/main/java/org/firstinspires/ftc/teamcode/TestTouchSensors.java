package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (name = "TouchSensorTestKyle", group = "Lift")
@Disabled
public class TestTouchSensors extends RobotHardware {
private DcMotor motor1;
private DcMotor motor2;
private DcMotor motor3;
private DcMotor motor4;

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();

        while (opModeIsActive())  {
            boolean is_touched = gobildaTouchSensors_.touching();
            telemetry.addData("Touch", String.valueOf(is_touched));
            telemetry.update();

            if (is_touched) {
                motor1.setPower(0);
                motor2.setPower(0);
                motor3.setPower(0);
                motor4.setPower(0);
                sleep(5000);
            } else {
                motor1.setPower(gamepad1.left_stick_y);
                motor2.setPower(gamepad1.left_stick_y);
                motor3.setPower(-gamepad1.left_stick_y);
                motor4.setPower(-gamepad1.left_stick_y);
            }
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        createGobildaTouchSensors();
        motor1 = hardwareMap.dcMotor.get("motor1");
        motor2 = hardwareMap.dcMotor.get("motor2");
        motor3 = hardwareMap.dcMotor.get("motor3");
        motor4 = hardwareMap.dcMotor.get("motor4");
        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);
        motor4.setPower(0);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    void cleanUpAtEndOfRun() {
        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);
        motor4.setPower(0);
    }
}
