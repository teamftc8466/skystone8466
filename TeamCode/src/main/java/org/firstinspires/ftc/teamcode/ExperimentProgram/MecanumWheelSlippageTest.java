package org.firstinspires.ftc.teamcode.ExperimentProgram;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Hardware;
import org.firstinspires.ftc.robotcore.external.Telemetry;

//@Disabled
@TeleOp(name = "MechanumWheelslip")
public class MecanumWheelSlippageTest extends OpMode {
    LucasMecanum mecanum;

    @Override
    public void init() {
        mecanum = new LucasMecanum(hardwareMap, telemetry);
        mecanum.StartEncoders();
    }
    @Override
    public void loop() {
        mecanum.omniMecanumDrive(gamepad1);
        telemetry.addData("yeet", mecanum.targetflp);
        telemetry.update();
    }
}
