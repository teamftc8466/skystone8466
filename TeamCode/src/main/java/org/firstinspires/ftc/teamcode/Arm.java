package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm {
    public DcMotor extendermotor, pulleymotorL, pulleymotorR;
    public Servo rotationservo;
    public Servo grabbingservo;

    public int open = 0;//0 = open for the state of the servo

    public boolean Lhookispressed = false;
    public boolean RHookispressed = false;
    public int angle = 0;

    public boolean grabberispressed = false;

    public Arm(HardwareMap hwm) {
        pulleymotorL = hwm.get(DcMotor.class, "motorLiftLeft");
        pulleymotorR = hwm.get(DcMotor.class, "motorLiftRight");
        extendermotor = hwm.get(DcMotor.class, "craneMotor");
        rotationservo = hwm.get(Servo.class, "rotationServo");
        grabbingservo = hwm.get(Servo.class, "clampServo");

        extendermotor.setDirection(DcMotorSimple.Direction.FORWARD);
        pulleymotorL.setDirection(DcMotorSimple.Direction.FORWARD);
        pulleymotorR.setDirection(DcMotorSimple.Direction.REVERSE);

        pulleymotorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pulleymotorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void reset() {
        pulleymotorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pulleymotorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendermotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    private void Horizontal(double input) {

        extendermotor.setPower(input);
    }

    private void Grab(boolean input) {
        if (input) {
            grabberispressed = true;
        }

        if (input == false) {
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


    private void RotateLeft(Gamepad gamepad){
        if (gamepad.left_trigger >= .5) {
            Lhookispressed = true;
        }

        if (Lhookispressed && gamepad.left_trigger <= .5) {
            Lhookispressed = false;

            switch (angle) {
                case 0:
                    rotationservo.setPosition(0);
                    break;

                case 1:
                    rotationservo.setPosition(.45);
                    angle = 0;
                    break;

                case 2:
                    rotationservo.setPosition(1);
                    angle = 1;

                    break;
                default:
                    angle = 1;

                    break;
            }
        }
    }


    private void RotateRight(Gamepad gamepad) {
        if (gamepad.right_trigger >= .5) {
        RHookispressed = true;
        }

        if (RHookispressed && gamepad.right_trigger <= .5) {
            RHookispressed = false;

            switch (angle) {
                case 0:
                    rotationservo.setPosition(0);
                    angle = 1;
                    break;

                case 1:
                    rotationservo.setPosition(.45);
                    angle = 2;
                    break;

                case 2:
                    rotationservo.setPosition(1);

                    break;

                default:
                    angle = 1;

                    break;
            }
        }
    }


    private void Rotate(Gamepad gamepad) {
        RotateLeft(gamepad);
        RotateRight(gamepad);
    }


    private void Lift(float input) {
        pulleymotorL.setPower(input);
        pulleymotorR.setPower(input);
    }


    public void FullFunction(Gamepad gamepad) {
        Horizontal(gamepad.left_stick_y);
        Grab(gamepad.right_bumper);
        Rotate(gamepad);
        Lift(gamepad.right_stick_y);
    }

    public void HoldPostion(int Lpos, int Rpos) {
        pulleymotorL.setTargetPosition(Lpos);
        pulleymotorR.setTargetPosition(Rpos);

        pulleymotorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pulleymotorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void AutoHorizontal(double power, int movecount) {
        int LeftTarget = extendermotor.getCurrentPosition() + movecount;

        extendermotor.setTargetPosition(LeftTarget);

        extendermotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
