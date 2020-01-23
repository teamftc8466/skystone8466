package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

// IMU
public class RevImu {

    private Telemetry telemetry_;

    BNO055IMU imu_ = null;
    BNO055IMU.Parameters imuParameters_ = null;
    Orientation imuAngles_;
    double imuHeading_ = 0;

    double targetHeading_ = 0;

    public RevImu(BNO055IMU imu,
                  Telemetry telemetry) {
        imu_ = imu;
        telemetry_ = telemetry;

        imuParameters_ = new BNO055IMU.Parameters();

        imuParameters_.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters_.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParameters_.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        imuParameters_.loggingEnabled = true;
        imuParameters_.loggingTag = "IMU";
        imuParameters_.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu_.initialize(imuParameters_);
        imu_.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }

    public BNO055IMU getImu() {
	    return imu_;
    }

    void setTargetHeading(double set_heading) {
        targetHeading_ = (set_heading % 360.0);
    }

    double targetHeading() { return targetHeading_; }

    // Return current robot heading based on gyro/IMU reading:
    //    - The heading starts from 0 when robot is initialized.
    //    - When robot is turned a round from left, the heading will be increased from 0 to 180 and then increased from -180 to 0.
    //      NOTE: When the robot's head reaches almost to opposite direction, the heading increased close to 180. However, at
    //            the moment passing the opposite direction, the heading value will change from 180 to -180 suddenly.
    //    - When robot is turned a round from right, the heading will be decreased from 0 to -180 and then decreased from 180 to 0.
    //      NOTE: When the robot's head reaches almost to opposite direction, the heading decreased close to -180. However, at
    //            the moment passing the opposite direction, the heading value will change from -180 to 180 suddenly.
    double getHeading() {
        // acquiring angles are expensive, keep it minimal
        imuAngles_  = imu_.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
        imuHeading_ = AngleUnit.DEGREES.normalize(imuAngles_.firstAngle);

        return imuHeading_;
    }

    // Return heading diff for a given init heading.
    //
    // We allow difference to be less than -180 by CALC_HEADING_DIFF_GUARD_BAND and greater than 180 by
    // CALC_HEADING_DIFF_GUARD_BAND to avoid confusion due to sudden change from -180 to 180 for left turn or
    // 180 to -180 for right turn.
    double getHeadingDifference(double target_heading) {
        return getHeadingDifference(target_heading, getHeading());
    }

    double getHeadingDifference(double target_heading,
                                double curr_heading) {
        double diff = convertHeadingInRange(target_heading) - convertHeadingInRange(curr_heading);
        if (diff < -180) return (360 + diff);
        else if (diff > 180) return (-360 + diff);
        return diff;
    }

    // Convert the heading in degree to the range between -180 to 180.
    private double convertHeadingInRange(double heading) {
        if (heading >= -180 && heading <= 180) return heading;

        double curr_heading = heading % 360.0;
        if (curr_heading > 180.0) {
            return -(curr_heading - 180.0);
        } else if (curr_heading < -180.0) {
            return -(curr_heading + 180.0);
        }

        return 0;
    }

    void showHeading(boolean update_flag) {
        telemetry_.addData("IMU heading", String.valueOf(imuHeading_));
        if (update_flag == true) telemetry_.update();
    }
}
