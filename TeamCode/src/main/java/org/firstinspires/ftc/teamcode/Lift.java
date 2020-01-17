package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lift {
    // Lift position specified by encoder count
    public enum Position {
        LIFT_INIT_POSITION(0),
        LIFT_GRAB_STONE_READY(200),
        LIFT_GRAB_STONE_CATCH(10),
        LIFT_DELIVER_STONE(50),
        LIFT_TOP_POSITION(1000),
        LIFT_BOTTOM_POSITION(10);

        private final int val_;

        private Position(int val) { val_ = val; }
        public int getValue() { return val_; }
    };

    //Motors
    private DcMotor motorLeft_ = null;
    private DcMotor motorRight_ = null;
    private Servo grabberServo = null;
    private Servo rotatorServo = null;

    static final int ENC_THRESHOLD_FOR_HOLD = 50;

    /// Encoder time out variables
    static final double AUTO_ENC_STUCK_TIME_OUT = 2.0;
    private int prevReadEncCntMotorLeft_ = 0;
    private int prevReadEncCntMotorRight_ = 0;
    private double prevEncCntChangeStartTime_ = 0;

    private int encoderCntForTargetPosition_ = -1;
    private double startTimeToTargetPosition_ = 0;
    static final double SCALE_LIFT_POWER_TIME_INTERVAL = 0.1;
    static final double [] SCALE_LIFT_UP_POWER_BY_TIME = {0.05, 0.1, 0.15, 0.2, 0.25,
                                                          0.3, 0.35, 0.4, 0.45,  0.5,
                                                          0.6,  0.7, 0.8,  0.9,  1.0};    // Spend 1 sec to 50% of power and 1.5 sec to full power
    static final double [] SCALE_LIFT_DOWN_POWER_BY_TIME = {0.1, 0.2, 0.35, 0.5, 0.65,
                                                            0.8, 1.0,  1.0, 1.0,   1.0};   // Spend 0.7 sec to full power

    private Telemetry telemetry_;

    //Constructor
    public Lift(DcMotor motor_left,
                DcMotor motor_right,
                Servo rotator,
                Servo grabber,
                Telemetry telemetry) {
        motorLeft_ = motor_left;
        motorRight_ = motor_right;
        telemetry_ = telemetry;
        rotatorServo = rotator;
        grabberServo = grabber;

        motorLeft_.setDirection(DcMotor.Direction.REVERSE);
        // motorRight_.setDirection(DcMotor.Direction.REVERSE);

        resetEncoder(0);
    }

    void useEncoder(){
        motorLeft_.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorRight_.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    void resetEncoder(double time){
        motorLeft_.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight_.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        prevReadEncCntMotorLeft_ = 0;
        prevReadEncCntMotorRight_ = 0;
        prevEncCntChangeStartTime_ = time;

        useEncoder();
    }

    // public void setStartTimeToMovelift(double time) { startTimeToTargetPosition_ = time; }
    public void setCurrentEncoderCountAsTargetPosition() { encoderCntForTargetPosition_ = motorLeft_.getCurrentPosition(); }

    public boolean moveToPosition(Position position,
                                  double time) {
        if (time < 0) time = 0;

        final int target_encoder_cnt = position.getValue();
        if (target_encoder_cnt != encoderCntForTargetPosition_) {
            startTimeToTargetPosition_ = time;
            encoderCntForTargetPosition_ = target_encoder_cnt;
        }

        int drive_to_encoder_cnt = target_encoder_cnt + ENC_THRESHOLD_FOR_HOLD;
        int curr_enc_pos_motor_left = motorLeft_.getCurrentPosition();
        int curr_enc_pos_motor_right = motorRight_.getCurrentPosition();
        if (curr_enc_pos_motor_left < target_encoder_cnt) {
            if (curr_enc_pos_motor_left <= drive_to_encoder_cnt || curr_enc_pos_motor_right <= drive_to_encoder_cnt) {
                motorLeft_.setTargetPosition(drive_to_encoder_cnt);
                motorRight_.setTargetPosition(drive_to_encoder_cnt);

                double used_time = time - startTimeToTargetPosition_;
                setPower(scaleLiftUpPowerByTime(1.0, used_time));
                return false;
            }
        } else if (curr_enc_pos_motor_left > target_encoder_cnt) {
            motorLeft_.setTargetPosition(target_encoder_cnt);
            motorRight_.setTargetPosition(target_encoder_cnt);

            if (curr_enc_pos_motor_left > drive_to_encoder_cnt) {
                double used_time = time - startTimeToTargetPosition_;
                setPower(scaleLiftDownPowerByTime(1.0, used_time));
            } else {
                setPower(0.1);
            }

            return false;
        }

        setPower(0);
        startTimeToTargetPosition_ = 0;
        return true;
    }

    // Move the lift up. This method is used by teleop only.
    public void moveUp(double power_val,
                       double time) {
        final int enc_cnt_at_top_position = Position.LIFT_TOP_POSITION.getValue();
        if (encoderCntForTargetPosition_ != enc_cnt_at_top_position) {
            startTimeToTargetPosition_ = time;
            encoderCntForTargetPosition_ = enc_cnt_at_top_position;
        }

        if (power_val > 1.0) power_val = 1.0;

        int curr_enc_pos_motor_left = motorLeft_.getCurrentPosition();
        int curr_enc_pos_motor_right = motorRight_.getCurrentPosition();

        if (power_val <= 0 ||
                curr_enc_pos_motor_left >= enc_cnt_at_top_position ||
                curr_enc_pos_motor_right >= enc_cnt_at_top_position) {
            startTimeToTargetPosition_ = 0;
            hold();
        } else {
            motorLeft_.setTargetPosition(enc_cnt_at_top_position);
            motorRight_.setTargetPosition(enc_cnt_at_top_position);

            double used_time = time - startTimeToTargetPosition_;
            double set_power = scaleLiftDownPowerByTime(1.0, used_time);
            if (set_power > power_val) set_power = power_val;
            setPower(set_power);
        }
    }

    public void setRotatorServo(double position) {
        if (position >= 0.05 && position <= 0.39) { //Servo position range
            rotatorServo.setPosition(position);
        }
    }

    // Move the lift down. This method is used by teleop only.
    public void moveDown(double power_val,
                         double time) {
        final int enc_cnt_at_bottom_position = Position.LIFT_BOTTOM_POSITION.getValue();
        if (encoderCntForTargetPosition_ != enc_cnt_at_bottom_position) {
            startTimeToTargetPosition_ = time;
            encoderCntForTargetPosition_ = enc_cnt_at_bottom_position;
        }

        if (power_val > 1.0) power_val = 1.0;

        int curr_enc_pos_motor_left = motorLeft_.getCurrentPosition();
        int curr_enc_pos_motor_right = motorRight_.getCurrentPosition();
        if (power_val <= 0 ||
                curr_enc_pos_motor_left <= enc_cnt_at_bottom_position ||
                curr_enc_pos_motor_right <= enc_cnt_at_bottom_position) {
            startTimeToTargetPosition_ = 0;
            hold();
        } else {
            motorLeft_.setTargetPosition(enc_cnt_at_bottom_position);
            motorRight_.setTargetPosition(enc_cnt_at_bottom_position);

            double used_time = time - startTimeToTargetPosition_;
            double set_power = scaleLiftDownPowerByTime(1.0, used_time);
            if (set_power > power_val) set_power = power_val;
            setPower(set_power);
        }
    }

    void hold() {
        if (encoderCntForTargetPosition_ <= Position.LIFT_BOTTOM_POSITION.getValue()) {
            setPower(0);
            return;
        }

        int drive_to_encoder_cnt = encoderCntForTargetPosition_ + ENC_THRESHOLD_FOR_HOLD;
        if (drive_to_encoder_cnt > Position.LIFT_TOP_POSITION.getValue()) {
            drive_to_encoder_cnt = Position.LIFT_TOP_POSITION.getValue();
        }

        motorLeft_.setTargetPosition(drive_to_encoder_cnt);
        motorRight_.setTargetPosition(drive_to_encoder_cnt);

        final int curr_enc_pos_motor_left = motorLeft_.getCurrentPosition();
        final int curr_enc_pos_motor_right = motorRight_.getCurrentPosition();
        if (curr_enc_pos_motor_left < drive_to_encoder_cnt ||
            curr_enc_pos_motor_right < drive_to_encoder_cnt) {
            setPower(0.1);
        } else {
            setPower(0);
        }
    }

    private double scaleLiftUpPowerByTime(double power_val,
                                          double time_used) {
        final int last_scale_id = SCALE_LIFT_UP_POWER_BY_TIME.length - 1;
        if (time_used <= 0) time_used = 0;
        else if (time_used >= SCALE_LIFT_UP_POWER_BY_TIME[last_scale_id]) return power_val;

        int index = (int)(time_used / SCALE_LIFT_POWER_TIME_INTERVAL);
        return (power_val * SCALE_LIFT_UP_POWER_BY_TIME[index]);
    }

    private double scaleLiftDownPowerByTime(double power_val,
                                            double time_used) {
        final int last_scale_id = SCALE_LIFT_DOWN_POWER_BY_TIME.length - 1;
        if (time_used <= 0) time_used = 0;
        else if (time_used >= SCALE_LIFT_UP_POWER_BY_TIME[last_scale_id]) return power_val;

        int index = (int)(time_used / SCALE_LIFT_POWER_TIME_INTERVAL);
        return (power_val * SCALE_LIFT_DOWN_POWER_BY_TIME[index]);
    }

    void setPower(double power) {
        motorLeft_.setPower(power);
        motorRight_.setPower(power);
    }

    boolean allEncodersAreReset() {
        return (motorLeft_.getCurrentPosition() == 0 &&
                motorRight_.getCurrentPosition() == 0);
    }

    boolean reachToTargetEncoderCount(int target_encoder_cnt) {
        int curr_enc_pos_motor_left = motorLeft_.getCurrentPosition();
        int curr_enc_pos_motor_right = motorRight_.getCurrentPosition();

        return ((curr_enc_pos_motor_left >= target_encoder_cnt &&
                 curr_enc_pos_motor_left <= (target_encoder_cnt + ENC_THRESHOLD_FOR_HOLD)) ||
                (curr_enc_pos_motor_right >= target_encoder_cnt &&
                 curr_enc_pos_motor_right <= (target_encoder_cnt + ENC_THRESHOLD_FOR_HOLD)));
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
        telemetry_.addData("Current lift encoder value",
                "Target="+String.valueOf(encoderCntForTargetPosition_)+
                      "Left="+String.valueOf(motorLeft_.getCurrentPosition())+
                      ", Right="+String.valueOf(motorRight_.getCurrentPosition()));

        if (update_flag == true) telemetry_.update();
    }
}
