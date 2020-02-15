package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Arm {

    public DcMotor extendermotor;
    public Servo rotationservo;
    public Servo grabbingservo;

    private int open = 0;//0 = open for the state of the servo

    private boolean Rtrigispressed = false;
    private int angle = 1;

    private boolean grabberispressed = false;

    //still need to set min and max, but problem with math class

    public Arm(HardwareMap hwm) {
        extendermotor = hwm.get(DcMotor.class, "craneMotor");
        rotationservo = hwm.get(Servo.class, "rotationServo");
        grabbingservo = hwm.get(Servo.class, "clampServo");

        extendermotor.setDirection(DcMotorSimple.Direction.FORWARD);
        extendermotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        extendermotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void Horizontal(float input) {

        extendermotor.setPower(input);
    }

    private void Grab(boolean input) {
        if (input) {
            grabberispressed = true;
        }

        if (input == false && grabberispressed) {
            grabberispressed = false;

            switch (open) {
                case 0:
                    grabbingservo.setPosition(.1);
                    open = 1;
                    break;

                case 1:
                    grabbingservo.setPosition(.5);
                    open = 0;
                    break;

                default:
                    open = 0;
            }
        }
    }

    private void Rotate(Gamepad gamepad) {
        if (extendermotor.getCurrentPosition() <= 0) {
            if (gamepad.right_trigger >= .5) {
                Rtrigispressed = true;
            }

            if (Rtrigispressed && gamepad.right_trigger <= .5) {
                Rtrigispressed = false;

                switch (angle) {
                    case 0:
                        rotationservo.setPosition(0);
                        angle = 1;
                        break;

                        case 1:
                            rotationservo.setPosition(.45);
                            angle = 0;
                            break;

                            default:
                                angle = 1;
                                break;
                }
            }
        }
    }

    public void FullFunction(Gamepad gamepad) {
        Horizontal(gamepad.left_stick_y);
        Grab(gamepad.right_bumper);
        Rotate(gamepad);
    }

    public void ArmCompact() {
        rotationservo.setPosition(0);

        extendermotor.setTargetPosition(0);
        extendermotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extendermotor.setPower(1);
    }
}
