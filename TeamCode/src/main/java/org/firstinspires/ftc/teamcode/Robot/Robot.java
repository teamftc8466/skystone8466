package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot implements IAutoRobot, ITeleOpRobot{

    HardwareMap hwm;
    RobotConfig rbc;

    //TODO: add subsystems, and initialize them in constructor
    //
    //TODO: add forward compatibility by finding common,
    //      roots between all the classes that are reusable
    //      from year to year. Like IMU, DriveTrain,
    //      controls, telemetry, and RobotConfig
    //TODO: possibly use annotations for forward compatibility

    @Override
    public void initAuto(HardwareMap hwm, RobotConfig rbc) {
        this.hwm = hwm;
        this.rbc = rbc;

        //Declare subsystem hardware members after this
    }

    @Override
    public void initTele(HardwareMap hwm, RobotConfig rbc) {
        this.hwm = hwm;
        this.rbc = rbc;

        //Declare subsystem hardware members after this
        //Ex: DcMotor m1 = Hwm.get(rbc.m1);
    }

    //TODO: generate getters and setters for subsystems
}
