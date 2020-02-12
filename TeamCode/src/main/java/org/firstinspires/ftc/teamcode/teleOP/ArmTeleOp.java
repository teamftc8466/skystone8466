package org.firstinspires.ftc.teamcode.teleOP;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Arm;

@TeleOp(name="ArmTest", group="Arm")
@Disabled
public class ArmTeleOp extends OpMode {

    Arm arm = null;

    @Override
    public void init() {
        arm = new Arm(hardwareMap);
    }
    @Override
    public void loop() {
        arm.FullFunction(gamepad1);
    }
}
