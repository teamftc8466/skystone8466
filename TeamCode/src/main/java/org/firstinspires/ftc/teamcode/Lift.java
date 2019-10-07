package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lift {

    private Telemetry telemetry_ = null;

    ///Motors
    DcMotor motorLift_;

    ///Servos
    Servo servo1_;
    Servo servo2_; //TODO: Rename later when you know the functions

    ///Constructor
    public Lift (DcMotor motor_lift,
                 Servo servo_1,
                 Servo servo_2,
                 Telemetry telemetry) {
        motorLift_ = motor_lift;
        servo1_ = servo_1;
        servo2_ = servo_2;
        telemetry_ = telemetry;

        motorLift_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        ///Reverse if necessary
        //motorLift_.setDirection(DcMotor.Direction.REVERSE);

        useEncoder();
    }

    void useEncoder(){
        motorLift_.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    void resetEncoder() {
        motorLift_.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
