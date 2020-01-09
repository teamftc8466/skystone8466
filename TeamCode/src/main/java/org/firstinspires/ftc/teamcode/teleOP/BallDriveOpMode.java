package org.firstinspires.ftc.teamcode.teleOP;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.teleOP.BallDrive;

@TeleOp(name="BallDrive TeleOp", group="BallDrive")
public class BallDriveOpMode extends OpMode{

    /*Declare OpMode members.*/
    BallDrive ballDrive;   //Use a Pushbot's hardware

    @Override
    public void init() {
        ballDrive = new BallDrive(hardwareMap, telemetry);
}

    @Override
    public void loop() {
        if(Math.abs(gamepad1.left_stick_y) <= .1 && Math.abs(gamepad1.right_stick_x) <= .1) {
            ballDrive.vertical(gamepad1.left_stick_y);
        }

        else if(Math.abs(gamepad1.right_stick_x) <= .1) {
            ballDrive.vertical((gamepad1.left_stick_y/2));
            ballDrive.horizontal(gamepad1.left_stick_x);
        }

        else if(Math.abs(gamepad1.left_stick_x) <= .1 && Math.abs(gamepad1.left_stick_y) <= .1) {
            ballDrive.turn(gamepad1.right_stick_x);
        }

        telemetry.addData("Joystick value", gamepad1.right_stick_y);
        telemetry.update();
    }
}

