package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;


public class MecanumDrive {
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;

    public MecanumDrive(HardwareMap hwm, Telemetry t) {
        frontLeft = hwm.get(DcMotor.class, "frontLeft");
        frontRight = hwm.get(DcMotor.class, "frontRight");
        backLeft = hwm.get(DcMotor.class, "backLeft");
        backRight = hwm.get(DcMotor.class, "backRight");
    }
    /*
    FrontLeft = Ch3 + Ch1 + Ch4
            RearLeft = Ch3 + Ch1 - Ch4
    FrontRight = Ch3 - Ch1 - Ch4
            RearRight = Ch3 - Ch1 + Ch4

    Where:
    Ch1 = Right joystick X-axis
    Ch3 = Left joystick Y-axis
    Ch4 = Left joystick X-axis
            */

    float frontLeftPower;
    float backLeftPower;
    float frontRightPower;
    float backRightPower;

    //This code doesn't work but I like to think it does, its good code, its my code. Its good code.

    public void setPowersMecanum(Gamepad gamepad) {
        frontLeftPower = -(gamepad.left_stick_y + gamepad.right_stick_x - gamepad.left_stick_x); //negatives for reverse correction
        backLeftPower = -(gamepad.left_stick_y - gamepad.left_stick_x - gamepad.right_stick_x);
        frontRightPower = gamepad.left_stick_y + gamepad.right_stick_x - gamepad.left_stick_x;
        backRightPower = gamepad.left_stick_y + gamepad.right_stick_x + gamepad.left_stick_x;
        setMecanumDrive(frontLeftPower, backLeftPower, frontRightPower, backRightPower);
    }
    public void setMecanumDrive(float frontL, float backL, float frontR, float backR) {

        // For Deadzones
        if (Math.abs(frontL) > 0.1) {
            frontLeft.setPower(frontL); //left
        }
        else {
            frontLeft.setPower(0);
        }

        if (Math.abs(backL) > 0.1) {
            backLeft.setPower(backL);  //left
        }
        else {
            backLeft.setPower(0);
        }

        if (Math.abs(frontR) > 0.1) {
            frontRight.setPower(frontR);
        }
        else {
            frontRight.setPower(0);
        }

        if (Math.abs(backR) > 0.1) {
            backRight.setPower(backR);
        }
        else {
            backRight.setPower(0);
        }

    }

}
