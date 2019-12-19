package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="ArmTest", group="Arm")
public class ArmTeleOp extends OpMode {

    Arm arm = null;
    int servostate = 0;

    @Override
    public void init() {
        arm = new Arm(hardwareMap);
    }
    @Override
    public void loop() {
        if (gamepad1.right_trigger <= .1 && gamepad1.left_trigger >= .1) {
            arm.Horizontal(gamepad1.left_trigger);
        }
        else if (gamepad1.right_trigger >= .1 && gamepad1.left_trigger <= .1) {
            arm.Horizontal(-gamepad1.right_trigger);
        }


        if (gamepad1.left_bumper && servostate == 0) {
            arm.Grab(0);
            servostate = 1;
        }
        else if (gamepad1.left_bumper && servostate == 1) {
            arm.Grab(180);
            servostate = 0;
        }

        if(gamepad1.b) {
            arm.Rotate(0);
        }
        else if(gamepad1.y) {
            arm.Rotate(90);
        }
        else if(gamepad1.x) {
            arm.Rotate(180);
        }
        else if(gamepad1.a) {
            arm.Rotate(90);
        }
    }
}
