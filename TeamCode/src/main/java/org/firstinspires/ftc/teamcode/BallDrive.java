package org.firstinspires.ftc.teamcode.teleOP;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BallDrive {
    public DcMotor motor1;
    public DcMotor motor2;
    public DcMotor motor3;

    public Servo servo1;
    public Servo servo2;

    public BallDrive(HardwareMap hdm , Telemetry telemetry) {

        motor1 = hdm.dcMotor.get("Motor1");
        motor2 = hdm.dcMotor.get("Motor2");
        motor3 = hdm.dcMotor.get("Motor3");

        servo1 = hdm.servo.get("servo1");
        servo2 = hdm.servo.get("servo2");

        motor1.setDirection(DcMotorSimple.Direction.FORWARD);
        motor2.setDirection(DcMotorSimple.Direction.FORWARD);
        motor3.setDirection(DcMotorSimple.Direction.FORWARD);

        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        telemetry = telemetry;
    }

    /*public void vertical(double joystick) {
        motor1.setPower(joystick);
        motor2.setPower(joystick);

    }

    public void turn(float joystick) {
        motor1.setPower(joystick);
        motor2.setPower(-joystick);
    }

    public void horizontal(float joystick) {
        motor3.setPower(joystick);
    }*/
}
