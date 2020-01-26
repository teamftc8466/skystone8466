package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Arm {
    public DcMotor extendermotor, pulleymotorL, pulleymotorR;
    public Servo rotationservo;
    public Servo grabbingservo;

    public int open = 0;//0 = open for the state of the servo
    public double input = 0;//the rotation servo postion


    //still need to set min and max, but problem with math class

    public Arm(HardwareMap hwm) {
        pulleymotorL = hwm.get(DcMotor.class, "motorLiftLeft");
        pulleymotorR = hwm.get(DcMotor.class, "motorLiftRight");
        extendermotor = hwm.get(DcMotor.class, "craneMotor");
        rotationservo = hwm.get(Servo.class, "rotationServo");
        grabbingservo = hwm.get(Servo.class, "clampServo");
    }

    public void Horizontal(double input) {
        extendermotor.setPower(input);
    }

    public void Grab(boolean input) {
        if (input) {
            switch (open) {
                case 0:
                    grabbingservo.setPosition(0);
                    open = 1;
                    break;

                case 1:
                    grabbingservo.setPosition(90);
                    open = 0;
                    break;

                default:
                    open = 0;
            }
        }
    }


    public void Rotate(Gamepad gamepad) {
        input += gamepad.right_trigger - gamepad.left_trigger;

        rotationservo.setPosition(input);
    }


    public void FullFunction(Gamepad gamepad) {
        Horizontal(gamepad.left_stick_y);
        Grab(gamepad.right_bumper);
        Rotate(gamepad);
    }


    public void AutoHorizontal(double power, int movecount) {
        int LeftTarget = extendermotor.getCurrentPosition() + movecount;

        extendermotor.setTargetPosition(LeftTarget);

        extendermotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
