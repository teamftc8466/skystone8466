package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class GoBildaDualServo {

    /// Servo modes & positions
    static final double CONT_MODE_STOP = 0.5;
    static final double MAX_SERVO_MODE_DEGREE = 280.0;
    static final double MAX_SERVO_MODE_POS = 1.0;
    static final double MIN_SERVO_MODE_POS = 0.0;

    /// Telemetry
    private Telemetry telemetry_ = null;

    /// Servo variables
    private Servo servo_ = null;
    private boolean useInContMode_ = false;             // =true: Use in continuous mode, =false: Use in 280 degree mode (aka servo mode)
    private String servoName_ = "";

    /// Constructor
    public GoBildaDualServo(String servo_name,
                            Servo servo,
                            boolean use_in_cont_mode,
                            double servo_mode_init_degree,
                            Telemetry telemetry) {
        servoName_ = servo_name;
        servo_ = servo;
        useInContMode_ = use_in_cont_mode;
        telemetry_ = telemetry;

        if (useInContMode_ == true) {
            setContinuousModePower(CONT_MODE_STOP);
        } else {
            setServoModePositionInDegree(servo_mode_init_degree, false);
        }
    }

    String servoName() { return servoName_; }
    boolean useInContinuousMode() { return useInContMode_; }

    Servo servo() { return servo_; }

    // 0 power = left full speed, 0.5 power = stop, 1 power = right full speed
    void setContinuousModePower(double power){
        power = Range.clip(power, 0.0, 1.0);

        servo_.setPosition(power);
    }

    // 0 pos = 0 degrees, 1 pos = MAX_SERVO_MODE_DEGREE degrees
    void setServoModePositionInDegree(double degree,
                                      boolean show_position_info) {
        final double degree_input_min = 0;

        if (degree >= MAX_SERVO_MODE_DEGREE) {
            servo_.setPosition(MAX_SERVO_MODE_POS);                                  // If the inputted number of degrees exceeds the range of the servo, automatically set the servo position to the max
        } else if (degree <= degree_input_min) {
            servo_.setPosition(MIN_SERVO_MODE_POS);                                  // If the inputted number of degrees is negative, automatically set the servo position to the minimum
        } else {
            double position = degree / MAX_SERVO_MODE_DEGREE;                        // Conversion from the user-inputted number of degrees (the variable degree) to the servo position scale equivalent (the variable servo280position_), i.e. input is 140 degrees, the servo will be set to 0.5 position because 140*(1/280) = 0.5)
            position = Range.clip(position, 0.0, 1.0);

            servo_.setPosition(position);
        }

        if (show_position_info == true) {
            showPosition(true);
        }
    }

    void showPosition(boolean update_flag){
        telemetry_.addData("Servor Position", servoName_+" at " + String.valueOf(servo_.getPosition()));
        if (update_flag == true) telemetry_.update();
    }
}
