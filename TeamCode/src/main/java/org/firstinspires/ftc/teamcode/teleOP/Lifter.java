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
    private static final int MIN_HOLD_POSITION = 238;
    private Telemetry telemetry = null;

    private int readyDrop = 525;
    private int driverheight = 0;


    public Lifter(HardwareMap hwm, Telemetry telemetry) {
        this.motorL = hwm.dcMotor.get("motorLiftLeft");
        this.motorR = hwm.dcMotor.get("motorLiftRight");
        motorL.setDirection(DcMotorSimple.Direction.FORWARD);
        motorR.setDirection(DcMotorSimple.Direction.REVERSE);
        this.telemetry = telemetry;
        motorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void manualdrive(float power){
        motorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if (power < 0 ) {
            motorL.setPower(power);
            motorR.setPower(power);
        }
        else {
            motorL.setPower(.5*power);
            motorR.setPower(.5*power);
        }
        isholding = false;
        telemetry.addData("Lifter L", motorL.getCurrentPosition());
        telemetry.addData("Lifter R",motorR.getCurrentPosition());
        telemetry.update();
    }

    public void holdposition(){

        if (Math.abs(motorL.getCurrentPosition())<MIN_HOLD_POSITION)
            return;

        if (isholding == false) {
            holdpositionL = motorL.getCurrentPosition();
            holdpositionR = motorR.getCurrentPosition();
            isholding = true;
        }
        if (isholding == true) {
            motorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorL.setTargetPosition(holdpositionL);
            motorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorR.setTargetPosition(holdpositionR);
            motorL.setPower(.5);
            motorR.setPower(.5);
        }
    }

    public void telem() {
        telemetry.addData("Lifter L", motorL.getCurrentPosition());
        telemetry.addData("Lifter R", motorR.getCurrentPosition());
    }

    private int SetHeight(boolean inc, boolean dec) {
        int changeInEncoders = -200; // test if this is the height of the increment/decrement

        if (inc) {
            readyDrop -= changeInEncoders;
        }
        if (dec && readyDrop <= 38) {
            readyDrop += changeInEncoders;
        }

        return readyDrop;
    }



    private void GoToDriveHeight(int dh, Gamepad gamepad) {
        if (gamepad.a) {
            motorR.setTargetPosition(dh);
            motorL.setTargetPosition(dh);
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
}
