package org.firstinspires.ftc.teamcode.teleOP;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lifter {
    private DcMotor motorL = null;
    private DcMotor motorR = null;
    private int holdpositionL = -1;
    private int holdpositionR = -1;
    private boolean isholding = false;
    private static final int MIN_HOLD_POSITION = 300;
    private Telemetry telemetry = null;


    public Lifter(HardwareMap hwm, Telemetry telemetry) {
        this.motorL = hwm.dcMotor.get("motorLiftLeft");
        this.motorR = hwm.dcMotor.get("motorLiftRight");
        motorL.setDirection(DcMotorSimple.Direction.FORWARD);
        motorR.setDirection(DcMotorSimple.Direction.REVERSE);
        this.telemetry = telemetry;

    }
    public void manualdrive(float power){
        motorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorL.setPower(power);
        motorR.setPower(power);
        isholding = false;
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


}
