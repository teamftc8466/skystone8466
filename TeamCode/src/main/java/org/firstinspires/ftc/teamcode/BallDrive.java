package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BallDrive {
    public DcMotor lmotor;
    public DcMotor rmotor;
    public DcMotor xmotor;

    public Servo lservo;
    public Servo rservo;

    public BallDrive(HardwareMap hdm , Telemetry telemetry) {

        lmotor = hdm.dcMotor.get("MotorL");
        rmotor = hdm.dcMotor.get("MotorR");
        xmotor = hdm.dcMotor.get("MotorX");

        lservo = hdm.servo.get("servo1");
        rservo = hdm.servo.get("servo2");

        lmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        xmotor.setDirection(DcMotorSimple.Direction.FORWARD);

        lmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        xmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

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
