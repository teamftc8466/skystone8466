package org.firstinspires.ftc.teamcode.ExperimentProgram;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;


public class LucasMecanum {
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;

    public LucasMecanum(HardwareMap hwm, Telemetry t) {
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

    double frontLeftPower;
    double backLeftPower;
    double frontRightPower;
    double backRightPower;

    /*public void turndrive(Gamepad gamepad) {
        frontLeftPower = gamepad.right_stick_x;
        backLeftPower = gamepad.right_stick_x;
        frontRightPower = -gamepad.right_stick_x;
        backRightPower = -gamepad.right_stick_x;
        setMecanumDrive(frontLeftPower, backLeftPower, frontRightPower, backRightPower);
    }*/

    public void omniMecanumDrive(Gamepad gamepad) {
        double hypotenuse = Math.sqrt(gamepad.left_stick_y*gamepad.left_stick_y+gamepad.left_stick_x*gamepad.left_stick_x);
        double angle = Math.atan2(gamepad.left_stick_y , gamepad.left_stick_x);
        double turn = gamepad.right_stick_x;
        omnimecanumdrivepowers(hypotenuse, angle, turn);
    }

    public void omnimecanumdrivepowers(double power, double angle, double turn) {
        frontLeftPower = -(power * Math.sin(angle - (Math.PI / 4)) - Math.cos(angle) * turn);
        backLeftPower = -(power * Math.cos(angle - (Math.PI / 4)) - Math.cos(angle) * turn); //back
        frontRightPower = (power * Math.cos(angle - (Math.PI / 4)) + Math.cos(angle) * turn);
        backRightPower = (power * Math.sin(angle - (Math.PI / 4)) + Math.cos(angle) * turn); //back

        setMecanumDrive(frontLeftPower, backLeftPower, frontRightPower, backRightPower);
    }
    /*public void setPowersMecanum(Gamepad gamepad) {
        frontLeftPower = gamepad.left_stick_y + gamepad.left_stick_x;
        backLeftPower = gamepad.left_stick_y + gamepad.left_stick_x;
        frontRightPower = gamepad.left_stick_y + gamepad.left_stick_x;
        backRightPower = gamepad.left_stick_y + gamepad.left_stick_x;
        setMecanumDrive(frontLeftPower, backLeftPower, frontRightPower, backRightPower);
    }*/
    public void setMecanumDrive(double frontL, double backL, double frontR, double backR) {


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