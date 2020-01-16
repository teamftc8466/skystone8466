package org.firstinspires.ftc.teamcode.ExperimentProgram;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="MecanumKyleTest", group="MecanumTest")
public class MecanumDriveOpMode extends OpMode {
    MecanumDrive robot = null;
    @Override
    public void init() {
        robot = new MecanumDrive(hardwareMap,telemetry);
    }
    @Override
    public void loop() {
        robot.setPowersMecanum(gamepad1);
    }
    
}
