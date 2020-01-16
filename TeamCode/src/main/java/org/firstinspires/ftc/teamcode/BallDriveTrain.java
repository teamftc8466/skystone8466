package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class BallDriveTrain {
    private DcMotor motorLeft_ = null;
    private DcMotor motorcenter_ = null;
    private DcMotor motorRight_ = null;

    BallDriveTrain(HardwareMap hwp) {
        motorLeft_ = hwp.dcMotor.get("MotorL");
        motorcenter_ = hwp.dcMotor.get("MotorC");
        motorRight_ = hwp.dcMotor.get("MotorR");

        motorLeft_.setDirection(DcMotorSimple.Direction.FORWARD);
        motorcenter_.setDirection(DcMotorSimple.Direction.FORWARD);
        motorRight_.setDirection(DcMotorSimple.Direction.FORWARD);

        motorLeft_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motorcenter_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motorRight_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    void driveByGamePad(Gamepad gamepad) {
        motorLeft_.setPower(-1.5*gamepad.left_stick_y - gamepad.right_stick_x);
        motorcenter_.setPower(1.5*gamepad.left_stick_y - gamepad.right_stick_x);
        motorRight_.setPower(gamepad.left_stick_x);
    }
}
