package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TestGamepadButtons", group="FS")
@Disabled
public class TestGamepadButtons extends RobotHardware {
    final double JOY_STICK_DEAD_ZONE = 0.1;

    GamepadButtons gamepadButtons_ = null;

    @Override
    public void runOpMode() {
        gamepadButtons_ = new GamepadButtons(gamepad1, gamepad2);

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start TeleOp");
        telemetry.update();

        waitForStart();


        while (opModeIsActive()) {
            currTime_ = timer_.time();
            showButtonCount();
        }
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;

        driveTrain_.resetEncoder(0);
    }

    void showButtonCount() {
        GamepadButtons.Button pressed_button_in_gamepad_1 = gamepadButtons_.pressedButton(GamepadButtons.GamepadId.PAD_1, currTime_);
        telemetry.addData("Pad1",
                "X="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.X))+
                " Y="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.Y))+
                        " A="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.A))+
                        " B="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.B))+
                        " LB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.LEFT_BUMPER))+
                        " RB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.RIGHT_BUMPER))+
                        " LSB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.LEFT_STICK_BUTTON))+
                        " RSB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_1, GamepadButtons.Button.RIGHT_STICK_BUTTON)));

        switch (pressed_button_in_gamepad_1) {
            case X: telemetry.addData("Pad 1 pressed", "X"); break;
            case Y: telemetry.addData("Pad 1 pressed", "Y"); break;
            case A: telemetry.addData("Pad 1 pressed", "A"); break;
            case B: telemetry.addData("Pad 1 pressed", "B"); break;
            case LEFT_BUMPER: telemetry.addData("Pad 1 pressed", "LB"); break;
            case RIGHT_BUMPER: telemetry.addData("Pad 1 pressed", "RB"); break;
            case LEFT_STICK_BUTTON: telemetry.addData("Pad 1 pressed", "LSB"); break;
            case RIGHT_STICK_BUTTON: telemetry.addData("Pad 1 pressed", "RSB"); break;
            default: break;
        }

        GamepadButtons.Button pressed_button_in_gamepad_2 = gamepadButtons_.pressedButton(GamepadButtons.GamepadId.PAD_2, currTime_);
        telemetry.addData("Pad1",
                "X="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.X))+
                        " Y="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.Y))+
                        " A="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.A))+
                        " B="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.B))+
                        " LB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.LEFT_BUMPER))+
                        " RB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.RIGHT_BUMPER))+
                        " LSB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.LEFT_STICK_BUTTON))+
                        " RSB="+String.valueOf(gamepadButtons_.pressedButtonCount(GamepadButtons.GamepadId.PAD_2, GamepadButtons.Button.RIGHT_STICK_BUTTON)));

        telemetry.update();
    }
}
