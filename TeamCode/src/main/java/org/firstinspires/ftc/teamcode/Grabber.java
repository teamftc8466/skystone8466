package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Grabber {
    // Lift position specified by encoder count
    public enum CranePosition {
        CRANE_INIT_POSITION(0),
        CRANE_GRAB_STONE(200),
        CRANE_MAX_STRETCH_OUT_POSITION(1000),
        CRANE_DRAW_BACK_POSITION(50);

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

    //Motors
    private DcMotor craneMotor_ = null;
    private GoBildaDualServo rotationServo_ = null;
    private GoBildaDualServo clampServo_ = null;

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

        // craneMotor_.setDirection(DcMotor.Direction.REVERSE);

        resetEncoder(0);
    }

    void useEncoder(){
        craneMotor_.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    void resetEncoder(double time){
        craneMotor_.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        prevReadEncCnt_ = 0;
        prevEncCntChangeStartTime_ = time;

        useEncoder();
    }

    // Methods for crane
    public void stopCrane() {
        setCranePower(0);
    }

    public boolean moveCraneToPosition(CranePosition position) {
        int target_encoder_cnt = position.getValue();
        if (target_encoder_cnt < 0) target_encoder_cnt = 0;
        else if (target_encoder_cnt >= CranePosition.CRANE_MAX_STRETCH_OUT_POSITION.getValue()) {
            target_encoder_cnt = CranePosition.CRANE_MAX_STRETCH_OUT_POSITION.getValue();
        }

        int curr_enc_pos = craneMotor_.getCurrentPosition();
        if (curr_enc_pos != target_encoder_cnt) {
            craneMotor_.setTargetPosition(target_encoder_cnt);
            setCranePower(1.0);
            return false;
        }

        setCranePower(0);
        return true;
    }

    // Stretch crane out. This method is used by teleop only.
    public void craneStretchOut(double power_val) {
        final int enc_cnt_at_max_stretch_out_position = CranePosition.CRANE_MAX_STRETCH_OUT_POSITION.getValue();

        if (power_val > 1.0) power_val = 1.0;

        int curr_enc_pos = craneMotor_.getCurrentPosition();
        if (power_val <= 0 ||
                curr_enc_pos >= enc_cnt_at_max_stretch_out_position) {
            setCranePower(0);
        } else {
            craneMotor_.setTargetPosition(enc_cnt_at_max_stretch_out_position);
            setCranePower(power_val);
        }
    }

    // Move the lift down. This method is used by teleop only.
    public void craneDrawBack(double power_val) {
        final int enc_cnt_at_draw_back_position = CranePosition.CRANE_DRAW_BACK_POSITION.getValue();

        if (power_val > 1.0) power_val = 1.0;

        int curr_enc_pos = craneMotor_.getCurrentPosition();
        if (power_val <= 0 ||
                curr_enc_pos <= enc_cnt_at_draw_back_position) {
            setCranePower(0);
        } else {
            craneMotor_.setTargetPosition(enc_cnt_at_draw_back_position);
            setCranePower(power_val);
        }
    }

    void setCranePower(double power) {
        craneMotor_.setPower(power);
    }

    boolean allEncodersAreReset() {
        return (craneMotor_.getCurrentPosition() == 0);
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
        telemetry_.addData("Crane encoder value", String.valueOf(craneMotor_.getCurrentPosition()));
        clampServo_.showPosition(false);
        rotationServo_.showPosition(false);
        if (update_flag == true) telemetry_.update();
    }
}
