package org.firstinspires.ftc.teamcode.teleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Arm;
import org.firstinspires.ftc.teamcode.ExperimentProgram.LucasMecanum;

@TeleOp(name = "completeTeleOp", group = "full")
//@Disabled
public class FullTeleOpLinear extends LinearOpMode {
    LucasMecanum drivetrain;
    Arm arm;

    public int Llifterencoder = 0;
    public int Rlifterencoder = 0;


    @Override
    public void runOpMode() {
        initialize();

        waitForStart();

        resetencoders();

        while (opModeIsActive()) {
            Llifterencoder = arm.pulleymotorL.getCurrentPosition();
            Rlifterencoder = arm.pulleymotorR.getCurrentPosition();

            drivetrain.omniMecanumDrive(gamepad1);
            arm.FullFunction(gamepad2);
            drivetrain.Hook(gamepad1.left_bumper);
            telemetry.addData("grabeer: ", arm.grabbingservo.getPosition());
            telemetry.addData("Lhook: ", drivetrain.servoL.getPosition());
            telemetry.addData("Rhook: ", drivetrain.servoR.getPosition());
            telemetry.addData("Rotation time: ", arm.rotationservo.getPosition());
            telemetry.update();
            arm.HoldPostion(Llifterencoder, Rlifterencoder);
        }
    }

        private void initialize() {
            drivetrain = new LucasMecanum(hardwareMap,telemetry);
            arm = new Arm(hardwareMap);
        }

        private void resetencoders() {
            arm.reset();
            drivetrain.reset();
        }
}
