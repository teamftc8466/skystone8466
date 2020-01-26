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

    public boolean wheelslippage = false;

    double accumulatederror = 0;

    double olderror = 0;
    double oldtime = 0;

    ElapsedTime elaptime;

    public LucasMecanum(HardwareMap hwm, Telemetry t) {
        frontLeft = hwm.get(DcMotor.class, "motorLF");
        frontRight = hwm.get(DcMotor.class, "motorRF");
        backLeft = hwm.get(DcMotor.class, "motorLB");
        backRight = hwm.get(DcMotor.class, "motorRB");

        servoL = hwm.servo.get("leftHookServo");
        servoR = hwm.servo.get("rightHookServo");
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


    public void Hook(boolean input) {
        if (input) {
            switch (open) {
                case 0:
                    servoL.setPosition(90);
                    servoR.setPosition(90);
                    open = 1;
                    break;

                case 1:
                    servoL.setPosition(0);
                    servoR.setPosition(0);
                    open = 0;
                    break;

                default:
                    open = 0;
            }
        }
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

    public void Target() {
        targetflp += frontLeftPower;
        targetfrp += frontRightPower;
        targetblp += backLeftPower;
        targetbrp += backRightPower;

        Error(targetflp, targetfrp, targetblp, targetbrp);
    }

    public void Error(double flp, double frp, double blp, double brp) {
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

    public void omniMecanumDrive(Gamepad gamepad) {
        double hypotenuse = Math.sqrt(gamepad.left_stick_y*gamepad.left_stick_y+gamepad.left_stick_x*gamepad.left_stick_x);
        double angle = Math.atan2(gamepad.left_stick_y , gamepad.left_stick_x);
        double turn = gamepad.right_stick_x;
        omnimecanumdrivepowers(hypotenuse, angle, turn);
    }

    public void omnimecanumdrivepowers(double power, double angle, double turn) {
        frontLeftPower = -(power * Math.sin(angle - (Math.PI / 4)) - Math.cos(angle) * turn);
        backLeftPower = -(power * Math.cos(angle - (Math.PI / 4)) - Math.cos(angle) * turn); //back
        frontRightPower = (power * Math.cos(angle - (Math.PI / 4)) + Math.cos(angle) * turn);
        backRightPower = (power * Math.sin(angle - (Math.PI / 4)) + Math.cos(angle) * turn); //back

        if (wheelslippage == true) {
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
    public void setMecanumDrive(double frontL, double backL, double frontR, double backR) {


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

}