package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Arm;

public class ArmAuto extends LinearOpMode {
    Arm arm = null;


    static final double     COUNTS_PER_MOTOR_REV    = .0/*1440*/ ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = .0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = .0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /(WHEEL_DIAMETER_INCHES * 3.1415);

    @Override
    public void runOpMode() {
        arm = new Arm(hardwareMap);

        waitForStart();


    }
}
