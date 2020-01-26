package org.firstinspires.ftc.teamcode.teleOP;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Arm;
import org.firstinspires.ftc.teamcode.ExperimentProgram.LucasMecanum;

//@Disabled
@TeleOp(name = "completeTeleOp", group = "full")
public class fullTeleOp extends OpMode {
    LucasMecanum drivetrain;
    Arm arm;

    @Override
    public void init() {
        drivetrain = new LucasMecanum(hardwareMap,telemetry);
        arm = new Arm(hardwareMap);
    }


    @Override
    public void loop() {
        drivetrain.omniMecanumDrive(gamepad1);
        arm.FullFunction(gamepad2);
        drivetrain.Hook(gamepad1.left_bumper);
    }
}
