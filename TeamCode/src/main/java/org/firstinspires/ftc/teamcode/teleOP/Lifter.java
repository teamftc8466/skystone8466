package org.firstinspires.ftc.teamcode.teleOP;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lifter {
    private DcMotor motorL = null;
    private DcMotor motorR = null;
    private int holdpositionL = -1;
    private int holdpositionR = -1;
    public boolean isholding = false;
    private static final int MIN_HOLD_POSITION = -300;
    private Telemetry telemetry = null;
    private static final int MAX_POSITION = 2300;
    private static final int MIN_POSITION = 0;

    public int readyDrop = -500;
    private int driverheight = 0;
    private final int changeInEncoders = 200; // test if this is the height of the increment/decrement
    private boolean upispressed = false;
    private boolean downispressed = false;

    //Constructer, sets up code and gets the two motors w/ telemetry
    public Lifter(HardwareMap hwm, Telemetry telemetry) {
        this.motorL = hwm.dcMotor.get("motorLiftLeft");
        this.motorR = hwm.dcMotor.get("motorLiftRight");
        motorL.setDirection(DcMotorSimple.Direction.FORWARD);
        motorR.setDirection(DcMotorSimple.Direction.REVERSE);
        this.telemetry = telemetry;

        //motorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //motorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    //Method for driving up tests if its less than max position
    public void driveup (double power){
        if (Math.abs(motorL.getCurrentPosition())< MAX_POSITION){
            motorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorL.setPower(power);
            motorR.setPower(power);
        }
    }

    public void drivedown (double power){
        if (Math.abs(motorL.getCurrentPosition())> MIN_POSITION){
            motorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorR.setPower(.5*power);
            motorL.setPower(.5*power);
        }
        else{
            motorL.setPower(0);
            motorR.setPower(0);
        }
    }
    public void manualdrive(float power){
        if (power >=.1){
            drivedown(power);
            isholding = false;
        }
        else if (power<=-.1){
            driveup(power);
            isholding = false;
        }


    }

    public void holdposition(){

        /**if (Math.abs(motorL.getCurrentPosition())<MIN_HOLD_POSITION){
            motorL.setPower(0);
            motorR.setPower(0);
            return;
        }**/


        if (isholding == false) {
            holdpositionL = motorL.getCurrentPosition();
            holdpositionR = motorR.getCurrentPosition();
            isholding = true;
        }
        if (isholding == true) {
            motorL.setTargetPosition(holdpositionL);
            motorR.setTargetPosition(holdpositionR);
            motorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorL.setPower(.5);
            motorR.setPower(.5);
        }
    }

    public void telem() {
        telemetry.addData("Lifter L", motorL.getCurrentPosition());
        telemetry.addData("Lifter R", motorR.getCurrentPosition());
    }

    public int SetHeight(boolean inc, boolean dec) {
        if (inc) {
            upispressed = true;
        }

        if (inc == false && Math.abs(readyDrop)<(MAX_POSITION-changeInEncoders) && upispressed) {
            readyDrop -= changeInEncoders;
        }




        if (dec) {
            downispressed = true;
        }

        if (dec == false && Math.abs(readyDrop)>changeInEncoders && downispressed) {
            readyDrop += changeInEncoders;
        }

        return readyDrop;
    }


    public void GoToDriveHeight(Gamepad gamepad) {
        if (gamepad.a) {
            if (motorL.getCurrentPosition() <= motorL.getTargetPosition()) {
                motorL.setTargetPosition(readyDrop);
                motorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorR.setTargetPosition(readyDrop);
                motorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorL.setPower(1);
                motorR.setPower(1);
            }

            else {
                motorL.setTargetPosition(readyDrop);
                motorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorR.setTargetPosition(readyDrop);
                motorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorL.setPower(-1);
                motorR.setPower(-1);
            }

        }
        if (motorL.getCurrentPosition() == motorL.getTargetPosition()) {
            motorL.setPower(0);
            motorR.setPower(0);
            isholding = false;
        }
    }

    public void LifterCompact() {
            motorL.setTargetPosition(0);
            motorR.setTargetPosition(0);
            motorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorL.setPower(1);
            motorR.setPower(1);
        if (motorL.getCurrentPosition() == motorL.getTargetPosition()) {
            motorL.setPower(0);
            motorR.setPower(0);
            isholding = false;
        }
    }


    public void RaiseToCollectHeight() {
        if (motorL.getCurrentPosition() <= motorL.getTargetPosition()) {
            motorL.setTargetPosition(-400);
            motorR.setTargetPosition(-400);
            motorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorL.setPower(1);
            motorR.setPower(1);
        }
        else {
            motorL.setTargetPosition(-400);
            motorR.setTargetPosition(-400);
            motorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorL.setPower(-1);
            motorR.setPower(-1);
        }
        if (motorL.getCurrentPosition() == motorL.getTargetPosition()) {
            motorL.setPower(0);
            motorR.setPower(0);
            isholding = false;
        }
    }

    public void LowerToCollectHeight() {
        if (motorL.getCurrentPosition() <= motorL.getTargetPosition()) {
            motorL.setTargetPosition(-140);
            motorR.setTargetPosition(-140);
            motorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorL.setPower(1);
            motorR.setPower(1);
        }
        else {
            motorL.setTargetPosition(-140);
            motorR.setTargetPosition(-140);
            motorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorL.setPower(-1);
            motorR.setPower(-1);
        }
        if (motorL.getCurrentPosition() == motorL.getTargetPosition()) {
            motorL.setPower(0);
            motorR.setPower(0);
            isholding = false;
        }
    }
}
