package org.firstinspires.ftc.teamcode.hardwareGroups;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class KiwiDriveTrain {
    private DcMotor motor1 = null;
    private DcMotor motor2 = null;
    private DcMotor motor3 = null;

    public KiwiDriveTrain(HardwareMap hdm) {
        motor1 = hdm.dcMotor.get("Motor1");
        motor2 = hdm.dcMotor.get("Motor2");
        motor3 = hdm.dcMotor.get("Motor3");
    }
    public void forward(float joystick) {
        motor2.setDirection(DcMotorSimple.Direction.FORWARD);
        motor3.setDirection(DcMotorSimple.Direction.FORWARD);
          motor2.setPower(joystick);
          motor3.setPower(joystick);

    }
    public void backward(float joystick) {
        motor2.setDirection(DcMotorSimple.Direction.REVERSE);
        motor3.setDirection(DcMotorSimple.Direction.REVERSE);
            motor2.setPower(joystick);
            motor3.setPower(joystick);
    }
    //public void
}
