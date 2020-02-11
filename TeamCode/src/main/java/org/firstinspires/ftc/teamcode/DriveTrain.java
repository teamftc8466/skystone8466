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

    private final double MAX_TOLERATE_HEADING_DIFFERENCE_ERROR_IN_DEGREE = 2;
    private boolean controlTurnDegreeByEncoderCnt_ = true;
    private boolean showDriveTrainInfo_ = false;

    /// Constructor
    public DriveTrain(RevImu imu,
                      Telemetry telemetry) {
        imu_ = imu;
        telemetry_ = telemetry;
    }

    public void enableShowDriveTrainInfo() { showDriveTrainInfo_ = true; }
    public void disableShowDriveTrainInfo() { showDriveTrainInfo_ = false; }

    public void disableToUseImu() {
        if (mecanumDriveTrain_ != null) mecanumDriveTrain_.disableToUseImu();
    }
    public void enableToUseImu() {
        if (mecanumDriveTrain_ != null) mecanumDriveTrain_.enableToUseImu();
    }

    public void disableControlTurnDegreeByEncoderCount() { controlTurnDegreeByEncoderCnt_ = false; }
    public void enableControlTurnDegreeByEncoderCount() { controlTurnDegreeByEncoderCnt_ = true; }

    public void createMecanumDriveTrain(HardwareMap hardware_map) {
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

    public void resetEncoder(double time) {
        if (mecanumDriveTrain_ != null) mecanumDriveTrain_.resetEncoder(time);
        // if (ballDriveTrain_ != null) ballDriveTrain_.resetEncoder(time);
    }

    public boolean allEncodersAreReset() {
        if (mecanumDriveTrain_ != null) mecanumDriveTrain_.allEncodersAreReset();
        // if (ballDriveTrain_ != null) ballDriveTrain_.allEncodersAreReset(time);

        return true;
    }

    public void setPowerFactor(double input_power_factor) {
        if (mecanumDriveTrain_ != null) mecanumDriveTrain_.setPowerFactor(input_power_factor);
        // if (ballDriveTrain_ != null) ballDriveTrain_.setPowerFactor(input_power_factor);         // Sample line for when the ball drive will be implemented
    }

    public double powerFactor() {
        if (mecanumDriveTrain_ != null) return mecanumDriveTrain_.powerFactor();
        // else if (ballDriveTrain_ != null) return ballDriveTrain_.powerFactor();

        return 1;
    }

    void driveByGamePad(Gamepad gamepad) {
        if (mecanumDriveTrain_ != null) mecanumDriveTrain_.driveByGamePad(gamepad);
        else if (ballDriveTrain_ != null) ballDriveTrain_.driveByGamePad(gamepad);
    }

    public boolean driveByMode(DriveTrainMode drive_mode,
                        double drive_parameter,
                        double power_factor,
                        double time) {
        if (mecanumDriveTrain_ != null) {
            final double saved_power_factor = mecanumDriveTrain_.powerFactor();
            if (power_factor > 0) mecanumDriveTrain_.setPowerFactor(power_factor);

            boolean finish_flag = mecanumDriveByMode(drive_mode, drive_parameter, time);

            // Restore original power factor
            if (saved_power_factor != mecanumDriveTrain_.powerFactor()) {
                mecanumDriveTrain_.setPowerFactor(saved_power_factor);
            }

            return finish_flag;
        }

        //if (ballDriveTrain_ != null) return ballDriveByMode(drive_mode, drive_parameter, power_factor, time);

        return true;
    }

    public boolean applyImuToControlTurningToTargetHeading(double max_tolerate_error_in_degree,
                                                    double power_factor,
                                                    double min_reduced_power_factor,
                                                    double time) {
        if (mecanumDriveTrain_ != null) return mecanumDriveApplyImuToControlTurningToTargetHeading(max_tolerate_error_in_degree,
                                                                                                   power_factor,
                                                                                                   min_reduced_power_factor,
                                                                                                   time);
        return true;
    }

    private boolean mecanumDriveByMode(DriveTrainMode drive_mode,
                                       double drive_parameter,
                                       double time) {
        if (drive_parameter <= 0){
            mecanumDriveTrain_.setPower(0,0,0,0);
            return true;
        }

        int target_enc_cnt = 0;
        boolean is_turn_operation_flag = false;
        boolean finish_flag = false;
        switch (drive_mode){
            case FORWARD:
            case BACKWARD:
                target_enc_cnt = mecanumDriveTrain_.convertDistanceToEncoderCount(drive_parameter);
                break;
            case TURN_LEFT:
            case TURN_RIGHT:
                is_turn_operation_flag = true;
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
            if (is_turn_operation_flag == true &&
                    controlTurnDegreeByEncoderCnt_ == false &&
                    mecanumDriveTrain_.useImu() == true) {
                return mecanumDriveApplyImuToControlTurningToTargetHeading(MAX_TOLERATE_HEADING_DIFFERENCE_ERROR_IN_DEGREE,
                                                                           mecanumDriveTrain_.powerFactor(),
                                                                           0.3,
                                                                           time);
            }

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
                                if (Math.abs(imu_.getHeadingDifference(imu_.targetHeading())) <= MAX_TOLERATE_HEADING_DIFFERENCE_ERROR_IN_DEGREE) {
                                    mecanumDriveTrain_.setPower(0,0,0,0);
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

    private boolean mecanumDriveApplyImuToControlTurningToTargetHeading(double max_tolerate_error_in_degree,
                                                                        double power_factor,
                                                                        double min_reduced_power_factor,
                                                                        double time) {
        if (max_tolerate_error_in_degree < 1) max_tolerate_error_in_degree = 1;
        else if (max_tolerate_error_in_degree > 10) max_tolerate_error_in_degree = 10;

        final double heading_diff = imu_.getHeadingDifference(imu_.targetHeading());
        final double abs_heading_diff = Math.abs(heading_diff);
        if (abs_heading_diff <= max_tolerate_error_in_degree) {
            mecanumDriveTrain_.setPower(0, 0, 0, 0);
            return true;
        }

        final double saved_power_factor = mecanumDriveTrain_.powerFactor();
        if (mecanumDriveTrain_.powerFactor() != power_factor) mecanumDriveTrain_.setPowerFactor(power_factor);

        if (abs_heading_diff < 10) {
            // When closing to the target degree, need to reduce the power for precise control
            double reduced_power_factor = 0.7;
            if (abs_heading_diff <= 5) reduced_power_factor = 0.35;

            if (reduced_power_factor < min_reduced_power_factor) reduced_power_factor = min_reduced_power_factor;

            if (mecanumDriveTrain_.powerFactor() > reduced_power_factor) mecanumDriveTrain_.setPowerFactor(reduced_power_factor);
        }

        if (showDriveTrainInfo_ == true) {
            telemetry_.addData("Target heading", String.valueOf(imu_.targetHeading()));
        }

        if (heading_diff > 0) {
            mecanumDriveTrain_.driveByMode(DriveTrainMode.TURN_LEFT, showDriveTrainInfo_);
        } else {
            mecanumDriveTrain_.driveByMode(DriveTrainMode.TURN_RIGHT, showDriveTrainInfo_);
        }

        // Restore original power factor
        if (saved_power_factor != mecanumDriveTrain_.powerFactor()) {
            mecanumDriveTrain_.setPowerFactor(saved_power_factor);
        }

        if (mecanumDriveTrain_.isEncoderStuck(time) == true) {
            // Encoder is stuck. Force current drive mode to end.
            mecanumDriveTrain_.setPower(0,0,0,0);
            return true;
        }

        return false;
    }
}
