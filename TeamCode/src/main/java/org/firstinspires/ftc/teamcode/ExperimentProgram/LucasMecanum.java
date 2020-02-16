package org.firstinspires.ftc.teamcode.ExperimentProgram;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import java.util.concurrent.TimeUnit;


public class LucasMecanum {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    public Servo servoL;
    public Servo servoR;

    public int open = 0;//0 = up
    private boolean lbispressed = false;

    public boolean wheelslippage = false;

    double accumulatederror = 0;

    double olderror = 0;
    double oldtime = 0;

    ElapsedTime elaptime;

    public LucasMecanum(HardwareMap hwm, Telemetry t) {
        frontLeft = hwm.get(DcMotor.class, "motorLB");
        frontRight = hwm.get(DcMotor.class, "motorRB");
        backLeft = hwm.get(DcMotor.class, "motorLF");
        backRight = hwm.get(DcMotor.class, "motorRF");

        servoL = hwm.servo.get("leftHookServo");
        servoR = hwm.servo.get("rightHookServo");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void StartEncoders() {
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        wheelslippage = true;
    }

    public void reset() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }


    public void Hook(boolean input) {
        if (input) {
            lbispressed = true;
        }

        if (input == false && lbispressed) {
            lbispressed = false;

            switch (open) {
                case 0:
                    servoL.setPosition(.7);
                    servoR.setPosition(.4);
                    open = 1;
                    break;

                case 1:
                    servoL.setPosition(.3);
                    servoR.setPosition(.8);
                    open = 0;
                    break;

                default:
                    open = 0;
            }
        }
    }


    public void Brake() {
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private void Float() {
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }
    /*
    FrontLeft = Ch3 + Ch1 + Ch4
            RearLeft = Ch3 + Ch1 - Ch4
    FrontRight = Ch3 - Ch1 - Ch4
            RearRight = Ch3 - Ch1 + Ch4

    Where:
    Ch1 = Right joystick X-axis
    Ch3 = Left joystick Y-axis
    Ch4 = Left joystick X-axis
            */

    double frontLeftPower;
    double backLeftPower;
    double frontRightPower;
    double backRightPower;

    double targetflp;
    double targetfrp;
    double targetblp;
    double targetbrp;

    private void Target() {
        targetflp += frontLeftPower;
        targetfrp += frontRightPower;
        targetblp += backLeftPower;
        targetbrp += backRightPower;

        Error(targetflp, targetfrp, targetblp, targetbrp);
    }

    private void Error(double flp, double frp, double blp, double brp) {
        double flperror = flp - frontLeft.getCurrentPosition();
        double frperror = frp - frontLeft.getCurrentPosition();
        double blperror = blp - frontLeft.getCurrentPosition();
        double brperror = brp - frontLeft.getCurrentPosition();

        double trueflp = targetflp + P(flperror) + I(flperror) + D(flperror);
        double truefrp = targetfrp + P(frperror) + I(frperror) + D(frperror);
        double trueblp = targetblp + P(blperror) + I(blperror) + D(blperror);
        double truebrp = targetbrp + P(brperror) + I(brperror) + D(brperror);

        setMecanumDrive(trueflp, trueblp, truefrp, truebrp);
    }

    public double P(double error) {
        double proportional = error * 1.2;

        return proportional;
    }

    public double I(double error) {
        accumulatederror += error;
        double integral = accumulatederror * 1.5;

        return integral;
    }

    public double D(double error) {
        double derivative = ((error - olderror)/(elaptime.time(TimeUnit.SECONDS) - oldtime)) * 3;

        olderror = error;
        oldtime = elaptime.time(TimeUnit.SECONDS);
        return derivative;
    }
    /*public void turndrive(Gamepad gamepad) {
        frontLeftPower = gamepad.right_stick_x;
        backLeftPower = gamepad.right_stick_x;
        frontRightPower = -gamepad.right_stick_x;
        backRightPower = -gamepad.right_stick_x;
        setMecanumDrive(frontLeftPower, backLeftPower, frontRightPower, backRightPower);
    }*/

    public void Zero() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public void omniMecanumDrive(Gamepad gamepad) {
        double hypotenuse = Math.sqrt(gamepad.left_stick_y*gamepad.left_stick_y+gamepad.right_stick_x*gamepad.right_stick_x);
        double angle = Math.atan2(gamepad.left_stick_y , gamepad.right_stick_x);
        double turn = gamepad.left_stick_x;
        if (gamepad.right_trigger >= .5) {
            omnimecanumdrivepowers(hypotenuse*.25, angle, turn*.25, gamepad);
        }
        else {
            omnimecanumdrivepowers(hypotenuse, angle, turn, gamepad);
        }
    }

    private void omnimecanumdrivepowers(double power, double angle, double turn, Gamepad gamepad) {
        Float();

        if (Math.abs(gamepad.left_stick_y) < .1 && Math.abs(gamepad.right_stick_x) < .1) {
            frontLeftPower = -turn;
            backLeftPower = -turn; //back
            frontRightPower = -turn;
            backRightPower = -turn; //back
        }
        else if (Math.abs(gamepad.right_stick_x) <= .1 && Math.abs(gamepad.left_stick_x) <= .1) {
            frontLeftPower = -gamepad.left_stick_y;
            backLeftPower = gamepad.left_stick_y;
            frontRightPower = gamepad.left_stick_y;
            backRightPower = -gamepad.left_stick_y;
        }
        else {
            frontLeftPower = (power * Math.cos(angle - (Math.PI / 4)) + Math.cos(angle) * turn);
            backLeftPower = (power * Math.sin(angle - (Math.PI / 4)) + Math.sin(angle) * turn); //back
            frontRightPower = -(power * Math.sin(angle - (Math.PI / 4)) - Math.sin(angle) * turn);
            backRightPower = -(power * Math.cos(angle - (Math.PI / 4)) - Math.cos(angle) * turn); //back
        }
        if (wheelslippage) {
            Target();
        }
        else {
            setMecanumDrive(frontLeftPower, backLeftPower, frontRightPower, backRightPower);
        }
    }
    /*public void setPowersMecanum(Gamepad gamepad) {
        frontLeftPower = gamepad.left_stick_y + gamepad.left_stick_x;
        backLeftPower = gamepad.left_stick_y + gamepad.left_stick_x;
        frontRightPower = gamepad.left_stick_y + gamepad.left_stick_x;
        backRightPower = gamepad.left_stick_y + gamepad.left_stick_x;
        setMecanumDrive(frontLeftPower, backLeftPower, frontRightPower, backRightPower);
    }*/
    private void setMecanumDrive(double frontL, double backL, double frontR, double backR) {


        // For Deadzones
        if (Math.abs(frontL) > 0.1) {
            frontLeft.setPower(frontL); //left
        }
        else {
            frontLeft.setPower(0);
        }

        if (Math.abs(backL) > 0.1) {
            backLeft.setPower(backL);  //left
        }
        else {
            backLeft.setPower(0);
        }

        if (Math.abs(frontR) > 0.1) {
            frontRight.setPower(frontR);
        }
        else {
            frontRight.setPower(0);
        }

        if (Math.abs(backR) > 0.1) {
            backRight.setPower(backR);
        }
        else {
            backRight.setPower(0);
        }

    }

    public void MechanumCompact() {
        servoL.setPosition(.7);
        servoR.setPosition(.4);
    }
}