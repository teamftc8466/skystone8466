package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TestGrabber", group="FS")
 @Disabled
public class TestGrabber extends RobotHardware {

    static final double  MIN_PRESS_BUTTON_INTERVAL = 0.3;   // Recognize pressed button at least every 0.3 sec
    double lastPressButtonTime_ = 0;

    int [] aCnt_ = {0, 0};                 // number of times A is pressed for pads 1 and 2
    int [] bCnt_ = {0, 0};                 // number of times B is pressed for pads 1 and 2
    int [] xCnt_ = {0, 0};                 // number of times X is pressed for pads 1 and 2
    int [] yCnt_ = {0, 0};                 // number of times Y is pressed for pads 1 and 2
    int [] lbCnt_ = {0, 0};                // number of times left_bumper is pressed for pads 1 and 2
    boolean rsyActive_ = false;

    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start test lift program");
        telemetry.update();

        waitForStart();

        initializeWhenStart();

        while (opModeIsActive()) {
            currTime_ = timer_.time();
            driveGrabberByGamePad();
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        createGrabber();
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        // TBD
    }

    void driveGrabberByGamePad() {
        final double JOY_STICK_DEAD_ZONE = 0.1;

        // left_stick_y is used to control lift up and down and it ranges from -1 to 1, where -1 is full left and 1 is full right
        double rsy = gamepad2.right_stick_y;
        if (Math.abs(rsy) > JOY_STICK_DEAD_ZONE) {
            aCnt_[1] = 0;
            bCnt_[1] = 0;
            xCnt_[1] = 0;
            yCnt_[1] = 0;
            lbCnt_[1] = 0;

            if (rsy < 0) grabber_.craneStretchOut(-rsy);
            else grabber_.craneDrawBack(rsy);

            rsyActive_ = true;

            grabber_.showValues(true);
            return;
        }

        if (rsyActive_ == true) {
            grabber_.setCurrentCranePositionAsTargetPosition();
            rsyActive_ = false;
        }

        boolean any_pressed_button_flag = false;
        if ((currTime_ - lastPressButtonTime_) > MIN_PRESS_BUTTON_INTERVAL) {
            if (gamepad2.x) {
                any_pressed_button_flag = true;
                aCnt_[1] = 0;
                bCnt_[1] = 0;
                xCnt_[1] += 1;
                yCnt_[1] = 0;
                lbCnt_[1] = 0;
                switch (xCnt_[1] % 4) {
                    case 1:
                        grabber_.moveCraneToPosition(Grabber.CranePosition.CRANE_MAX_STRETCH_OUT_POSITION);
                        break;
                    case 3:
                        grabber_.moveCraneToPosition(Grabber.CranePosition.CRANE_DRAW_BACK_POSITION);
                        break;
                    default:
                        grabber_.stopCrane();
                }
            } else if (gamepad2.y) {
                any_pressed_button_flag = true;
                aCnt_[1] = 0;
                bCnt_[1] = 0;
                xCnt_[1] = 0;
                yCnt_[1] += 1;
                lbCnt_[1] = 0;
                grabber_.moveCraneToPosition(Grabber.CranePosition.CRANE_GRAB_STONE);
            } else if (gamepad2.a) {
                any_pressed_button_flag = true;
                aCnt_[1] += 1;
                bCnt_[1] = 0;
                xCnt_[1] = 0;
                yCnt_[1] = 0;
                lbCnt_[1] = 0;
                if ((aCnt_[1] % 2) == 1) grabber_.clampOpen();
                else grabber_.clampClose();
            } else if (gamepad2.b) {
                any_pressed_button_flag = true;
                aCnt_[1] = 0;
                bCnt_[1] += 1;
                xCnt_[1] = 0;
                yCnt_[1] = 0;
                lbCnt_[1] = 0;
                switch (bCnt_[1] % 4) {
                    case 0: grabber_.rotationIn(); break;
                    case 1: grabber_.rotationHalfOut(); break;
                    case 2: grabber_.rotationOut(); break;
                    case 3: grabber_.rotationHalfOut(); break;
                    default: grabber_.rotationIn(); bCnt_[1] = 0;
                }
            }
        }

        if (any_pressed_button_flag == false) {
            grabber_.holdCraneAtTargetPosition();
        } else {
            lastPressButtonTime_ = currTime_;
        }

        telemetry.addData("Cnt",
                " A="+String.valueOf(aCnt_[1])+
                        " B="+String.valueOf(bCnt_[1])+
                        " X="+String.valueOf(xCnt_[1])+
                        " Y="+String.valueOf(yCnt_[1])+
                        " lb="+String.valueOf(lbCnt_[1]));
        grabber_.showValues(true);
    }
}
