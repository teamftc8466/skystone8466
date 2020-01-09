package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm {
    public DcMotor extendermotor, pulleymotor1, pulleymotor2;
    public Servo rotationservo;
    public Servo grabbingservo;

    public Arm(HardwareMap hwm) {
        pulleymotor1 = hwm.get(DcMotor.class, "pulleymotor1");
        pulleymotor2 = hwm.get(DcMotor.class, "pulleymotor2");
        extendermotor = hwm.get(DcMotor.class, "extenderMotor");
        rotationservo = hwm.get(Servo.class, "rotationServo");
        grabbingservo = hwm.get(Servo.class, "grabbingServo");
    }

    public void Horizontal(double input) {
        extendermotor.setPower(input);
    }

    public void Grab(double input) {
        grabbingservo.setPosition(input);
    }

    public void Rotate(double input) {
        rotationservo.setPosition(input);
    }

    public void AutoHorizontal(double power, int movecount) {
        int LeftTarget = extendermotor.getCurrentPosition() + movecount;

        extendermotor.setTargetPosition(LeftTarget);

        extendermotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
