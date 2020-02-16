package org.firstinspires.ftc.teamcode.teleOP;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Lift;

@TeleOp(name="Test Lifter", group=" ")
public class LifterTeleOp extends OpMode {

    private Lifter lifter = null;

    @Override
    public void init() {
        lifter = new Lifter(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        if (Math.abs(gamepad2.right_stick_y) > .1) {
            lifter.manualdrive(gamepad2.right_stick_y);
        }
        else if (gamepad2.a == false) {
            lifter.holdposition();
        }
        else if (gamepad2.a) {
            lifter.GoToDriveHeight(gamepad2);
        }
        lifter.SetHeight(gamepad2.dpad_up, gamepad2.dpad_down);
        lifter.telem();
        telemetry.update();
    }
}
