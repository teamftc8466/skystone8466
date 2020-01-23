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
import com.qualcomm.robotcore.hardware.HardwareMap;

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

public class DriveTrain {
    private Telemetry telemetry_ = null;

    /// IMU used for detecting heading during autonomous
    private RevImu imu_ = null;

    private MecanumDriveTrain mecanumDriveTrain_ = null;

    private BallDriveTrain ballDriveTrain_ = null;

    private boolean showDriveTrainInfo_ = false;

    /// Constructor
    public DriveTrain(RevImu imu,
                      Telemetry telemetry) {
        imu_ = imu;
        telemetry_ = telemetry;
    }

    void enableShowDriveTrainInfo() { showDriveTrainInfo_ = true; }
    void disShowDriveTrainInfo() { showDriveTrainInfo_ = false; }

    void disableToUseImu() {
        if (mecanumDriveTrain_ != null) mecanumDriveTrain_.disableToUseImu();
    }

    void enableToUseImu() {
        if (mecanumDriveTrain_ != null) mecanumDriveTrain_.enableToUseImu();
    }

    void createMecanumDriveTrain(HardwareMap hardware_map) {
        mecanumDriveTrain_ = new MecanumDriveTrain(hardware_map,
                                                   imu_,
                                                   telemetry_);
        mecanumDriveTrain_.useEncoder();
        mecanumDriveTrain_.resetEncoder(0);
    }

    MecanumDriveTrain mecanumDriveTrain() { return mecanumDriveTrain_; }

    boolean isMecanumDriveTrainExisting() {                           // For debugging purposes
        if (mecanumDriveTrain_ == null) {
            telemetry_.addLine("Mecanum drive does not exist");
            telemetry_.update();
            return false;
        }

        telemetry_.addLine("Mecanum drive exists");
        if (mecanumDriveTrain_.motorLF() != null) telemetry_.addLine("MotorLF exists");
        if (mecanumDriveTrain_.motorRF() != null) telemetry_.addLine("MotorRF exists");
        if (mecanumDriveTrain_.motorLB() != null) telemetry_.addLine("MotorLB exists");
        if (mecanumDriveTrain_.motorRB() != null) telemetry_.addLine("MotorRB exists");
        telemetry_.update();
        return true;
    }

    void createBallDriveTrain(HardwareMap hardwave_map) {
        ballDriveTrain_ = new BallDriveTrain(hardwave_map);
    }

    void useEncoder() {
        if (mecanumDriveTrain_ != null) mecanumDriveTrain_.useEncoder();
        // if (ballDriveTrain_ != null) ballDriveTrain_.useEncoder(time);
    }

    void resetEncoder(double time) {
        if (mecanumDriveTrain_ != null) mecanumDriveTrain_.resetEncoder(time);
        // if (ballDriveTrain_ != null) ballDriveTrain_.resetEncoder(time);
    }

    boolean allEncodersAreReset() {
        if (mecanumDriveTrain_ != null) mecanumDriveTrain_.allEncodersAreReset();
        // if (ballDriveTrain_ != null) ballDriveTrain_.allEncodersAreReset(time);

        return true;
    }

    void setPowerFactor(double input_power_factor) {
        if (mecanumDriveTrain_ != null) mecanumDriveTrain_.setPowerFactor(input_power_factor);
        // if (ballDriveTrain_ != null) ballDriveTrain_.setPowerFactor(input_power_factor);         // Sample line for when the ball drive will be implemented
    }

    double powerFactor() {
        if (mecanumDriveTrain_ != null) return mecanumDriveTrain_.powerFactor();
        // else if (ballDriveTrain_ != null) return ballDriveTrain_.powerFactor();

        return 1;
    }

    void driveByGamePad(Gamepad gamepad) {
        if (mecanumDriveTrain_ != null) mecanumDriveTrain_.driveByGamePad(gamepad);
        else if (ballDriveTrain_ != null) ballDriveTrain_.driveByGamePad(gamepad);
    }

    boolean driveByMode(DriveTrainMode drive_mode,
                     double drive_parameter,
                     double time) {
        if (mecanumDriveTrain_ != null) return mecanumDriveByMode(drive_mode, drive_parameter, time);
        //if (ballDriveTrain_ != null) return ballDriveByMode(drive_mode, drive_parameter, time);

        return true;
    }

    boolean mecanumDriveByMode(DriveTrainMode drive_mode,
                                    double drive_parameter,
                                    double time) {
        if (drive_parameter <= 0){
            mecanumDriveTrain_.setPower(0,0,0,0);
            return true;
        }

        int target_enc_cnt = 0;
        boolean finish_flag = false;
        switch (drive_mode){
            case FORWARD:
            case BACKWARD:
                target_enc_cnt = mecanumDriveTrain_.convertDistanceToEncoderCount(drive_parameter);
                break;
            case TURN_LEFT:
            case TURN_RIGHT:
                target_enc_cnt = mecanumDriveTrain_.convertTurnDegreeToEncoderCount(drive_parameter);
                break;
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
                target_enc_cnt = mecanumDriveTrain_.convertShiftDistanceToEncoderCount(drive_parameter);
                break;
            default:
                finish_flag = true;
        }

        if (finish_flag == false){
            if (mecanumDriveTrain_.reachToTargetEncoderCount(target_enc_cnt) == false) {
                if (showDriveTrainInfo_ == true) {
                    telemetry_.addData("Target encoder count", String.valueOf(target_enc_cnt));
                }

                mecanumDriveTrain_.driveByMode(drive_mode, showDriveTrainInfo_);

                if (mecanumDriveTrain_.isEncoderStuck(time) == false) {
                    if (mecanumDriveTrain_.useImu() == true) {
                        switch (drive_mode) {
                            case TURN_LEFT:
                            case TURN_RIGHT:
                                if (Math.abs(imu_.getHeadingDifference(imu_.targetHeading())) < 2) {
                                    return true;
                                }
                                break;
                            default:
                                break;
                        }
                    }

                    return false;
                } else {
                    // Encoder is stuck. Force current drive mode to end.
                    mecanumDriveTrain_.setPower(0,0,0,0);
                    return true;
                }
            }
        }

        // The task is finished
        mecanumDriveTrain_.setPower(0,0,0,0);
        return true;
    }
}
