package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadButtons {
    enum Button {
        X(0),
        Y(1),
        A(2),
        B(3),
        LEFT_BUMPER(4),
        RIGHT_BUMPER(5),
        LEFT_STICK_BUTTON(6),
        RIGHT_STICK_BUTTON(7),
        UNKNOWN(8);

        private final int id_;

        // Constructor to give each constant a specific identifier value
        private Button(int id) { id_ = id; }
        public int getId() { return id_; }
    };

    private static final int NumButtons_ = Button.values().length; // Number of buttons = the length of the buttons enum

    enum GamepadId {
        PAD_1(0),
        PAD_2(1);

        private final int id_;

        private GamepadId(int id) { id_ = id; }
        public int getId() { return id_; }
    }

    static final double  MIN_PRESS_BUTTON_INTERVAL = 0.3;   // Recognize pressed button at least every 0.3 sec
    private double [] lastPressButtonTime_ = {0, 0};

    private int [][] pressedCnt_= new int[2][NumButtons_];

    private Gamepad [] gamepad_ = {null, null};

    private Button [] lastPressedButton_ =  {Button.UNKNOWN, Button.UNKNOWN};

    GamepadButtons(Gamepad pad1,
                   Gamepad pad2) {
        gamepad_[0] = pad1;
        gamepad_[1] = pad2;

        resetAllPressedButtonCount();
    }

    void resetAllPressedButtonCount() {
        for (int i=0; i<2; ++i) {
            for (int j=0; j<NumButtons_; ++j) pressedCnt_[i][j] = 0;
        }
    }

    void resetPressedButtonCount(GamepadId pad_id,
                                 Button button) {
        pressedCnt_[pad_id.getId()][button.getId()] = 0;
    }

    int pressedButtonCount(GamepadId pad_id,
                           Button button) {
        return pressedCnt_[pad_id.getId()][button.getId()];
    }

    Button pressedButton(GamepadId pad_id,
                               double curr_time) {
        final int id = pad_id.getId();

        if ((curr_time - lastPressButtonTime_[id]) < MIN_PRESS_BUTTON_INTERVAL) return Button.UNKNOWN;

        Button pressed_button = Button.UNKNOWN;
        if (gamepad_[id].left_bumper) {
            pressed_button = Button.LEFT_BUMPER;
        } else if (gamepad_[id].right_bumper) {
            pressed_button = Button.RIGHT_BUMPER;
        } else if (gamepad_[id].x) {
            pressed_button = Button.X;
        } else if (gamepad_[id].y) {
            pressed_button = Button.Y;
        } else if (gamepad_[id].a) {
            pressed_button = Button.A;
        } else if (gamepad_[id].b) {
            pressed_button = Button.B;
        } else  if (gamepad_[id].left_stick_button) {
            pressed_button = Button.LEFT_STICK_BUTTON;
        } else if (gamepad_[id].right_stick_button) {
            pressed_button = Button.RIGHT_STICK_BUTTON;
        }

        lastPressedButton_[id] = pressed_button;
        if (pressed_button != Button.UNKNOWN) {
            pressedCnt_[id][pressed_button.getId()]++;
            lastPressButtonTime_[id] = curr_time;
        }

        return lastPressedButton_[id];
    }

    Button lastPressedButton(GamepadId pad_id) {
        return lastPressedButton_[pad_id.getId()];
    }
}
