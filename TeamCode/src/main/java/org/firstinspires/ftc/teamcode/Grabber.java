package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Grabber {
    // Lift position specified by encoder count
    public enum CranePosition {
        CRANE_DRAW_BACK_POSITION(1),
        CRANE_GRAB_STONE(1170),      //was 1100
        CRANE_DROP_STONE(1400),
        CRANE_PROTECT_ROTATION_POSITION(1700),
        CRANE_MAX_STRETCH_OUT_POSITION(1750);

        private final int val_;

        private CranePosition(int val) { val_ = val; }
        public int getValue() { return val_; }
    };

    public enum RotationPosition {
        ROTATION_IN(15),
        ROTATION_HALF_OUT(75),
        ROTATION_OUT(135);

        private final int val_;

        private RotationPosition(int val) { val_ = val; }
        public int getValue() { return val_; }
    };

    public enum ClampPosition {
        CLAMP_OPEN(30),
        CLAMP_CLOSE(140);

        private final int val_;

        private ClampPosition(int val) { val_ = val; }
        public int getValue() { return val_; }
    };

    static final int CRANE_ENCODER_THRESHOLD_FOR_TARGET_POSITION = 25;
    static final double MAX_CRANE_MOTOR_POWER = 0.8;
    static final boolean PROTECT_CLAMP_WHEN_ROTATING_OUT = false;

    private boolean showGrabberInfo_ = false;

    // Motors and servos
    private DcMotor craneMotor_ = null;
    private GoBildaDualServo rotationServo_ = null;
    private GoBildaDualServo clampServo_ = null;

    private boolean craneWithMoveToPositionAppliedFlag_ = false;
    private CranePosition craneTargetMoveToPosition_ = CranePosition.CRANE_DRAW_BACK_POSITION;
    private int encoderCntForTargetPosition_ = 0;
    private double lastSetPower_ = 0;

    private RotationPosition lastRotationPosition_ = RotationPosition.ROTATION_IN;
    private ClampPosition lastClampPosition_ = ClampPosition.CLAMP_OPEN;

    /// Encoder time out variables
    static final double AUTO_ENC_STUCK_TIME_OUT = 1.0;
    static final int MIN_ENC_NON_STUCK_RANGE = 15;
    private int prevReadEncCnt_ = 0;
    private double prevEncCntChangeStartTime_ = 0;

    private Telemetry telemetry_;

    //Constructor
    public Grabber(DcMotor crane_motor,
                   String rotation_servo_name,
                   Servo rotation_servo,
                   String clamp_servo_name,
                   Servo clamp_servo,
                   Telemetry telemetry) {
        craneMotor_ = crane_motor;

        lastRotationPosition_ = RotationPosition.ROTATION_IN;
        rotationServo_ = new GoBildaDualServo(rotation_servo_name,
                                              rotation_servo,
                               false,
                                              lastRotationPosition_.getValue(),
                                              telemetry);

        lastClampPosition_ = ClampPosition.CLAMP_OPEN;
        clampServo_ = new GoBildaDualServo(clamp_servo_name,
                                           clamp_servo,
                            false,
                                           lastClampPosition_.getValue(),
                                           telemetry);

        telemetry_ = telemetry;

        craneMotor_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // craneMotor_.setDirection(DcMotor.Direction.REVERSE);

        resetEncoder(0);
    }

    void enableShowGrabberInfo() { showGrabberInfo_ = true; }
    void disableShowGrabberInfo() { showGrabberInfo_ = false; }

    void useEncoder(){
        craneMotor_.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    void resetEncoder(double time){
        craneMotor_.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // craneMotor_.setTargetPosition(0);

        prevReadEncCnt_ = 0;
        prevEncCntChangeStartTime_ = time;

        useEncoder();
    }

    // Methods for crane
    public void stopCrane() {
        setCranePower(0);
    }

    public int getCraneEncoderCount() { return craneMotor_.getCurrentPosition(); }

    public void setCurrentCranePositionAsTargetPosition() {
        int curr_encoder_cnt = Math.abs(craneMotor_.getCurrentPosition());
        if (curr_encoder_cnt > CranePosition.CRANE_MAX_STRETCH_OUT_POSITION.getValue()) {
            curr_encoder_cnt = CranePosition.CRANE_MAX_STRETCH_OUT_POSITION.getValue();
        } else if (curr_encoder_cnt < CranePosition.CRANE_DRAW_BACK_POSITION.getValue()) {
            curr_encoder_cnt = CranePosition.CRANE_DRAW_BACK_POSITION.getValue();
        }

        setEncoderCountForTargetPosition(curr_encoder_cnt);
    }

    public void holdCraneAtTargetPosition() {
        moveCraneToTargetPosition(MAX_CRANE_MOTOR_POWER);
    }

    void resetCraneWithMoveToPositionApplied() { craneWithMoveToPositionAppliedFlag_ = false; }

    boolean isCraneWithMoveToPositionApplied(CranePosition position) {
        return (craneWithMoveToPositionAppliedFlag_ == true &&
                craneTargetMoveToPosition_ == position);
    }

    public boolean moveCraneToPosition(CranePosition position) {
        craneWithMoveToPositionAppliedFlag_ = true;
        craneTargetMoveToPosition_ = position;

        final int target_encoder_cnt = position.getValue();
        setEncoderCountForTargetPosition(target_encoder_cnt);

        moveCraneToTargetPosition(MAX_CRANE_MOTOR_POWER);

        return craneReachToTargetEncoderCount();
    }

    // Stretch crane out. This method is used by teleop only.
    public void craneStretchOut(double power_val) {
        craneWithMoveToPositionAppliedFlag_ = false;

        final int enc_cnt_at_max_stretch_out_position = CranePosition.CRANE_MAX_STRETCH_OUT_POSITION.getValue();
        setEncoderCountForTargetPosition(enc_cnt_at_max_stretch_out_position);

        moveCraneToTargetPosition(power_val);
    }

    // Move the lift down. This method is used by teleop only.
    public void craneDrawBack(double power_val) {
        craneWithMoveToPositionAppliedFlag_ = false;

        final int enc_cnt_at_draw_back_position = CranePosition.CRANE_DRAW_BACK_POSITION.getValue();
        setEncoderCountForTargetPosition(enc_cnt_at_draw_back_position);

        moveCraneToTargetPosition(power_val);
    }

    // Return true if the crane is fully drawn back.
    // Otherwise, return false.
    public boolean enforceCraneDrawBackToEnd(
            double power_val,
            ElapsedTime timer,
            double max_applied_time) {
        craneWithMoveToPositionAppliedFlag_ = false;

        if (max_applied_time < 0.1) max_applied_time = 0.1;

        power_val = Math.abs(power_val);
        if (power_val < 0.1) power_val = 0.1;

        final double beg_time = timer.time();
        prevReadEncCnt_ = craneMotor_.getCurrentPosition();
        prevEncCntChangeStartTime_ = beg_time;

        boolean succ_flag = false;
        double curr_time = timer.time();
        do {
            // Positive power makes crane draw back
            setCranePower(power_val);

            int curr_enc_pos = craneMotor_.getCurrentPosition();

            if (isEncoderStuck(curr_time) == true) {
                succ_flag = true; break;
            };

            curr_time = timer.time();
        } while ((curr_time - beg_time) <= max_applied_time);

        resetEncoder(curr_time);

        // Wait for 0.1 sec for resetting encoder
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return succ_flag;
    }

    private void moveCraneToTargetPosition(double power_val) {
        power_val = Math.abs(power_val);
        power_val = Range.clip(power_val, 0, 1);

        final int curr_enc_pos = Math.abs(craneMotor_.getCurrentPosition());
        if (PROTECT_CLAMP_WHEN_ROTATING_OUT == true &&
                lastRotationPosition_ != RotationPosition.ROTATION_IN) {
            // Protect the clamp when it is rotated out
            if (curr_enc_pos < CranePosition.CRANE_PROTECT_ROTATION_POSITION.getValue()) {
                setCranePower(0);
                return;
            };
        }

        final int enc_diff = Math.abs(encoderCntForTargetPosition_ - curr_enc_pos);
        double set_power = power_val;
        if (enc_diff < 10) {
            set_power = 0;
        } else if (enc_diff < 50) {
            if (set_power > 0.25) set_power = 0.25;
        } else if (enc_diff < 150) {
            if (set_power > 0.4) set_power = 0.4;
        } else if (enc_diff < 250) {
            if (set_power > 0.5) set_power = 0.5;
        }

        // Postive power draws back the crane.
        // Negative power stretches the crane.
        if (encoderCntForTargetPosition_ >= curr_enc_pos) setCranePower(-set_power);
        else setCranePower(set_power);

        if (showGrabberInfo_ == true) showValues(true);
    }

    void setEncoderCountForTargetPosition(int enc_cnt_at_target_pos) {
        if (encoderCntForTargetPosition_ == Math.abs(enc_cnt_at_target_pos)) return;

        encoderCntForTargetPosition_ = Math.abs(enc_cnt_at_target_pos);
    }

    void setCranePower(double power) {
        if (lastSetPower_ == power) return;

        craneMotor_.setPower(power);
        lastSetPower_ = power;
    }

    boolean craneReachToTargetEncoderCount() {
        final int curr_enc_pos = Math.abs(craneMotor_.getCurrentPosition());
        if (encoderCntForTargetPosition_ > CRANE_ENCODER_THRESHOLD_FOR_TARGET_POSITION) {
            return (curr_enc_pos >= (encoderCntForTargetPosition_ - CRANE_ENCODER_THRESHOLD_FOR_TARGET_POSITION) &&
                    curr_enc_pos < (encoderCntForTargetPosition_ + CRANE_ENCODER_THRESHOLD_FOR_TARGET_POSITION));
        }

        return (curr_enc_pos >= 0 &&
                curr_enc_pos < (encoderCntForTargetPosition_ + CRANE_ENCODER_THRESHOLD_FOR_TARGET_POSITION));
    }

    boolean isEncoderStuck(double time) {
        int curr_enc_pos = craneMotor_.getCurrentPosition();

        if (Math.abs(curr_enc_pos - prevReadEncCnt_) >= MIN_ENC_NON_STUCK_RANGE) {
            prevReadEncCnt_ = curr_enc_pos;
            prevEncCntChangeStartTime_ = time;

            return false;
        }

        return (time >= (prevEncCntChangeStartTime_ + AUTO_ENC_STUCK_TIME_OUT));
    }

    // Methods for clamp servo
    ClampPosition clampPosition() {
        return lastClampPosition_;
    }

    void clampOpen() {
        clampServo_.setServoModePositionInDegree(ClampPosition.CLAMP_OPEN.getValue(), false);
        lastClampPosition_ = ClampPosition.CLAMP_OPEN;
    }

    void clampClose() {
        clampServo_.setServoModePositionInDegree(ClampPosition.CLAMP_CLOSE.getValue(), false);
        lastClampPosition_ = ClampPosition.CLAMP_CLOSE;
    }

    // Methods for rotation servo
    RotationPosition rotationPosition() {
        return lastRotationPosition_;
    }

    boolean rotationIn() {
        if (PROTECT_CLAMP_WHEN_ROTATING_OUT == true &&
                lastRotationPosition_ != RotationPosition.ROTATION_IN) {
            final int curr_enc_pos = Math.abs(craneMotor_.getCurrentPosition());
            if (curr_enc_pos < CranePosition.CRANE_PROTECT_ROTATION_POSITION.getValue()) {
                return false;
            }
        }

        lastRotationPosition_ = RotationPosition.ROTATION_IN;
        rotationServo_.setServoModePositionInDegree(RotationPosition.ROTATION_IN.getValue(), false);
        return true;
    }

    boolean rotationOut() {
        if (PROTECT_CLAMP_WHEN_ROTATING_OUT == true &&
                lastRotationPosition_ != RotationPosition.ROTATION_OUT) {
            final int curr_enc_pos = Math.abs(craneMotor_.getCurrentPosition());
            if (curr_enc_pos < CranePosition.CRANE_PROTECT_ROTATION_POSITION.getValue()) {
                return false;
            }
        }

        lastRotationPosition_ = RotationPosition.ROTATION_OUT;
        rotationServo_.setServoModePositionInDegree(RotationPosition.ROTATION_OUT.getValue(), false);
        return true;
    }

    boolean rotationHalfOut() {
        if (PROTECT_CLAMP_WHEN_ROTATING_OUT == true &&
                lastRotationPosition_ != RotationPosition.ROTATION_HALF_OUT) {
            final int curr_enc_pos = Math.abs(craneMotor_.getCurrentPosition());
            if (curr_enc_pos < CranePosition.CRANE_PROTECT_ROTATION_POSITION.getValue()) {
                return false;
            }
        }

        lastRotationPosition_ = RotationPosition.ROTATION_HALF_OUT;
        rotationServo_.setServoModePositionInDegree(RotationPosition.ROTATION_HALF_OUT.getValue(), false);
        return true;
    }

    public void showValues(boolean update_flag){
        telemetry_.addData("Crane",  "Power"+String.valueOf(lastSetPower_)+
                " ApplyToTarget="+String.valueOf(craneWithMoveToPositionAppliedFlag_)+
                " Reached="+String.valueOf(craneReachToTargetEncoderCount())+
                        " Target="+String.valueOf(encoderCntForTargetPosition_)+
                        " Encoder="+String.valueOf(craneMotor_.getCurrentPosition()));
        clampServo_.showPosition(false);
        rotationServo_.showPosition(false);
        if (update_flag == true) telemetry_.update();
    }
}
