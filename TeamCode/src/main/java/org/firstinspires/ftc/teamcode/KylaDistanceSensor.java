package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class KylaDistanceSensor {

    private Rev2mDistanceSensor sensorRange;

    Telemetry telemetry;


    public KylaDistanceSensor(DistanceSensor dis, Telemetry t) {
        telemetry = t;

        sensorRange = (Rev2mDistanceSensor)dis;
    }

    double getDistance(DistanceUnit unit){
        return sensorRange.getDistance(unit);
    }

    public void debug(){

        // generic DistanceSensor methods.
        telemetry.addData("deviceName",sensorRange.getDeviceName() );
        telemetry.addData("range", String.format("%.01f mm", sensorRange.getDistance(DistanceUnit.MM)));
        telemetry.addData("range", String.format("%.01f cm", sensorRange.getDistance(DistanceUnit.CM)));
        telemetry.addData("range", String.format("%.01f m", sensorRange.getDistance(DistanceUnit.METER)));
        telemetry.addData("range", String.format("%.01f in", sensorRange.getDistance(DistanceUnit.INCH)));

        // Rev2mDistanceSensor specific methods.
        telemetry.addData("ID", String.format("%x", sensorRange.getModelID()));
        telemetry.addData("did time out", Boolean.toString(sensorRange.didTimeoutOccur()));

        telemetry.update();
    }

}
