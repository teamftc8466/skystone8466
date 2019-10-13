package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

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
        // WebcamName webcam_name = hardwareMap+6.get(WebcamName.class, "webcam");
        WebcamName webcam_name = null;

        detectSkystone_ = new DetectSkystone(webcam_name,
                                             tfod_monitor_view_id,
                                             telemetry);
    }

    void createLift() {
        DcMotor motor_lift = null; // hardwareMap.dcMotor.get("liftMotor");
        Servo servo_1 = null; // hardwareMap.dcMotor.get("liftServo1");
        Servo servo_2 = null; // hardwareMap.dcMotor.get("liftServo2"); //TODO:Rename servos later

        lift_ = new Lift(motor_lift,
                         servo_1,
                         servo_2,
                         telemetry);
    }

    void createColor() {
        ColorSensor c1 = hardwareMap.colorSensor.get("color1");

        color_ = new KylaColorSensor(c1, telemetry);
    }


}
