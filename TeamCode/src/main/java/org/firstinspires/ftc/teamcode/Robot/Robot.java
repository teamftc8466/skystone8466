package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class Robot {

    public HardwareMap hwm;

    public Robot(HardwareMap hwm){
        this.hwm = hwm;
    }

    public abstract void initializeTele();
    public abstract void initializeAuto();
}
