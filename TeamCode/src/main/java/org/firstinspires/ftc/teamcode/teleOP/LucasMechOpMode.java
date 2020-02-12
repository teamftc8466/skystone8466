package org.firstinspires.ftc.teamcode.teleOP;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.ExperimentProgram.LucasMecanum;

@TeleOp(name="MecanumLucasTest", group="MecanumTest")
@Disabled
public class LucasMechOpMode extends OpMode {
    LucasMecanum robot = null;
    @Override
    public void init() {
        robot = new LucasMecanum(hardwareMap,telemetry);
    }
    @Override
    public void loop() {
            robot.omniMecanumDrive(gamepad1);
            /*robot.turndrive(gamepad1);*/
    }

}