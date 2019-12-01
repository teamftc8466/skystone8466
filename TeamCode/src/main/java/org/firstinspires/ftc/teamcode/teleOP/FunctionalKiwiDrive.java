package org.firstinspires.ftc.teamcode.teleOP;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.FormatFlagsConversionMismatchException;

@TeleOp
public class FunctionalKiwiDrive extends OpMode {
    private DcMotor motor1 = null;
    private DcMotor motor2 = null;
    private DcMotor motor3 = null;

    @Override
    public void init() {
        this.motor1 = super.hardwareMap.dcMotor.get("Motor1");
        this.motor2 = super.hardwareMap.dcMotor.get("Motor2");
        this.motor3 = super.hardwareMap.dcMotor.get("Motor3");
    }

    @Override
    public void loop() {
        if (Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1) {
            motor1.setPower((-1.0 / 3.0) * gamepad1.left_stick_x + (Math.sqrt(3.0) / 3.0) * (-gamepad1.left_stick_y));
            motor2.setPower((-1.0 / 3.0) * gamepad1.left_stick_x - (Math.sqrt(3.0) / 3.0) * (-gamepad1.left_stick_y));
            motor3.setPower((2.0 / 3.0) * gamepad1.left_stick_x);
        }
        if (gamepad1.a) {
            motor1.setDirection(DcMotorSimple.Direction.FORWARD);
            motor2.setDirection(DcMotorSimple.Direction.FORWARD);
            motor3.setDirection(DcMotorSimple.Direction.FORWARD);
            motor1.setPower(0.3);
            motor2.setPower(0.3);
            motor3.setPower(0.3);
        }
        if (gamepad1.x) {
            motor1.setDirection(DcMotorSimple.Direction.REVERSE);
            motor2.setDirection(DcMotorSimple.Direction.REVERSE);
            motor3.setDirection(DcMotorSimple.Direction.REVERSE);
            motor1.setPower(0.3);
            motor2.setPower(0.3);
            motor3.setPower(0.3);
        }
        if (Math.abs(gamepad1.left_stick_x) < 0.1 && Math.abs(gamepad1.left_stick_y) < 0.1 && !gamepad1.a && !gamepad1.x) {
            motor1.setPower(0.0);
            motor2.setPower(0.0);
            motor3.setPower(0.0);
        }

        /* Code for testing for each seperate motor below
        if (gamepad1.b == true) {
            motor1.setPower(0.3);
        }
        if (gamepad1.x == true) {
            motor2.setPower(0.3);

        if (gamepad1.y==true){
            motor3.setPower(0.3);

        }
        if (gamepad1.b==false&&gamepad1.x==false&&gamepad1.y==false){
            motor1.setPower(0);
            motor2.setPower(0);
            motor3.setPower(0);
        }
      }
      */
        }
    }