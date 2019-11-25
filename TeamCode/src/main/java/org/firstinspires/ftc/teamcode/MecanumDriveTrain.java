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
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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

public class MecanumDriveTrain {
    private Telemetry telemetry_ = null;

    /// Heading variables                                        // TODO: Correct numbers later
    static final double MAX_HEADING_CORRECTION_ERROR = 40;
    static final double MAX_HEADING_CORRECTION_GAIN = 0.012;
    static final double MAX_HEADING_CORRECTION = 0.95;

    /// Default drivetrain power
    static final double DEFAULT_DRIVE_POWER = 0.40;              // defualt driving power, change later
    static final double DEFAULT_TURN_POWER = 0.15;               // default turning power, change later
    static final double DEFAULT_SHIFT_POWER = 0.15;              // default shifting power, change later

    /// Encoder length conversion scales                         // TODO: Adjust based on experiments
    static final double ENCODER_DISTANCE_SCALE = (2000.0 / 1.25);
    static final double ENCODER_SHIFT_DISTANCE_SCALE = (2000.0 / 1.25);
    static final double ENCODER_DEGREE_SCALE = (2000.0 / 225.0);

    /// Drive train motors
    private DcMotor motorLF_ = null;
    private DcMotor motorLB_ = null;
    private DcMotor motorRF_ = null;
    private DcMotor motorRB_ = null;

    /// Power scale
    double powerFactor_ = 1.0;

    /// Encoder time out variables
    static final double AUTO_ENC_STUCK_TIME_OUT = 2.0;
    int prevReadEncCntMotorLF_ = 0;
    int prevReadEncCntMotorRF_ = 0;
    double prevEncCntChangeStartTime_ = 0;


    /// Constructor
    public MecanumDriveTrain(DcMotor motor_lf,
                             DcMotor motor_rf,
                             DcMotor motor_lb,
                             DcMotor motor_rb,
                             Telemetry telemetry) {
        motorLF_ = motor_lf;
        motorRF_ = motor_rf;
        motorLB_ = motor_lb;
        motorRB_ = motor_rb;
        telemetry_ = telemetry;

        motorLF_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLB_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRF_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRB_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        ///reverseMotorDirection();

        useEncoder();
        resetEncoder(0);
    }

    void reverseMotorDirection(){
        motorLF_.setDirection(DcMotor.Direction.REVERSE);
        motorLB_.setDirection(DcMotor.Direction.REVERSE);
        motorRF_.setDirection(DcMotor.Direction.REVERSE);
        motorRB_.setDirection(DcMotor.Direction.REVERSE);
    }

    void useEncoder(){
        motorLF_.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLB_.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRF_.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRB_.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    void resetEncoder(double time){
        motorLF_.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLB_.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRF_.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRB_.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        prevReadEncCntMotorLF_ = 0;
        prevReadEncCntMotorRF_ = 0;
        prevEncCntChangeStartTime_ = time;
    }

    void driveByGamePad(Gamepad gamepad) {
        double power_lf = 0.0;
        double power_lb = 0.0;
        double power_rf = 0.0;
        double power_rb = 0.0;

        // TBD: Depend on game pad to set power value

        setPower(power_lf, power_lb, power_rf, power_rb);
    }

    void driveByMode(DriveTrainMode drive_mode,
                     boolean show_set_motor_info) {
        double power_lf = getMotorLFPowerByMode(drive_mode);
        double power_rf = getMotorRFPowerByMode(drive_mode);

        double power_lb = power_lf;
        double power_rb = power_rf;
        switch (drive_mode) {
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
                power_lb = -power_lf;
                power_rb = -power_lf;
                break;
            default:
                break;
        }

        setPower(power_lf, power_lb, power_rf, power_rb);

        if(show_set_motor_info = true) {
            telemetry_.addData("Current drivemode", String.valueOf(drive_mode));

            showSetPower(power_lf, power_lb, power_rf, power_rb, false);
            showEncoderValue(false);

            telemetry_.update(); // Keep so that if telemetry is true for both functions, all telemetry will be shown
        }
    }

    void showSetPower(double power_lf,
                      double power_lb,
                      double power_rf,
                      double power_rb,
                      boolean update_flag) {
        telemetry_.addData("Set power",
                "PowerFactor="+String.valueOf(powerFactor_)+
                        "LF="+String.valueOf(power_lf)+
                        ", LB="+String.valueOf(power_lb)+
                        ", RF="+String.valueOf(power_rf)+
                        ", RB="+String.valueOf(power_rb));

        if (update_flag == true) telemetry_.update();
    }

    void showEncoderValue(boolean update_flag){
        telemetry_.addData("Current encoder value",
                "LF="+String.valueOf(motorLF_.getCurrentPosition())+
                        ", LB="+String.valueOf(motorLB_.getCurrentPosition())+
                        ", RF="+String.valueOf(motorRF_.getCurrentPosition())+
                        ", RB="+String.valueOf(motorRB_.getCurrentPosition()));

        if (update_flag == true) telemetry_.update();
    }

    double getMotorLFPowerByMode(DriveTrainMode drive_mode) {
        switch (drive_mode) {
            case FORWARD:
                return DEFAULT_DRIVE_POWER;
            case BACKWARD:
                return -DEFAULT_DRIVE_POWER;
            case TURN_LEFT:
                return -DEFAULT_TURN_POWER;
            case TURN_RIGHT:
                return DEFAULT_TURN_POWER;
            case SHIFT_LEFT:
                return DEFAULT_SHIFT_POWER;    // Fix later
            case SHIFT_RIGHT:
                return -DEFAULT_SHIFT_POWER;     // Fix later
            default:
                break;
        }

        return 0.0;
    }

    double getMotorRFPowerByMode(DriveTrainMode drive_mode) {
        switch (drive_mode) {
            case FORWARD:
                return -DEFAULT_DRIVE_POWER;
            case BACKWARD:
                return DEFAULT_DRIVE_POWER;
            case TURN_LEFT:
                return -DEFAULT_TURN_POWER;
            case TURN_RIGHT:
                return DEFAULT_TURN_POWER;
            case SHIFT_LEFT:
                return DEFAULT_SHIFT_POWER;    // Fix later
            case SHIFT_RIGHT:
                return -DEFAULT_SHIFT_POWER;   // Fix later
            default:
                break;
        }

        return 0.0;
    }

    void setPower(double power_lf,
                  double power_lb,
                  double power_rf,
                  double power_rb) {

        if (powerFactor_ != 1.0) {
            power_lf *= powerFactor_;
            power_lb *= powerFactor_;
            power_rf *= powerFactor_;
            power_rb *= powerFactor_;
        }

        power_lf = Range.clip(power_lf, -1, 1);
        power_lb = Range.clip(power_lb, -1, 1);
        power_rf = Range.clip(power_rf, -1, 1);
        power_rb = Range.clip(power_rf, -1, 1);

        motorLF_.setPower(power_lf);
        motorLB_.setPower(power_lb);
        motorRF_.setPower(power_rf);
        motorRB_.setPower(power_rb);
    }

    void setPowerFactor(double input_power_factor) {         // Setting boundaries of power factors
        if (input_power_factor <= 0.33) {
            powerFactor_ = 0.33;
        } else if (input_power_factor >= 3.0) {
            powerFactor_ = 3.0;
        } else {
            powerFactor_ = input_power_factor;
        }
    }

    int convertDistanceToEncoderCount(double distance_in_meters) {
        return (int)(distance_in_meters * (double)ENCODER_DISTANCE_SCALE);
    }

    int convertShiftDistanceToEncoderCount(double distance_in_meters) {
        return (int)(distance_in_meters * (double)ENCODER_SHIFT_DISTANCE_SCALE);
    }

    int convertTurnDegreeToEncoderCount(double turn_degree) {
        return (int)(turn_degree * ENCODER_DEGREE_SCALE);
    }

    boolean allEncodersAreReset() {
        return (motorLF_.getCurrentPosition() == 0 &&
                motorRF_.getCurrentPosition() == 0 &&
                motorLB_.getCurrentPosition() == 0 &&
                motorRB_.getCurrentPosition() == 0);
    }

    boolean reachToTargetEncoderCount(int target_encoder_cnt) {
        return (motorLF_.getCurrentPosition() >= target_encoder_cnt &&
                motorRF_.getCurrentPosition() >= target_encoder_cnt &&
                motorLB_.getCurrentPosition() >= target_encoder_cnt &&
                motorRB_.getCurrentPosition() >= target_encoder_cnt);
    }

    boolean isEncoderStuck(double time) {
        int currEncPosMotorLF_ = motorLF_.getCurrentPosition();
        int currEncPosMotorRF_ = motorRF_.getCurrentPosition();

        if (currEncPosMotorLF_ != prevReadEncCntMotorLF_ &&
                currEncPosMotorLF_ != prevReadEncCntMotorRF_){
            prevReadEncCntMotorLF_ = currEncPosMotorLF_;
            prevReadEncCntMotorRF_ = currEncPosMotorRF_;
            prevEncCntChangeStartTime_ = time;

            return false;
        }

        return (time >= (prevEncCntChangeStartTime_ + AUTO_ENC_STUCK_TIME_OUT));
    }
}
