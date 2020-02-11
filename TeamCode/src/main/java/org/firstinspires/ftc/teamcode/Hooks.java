package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Hooks {
    enum Position {
        INIT(0),
        PULL(1),
        RELEASE(2);

        private final int position_;

        private Position(int position) { position_ = position; }
        public int getValue() { return position_; }
    }

    // Left and right hooks are mounted by facing opposite direction
    private enum LeftPosition {
        INIT_POSITION(230),
        PULL_POSITION(80),
        RELEASE_POSITION(230);

        private final int degree_;

        private LeftPosition(int degree) { degree_ = degree; }
        public int getValue() { return degree_; }
    };

    private enum RightPosition {
        INIT_POSITION(80),
        PULL_POSITION(230),
        RELEASE_POSITION(80);

        private final int degree_;

        private RightPosition(int degree) { degree_ = degree; }
        public int getValue() { return degree_; }
    };

    private GoBildaDualServo leftHookServo_ = null;
    private GoBildaDualServo rightHookServo_ = null;

    private Telemetry telemetry_;

    Hooks(Servo left_servo,
          String left_servo_name,
          Servo right_servo,
          String right_servo_name,
          Position init_position,
          Telemetry telemetry) {
        LeftPosition init_left_hook_position = LeftPosition.INIT_POSITION;
        RightPosition init_right_hook_position = RightPosition.INIT_POSITION;
        switch (init_position) {
            case INIT:
                break;
            case PULL:
                init_left_hook_position = LeftPosition.PULL_POSITION;
                init_right_hook_position = RightPosition.PULL_POSITION;
                break;
            case RELEASE:
                init_left_hook_position = LeftPosition.RELEASE_POSITION;
                init_right_hook_position = RightPosition.RELEASE_POSITION;
                break;
            default:
                break;
        }

        leftHookServo_ = new GoBildaDualServo(left_servo_name,
                left_servo,
                false,
                init_left_hook_position.getValue(),
                telemetry);

        rightHookServo_ = new GoBildaDualServo(right_servo_name,
                right_servo,
                false,
                init_right_hook_position.getValue(),
                telemetry);

        telemetry_ = telemetry;
    }

    Position convertToPosition(int position) {
        if (position == Position.INIT.getValue()) return Position.INIT;
        else if (position == Position.PULL.getValue()) return Position.PULL;
        else if (position == Position.RELEASE.getValue()) return Position.RELEASE;

        return Position.RELEASE;
    }

    void moveHooksToPosition(Position position) {
        switch (position) {
            case INIT:
                leftHookServo_.setServoModePositionInDegree(LeftPosition.INIT_POSITION.getValue(), false);
                rightHookServo_.setServoModePositionInDegree(RightPosition.INIT_POSITION.getValue(), false);
                break;
            case PULL:
                leftHookServo_.setServoModePositionInDegree(LeftPosition.PULL_POSITION.getValue(), false);
                rightHookServo_.setServoModePositionInDegree(RightPosition.PULL_POSITION.getValue(), false);
                break;
            case RELEASE:
                leftHookServo_.setServoModePositionInDegree(LeftPosition.RELEASE_POSITION.getValue(), false);
                rightHookServo_.setServoModePositionInDegree(RightPosition.RELEASE_POSITION.getValue(), false);
                break;
            default:
                break;
        }
    }

    void moveHooksInDegree(double left_hook_degree,
                           double right_hook_degree) {
        leftHookServo_.setServoModePositionInDegree(left_hook_degree, false);
        rightHookServo_.setServoModePositionInDegree(right_hook_degree, false);
    }

    void showPosition(boolean update_flag) {
        leftHookServo_.showPosition(false);
        rightHookServo_.showPosition(false);

        if (update_flag == true) telemetry_.update();
    }
}
