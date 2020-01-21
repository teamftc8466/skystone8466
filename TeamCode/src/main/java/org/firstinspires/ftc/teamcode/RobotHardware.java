package org.firstinspires.ftc.teamcode;

import android.text.method.Touch;
import android.view.View;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class RobotHardware extends LinearOpMode {
    /// Main timer
    ElapsedTime timer_ = new ElapsedTime();
    double currTime_ = 0;

    /// IMU used for detecting heading during autonomous
    RevImu imu_ = null;

    /// Drive train
    DriveTrain driveTrain_ = null;

    /// Tfod for detecting skystone
    DetectSkystone detectSkystone_ = null;

    /// Hooks including left and right hooks
    Hooks hooks_ = null;

    /// Lift
    Lift lift_ = null;

    /// Grabber
    Grabber grabber_ = null;

    /// Distance sensor
    KylaColorSensor color_ = null;

    /// Detecting Vuforia targets
    DetectNavigationTarget detectNavigationTarget_ = null;

    /// Distance sensor
    KylaDistanceSensor distance_ = null;

    //Touch Sensors
    GobildaTouchSensors gobildaTouchSensors_;

    @Override
    public void runOpMode() {
        // Do nothing
    }

    // Code to run when op mode is initialized
    public void initializeAutonomous() {
        createMecanumDriveTrain();

        // createDetectNavigationTarget();  // Create object to use Vuforia to detect navigation targets including skystone
        createDetectSkystone();          // Create object to use tensor flow to detect skystone

        createHooks(Hooks.Position.INIT);
        // createLift();
        // createGrabber();
    }

    public void initializeTeleOp() {
        createMecanumDriveTrain();

        // createHooks(Hooks.Position.INIT);
        // createLift();
        // createGrabber();
    }

    RevImu getImu() {
        return imu_;
    }

    DetectSkystone getDetectSkystone() {
        return detectSkystone_;
    }

    DriveTrain getDriveTrain() {
        return driveTrain_;
    }

    Lift getLift() {
        return lift_;
    }

    Hooks getHooks() {
        return hooks_;
    }

    void createImu() {
        imu_ = new RevImu(hardwareMap.get(BNO055IMU.class, "imu"),
                telemetry);
    }

    void createMecanumDriveTrain() {
        driveTrain_ = new DriveTrain(telemetry);
        driveTrain_.createMecanumDriveTrain(hardwareMap);
    }

    void createDetectSkystone() {
        int tfod_monitor_view_id = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcam_name = hardwareMap.get(WebcamName.class, "webcam");
        // WebcamName webcam_name = null;

        detectSkystone_ = new DetectSkystone(webcam_name,
                tfod_monitor_view_id,
                telemetry);
    }

    void createLift() {
        DcMotor motor_left = hardwareMap.dcMotor.get("motorLiftLeft");
        DcMotor motor_right = hardwareMap.dcMotor.get("motorLiftRight");

        lift_ = new Lift(motor_left,
                motor_right,
                telemetry);
    }

    void createGrabber() {
        DcMotor crane_motor = hardwareMap.get(DcMotor.class, "craneMotor");
        ;
        Servo rotation_servo = hardwareMap.get(Servo.class, "rotationServo");
        Servo clamp_servo = hardwareMap.get(Servo.class, "clampServo");

        grabber_ = new Grabber(crane_motor,
                "rotationServo",
                rotation_servo,
                "clampServo",
                clamp_servo,
                telemetry);
    }

    void createHooks(Hooks.Position init_position) {
        Servo left_servo = hardwareMap.get(Servo.class, "leftHookServo");
        Servo right_servo = hardwareMap.get(Servo.class, "rightHookServo");

        hooks_ = new Hooks(left_servo,
                "leftHookServo",
                right_servo,
                "rightHookServo",
                init_position,
                telemetry);
    }

    void createColor() {
        ColorSensor c1 = hardwareMap.colorSensor.get("color1");

        color_ = new KylaColorSensor(c1, telemetry);
    }

    void createDetectNavigationTarget() {
        int camera_monitor_view_id = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcam_name = hardwareMap.get(WebcamName.class, "webcam");
        //WebcamName webcam_name = null;

        detectNavigationTarget_ = new DetectNavigationTarget(webcam_name,
                                                             camera_monitor_view_id,
                                                             telemetry);
    }

    void createDistance() {
        DistanceSensor dis = hardwareMap.get(DistanceSensor.class, "sensor_range");

        distance_ = new KylaDistanceSensor(dis, telemetry);
    }

    void createGobildaTouchSensors() {
        TouchSensor touch1 = hardwareMap.touchSensor.get("TouchSensor1");
      //  TouchSensor touch2 = hardwareMap.touchSensor.get("TouchSensor2");

        gobildaTouchSensors_ = new GobildaTouchSensors(touch1, telemetry);
    }

}
