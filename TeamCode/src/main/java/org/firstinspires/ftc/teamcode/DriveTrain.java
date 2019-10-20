/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

/**
 * This 2019-2020 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Skystone game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */

public class DriveTrain {
    enum DriveMode {
        FORWARD,
        BACKWARD,
        TURN_LEFT,
        TURN_RIGHT,
        SHIFT_LEFT,
        SHIFT_RIGHT,
        STOP
    }

    private Telemetry telemetry_ = null;

    /// Drive train motors
    private DcMotor motorRight_ = null;
    private DcMotor motorLeft_ = null;
    private DcMotor motorCenter_ = null;

    /// Power scale
    double powerFactor_ = 1.0;

    /// Constructor
    public DriveTrain(DcMotor motor_left,
                      DcMotor motor_center,
                      DcMotor motor_right,
                      Telemetry telemetry) {
        motorLeft_ = motor_left;
        motorRight_ = motor_right;
        motorCenter_ = motor_center;
        telemetry_ = telemetry;

        motorLeft_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorCenter_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRight_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        ///Reverse if necessary
        //motorLeft_.setDirection(DcMotor.Direction.REVERSE);
        //motorCenter_.setDirection(DcMotor.Direction.REVERSE);
        //motorRight_.setDirection(DcMotor.Direction.REVERSE);

        useEncoder();
    }

    void useEncoder(){
        motorLeft_.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorCenter_.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRight_.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    void resetEncoder() {
        motorLeft_.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorCenter_.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight_.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    void driveByGamePad(Gamepad gamepad) {
        double power_left = 0.0;
        double power_center = 0.0;
        double power_right = 0.0;

        // TBD: Depend on game pad to set power value

        setPower(power_left, power_center, power_right);
    }

    void driveByAutonomous(DriveMode drive_mode) {
        double power_left = 0.0;
        double power_center = 0.0;
        double power_right = 0.0;

        switch (drive_mode) {
            case FORWARD:
                // TBD
                break;
            case BACKWARD:
                // TBD
                break;
            case TURN_LEFT:
                //TBD
                break;
            case TURN_RIGHT:
                //TBD
                break;
            case SHIFT_LEFT:
                //TBD
                break;
            case SHIFT_RIGHT:
                //TBD
                break;
            default:
                break;
        }

        setPower(power_left, power_center, power_right);
    }

    void setPower(double power_left,
                  double power_center,
                  double power_right) {

        if (powerFactor_ != 1.0) {
            power_left *= powerFactor_;
            power_center *= powerFactor_;
            power_right *= powerFactor_;
        }

        power_left = Range.clip(power_left, -1, 1);
        power_center = Range.clip(power_center, -1, 1);
        power_right = Range.clip(power_right, -1, 1);

        motorLeft_.setPower(power_left);
        motorCenter_.setPower(power_center);
        motorRight_.setPower(power_right);
    }
}
