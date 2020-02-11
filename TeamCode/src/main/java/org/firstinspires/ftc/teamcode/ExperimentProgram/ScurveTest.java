package org.firstinspires.ftc.teamcode.ExperimentProgram;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.util.ElapsedTime;

//@Disabled
@TeleOp(name = "S-curve Test")
@Disabled
public class ScurveTest extends OpMode {
    //motor declare
    public DcMotor pulleymotor1;

    ElapsedTime elaptime = new ElapsedTime();
    //public double functime = getRuntime();
    public double deltafunctime = 0;
    public double startfunctime = 0;
    public double motorpower1 = 0;//same with motorpower

    public void init() {
        //obtains the hardware map
        HardwareMap hvm = hardwareMap;
        pulleymotor1 = hvm.get(DcMotor.class, "pulleymotor1");
    }

    public double timer(double time) {
        deltafunctime = time - startfunctime;
        telemetry.addData("poop", deltafunctime);
        return deltafunctime;

    }

    public void loop() {
            /*pulleymotor1.setPower(gamepad1.right_stick_y);
            pulleymotor1.setPower(gamepad1.right_stick_y);*/


        //I maka da deadzone
        if (Math.abs(gamepad1.right_stick_y) < .1) {
            pulleymotor1.setPower(0);
            startfunctime = elaptime.seconds();
        }
        else {
            while (timer(elaptime.seconds()) <= 10) {
                motorpower1 = Math.pow((1 / 10) * timer(elaptime.seconds()) * (1 / gamepad1.right_stick_y), 3);
                pulleymotor1.setPower(motorpower1);
            }

            if (timer(elaptime.seconds()) > 10) {
                pulleymotor1.setPower(gamepad1.right_stick_y);
            }
        }


                /*while (deltafunctime<20) {
                    motorpower1 = Math.pow((1 / 20) * deltafunctime * (1 / gamepad1.right_stick_y), 3);
                    pulleymotor1.setPower(motorpower1);

                    deltafunctime++;

                    try {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                }

                if (deltafunctime>=20) {
                    pulleymotor1.setPower(gamepad1.right_stick_y);
                }*/




                /*if (deltafunctime > 0 && deltafunctime <= 5) {

                    deltafunctime = getRuntime() - functime;
                    motorpower1 = Math.pow((1 / 3) * deltafunctime * (1 / gamepad1.right_stick_y), 3);

                    pulleymotor1.setPower(motorpower1);

                }
                else if (deltafunctime > 5) {
                    pulleymotor1.setPower(gamepad1.right_stick_y);
                }*/
    }
}

//motor wheel diameter 21.95mm
