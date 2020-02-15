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
    private boolean isholding = false;
    private static final int MIN_HOLD_POSITION = -300;
    private Telemetry telemetry = null;
    private static final int MAX_POSITION = 2300;
    private static final int MIN_POSITION = 100;

    private int readyDrop = -525;
    private int driverheight = 0;
    private final int changeInEncoders = -200; // test if this is the height of the increment/decrement

    //Constructer, sets up code and gets the two motors w/ telemetry
    public Lifter(HardwareMap hwm, Telemetry telemetry) {
        this.motorL = hwm.dcMotor.get("motorLiftLeft");
        this.motorR = hwm.dcMotor.get("motorLiftRight");
        motorL.setDirection(DcMotorSimple.Direction.FORWARD);
        motorR.setDirection(DcMotorSimple.Direction.REVERSE);
        this.telemetry = telemetry;

        motorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    //Method for driving up tests if its less than max position
    public void driveup (double power){
        if (Math.abs(motorR.getCurrentPosition())< MAX_POSITION){
            motorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorL.setPower(power);
            motorR.setPower(power);
        }
    }

    public void drivedown (double power){
        if (Math.abs(motorR.getCurrentPosition())> MIN_POSITION){
            motorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorR.setPower(power);
            motorL.setPower(power);
        }
        else{
            motorL.setPower(0);
            motorR.setPower(0);
        }
    }
    public void manualdrive(float power){
        if (power >=.1){
            drivedown(power );
            isholding = false;
        }
        else if (power<=-.1){
            driveup(power);
            isholding = false;
        }


    }

    public void holdposition(){

        if (Math.abs(motorL.getCurrentPosition())<MIN_HOLD_POSITION){
            motorL.setPower(0);
            motorR.setPower(0);
            return;
        }


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

    private int SetHeight(boolean inc, boolean dec) {

        if (inc && Math.abs(readyDrop)<MAX_POSITION-Math.abs(changeInEncoders)) {
            readyDrop -= changeInEncoders;
        }
        if (dec && Math.abs(readyDrop)>(38+Math.abs(changeInEncoders))) {
            readyDrop += changeInEncoders;
        }

        return readyDrop;
    }



    private void GoToDriveHeight(int dh, Gamepad gamepad) {
        if (gamepad.a) {
            motorL.setTargetPosition(dh);
            motorR.setTargetPosition(dh);
            motorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorL.setPower(1);
            motorR.setPower(1);
        }
        isholding = false;
    }

    private void AutoCollect() {

    }

    public void ExtendedFunction(Gamepad gamepad) {
        driverheight = SetHeight(gamepad.dpad_up, gamepad.dpad_down);
        GoToDriveHeight(driverheight, gamepad);
        //AutoCollect();
    }

    public void LifterCompact() {
        motorL.setTargetPosition(0);
        motorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorR.setTargetPosition(0);
        motorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorL.setPower(1);
        motorR.setPower(1);
    }
}
