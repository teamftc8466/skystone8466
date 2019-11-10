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

    //Lift
    Lift lift_ = null;

    KylaColorSensor color_ = null;

    // Detecting Vuforia targets
    DetectNavigationTarget detectNavigationTarget_ = null;

    KylaDistanceSensor distance_ = null;

    @Override
    public void runOpMode() {
        // Do nothing
    }

    // Code to run when op mode is initialized
    public void initializeAutonomous() {
        createDriveTrain();

        createDetectSkystone();

        createLift();
    }

    public void initializeTeleOp(){
        createDriveTrain();

        createLift();
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

    void createDriveTrain() {
        DcMotor motor_left = hardwareMap.dcMotor.get("motorLeft");
        DcMotor motor_center = hardwareMap.dcMotor.get("motorCenter");
        DcMotor motor_right = hardwareMap.dcMotor.get("motorRight");

        driveTrain_ = new DriveTrain(motor_left,
                                     motor_center,
                                     motor_right,
                                     telemetry);
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

    void createColor() {
        ColorSensor c1 = hardwareMap.colorSensor.get("color1");

        color_ = new KylaColorSensor(c1, telemetry);
    }


    void createDetectNavigationTarget() {
        int camera_monitor_view_id = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        //WebcamName webcam_name = hardwareMap+6.get(WebcamName.class, "webcam");
        WebcamName webcam_name = null;

        detectNavigationTarget_ = new DetectNavigationTarget(webcam_name,
                                                             camera_monitor_view_id,
                                                             telemetry);
    }

    void createDistance() {
        DistanceSensor dis = hardwareMap.get(DistanceSensor.class, "sensor_range");

        distance_ = new KylaDistanceSensor(dis, telemetry);
    }

}
