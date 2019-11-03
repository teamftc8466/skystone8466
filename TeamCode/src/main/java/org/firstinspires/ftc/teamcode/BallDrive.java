package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BallDrive {
    private DcMotor motor1 = null;
    private DcMotor motor2 = null;
    private DcMotor motor3 = null;

    public BallDrive(HardwareMap hdm , Telemetry telemetry) {
        motor1 = hdm.dcMotor.get("Motor1");
        motor2 = hdm.dcMotor.get("Motor2");
        motor3 = hdm.dcMotor.get("Motor2");
        motor1.setDirection(DcMotorSimple.Direction.FORWARD);
        motor2.setDirection(DcMotorSimple.Direction.FORWARD);
        motor3.setDirection(DcMotorSimple.Direction.FORWARD);

        telemetry = telemetry;
    }

    public void vertical(float joystick) {
        motor1.setPower(joystick);
        motor2.setPower(joystick);

    }

    public void turn(float joystick) {
        motor1.setPower(joystick);
        motor2.setPower(-joystick);
    }

    public void horizontal(float joystick) {
        motor3.setPower(joystick);
    }
}
