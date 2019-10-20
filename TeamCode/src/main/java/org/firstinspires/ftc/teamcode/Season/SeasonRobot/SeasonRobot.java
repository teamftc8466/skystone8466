package org.firstinspires.ftc.teamcode.Season.SeasonRobot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Robot.Robot;

public class SeasonRobot extends Robot {
    DriveTrain dt;

    public SeasonRobot(HardwareMap hwm){
        super(hwm);
    }

    @Override
    public void initializeTele() {
        dt = new DriveTrain(hwm);
    }

    @Override
    public void initializeAuto() {
        dt = new DriveTrain(hwm);
    }

}
