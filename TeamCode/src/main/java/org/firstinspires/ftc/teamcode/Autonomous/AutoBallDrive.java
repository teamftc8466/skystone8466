package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.BallDrive;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AutoBallDrive /*extends LinearOpMode*/ {
    //static final double     COUNTS_PER_MOTOR_REV    = .0/*1440*/ ;    // eg: TETRIX Motor Encoder
    //static final double     DRIVE_GEAR_REDUCTION    = .0 ;     // This is < 1.0 if geared UP
    //static final double     WHEEL_DIAMETER_INCHES   = .0 ;     // For figuring circumference
    //static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /(WHEEL_DIAMETER_INCHES * 3.1415);
/*
    @Override
    public void runOpMode() {
        double LeftTarget = BallDrive.motor1.getCurrentPosition() + moveCount;
        double RightTarget = BallDrive.motor2.getCurrentPosition() + moveCount;

// Set Target and Turn On RUN_TO_POSITION
        BallDrive.motor1.setTargetPosition(LeftTarget);
        BallDrive.motor2.setTargetPosition(RightTarget);

        BallDrive.motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BallDrive.motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }*/
}