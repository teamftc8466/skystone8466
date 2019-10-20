package org.firstinspires.ftc.teamcode.Robot;

import android.hardware.Sensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public abstract class Subsystem {

    public DcMotor[] motors;
    public Servo[]   servos;
    public Sensor[]  sensors;

    public Subsystem(DcMotor[] motors, Servo[] servos, Sensor[] sensors){
        this.motors = motors;
        this.servos = servos;
        this.sensors = sensors;
    }

}
