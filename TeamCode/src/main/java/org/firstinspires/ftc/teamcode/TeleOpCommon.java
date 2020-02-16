package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TeleOpCommon", group="FS")
// @Disabled
public class TeleOpCommon extends RobotHardware {
    final double JOY_STICK_DEAD_ZONE = 0.1;

    boolean enableShowGamepadInfo_ = false;              // For debug purpose. Turn this off afterward.
    boolean enableShowDriveTrainInfo_ = false;
    boolean enableShowLiftInfo_ = false;
    boolean enableShowGrabberInfo_ = false;

    GamepadButtons gamepadButtons_ = null;

    boolean activatedCtlLiftByJoystick_ = false;           // Indicate if the joystick in gamepad is active to control lift.
                                                           // If it is active, ignore the pressed buttons used to control lift.
    boolean activatedCtlGrabberCraneByJoystick_ = false;   // Indicate if the joystick in gamepad is active to control grabber crane
                                                           // If it is active, ignore the pressed buttons used to control grabber crane.

    boolean holdLiftAtCurrentPosition_ = false;
    boolean holdGrabberCraneAtCurrentPosition = false;

    boolean allowAutoMoveToCatchStoneReadyPosition_ = false;
    boolean allowAutoCatchStone_ = true;

    final double WAITING_TIME_FOR_CLOSE_CLAMP = 0.3;      // Time spent by closing clamp for auto operation
    boolean autoCloseClampForCatchStoneApplied_ = false;
    double startTimeToAutoCloseClampForCatchStone_ = 0;

    final double ENFORCE_TO_DRAW_BACK_CRANE_TO_END_POWER = 0.6;
    final double MAX_TIME_FOR_ENFORCE_TO_DRAW_BACK_CRANE_TO_END = 4;
    final double ENC_STUCK_TIME_OUT_FOR_ENFORCE_TO_DRAW_BACK_CRANE_TO_END = 1.5;
    boolean enforceToDrawbackCraneToEnd_ = false;
    int encCntForChkEnforceToDrawbackCraneToEnd_ = 0;
    double timeForChkEnforceToDrawbackCraneToEnd_ = 0;
    double startTimeToEnforceToDrawbackCraneToEnd_ = 0;

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
        gamepadButtons_ = new GamepadButtons(gamepad1, gamepad2);

        initializeTeleOp();

        timer_.reset();
        if (grabber_ != null) {
            grabber_.enforceCraneDrawBackToEnd(0.4, timer_, 2.5);
        }
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;

        driveTrain_.resetEncoder(0);
    }

    void setDisplayDebuggingInfo() {
        driveTrain_.disableShowDriveTrainInfo();
        if (grabber_ != null) grabber_.disableShowGrabberInfo();
        if (lift_ != null) lift_.disableShowLiftInfo();

        if (enableShowGamepadInfo_ == false) {
            if (enableShowDriveTrainInfo_ == true) {
                driveTrain_.enableShowDriveTrainInfo();
            } else if (enableShowGrabberInfo_ == true) {
                if (grabber_ != null) grabber_.enableShowGrabberInfo();
            } else if (enableShowLiftInfo_ == true) {
                if (lift_ != null) lift_.enableShowLiftInfo();
            }
        }
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
    //   - right bumper: Repeat following modes
    //      + Pressed once: hook down
    //      - Pressed twice: hook up
    //    - Button A: used to enable display robot status. After pressing 5 times,
    //      - Pressed once: show gamepad info
    //      - Pressed twice: show drive train info
    //      - Pressed third time: show lift info
    //      - Pressed fourth time: show grabber info
    //      - Pressed fifth time: stop showing robot status
    //    - Button B: Repeat following modes
    //      + Pressed once: Enforce to draw back grabber crane to the end and reset encoder
    //      + Pressed again: Stop to enforce to draw back grabber crane to the end and reset encoder
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
    //       + Released: Do nothing
    //    - Button B: Catch the stone
    //       + Pressed: Move lift down to catch stone position and close the clamp
    //       + Released: Do nothing
    //    - Button A: Repeat following modes
    //       + Pressed once: Grabber rotates out
    //       + Pressed again: Grabber rotates in
    void operateRobot() {
        // Use joy stick of gamepad 1 to control drive train
        driveTrain_.driveByGamePad(gamepad1);

        // left_joystick_y is used to control lift up and down and it ranges from -1 to 1, where -1 is full left and 1 is full right
        checkControlOfLiftByJoystick();

        // right_joystick_y is used to control grabber crane in and out
        checkControlOfGrabberCraneByJoystick();

        GamepadButtons.Button pressed_button_in_gamepad_1 = gamepadButtons_.pressedButton(GamepadButtons.GamepadId.PAD_1, currTime_);
        switch (pressed_button_in_gamepad_1) {
            case LEFT_BUMPER:
                if (activatedCtlLiftByJoystick_ == false) {
                    if (lift_!= null) lift_.moveToPosition(Lift.Position.LIFT_DELIVER_STONE, currTime_);

                    allowAutoCatchStone_ = false;
                    autoCloseClampForCatchStoneApplied_ = false;
                    allowAutoMoveToCatchStoneReadyPosition_ = false;
                }
                break;
            case RIGHT_BUMPER:
                if (hooks_ != null) {
                    int cnt = gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.RIGHT_BUMPER);
                    if ((cnt % 2) != 0) hooks_.moveHooksToPosition(Hooks.Position.PULL);
                    else hooks_.moveHooksToPosition(Hooks.Position.RELEASE);
                }
                break;
            case A:
            {
                int cnt = gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.A);

                if (cnt > 5) {
                    enableShowGamepadInfo_ = false;
                    enableShowDriveTrainInfo_ = false;
                    enableShowLiftInfo_ = false;
                    enableShowGrabberInfo_ = false;
                    switch (cnt % 5) {
                        case 1 : enableShowGamepadInfo_ = true; break;
                        case 2 : enableShowDriveTrainInfo_ = true; break;
                        case 3 : enableShowLiftInfo_ = true; break;
                        case 4 : enableShowGrabberInfo_ = true; break;
                        default: break;
                    }

                    setDisplayDebuggingInfo();
                }

                break;
            }
            case B:
                if (grabber_ != null) {
                    int cnt = gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.B);
                    if ((cnt % 2) != 0) {
                        if (enforceToDrawbackCraneToEnd_ == false) {
                            beginEnforceToDrawbackCraneToEnd();
                        }
                    } else if (enforceToDrawbackCraneToEnd_ == true) {
                        stopEnforceToDrawbackCraneToEnd();
                    }
                }
                break;
            default:
                break;
        }

        if (enforceToDrawbackCraneToEnd_ == true) {
            applyEnforceToDrawbackCraneToEnd();
        }

        GamepadButtons.Button pressed_button_in_gamepad_2 = gamepadButtons_.pressedButton(GamepadButtons.GamepadId.PAD_2, currTime_);
        switch (pressed_button_in_gamepad_2) {
            case LEFT_BUMPER:
                if (grabber_ != null) {
                    int cnt = gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.LEFT_BUMPER);
                    if ((cnt % 2) != 0) {
                        grabber_.clampClose();

                        // Disable to move to catch stone ready position in order to allow clamp close
                        allowAutoMoveToCatchStoneReadyPosition_ = false;
                    } else {
                        grabber_.clampOpen();
                    }

                    allowAutoCatchStone_ = false;
                    autoCloseClampForCatchStoneApplied_ = false;
                }
                break;
            case RIGHT_BUMPER:
                if (enforceToDrawbackCraneToEnd_ == true) break;

                if (activatedCtlGrabberCraneByJoystick_ == false) {
                    if (grabber_ != null) {
                        grabber_.moveCraneToPosition(Grabber.CranePosition.CRANE_GRAB_STONE);
                        grabber_.clampOpen();
                    }
                }

                if (activatedCtlLiftByJoystick_ == false) {
                    if (lift_ != null) lift_.moveToPosition(Lift.Position.LIFT_STONE_READY, currTime_);
                }

                allowAutoMoveToCatchStoneReadyPosition_ = (activatedCtlGrabberCraneByJoystick_ == true &&
                                                           activatedCtlLiftByJoystick_ == true);
                allowAutoCatchStone_ = false;
                autoCloseClampForCatchStoneApplied_ = false;
                break;
            case A:
                if (enforceToDrawbackCraneToEnd_ == true) break;

                if (grabber_ != null){
                    int cnt = gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.A);
                    if ((cnt % 2) != 0) grabber_.rotationOut();
                    else grabber_.rotationIn();

                    allowAutoCatchStone_ = false;
                    autoCloseClampForCatchStoneApplied_ = false;
                }
                break;
            case B:
                if (enforceToDrawbackCraneToEnd_ == true) break;

                if (activatedCtlGrabberCraneByJoystick_ == true ||
                        activatedCtlLiftByJoystick_ == true) {
                    allowAutoCatchStone_ = false;
                    autoCloseClampForCatchStoneApplied_ = false;
                } else if (grabber_ != null &&
                           lift_ != null) {
                    // if (grabber_.clampPosition() == Grabber.ClampPosition.CLAMP_OPEN &&
                    //        grabber_.isCraneWithMoveToPositionApplied(Grabber.CranePosition.CRANE_GRAB_STONE) ==  true &&
                    //        grabber_.craneReachToTargetEncoderCount() == true) {
                        lift_.moveToPosition(Lift.Position.LIFT_GRAB_STONE_CATCH, currTime_);

                        allowAutoMoveToCatchStoneReadyPosition_ = false;
                        allowAutoCatchStone_ = true;
                        autoCloseClampForCatchStoneApplied_ = false;
                    // }
                }
                break;
            default:
                break;
        }

        if (grabber_ != null) {
            if (holdGrabberCraneAtCurrentPosition == true) {
                grabber_.holdCraneAtTargetPosition();

                if (allowAutoMoveToCatchStoneReadyPosition_ == true) {
                    if (grabber_.isCraneWithMoveToPositionApplied(Grabber.CranePosition.CRANE_GRAB_STONE) == true &&
                            grabber_.craneReachToTargetEncoderCount() == true) {
                        allowAutoMoveToCatchStoneReadyPosition_ = false; // Auto disable it after finish
                    }
                } else if (allowAutoCatchStone_ == true) {
                    if (lift_ != null &&
                            lift_.isMoveToPositionApplied(Lift.Position.LIFT_GRAB_STONE_CATCH) == true &&
                            lift_.reachToTargetEncoderCount()==true) {
                        if (autoCloseClampForCatchStoneApplied_ == false) {
                            grabber_.clampClose();
                            autoCloseClampForCatchStoneApplied_ = true;
                            startTimeToAutoCloseClampForCatchStone_ = timer_.time();
                        } else if (grabber_.clampPosition() == Grabber.ClampPosition.CLAMP_CLOSE) {
                            double time = timer_.time();
                            if ((time - startTimeToAutoCloseClampForCatchStone_) >= WAITING_TIME_FOR_CLOSE_CLAMP) {
                                lift_.moveToPosition(Lift.Position.LIFT_DELIVER_STONE, time);

                                allowAutoCatchStone_ = false; // Disable it to indicate finishing auto catch the stone
                                autoCloseClampForCatchStoneApplied_ = false;
                            }
                        }
                    }
                }
            }
        }

        // Must put the control of hold lift after handling grabber since auto catch the stone will change
        // lift position.
        if (lift_ != null) {
            if (holdLiftAtCurrentPosition_ == true) lift_.holdAtTargetPosition(currTime_);
        }

        if (enableShowGamepadInfo_ == true) showGamepadControlInfo(true);
    }

    void checkControlOfLiftByJoystick() {
        holdLiftAtCurrentPosition_ = true;

        final double lsy_in_gamepad_2 = gamepad2.left_stick_y;
        if (Math.abs(lsy_in_gamepad_2) > JOY_STICK_DEAD_ZONE) {
            activatedCtlLiftByJoystick_ = true;

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
        } else if (activatedCtlLiftByJoystick_ == true) {
            activatedCtlLiftByJoystick_ = false;

            // Hold the lift at current position
            if (lift_ != null) lift_.setCurrentPositionAsTargetPosition(currTime_);
        }
    }

    void checkControlOfGrabberCraneByJoystick() {
        if (enforceToDrawbackCraneToEnd_ == true) {
            activatedCtlLiftByJoystick_ = false;
            holdGrabberCraneAtCurrentPosition = false;
            return;
        }

        holdGrabberCraneAtCurrentPosition = true;

        final double rsy_in_gamepad_2 = gamepad2.right_stick_y;
        if (Math.abs(rsy_in_gamepad_2) > JOY_STICK_DEAD_ZONE) {
            activatedCtlGrabberCraneByJoystick_ = true;

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
        } else if (activatedCtlGrabberCraneByJoystick_ == true) {
            activatedCtlGrabberCraneByJoystick_ = false;

            if (grabber_ != null) grabber_.setCurrentCranePositionAsTargetPosition();
        }
    }

    void beginEnforceToDrawbackCraneToEnd() {
        enforceToDrawbackCraneToEnd_ = true;

        encCntForChkEnforceToDrawbackCraneToEnd_ = grabber_.getCraneEncoderCount();
        timeForChkEnforceToDrawbackCraneToEnd_ = timer_.time();
        startTimeToEnforceToDrawbackCraneToEnd_ = timer_.time();

        grabber_.setCranePower(ENFORCE_TO_DRAW_BACK_CRANE_TO_END_POWER);
        grabber_.resetCraneWithMoveToPositionApplied();
    }

    void stopEnforceToDrawbackCraneToEnd() {
        enforceToDrawbackCraneToEnd_ = false;

        grabber_.setCranePower(0);
        grabber_.resetCraneWithMoveToPositionApplied();
        grabber_.resetEncoder(timer_.time());
        // sleep(100);
    }

    void applyEnforceToDrawbackCraneToEnd() {
        if (grabber_ == null) {
            enforceToDrawbackCraneToEnd_ = false;
            return;
        }

        grabber_.setCranePower(ENFORCE_TO_DRAW_BACK_CRANE_TO_END_POWER);

        double curr_time = timer_.time();
        if ((curr_time - startTimeToEnforceToDrawbackCraneToEnd_) >= MAX_TIME_FOR_ENFORCE_TO_DRAW_BACK_CRANE_TO_END) {
            enforceToDrawbackCraneToEnd_ = false;
        } else {
            final int curr_crane_enc_cnt = grabber_.getCraneEncoderCount();
            if (Math.abs(curr_crane_enc_cnt - encCntForChkEnforceToDrawbackCraneToEnd_) >= 15) {
                encCntForChkEnforceToDrawbackCraneToEnd_ = grabber_.getCraneEncoderCount();
                timeForChkEnforceToDrawbackCraneToEnd_ = curr_time;
            }

            if (curr_time >= (timeForChkEnforceToDrawbackCraneToEnd_ + ENC_STUCK_TIME_OUT_FOR_ENFORCE_TO_DRAW_BACK_CRANE_TO_END)) {
                enforceToDrawbackCraneToEnd_ = false;
            }
        }

        if (enforceToDrawbackCraneToEnd_ == false) {
            stopEnforceToDrawbackCraneToEnd();
        } else {
            allowAutoCatchStone_ = false;
            autoCloseClampForCatchStoneApplied_ = false;
            allowAutoMoveToCatchStoneReadyPosition_ = false;

            grabber_.resetCraneWithMoveToPositionApplied();
        }
    }

    void showGamepadControlInfo(boolean update_flag) {
        telemetry.addData("Pad1",
                "LS=("+String.valueOf(gamepad1.left_stick_x)+" ,"+String.valueOf(gamepad1.left_stick_y)+
                        ") RS=("+String.valueOf(gamepad1.right_stick_x)+" ,"+String.valueOf(gamepad1.right_stick_y)+
                        ") X="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.X))+
                        " Y="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.Y))+
                        " A="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.A))+
                        " B="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.B))+
                        " LB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.LEFT_BUMPER))+
                        " RB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.RIGHT_BUMPER))+
                        " LSB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.LEFT_STICK_BUTTON))+
                        " RSB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.RIGHT_STICK_BUTTON)));

        telemetry.addData("Pad2",
                "LS=("+String.valueOf(gamepad2.left_stick_x)+" ,"+String.valueOf(gamepad2.left_stick_y)+
                        ") RS=("+String.valueOf(gamepad2.right_stick_x)+" ,"+String.valueOf(gamepad2.right_stick_y)+
                        ") X="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.X))+
                        " Y="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.Y))+
                        " A="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.A))+
                        " B="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.B))+
                        " LB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.LEFT_BUMPER))+
                        " RB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.RIGHT_BUMPER))+
                        " LSB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.LEFT_STICK_BUTTON))+
                        " RSB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.RIGHT_STICK_BUTTON)));

        telemetry.addData("Auto",
                "Catch="+String.valueOf(allowAutoCatchStone_)+
                " ReadyPos="+String.valueOf(allowAutoMoveToCatchStoneReadyPosition_)+
                " CloseClamp="+String.valueOf(autoCloseClampForCatchStoneApplied_));

        telemetry.addData("EnforceDrawbackCrane",
                String.valueOf(enforceToDrawbackCraneToEnd_) +
                " StartTime= " + String.valueOf(startTimeToEnforceToDrawbackCraneToEnd_));


        if (update_flag == true) telemetry.update();
    }
}
