package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class RobotHardware extends LinearOpMode {
    /// Main timer
    ElapsedTime timer_ = new ElapsedTime();
    double currTime_ = 0;

    /// Drive train
    DriveTrain driveTrain_ = null;

    /// Tfod for detecting skystone
    DetectSkystone detectSkystone_ = null;

    /// GoBuilda Servo for left and right hooks
    GoBildaDualServo leftHookServo_ = null;
    GoBildaDualServo rightHookServo_ = null;
    final double INIT_LEFT_HOOK_DEGREE = 230;    // Initial positions for the left and right servos are on opposite ends of the position spectrum
    final double INIT_RIGHT_HOOK_DEGREE = 50;    // (0 degrees to 280 degrees) because they are facing opposite directions when mounted on the robot
    final double LEFT_HOOK_PULL_DEGREE = 80;
    final double RIGHT_HOOK_PULL_DEGREE = 210;
    final double LEFT_HOOK_RELEASE_DEGREE = 240;
    final double RIGHT_HOOK_RELEASE_DEGREE = 40;

    /// Lift
    Lift lift_ = null;

    /// Distance sensor
    KylaColorSensor color_ = null;

    /// Detecting Vuforia targets
    DetectNavigationTarget detectNavigationTarget_ = null;

    /// Distance sensor
    KylaDistanceSensor distance_ = null;

    @Override
    public void runOpMode() {
        // Do nothing
    }

    // Code to run when op mode is initialized
    public void initializeAutonomous() {
        createMecanumDriveTrain();
        createHookServoSystem(INIT_LEFT_HOOK_DEGREE, INIT_RIGHT_HOOK_DEGREE);

        // createDetectNavigationTarget();  // Create object to use Vuforia to detect navigation targets including skystone
        // createDetectSkystone();          // Create object to use tensor flow to detect skystone
        // createLift();

    }

    public void initializeTeleOp(){
        createMecanumDriveTrain();

        // createLift();
    }

    DetectSkystone getDetectSkystone() {
        return detectSkystone_;
    }

    DriveTrain getDriveTrain() {
        return  driveTrain_;
    }

    Lift getLift(){
        return lift_;
    }

    GoBildaDualServo getLeftHookServo_() { return leftHookServo_; }
    GoBildaDualServo getRightHookServo_() { return rightHookServo_; }

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
        DcMotor motor_lift = null; // hardwareMap.dcMotor.get("liftMotor");
        // Servo servo_1 = null; // hardwareMap.dcMotor.get("liftServo1");
        // Servo servo_2 = null; // hardwareMap.dcMotor.get("liftServo2");

        lift_ = new Lift(motor_lift,
                         telemetry);
    }

    void createHookServoSystem(double init_left_hook_degree,
                               double init_right_hook_degree) {
        Servo left_servo = hardwareMap.get(Servo.class,"leftHookServo");
        Servo right_servo = hardwareMap.get(Servo.class,"rightHookServo");

        leftHookServo_ = new GoBildaDualServo("LeftHook",
                                              left_servo,
                               false,
                                              init_left_hook_degree,
                                              telemetry);

        rightHookServo_ = new GoBildaDualServo("RightHook",
                                               right_servo,
                                false,
                                               init_right_hook_degree,
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

}
