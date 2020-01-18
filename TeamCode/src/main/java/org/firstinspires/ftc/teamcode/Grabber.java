package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Grabber {
    // Lift position specified by encoder count
    public enum CranePosition {
        CRANE_DRAW_BACK_POSITION(50),
        CRANE_GRAB_STONE(200),
        CRANE_MAX_STRETCH_OUT_POSITION(2000);

        private final int val_;

        private CranePosition(int val) { val_ = val; }
        public int getValue() { return val_; }
    };

    public enum RotationPosition {
        ROTATION_IN(0),
        ROTATION_HALF_OUT(45),
        ROTATION_OUT(90);

        private final int val_;

        private RotationPosition(int val) { val_ = val; }
        public int getValue() { return val_; }
    };
    public enum ClampPosition {
        CLAMP_OPEN(0),
        CLAMP_CLOSE(90);

        private final int val_;

        private ClampPosition(int val) { val_ = val; }
        public int getValue() { return val_; }
    };

    static final int CRANE_ENCODER_THRESHOLD_FOR_TARGET_POSITION = 25;

    // Motors and servos
    private DcMotor craneMotor_ = null;
    private GoBildaDualServo rotationServo_ = null;
    private GoBildaDualServo clampServo_ = null;

    private int encoderCntForTargetPosition_ = 0;
    private double lastSetPower_ = 0;

    /// Encoder time out variables
    static final double AUTO_ENC_STUCK_TIME_OUT = 2.0;
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

        rotationServo_ = new GoBildaDualServo(rotation_servo_name,
                                              rotation_servo,
                               false,
                                              RotationPosition.ROTATION_IN.getValue(),
                                              telemetry);

        clampServo_ = new GoBildaDualServo(clamp_servo_name,
                                           clamp_servo,
                            false,
                                           ClampPosition.CLAMP_CLOSE.getValue(),
                                           telemetry);

        telemetry_ = telemetry;

        craneMotor_.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // craneMotor_.setDirection(DcMotor.Direction.REVERSE);

        resetEncoder(0);
    }

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

    public void setCurrentCranePositionAsTargetPosition() {
        int curr_encoder_cnt = craneMotor_.getCurrentPosition();
        if (curr_encoder_cnt > CranePosition.CRANE_MAX_STRETCH_OUT_POSITION.getValue()) {
            curr_encoder_cnt = CranePosition.CRANE_MAX_STRETCH_OUT_POSITION.getValue();
        } else if (curr_encoder_cnt < CranePosition.CRANE_DRAW_BACK_POSITION.getValue()) {
            curr_encoder_cnt = CranePosition.CRANE_DRAW_BACK_POSITION.getValue();
        }

        setEncoderCountForTargetPosition(curr_encoder_cnt);
    }

    public void holdCraneAtTargetPosition() {
        moveCraneToTargetPosition(0.4);
    }

    public boolean moveCraneToPosition(CranePosition position) {
        final int target_encoder_cnt = position.getValue();
        setEncoderCountForTargetPosition(target_encoder_cnt);

        moveCraneToTargetPosition(1.0);

        return craneReachToTargetEncoderCount();
    }

    // Stretch crane out. This method is used by teleop only.
    public void craneStretchOut(double power_val) {
        final int enc_cnt_at_max_stretch_out_position = CranePosition.CRANE_MAX_STRETCH_OUT_POSITION.getValue();
        setEncoderCountForTargetPosition(enc_cnt_at_max_stretch_out_position);

        moveCraneToTargetPosition(power_val);
    }

    // Move the lift down. This method is used by teleop only.
    public void craneDrawBack(double power_val) {
        final int enc_cnt_at_draw_back_position = CranePosition.CRANE_DRAW_BACK_POSITION.getValue();
        setEncoderCountForTargetPosition(enc_cnt_at_draw_back_position);

        moveCraneToTargetPosition(power_val);
    }

    private void moveCraneToTargetPosition(double power_val) {
        power_val = Math.abs(power_val);
        power_val = Range.clip(power_val, 0, 1);

        final int curr_enc_pos = craneMotor_.getCurrentPosition();
        final int enc_diff = Math.abs(encoderCntForTargetPosition_ - curr_enc_pos);
        double set_power = power_val;
        if (enc_diff < 15) {
            set_power = 0;
        } else if (enc_diff < 50) {
            if (set_power > 0.1) set_power = 0.1;
        } else if (enc_diff < 90) {
            if (set_power > 0.15) set_power = 0.15;
        } else if (enc_diff < 150) {
            if (set_power > 0.2) set_power = 0.2;
        } else if (enc_diff < 250) {
            if (set_power > 0.4) set_power = 0.4;
        } else if (enc_diff < 500) {
            if (set_power > 0.7) set_power = 0.7;
        }

        if (encoderCntForTargetPosition_ >= curr_enc_pos) setCranePower(set_power);
        else setCranePower(-set_power);
    }

    void setEncoderCountForTargetPosition(int enc_cnt_at_target_pos) {
        if (encoderCntForTargetPosition_ == enc_cnt_at_target_pos) return;

        encoderCntForTargetPosition_ = enc_cnt_at_target_pos;
    }

    void setCranePower(double power) {
        if (lastSetPower_ == power) return;

        craneMotor_.setPower(power);
        lastSetPower_ = power;
    }

    boolean craneReachToTargetEncoderCount() {
        final int curr_enc_pos = craneMotor_.getCurrentPosition();
        return (curr_enc_pos >= encoderCntForTargetPosition_ &&
                curr_enc_pos < (encoderCntForTargetPosition_ + CRANE_ENCODER_THRESHOLD_FOR_TARGET_POSITION));
    }

    boolean isEncoderStuck(double time) {
        int curr_enc_pos = craneMotor_.getCurrentPosition();

        if (curr_enc_pos != prevReadEncCnt_) {
            prevReadEncCnt_ = curr_enc_pos;
            prevEncCntChangeStartTime_ = time;

            return false;
        }

        return (time >= (prevEncCntChangeStartTime_ + AUTO_ENC_STUCK_TIME_OUT));
    }

    // Methods for clamp servo
    void clampOpen() {
        clampServo_.setServoModePositionInDegree(ClampPosition.CLAMP_OPEN.getValue(), false);
    }

    void clampClose() {
        clampServo_.setServoModePositionInDegree(ClampPosition.CLAMP_CLOSE.getValue(), false);
    }

    // Methods for rotation servo
    void rotationIn() {
        rotationServo_.setServoModePositionInDegree(RotationPosition.ROTATION_IN.getValue(), false);
    }

    void rotationOut() {
        rotationServo_.setServoModePositionInDegree(RotationPosition.ROTATION_OUT.getValue(), false);
    }

    void rotationHalfOut() {
        rotationServo_.setServoModePositionInDegree(RotationPosition.ROTATION_HALF_OUT.getValue(), false);
    }

    public void showValues(boolean update_flag){
        telemetry_.addData("Crane",  "Power"+String.valueOf(lastSetPower_)+
                        " Target="+String.valueOf(encoderCntForTargetPosition_)+
                        " Encoder="+String.valueOf(craneMotor_.getCurrentPosition()));
        clampServo_.showPosition(false);
        rotationServo_.showPosition(false);
        if (update_flag == true) telemetry_.update();
    }
}
