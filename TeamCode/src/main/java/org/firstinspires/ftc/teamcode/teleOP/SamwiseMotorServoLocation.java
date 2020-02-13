package org.firstinspires.ftc.teamcode.teleOP;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp

public class SamwiseMotorServoLocation extends OpMode {

    private Servo unknownservo1 = null;
    private Servo unknownservo2 = null;
    private Servo unknownservo3 = null;
    private Servo unknownservo4 = null;
    private Servo unknownservo5 = null;
    private DcMotor unknownmotor1 = null;
    private DcMotor unknownmotor2 = null;
    private DcMotor unknownmotor3 = null;
    private DcMotor unknownmotor4 = null;
    private DcMotor unknownmotor5 = null;
    private DcMotor unknownmotor6 = null;
    private DcMotor unknownmotor7 = null;
    //private DcMotor unknownmotor8 = null;
    @Override
    public void init() {
        this.unknownservo1 = super.hardwareMap.servo.get("Servo1_0");
        this.unknownservo2 = super.hardwareMap.servo.get("Servo1_1");
        this.unknownservo3 = super.hardwareMap.servo.get("Servo1_2");
        this.unknownservo3 = super.hardwareMap.servo.get("Servo2_4");
        this.unknownservo4 = super.hardwareMap.servo.get("Servo2_5");
        this.unknownmotor1 = super.hardwareMap.dcMotor.get("Motor1_0");
        this.unknownmotor2 = super.hardwareMap.dcMotor.get("Motor1_1");
        this.unknownmotor3 = super.hardwareMap.dcMotor.get("Motor1_2");
        this.unknownmotor4 = super.hardwareMap.dcMotor.get("Motor1_3");
        this.unknownmotor5 = super.hardwareMap.dcMotor.get("Motor2_0");
        this.unknownmotor6 = super.hardwareMap.dcMotor.get("Motor2_1");
        this.unknownmotor7 = super.hardwareMap.dcMotor.get("Motor2_2");
        //this.unknownmotor8 = super.hardwareMap.dcMotor.get("Motor2_3");
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            unknownmotor1.setPower(0.2);
            unknownservo1.setPosition(0.2);
        }
        if (gamepad1.b) {
            unknownmotor2.setPower(0.2);
            unknownservo2.setPosition(0.2);
        }
        if (gamepad1.x) {
            unknownmotor3.setPower(0.2);
            unknownservo3.setPosition(0.2);
        }
        if (gamepad1.y) {
            unknownmotor4.setPower(0.2);
            unknownservo4.setPosition(0.2);
        }
        if (gamepad1.dpad_right) {
            unknownmotor5.setPower(0.2);
            unknownservo5.setPosition(0.2);
        }
        if (gamepad1.dpad_down) {
            unknownmotor6.setPower(0.2);
        }
        if (gamepad1.dpad_left) {
            unknownmotor7.setPower(0.2);
        }
        /*if (gamepad1.dpad_up) {
            unknownmotor8.setPower(0.2);
        }*/
        if(!gamepad1.a && !gamepad1.b && !gamepad1.x && !gamepad1.y && !gamepad1.dpad_left && !gamepad1.dpad_down && !gamepad1.dpad_right) { //&&!gamepad1.dpad_up
            unknownmotor1.setPower(0);
            unknownmotor2.setPower(0);
            unknownmotor3.setPower(0);
            unknownmotor4.setPower(0);
            unknownmotor5.setPower(0);
            unknownmotor6.setPower(0);
            unknownmotor7.setPower(0);
            //unknownmotor8.setPower(0);
            unknownservo1.setPosition(0);
            unknownservo2.setPosition(0);
            unknownservo3.setPosition(0);
            unknownservo4.setPosition(0);
            unknownservo5.setPosition(0);
        }
    }
}
