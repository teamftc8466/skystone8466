package org.firstinspires.ftc.teamcode.ExperimentProgram;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name="WheelSlippageTest", group="BallDrive")
@Disabled
public class WheelSlippageTest extends OpMode {
    BNO055IMU imu;
    DcMotor leftMotor;
    double correction;
    double ksubp, ksubi, ksubd;
    double error;

    @Override
    public void init() {


        leftMotor = hardwareMap.get(DcMotor.class, "motor");

        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        while (imu.isSystemCalibrated() == true) {
            telemetry.addData("status:  ", imu.getCalibrationStatus());
            telemetry.update();
        }
    }

    public double GetAngle(double requested_angle) {
        Orientation current_robot_angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS);
        double delta_angle = current_robot_angles.firstAngle - requested_angle;

        return delta_angle;
    }

    public double I(double requested_angle) {

        return error;
    }

    public double GetCorrection(double requested_angle) {
        correction = (-GetAngle(requested_angle) * ksubp) - (ksubi * I(requested_angle)) - (ksubd);
        return correction;
    }

    @Override
    public void loop() {
        telemetry.addData("orientation: ", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS));
        telemetry.addData("position: ", imu.getPosition());
        telemetry.update();
    }
}
