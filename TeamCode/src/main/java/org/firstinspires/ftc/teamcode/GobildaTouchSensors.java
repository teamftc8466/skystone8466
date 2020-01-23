package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class GobildaTouchSensors {
    TouchSensor touch1;
    TouchSensor touch2;
    Telemetry telemetry;

    public GobildaTouchSensors(TouchSensor t1, TouchSensor t2, Telemetry t) {
        touch1 = t1;
        touch2 = t2;
        telemetry = t;
    }

    boolean touching() {
        if (touch1.isPressed() && touch2.isPressed()) {
            return true;
        }
        return false;
    }

}
