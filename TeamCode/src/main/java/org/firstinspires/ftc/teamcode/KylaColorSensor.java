package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class KylaColorSensor {

    com.qualcomm.robotcore.hardware.ColorSensor color1;
    //KylaColorSensor color2;

    Telemetry telemetry;
    //HardwareMap hardwareMap;

    int RED;
    int GREEN;
    int BLUE;
    color c;


    public KylaColorSensor(ColorSensor c1, Telemetry t){
        telemetry = t;

        color1 = c1;
        //color2 = hardwareMap.colorSensor.get(sensor2);
        color1.enableLed(true);
    }

    public enum color{
        RED,
        BLUE,
        GREEN,
        GREY
    }



    public boolean isAligned(){
        boolean aligned = false;

        /*if (senseColor(color1).equals(color.RED) && senseColor(color2).equals(color.RED) || senseColor(color1).equals(color.BLUE) && senseColor(color2).equals(color.BLUE)){
            aligned = true;
        }*/

        return aligned;
    }

    public boolean isBlack(int threshold) {
        if(color1.red() < threshold && color1.blue() < threshold && color1.green() < threshold) {
            return true;
        }
        return false;
    }



    public color senseColor(){
        RED = color1.red();
        BLUE = color1.blue();

        if(RED > BLUE*2){
            c = color.RED;
        }
        else if( BLUE > RED*2){
            c = color.BLUE;
        }
        else {
            c = color.GREY;
        }
        return c;
    }

    public boolean senseBlueAndRed(){
        RED = color1.red();
        BLUE = color1.blue();

        if(RED > BLUE*2){
           return true;
        }
        else if( BLUE > RED*2){
            return true;
        }
        return false;
    }


    public void colorDebug(){

        telemetry.addData("Sensor 1 Red:",color1.red());
        telemetry.addData("Sensor 1 Blue:",color1.blue());
        telemetry.addData("Sensor 1 Green:",color1.green());
        telemetry.addLine("------------------");
        /*telemetry.addData("Sensor 2 Red:",color2.red());
        telemetry.addData("Sensor 2 Blue:",color2.blue());
        telemetry.addData("Sensor 2 Green:",color2.green());
        telemetry.addLine("------------------");*/
        telemetry.addData("S1 Color", senseColor());
        //telemetry.addData("S2 Color", senseColor(color2));
        telemetry.addData("Is color red or blue? ", senseBlueAndRed());
        telemetry.addData("Is black?  ", isBlack(10));
        telemetry.addLine("------------------");
        telemetry.addData("Aligned:", isAligned());
    }

}
