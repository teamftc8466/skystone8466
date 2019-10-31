package org.firstinspires.ftc.teamcode.teleOP;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

//@Disabled
@TeleOp(name = "WinchTest1")
public class pulleytest extends OpMode {

    //motor declare
    public DcMotor pulleymotor1;
    //public double functime = getRuntime();
    ElapsedTime functtime = new ElapsedTime();
    //public double deltafunctime = 0;
    public double motorpower1 = 0;//same with motorpower

        public void init() {
            //obtains the hardware map
            HardwareMap hvm = hardwareMap;
            pulleymotor1 = hvm.get(DcMotor.class, "pulleymotor1");
        }

        public void loop() {
            //I maka da deadzone
            if(gamepad1.right_stick_y < .1 && gamepad1.right_stick_y > -.1) {
                pulleymotor1.setPower(0);
                functtime.reset();
                //functime = getRuntime();
            }
            else {

                if(functtime.time() <= 10) {
                    motorpower1 = Math.pow((1 / 10) * functtime.time() * (1 / gamepad1.right_stick_y), 3);
                    pulleymotor1.setPower(motorpower1);
                    //pulleymotor1.setPower(motorpower1);


                }
                if(functtime.time() > 10) {
                    pulleymotor1.setPower(gamepad1.right_stick_y);
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
    }

//motor wheel diameter 21.95mm
