package org.firstinspires.ftc.teamcode.teleOP;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.BallDrive;

@TeleOp(name="BallDrive TeleOp", group="BallDrive")
@Disabled
public class BallDriveOpMode extends OpMode{

    /*Declare OpMode members.*/
    BallDrive ballDrive;   //Use a Pushbot's hardware

    @Override
    public void init() {
        ballDrive = new BallDrive(hardwareMap, telemetry);
}

    @Override
    public void loop() {
        /*ballDrive.motor1.setPower(-gamepad1.left_stick_y - gamepad1.right_stick_x);
        ballDrive.motor2.setPower(gamepad1.left_stick_y - gamepad1.right_stick_x);
        ballDrive.motor3.setPower(gamepad1.left_stick_x);*/
        ballDrive.lmotor.setPower(-1.5*gamepad1.left_stick_y - gamepad1.right_stick_x);
        ballDrive.rmotor.setPower(1.5*gamepad1.left_stick_y - gamepad1.right_stick_x);
        ballDrive.xmotor.setPower(gamepad1.left_stick_x);

        if (gamepad1.b) {
            ballDrive.lservo.setPosition(90);
            ballDrive.rservo.setPosition(90);
        }
        else if (gamepad1.a) {
            ballDrive.lservo.setPosition(0);
            ballDrive.rservo.setPosition(0);
        }
        /*if(Math.abs(gamepad1.left_stick_x) <= .1 && Math.abs(gamepad1.right_stick_x) <= .1) {
            ballDrive.motor1.setPower(gamepad1.left_stick_y);
            ballDrive.motor2.setPower(-gamepad1.left_stick_y);
        }

        else if(Math.abs(gamepad1.right_stick_x) <= .1) {
            ballDrive.motor1.setPower(gamepad1.left_stick_y/2);
            ballDrive.motor2.setPower(-gamepad1.left_stick_y/2);
            ballDrive.motor3.setPower(-gamepad1.left_stick_x);
        }

        else if(Math.abs(gamepad1.left_stick_x) <= .1 && Math.abs(gamepad1.left_stick_y) <= .1) {
            ballDrive.motor1.setPower(-gamepad1.right_stick_x);
            ballDrive.motor2.setPower(gamepad1.right_stick_x);
        }*/

        telemetry.addData("Joystick value", gamepad1.right_stick_y);
        telemetry.update();
    }
}

