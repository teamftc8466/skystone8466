package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TeleOpCommon", group="FS")
// @Disabled
public class TeleOpCommon extends RobotHardware {
    final double JOY_STICK_DEAD_ZONE = 0.1;

    GamepadButtons gamepadButtons_ = new GamepadButtons(gamepad1, gamepad2);

    boolean activatedCtlLiftByJoystick__ = false;          // Indicate if the joystick in gamepad is active to control lift.
                                                           // If it is active, ignore the pressed buttons used to control lift.
    boolean activatedCtlGrabberCraneByJoystick__ = false;  // Indicate if the joystick in gamepad is active to control grabber crane
                                                           // If it is active, ignore the pressed buttons used to control grabber crane.

    boolean holdLiftAtCurrentPosition_ = false;
    boolean holdGrabberCraneAtCurrentPosition = false;

    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start TeleOp");
        telemetry.update();

        waitForStart();

        initializeWhenStart();

        while (opModeIsActive()) {
            currTime_ = timer_.time();
            operateRobot();
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        initializeTeleOp();
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;

        driveTrain_.resetEncoder(0);
    }

    void cleanUpAtEndOfRun() {
        // TBD
    }

    // Gamepad 1:
    //   - left_joystick :
    //      + Y: control drive train moves forward and backward
    //      + X: control drive train turn
    //   - right_joystick :
    //      + Y: control drive train moves forward and backward
    //      + X: control drive train shift to left, right, or diagonal
    //   - left bumper:
    //       + Pressed (no hold needed): Move lift to deliver stone position
    //       + Released: do nothing
    //   - right bumper
    //      + Pressed and hold: hook down
    //      - Released: hook up
    //  Gamepad 2:
    //    - left_joystick :
    //       + Y: control lift up and down
    //       + X: Unused
    //    - right_joystick :
    //       + Y: control grabber crane in and out
    //       + X: Unused
    //    - left bumper: Repeat following modes
    //       + Pressed once: Grabber clamp close
    //       + Pressed again: Grabber clamp open
    //    - right bumper
    //       + Pressed (no hold needed): Move lift and grabber to be ready for grabbing stone
    //       + Released: do nothing
    //    - Button B: Repeat following modes
    //       + Pressed once: Grabber rotates out
    //       + Pressed again: Grabber rotates in
    void operateRobot() {
        // Use joy stick of gamepad 1 to control drive train
        driveTrain_.driveByGamePad(gamepad1);

        // left_joystick_y is used to control lift up and down and it ranges from -1 to 1, where -1 is full left and 1 is full right
        checkControlOfLiftByJoystick();

        // right_joystick_y is used to control grabber crane in and out
        checkControlOfGrabberCraneByJoystick();

        boolean move_hook_to_pull_position = false;

        GamepadButtons.Button pressed_button_in_gamepad_1 = gamepadButtons_.pressedButton(GamepadButtons.GamepadId.PAD_1, currTime_);
        switch (pressed_button_in_gamepad_1) {
            case LEFT_BUMPER:
                if (activatedCtlLiftByJoystick__ == false) {
                    if (lift_!= null) lift_.moveToPosition(Lift.Position.LIFT_DELIVER_STONE, currTime_);
                }
                break;
            case RIGHT_BUMPER:
                move_hook_to_pull_position = true;
                break;
            default:
                break;
        }

        GamepadButtons.Button pressed_button_in_gamepad_2 = gamepadButtons_.pressedButton(GamepadButtons.GamepadId.PAD_2, currTime_);
        switch (pressed_button_in_gamepad_2) {
            case LEFT_BUMPER:
                if (grabber_ != null) {
                    int cnt = gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.LEFT_BUMPER);
                    if ((cnt % 2) != 0) grabber_.clampClose();
                    else grabber_.clampOpen();
                }
                break;
            case RIGHT_BUMPER:
                if (activatedCtlGrabberCraneByJoystick__ == false) {
                    if (grabber_ != null) grabber_.moveCraneToPosition(Grabber.CranePosition.CRANE_GRAB_STONE);
                }
                break;
            case B:
                if (grabber_ != null){
                    int cnt = gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.B);
                    if ((cnt % 2) != 0) grabber_.rotationOut();
                    else grabber_.rotationIn();
                }
                break;
            default:
                break;
        }

        if (hooks_ != null) {
            if (move_hook_to_pull_position == true) hooks_.moveHooksToPosition(Hooks.Position.PULL);
            else hooks_.moveHooksToPosition(Hooks.Position.RELEASE);
        }

        if (lift_ != null) {
            if (holdLiftAtCurrentPosition_ == true) lift_.holdAtTargetPosition(currTime_);
        }

        if (grabber_ != null) {
            if (holdGrabberCraneAtCurrentPosition == true) grabber_.holdCraneAtTargetPosition();
        }
    }

    void checkControlOfLiftByJoystick() {
        holdLiftAtCurrentPosition_ = true;

        final double lsy_in_gamepad_2 = gamepad2.left_stick_y;
        if (Math.abs(lsy_in_gamepad_2) > JOY_STICK_DEAD_ZONE) {
            activatedCtlLiftByJoystick__ = true;

            if (lift_ != null) {
                if (lsy_in_gamepad_2 < 0) lift_.moveUp(-lsy_in_gamepad_2, currTime_);
                else lift_.moveDown(lsy_in_gamepad_2, currTime_);

                // telemetry.addData("LSY", String.valueOf(lsy_in_gamepad_2));
                // lift_.showEncoderValue(true);
            }

            holdLiftAtCurrentPosition_ = false;

            /*
            if (pressed_button_in_gamepad_1 == GamepadButtons.Button.LEFT_BUMPER) {
               pressed_button_in_gamepad_1 = GamepadButtons.Button.UNKNOWN; // Cancel move lift to grab stone position
            }
            */
        } else if (activatedCtlLiftByJoystick__ == true) {
            activatedCtlLiftByJoystick__ = false;

            // Hold the lift at current position
            if (lift_ != null) lift_.setCurrentPositionAsTargetPosition(currTime_);
        }
    }

    void checkControlOfGrabberCraneByJoystick() {
        holdGrabberCraneAtCurrentPosition = true;

        final double rsy_in_gamepad_2 = gamepad2.right_stick_y;
        if (Math.abs(rsy_in_gamepad_2) > JOY_STICK_DEAD_ZONE) {
            activatedCtlGrabberCraneByJoystick__ = true;

            if (grabber_ != null) {
                if (rsy_in_gamepad_2 < 0) grabber_.craneStretchOut(-rsy_in_gamepad_2);
                else grabber_.craneDrawBack(rsy_in_gamepad_2);

                // telemetry.addData("RSY", String.valueOf(rsy_in_gamepad_2));
                // grabber_.showValues(true);
            }

            holdGrabberCraneAtCurrentPosition = false;

            /*
            if (pressed_button_in_gamepad_1 == GamepadButtons.Button.LEFT_BUMPER) {
                pressed_button_in_gamepad_1 = GamepadButtons.Button.UNKNOWN; // Cancel move lift to grab stone position
            }
            */
        } else if (activatedCtlGrabberCraneByJoystick__ == true) {
            activatedCtlGrabberCraneByJoystick__ = false;

            if (grabber_ != null) grabber_.setCurrentCranePositionAsTargetPosition();
        }
    }
}
