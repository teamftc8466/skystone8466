package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Collection implements ISubsystem {

    HardwareMap hwm;

    public void init(HardwareMap hwm){
        hwm = this.hwm;
    }
}
