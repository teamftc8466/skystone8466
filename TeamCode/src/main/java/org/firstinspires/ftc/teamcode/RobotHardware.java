package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class RobotHardware extends LinearOpMode {
    /// Main timer
    ElapsedTime timer_ = new ElapsedTime();
    double currTime_ = 0;

    /// drive train
    // DriveTrain driveTrain_ = null;

    /// Tfod for detecting skystone
    DetectSkystone detectSkystone_ = null;

    @Override
    public void runOpMode() {
        // Do nothing
    }

    // Code to run when op mode is initialized
    public void initialize() {
        createDriveTrain();

        createDetectSkystone();
    }

    DetectSkystone getDetectSkystone() {
        return detectSkystone_;
    }

    private void createDriveTrain() {
        // driveTrain_ = new DriveTrain(hardwareMap.dcMotor.get("motorRF"),
        //                             hardwareMap.dcMotor.get("motorRB"),
        //                             hardwareMap.dcMotor.get("motorLF"),
        //                             hardwareMap.dcMotor.get("motorLB"),
        //                             hardwareMap.get(BNO055IMU.class, "imu"),
        //                             telemetry);
    }

    private void createDetectSkystone(){
        int tfod_monitor_view_id = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        // WebcamName webcam_name = hardwareMap.get(WebcamName.class, "webcam");
        WebcamName webcam_name = null;
        detectSkystone_ = new DetectSkystone(webcam_name,
                                             tfod_monitor_view_id,
                                             telemetry);
    }
}
