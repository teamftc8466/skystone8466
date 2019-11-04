package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lift {

    //Motors
    DcMotor motorLift_;

    Telemetry telemetry_;

    //Constructor
    public Lift (DcMotor motor_lift,
                 Telemetry telemetry) {
        motorLift_ = motor_lift;
        telemetry_ = telemetry;
    }

    public void loop(Gamepad g){

    }

    public void auto(){

    }

}
