package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class IMU implements ISubsystem {

    HardwareMap hwm;

    public void init(HardwareMap hwm){
        hwm = this.hwm;
    }
}
