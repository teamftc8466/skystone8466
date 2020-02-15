package org.firstinspires.ftc.teamcode.teleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Arm;
import org.firstinspires.ftc.teamcode.ExperimentProgram.LucasMecanum;

@TeleOp(name = "completeTeleOpLinear", group = "full")
//@Disabled
public class FullTeleOpLinear extends LinearOpMode {
    private LucasMecanum drivetrain;
    private Arm arm;
    private Lifter lifter = null;

    public int Llifterencoder = 0;
    public int Rlifterencoder = 0;

    private boolean autocollect = false;

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();

        while (opModeIsActive()) {
            super.resetStartTime();

            if (Math.abs(gamepad1.right_stick_x) < .1 && Math.abs(gamepad1.left_stick_y) < .1 && Math.abs(gamepad1.left_stick_x) < .1) {
                drivetrain.Brake();
                drivetrain.Zero();
            }
            else {
                drivetrain.omniMecanumDrive(gamepad1);
            }

            arm.FullFunction(gamepad2);
            drivetrain.Hook(gamepad1.left_bumper);

            if (Math.abs(gamepad2.right_stick_y) > .1) {
                lifter.manualdrive(gamepad2.right_stick_y);
            }
            else if (gamepad2.a == false) {
                lifter.holdposition();
            }
            else if (gamepad2.a) {
                lifter.ExtendedFunction(gamepad2);
            }

            if (gamepad1.y) {
                Compaction();
            }

            AutoCollect();

            if (gamepad2.right_bumper) {
                lifter.RaiseToCollectHeight();
            }

            System.out.println("Time after lifter: " + super.getRuntime());

            lifter.telem();
            telemetry.addData("grabeer: ", arm.grabbingservo.getPosition());
            telemetry.addData("Lhook: ", drivetrain.servoL.getPosition());
            telemetry.addData("Rhook: ", drivetrain.servoR.getPosition());
            telemetry.addData("Rotation time: ", arm.rotationservo.getPosition());
            telemetry.addData("horizonatal: ", arm.extendermotor.getCurrentPosition());
            telemetry.update();

            sleep(100);
        }
    }

    private void Compaction() {
        drivetrain.MechanumCompact();
        arm.ArmCompact();
        lifter.LifterCompact();
    }

    private void AutoCollect() {
        if (gamepad2.b && autocollect == false) {
            lifter.RaiseToCollectHeight();
            arm.AutoCollectHorizontal();
            arm.grabbingservo.setPosition(.1);
            autocollect = true;
        }

        if (gamepad2.b == false && autocollect) {
            lifter.LowerToCollectHeight();
            sleep(10);
            arm.grabbingservo.setPosition(.5);
            autocollect = false;
        }
    }

    private void initialize() {
        drivetrain = new LucasMecanum(hardwareMap,telemetry);
        arm = new Arm(hardwareMap);
        lifter = new Lifter(hardwareMap, telemetry);

        arm.rotationservo.setPosition(0);
    }

}
