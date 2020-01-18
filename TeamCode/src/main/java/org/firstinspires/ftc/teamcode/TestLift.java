package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="TestLift", group="FS")
// @Disabled
public class TestLift extends RobotHardware {

    int [] aCnt_ = {0, 0};                 // number of times A is pressed for pads 1 and 2
    int [] bCnt_ = {0, 0};                 // number of times B is pressed for pads 1 and 2
    int [] xCnt_ = {0, 0};                 // number of times X is pressed for pads 1 and 2
    int [] yCnt_ = {0, 0};                 // number of times Y is pressed for pads 1 and 2
    int [] lbCnt_ = {0, 0};                // number of times left_bumper is pressed for pads 1 and 2
    int [] rbCnt_ = {0, 0};                // number of times right_bumper is pressed for pads 1 and 2
    int [] lsbCnt_ = {0, 0};               // number of times left_joystick is pressed for pads 1 and 2
    int [] rsbCnt_ = {0, 0};               // number of times right_joystick is pressed for pads 1 and 2
    boolean lsyActive_ = false;

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
            driveLiftByGamePad();
            rotateRotatorServo();
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        createLift();
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        // TBD
    }

    void rotateRotatorServo() {
        boolean left_bumper = gamepad2.left_bumper;
        if (left_bumper == true) {
            lift_.setRotatorServo(0.30); //0.39
        }
        else {
            lift_.setRotatorServo(0.1);  //0.05
        }
    }

    void driveLiftByGamePad() {
        final double JOY_STICK_DEAD_ZONE = 0.1;

        // left_stick_y is used to control lift up and down and it ranges from -1 to 1, where -1 is full left and 1 is full right
        double lsy = gamepad2.left_stick_y;
        if (Math.abs(lsy) > JOY_STICK_DEAD_ZONE) {
            aCnt_[1] = 0;
            bCnt_[1] = 0;
            xCnt_[1] = 0;
            yCnt_[1] = 0;
            lbCnt_[1] = 0;

            lsyActive_ = true;

            if (lsy < 0) lift_.moveUp(-lsy, currTime_);
            else lift_.moveDown(lsy, currTime_);

            telemetry.addData("LSY", String.valueOf(lsy));
            lift_.showEncoderValue(true);
            return;
        }

        if (lsyActive_ == true) {
            lift_.setCurrentPositionAsTargetPosition(currTime_);
            lsyActive_ = false;
        }

        if (gamepad2.x) {
            aCnt_[1] = 0;
            bCnt_[1] = 0;
            xCnt_[1] += 1;
            yCnt_[1] = 0;
            lbCnt_[1] = 0;
        } else if (gamepad2.y) {
            aCnt_[1] = 0;
            bCnt_[1] = 0;
            xCnt_[1] = 0;
            yCnt_[1] += 1;
            lbCnt_[1] = 0;
        } else if (gamepad2.a) {
            aCnt_[1] += 1;
            bCnt_[1] = 0;
            xCnt_[1] = 0;
            yCnt_[1] = 0;
            lbCnt_[1] = 0;
        } else if (gamepad2.b) {
            aCnt_[1] = 0;
            bCnt_[1] += 1;
            xCnt_[1] = 0;
            yCnt_[1] = 0;
            lbCnt_[1] = 0;
        } else if (gamepad2.left_bumper) {
            aCnt_[1] = 0;
            bCnt_[1] = 0;
            xCnt_[1] = 0;
            yCnt_[1] = 0;
            lbCnt_[1] += 1;
        }

        if (aCnt_[1] != 0) {
            lift_.moveToPosition(Lift.Position.LIFT_DELIVER_STONE, currTime_);
        } else if (bCnt_[1] != 0) {
            lift_.moveToPosition(Lift.Position.LIFT_TOP_POSITION, currTime_);
        } else if (xCnt_[1] != 0) {
            lift_.moveToPosition(Lift.Position.LIFT_GRAB_STONE_READY, currTime_);
        } else if (yCnt_[1] != 0) {
            lift_.moveToPosition(Lift.Position.LIFT_GRAB_STONE_CATCH, currTime_);
        } else if (lbCnt_[1] != 0) {
            lift_.moveToPosition(Lift.Position.LIFT_BOTTOM_POSITION, currTime_);
        } else {
            lift_.holdAtTargetPosition(currTime_);
        }

        telemetry.addData("Cnt",
                          " A="+String.valueOf(aCnt_[1])+
                           " B="+String.valueOf(bCnt_[1])+
                           " X="+String.valueOf(xCnt_[1])+
                           " Y="+String.valueOf(yCnt_[1])+
                           " lb="+String.valueOf(lbCnt_[1]));
        lift_.showEncoderValue(true);
    }
}
