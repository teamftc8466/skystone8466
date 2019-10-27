package org.firstinspires.ftc.teamcode.Robot;

import android.hardware.Sensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public interface ISubsystem {
    void init(HardwareMap hwm);
}
