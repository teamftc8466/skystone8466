package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot implements IAutoRobot, ITeleOpRobot{

    public HardwareMap hwm;
    public RobotConfig rbc;

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
}
