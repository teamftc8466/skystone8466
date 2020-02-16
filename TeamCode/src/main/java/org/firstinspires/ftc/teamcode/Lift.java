package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lift {
    // Lift position specified by encoder count
    public enum Position {
        LIFT_BOTTOM_POSITION(15),
        LIFT_GRAB_STONE_CATCH(2),
        LIFT_DELIVER_STONE(70),          // Lifts stone slightly above ground to allow robot to move the stone
        LIFT_STONE_READY(430),
        LIFT_GRAB_STONE_READY(600),        // was 500
        LIFT_DROP_TO_FOUNDATION(525),
        LIFT_TOP_POSITION(2140);

        private final int val_;

        private Position(int val) { val_ = val; }
        public int getValue() { return val_; }
    };

    private boolean showLiftInfo_ = false;

    //Motors
    private DcMotor motorLeft_ = null;
    private DcMotor motorRight_ = null;

    // Since the motor cannot be exactly accurate mostly due to the momentum of the linear slide,
    // this threshold allows for some wiggle room
    static final int ENCODER_THRESHOLD_FOR_TARGET_POSITION = 15;
    static final int ENCODER_CNT_FOR_USE_FULL_LIFT_POWER = (Position.LIFT_TOP_POSITION.getValue() - 500);

    /// Encoder time out variables
    static final double AUTO_ENC_STUCK_TIME_OUT = 2.0;
    private int prevReadEncCntMotorLeft_ = 0;
    private int prevReadEncCntMotorRight_ = 0;
    private double prevEncCntChangeStartTime_ = 0;

    private boolean moveToPositionAppliedFlag_ = false;
    private Position targetMoveToPosition_ = Position.LIFT_BOTTOM_POSITION;
    private double lastSetPower_ = 0;
    private int encoderCntForTargetPosition_ = 0;
    private double startTimeToTargetPosition_ = 0;
    static final double MAX_MOVE_DOWN_POWER = 0.6;          // Need to give a threshold for moving down power to avoid dropping too fast
    static final double SCALE_LIFT_POWER_TIME_INTERVAL = 0.1;
    static final double [] SCALE_LIFT_UP_POWER_BY_TIME = {0.2, 0.3, 0.35, 0.5, 0.7, 0.85, 1.0};    // Spend 0.7 sec to full power
    static final double [] SCALE_LIFT_DOWN_POWER_BY_TIME = {0.2, 0.35, 0.6, 0.9, 1.0}; // Spend 0.5 sec to full power

    private Telemetry telemetry_;

    //Constructor
    public Lift(DcMotor motor_left,
                DcMotor motor_right,
                Telemetry telemetry) {
        motorLeft_ = motor_left;
        motorRight_ = motor_right;
        telemetry_ = telemetry;

        motorLeft_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRight_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        resetEncoder(0);
    }

    void enableShowLiftInfo() { showLiftInfo_ = true; }
    void disableShowLiftInfo() { showLiftInfo_ = false; }

    void useEncoder(){
        motorLeft_.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRight_.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void resetEncoder(double time){
        motorLeft_.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight_.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        prevReadEncCntMotorLeft_ = 0;
        prevReadEncCntMotorRight_ = 0;
        prevEncCntChangeStartTime_ = time;

        useEncoder();
    }

    int encoderCountForTargetPosition() { return encoderCntForTargetPosition_;}

    public void setCurrentPositionAsTargetPosition(double time) {
        moveToPositionAppliedFlag_ = false;

        int curr_encoder_cnt = motorRight_.getCurrentPosition();
        if (curr_encoder_cnt > Position.LIFT_TOP_POSITION.getValue()) {
            curr_encoder_cnt = Position.LIFT_TOP_POSITION.getValue();
        } else if (curr_encoder_cnt < Position.LIFT_BOTTOM_POSITION.getValue()) {
            curr_encoder_cnt = Position.LIFT_BOTTOM_POSITION.getValue();
        }

        setEncoderCountForTargetPosition(curr_encoder_cnt, time);
    }

    public void holdAtTargetPosition(double time) {
        moveToTargetPosition(0.6, time);
    }

    boolean isMoveToPositionApplied(Position position) {
        return (moveToPositionAppliedFlag_ == true &&
                targetMoveToPosition_ == position);
    }

    // Time is used to allow us to implement the control by using the power in the s-curve
    // i.e., at beginning of moving to target position, we cannot use full power to immediately
    // and the power is increased with time till reach to the full power after certain time.
    public boolean moveToPosition(Position position,
                                  double time) {
        moveToPositionAppliedFlag_ = true;
        targetMoveToPosition_ = position;

        final int target_encoder_cnt = position.getValue();
        setEncoderCountForTargetPosition(target_encoder_cnt, time);

        moveToTargetPosition(1.0, time);

        return reachToTargetEncoderCount();
    }

    // Move the lift up. This method is used by teleop only.
    public void moveUp(double power_val,
                       double time) {
        moveToPositionAppliedFlag_ = false;

        final int enc_cnt_at_top_position = Position.LIFT_TOP_POSITION.getValue();
        setEncoderCountForTargetPosition(enc_cnt_at_top_position, time);

        moveToTargetPosition(enc_cnt_at_top_position, time);
    }

    // Move the lift down. This method is used by teleop only.
    public void moveDown(double power_val,
                         double time) {
        moveToPositionAppliedFlag_ = false;

        final int enc_cnt_at_bottom_position = Position.LIFT_BOTTOM_POSITION.getValue();
        setEncoderCountForTargetPosition(enc_cnt_at_bottom_position, time);

        moveToTargetPosition(enc_cnt_at_bottom_position, time);
    }

    private void moveToTargetPosition(double power_val,
                                      double time) {
        if (time < startTimeToTargetPosition_) time = startTimeToTargetPosition_;

        power_val = Math.abs(power_val);
        power_val = Range.clip(power_val, 0, 1);

        int curr_enc_pos_motor_right = motorRight_.getCurrentPosition();

        // telemetry_.addData("Time", "time="+String.valueOf(time)+
        //                    " start="+String.valueOf(startTimeToTargetPosition_)+
        //                    " diff="+String.valueOf(time-startTimeToTargetPosition_));

        double set_power = 0;
        if (curr_enc_pos_motor_right < encoderCntForTargetPosition_) {
            final double used_time = time - startTimeToTargetPosition_;
            set_power = scaleLiftUpPowerByTime(1.0, used_time);
            if (set_power > power_val) set_power = power_val;

            final int enc_diff = encoderCntForTargetPosition_ - curr_enc_pos_motor_right;
            if (encoderCntForTargetPosition_ >= ENCODER_CNT_FOR_USE_FULL_LIFT_POWER) {
                if (enc_diff < 75) {
                    if (set_power > 0.3) set_power = 0.3;
                }
            } else if (enc_diff < 10) {
                if (set_power > 0.05) set_power = 0.05;
            } else if (enc_diff < 50) {
                if (set_power > 0.2) set_power = 0.2;
            } else if (enc_diff < 150) {
                if (set_power > 0.45) set_power = 0.45;
            } else if (enc_diff < 250) {
                if (set_power > 0.7) set_power = 0.7;
            }
        } else if (curr_enc_pos_motor_right > encoderCntForTargetPosition_) {
            final double used_time = time - startTimeToTargetPosition_;
            set_power = scaleLiftDownPowerByTime(1.0, used_time);
            if (set_power > power_val) set_power = power_val;
            if (set_power > MAX_MOVE_DOWN_POWER) set_power = MAX_MOVE_DOWN_POWER;

            final int enc_diff = curr_enc_pos_motor_right - encoderCntForTargetPosition_;
            if (enc_diff < 10) {
                // if (encoderCntForTargetPosition_ > Position.LIFT_DELIVER_STONE.getValue()) set_power = 0.1;
                // else set_power = 0;
                set_power = 0;
            } else if (enc_diff < 20) {
                set_power = 0.175;
            } else if (enc_diff < 50) {
                if (set_power > 0.25) set_power = 0.25;
            } else if (enc_diff < 150) {
                if (set_power > 0.45) set_power = 0.45;
            } else if (enc_diff < 250) {
                if (set_power > 0.7) set_power = 0.7;
            }

            set_power = -set_power;
        }

        setPower(set_power);

        if (showLiftInfo_ == true) showEncoderValue(true);
    }

    private double scaleLiftUpPowerByTime(double power_val,
                                          double time_used) {
        final int last_scale_id = SCALE_LIFT_UP_POWER_BY_TIME.length - 1;
        if (time_used <= 0) time_used = 0;
        else if (time_used >= SCALE_LIFT_UP_POWER_BY_TIME[last_scale_id]) return power_val;

        int index = (int)(time_used / SCALE_LIFT_POWER_TIME_INTERVAL);
        if (index > last_scale_id) index = last_scale_id;
        return (power_val * SCALE_LIFT_UP_POWER_BY_TIME[index]);
    }

    private double scaleLiftDownPowerByTime(double power_val,
                                            double time_used) {
        final int last_scale_id = SCALE_LIFT_DOWN_POWER_BY_TIME.length - 1;
        if (time_used <= 0) time_used = 0;
        else if (time_used >= SCALE_LIFT_UP_POWER_BY_TIME[last_scale_id]) return power_val;

        int index = (int)(time_used / SCALE_LIFT_POWER_TIME_INTERVAL);
        if (index > last_scale_id) index = last_scale_id;
        return (power_val * SCALE_LIFT_DOWN_POWER_BY_TIME[index]);
    }

    void setEncoderCountForTargetPosition(int enc_cnt_at_target_pos,
                                          double time) {
        if (encoderCntForTargetPosition_ == enc_cnt_at_target_pos) return;

        startTimeToTargetPosition_ = time;
        encoderCntForTargetPosition_ = enc_cnt_at_target_pos;
    }

    void setPower(double power) {
        if (lastSetPower_ == power) return;

        motorLeft_.setPower(-power);
        motorRight_.setPower(power);

        lastSetPower_ = power;
    }

    boolean reachToTargetEncoderCount() {
        final int curr_enc_pos_motor_left = motorRight_.getCurrentPosition();
        int threshold = ENCODER_THRESHOLD_FOR_TARGET_POSITION;
        /*
        if (encoderCntForTargetPosition_<= Position.LIFT_DELIVER_STONE.getValue() ||
            encoderCntForTargetPosition_ < 10) {
            if (encoderCntForTargetPosition_ < 10) {
                if (threshold > 3) threshold = 3;
            } else {
                if (threshold > 7) threshold = 7;
            }
        } */

        if (encoderCntForTargetPosition_ > threshold) {
            return (curr_enc_pos_motor_left >= (encoderCntForTargetPosition_ - threshold) &&
                    curr_enc_pos_motor_left < (encoderCntForTargetPosition_ + threshold));
        }

        return (curr_enc_pos_motor_left >= 0 &&
                curr_enc_pos_motor_left < (encoderCntForTargetPosition_ + threshold));
    }

    boolean isEncoderStuck(double time) {
        int curr_enc_pos_motor_left = motorLeft_.getCurrentPosition();
        int curr_enc_pos_motor_right = motorRight_.getCurrentPosition();

        if (curr_enc_pos_motor_left != prevReadEncCntMotorLeft_ &&
                curr_enc_pos_motor_right != prevReadEncCntMotorRight_){
            prevReadEncCntMotorLeft_ = curr_enc_pos_motor_left;
            prevReadEncCntMotorRight_ = curr_enc_pos_motor_right;
            prevEncCntChangeStartTime_ = time;

            return false;
        }

        return (time >= (prevEncCntChangeStartTime_ + AUTO_ENC_STUCK_TIME_OUT));
    }

    public void showEncoderValue(boolean update_flag){
        telemetry_.addData("Power", String.valueOf(lastSetPower_));
        telemetry_.addData("Current lift encoder value",
                "ApplyToTarget="+String.valueOf(moveToPositionAppliedFlag_)+
                " Target="+String.valueOf(encoderCntForTargetPosition_)+
                      ", Left="+String.valueOf(motorLeft_.getCurrentPosition())+
                      ", Right="+String.valueOf(motorRight_.getCurrentPosition()));

        if (update_flag == true) telemetry_.update();
    }
}
