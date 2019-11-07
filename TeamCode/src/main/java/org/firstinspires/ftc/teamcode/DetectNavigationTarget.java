package org.firstinspires.ftc.teamcode;

import com.vuforia.ViewerParameters;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

public class DetectNavigationTarget {
    // Trackable targets
    static final int SKY_STONE = 0;
    static final int BLUE_REAR_BRIDGE = 1;
    static final int RED_REAR_BRIDGE = 2;
    static final int RED_FRONT_BRIDGE = 3;
    static final int BLUE_FRONT_BRIDGE = 4;
    static final int RED_PERIMETER_1 = 5;
    static final int RED_PERIMETER_2 = 6;
    static final int FRONT_PERIMETER_1 = 7;
    static final int FRONT_PERIMETER_2 = 8;
    static final int BLUE_PERIMETER_1 = 9;
    static final int BLUE_PERIMETER_2 = 10;
    static final int REAR_PERIMETER_1 = 11;
    static final int REAR_PERIMETER_2 = 12;
    static final int NUM_TRACKABLES = 13;

    static final String [] trackableNames_ = {
        "SkyStone",
        "Blue Rear Bridge",
        "Red Rear Bridge",
        "Red Front Bridge",
        "Blue Front Bridge",
        "Red Perimeter 1",
        "Red Perimeter 2",
        "Front Perimeter 1",
        "Front Perimeter 2",
        "Blue Perimeter 1",
        "Blue Perimeter 2",
        "Rear Perimeter 1",
        "Rear Perimeter 2",
        "None"
    };

    private static final String VUFORIA_KEY =
            "AbCbPUz/////AAABmRlaEryonEVfsxxT+iHrRnIO+B0SFb6vzFX7lYpj3WD2pSxJG1pAEJeUJR3XWKQqKUbO8KUhq/4mnx2uvCcUM1Rg5/3f+qR0VytJNlyYBXAL9kvbpHVbHI/qjQziYKQ0/1SlKj4KX9nHDmPImH8Vd9vfXauFXJ8bnVE175BVln5MS6bYiK4vvxecGyrIvXpjojrYoHdynFVWcIiAtyy5pSjDbavzC/R12FO2uonKGuWNYfRDPUUnABkpSnObZGu6dxl+n1TznC/jBdWFACKJHaaxfqEiXdUkgXy3LUvUqSjhuYrYQAoL6hVlzkSEJs4AQkvybTeUCMRhCBO6cfheYDQuJnFFft8REdT6d5fyx4a1";

    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private static final boolean PHONE_IS_PORTRAIT = false;

    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    private static final float mmPerInch = 25.4f;
    private static final float mmTargetHeight = (6) * mmPerInch;          // the height of the center of the target image above the floor

    // Constant for Stone Target
    private static final float stoneZ = 2.00f * mmPerInch;

    // Constants for the center support targets
    private static final float bridgeZ = 6.42f * mmPerInch;
    private static final float bridgeY = 23 * mmPerInch;
    private static final float bridgeX = 5.18f * mmPerInch;
    private static final float bridgeRotY = 59;                                 // Units are degrees
    private static final float bridgeRotZ = 180;

    // Constants for perimeter targets
    private static final float halfField = 72 * mmPerInch;
    private static final float quadField  = 36 * mmPerInch;

    private WebcamName webcamName_ = null;
    private int cameraMonitorViewId_ = 0;
    private Telemetry telemetry_ = null;

    private VuforiaLocalizer.Parameters vuforiaParameters_ = null;
    private VuforiaLocalizer vuforia_ = null;
    private VuforiaTrackables trackables_ = null;

    // For convenience, gather together all the trackable objects in one easily-iterable collection */
    List<VuforiaTrackable> allTrackablesInList_ = null;

    private int lastTrackableId_ = NUM_TRACKABLES;
    private OpenGLMatrix lastLocation_;
    private double tX_ = 0; // x-coordinate
    private double tY_ = 0; // y-coordinate
    private double tZ_ = 0; // z-coordinate
    private double rX_ = 0; // Rotation about the x-axis
    private double rY_ = 0; // Rotation about the y-axis
    private double rZ_ = 0; // Rotation about the z-axis

    public DetectNavigationTarget(WebcamName webcam_name,
                                  int camera_monitor_view_id,
                                  Telemetry telemetry) {
        webcamName_=webcam_name;
        cameraMonitorViewId_=camera_monitor_view_id;
        telemetry_=telemetry;

        setup();
    }

    private void setup() {
        initVuforia();

        initTrackables();

        initPhoneLocation();
    }

    // Activate VuForia image processing.
    public void activate() {
        trackables_.activate();
    }

    // Deactivate VuForia image processing.
    public void deactivate() { trackables_.deactivate(); }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        vuforiaParameters_ = new VuforiaLocalizer.Parameters(cameraMonitorViewId_);

        vuforiaParameters_.vuforiaLicenseKey = VUFORIA_KEY;

        if (webcamName_ != null) vuforiaParameters_.cameraName = webcamName_;
        else vuforiaParameters_.cameraDirection = CAMERA_CHOICE;

        //  Instantiate the Vuforia engine
        vuforia_ = ClassFactory.getInstance().createVuforia(vuforiaParameters_);
    }

    private void initTrackables() {
        // Load the data sets for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        trackables_ = this.vuforia_.loadTrackablesFromAsset("Skystone");

        for (int i=0; i<NUM_TRACKABLES; ++i) {
            trackables_.get(i).setName(trackableNames_[i]);
        }

        allTrackablesInList_ = new ArrayList<VuforiaTrackable>();
        allTrackablesInList_.addAll(trackables_);

        /**
         * In order for localization to work, we need to tell the system where each target is on the field, and
         * where the phone resides on the robot.  These specifications are in the form of <em>transformation matrices.</em>
         * Transformation matrices are a central, important concept in the math here involved in localization.
         * See <a href="https://en.wikipedia.org/wiki/Transformation_matrix">Transformation Matrix</a>
         * for detailed information. Commonly, you'll encounter transformation matrices as instances
         * of the {@link OpenGLMatrix} class.
         *
         * If you are standing in the Red Alliance Station looking towards the center of the field,
         *     - The X axis runs from your left to the right. (positive from the center to the right)
         *     - The Y axis runs from the Red Alliance Station towards the other side of the field
         *       where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
         *     - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
         *
         * Before being transformed, each target image is conceptually located at the origin of the field's
         *  coordinate system (the center of the field), facing up.
         */

        // Set the position of the Stone Target.  Since it's not fixed in position, assume it's at the field origin.
        // Rotated it to to face forward, and raised it to sit on the ground correctly.
        // This can be used for generic target-centric approach algorithms
        trackables_.get(SKY_STONE).setLocation(OpenGLMatrix
                .translation(0, 0, stoneZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        //Set the position of the bridge support targets with relation to origin (center of field)
        trackables_.get(BLUE_FRONT_BRIDGE).setLocation(OpenGLMatrix
                .translation(-bridgeX, bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, bridgeRotZ)));

        trackables_.get(BLUE_REAR_BRIDGE).setLocation(OpenGLMatrix
                .translation(-bridgeX, bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, bridgeRotZ)));

        trackables_.get(RED_FRONT_BRIDGE).setLocation(OpenGLMatrix
                .translation(-bridgeX, -bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, 0)));

        trackables_.get(RED_REAR_BRIDGE).setLocation(OpenGLMatrix
                .translation(bridgeX, -bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, 0)));

        //Set the position of the perimeter targets with relation to origin (center of field)
        trackables_.get(RED_PERIMETER_1).setLocation(OpenGLMatrix
                .translation(quadField, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        trackables_.get(RED_PERIMETER_2).setLocation(OpenGLMatrix
                .translation(-quadField, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        trackables_.get(FRONT_PERIMETER_1).setLocation(OpenGLMatrix
                .translation(-halfField, -quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90)));

        trackables_.get(FRONT_PERIMETER_2).setLocation(OpenGLMatrix
                .translation(-halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));

        trackables_.get(BLUE_PERIMETER_1).setLocation(OpenGLMatrix
                .translation(-quadField, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        trackables_.get(BLUE_PERIMETER_2).setLocation(OpenGLMatrix
                .translation(quadField, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        trackables_.get(REAR_PERIMETER_1).setLocation(OpenGLMatrix
                .translation(halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , -90)));

        trackables_.get(REAR_PERIMETER_2).setLocation(OpenGLMatrix
                .translation(halfField, -quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));
    }

    //
    // Create a transformation matrix describing where the phone is on the robot.
    //
    // NOTE !!!!  It's very important that you turn OFF your phone's Auto-Screen-Rotation option.
    // Lock it into Portrait for these numbers to work.
    //
    // Info:  The coordinate frame for the robot looks the same as the field.
    // The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
    // Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
    //
    // The phone starts out lying flat, with the screen facing Up and with the physical top of the phone
    // pointing to the LEFT side of the Robot.
    // The two examples below assume that the camera is facing forward out the front of the robot.
    private void initPhoneLocation() {
        final float CAMERA_FORWARD_DISPLACEMENT  = 4.0f * mmPerInch;   // eg: Camera is 4 Inches in front of robot center //TODO: When the webcam (or phone) is mounted, find the correct measurements
        final float CAMERA_VERTICAL_DISPLACEMENT = 8.0f * mmPerInch;   // eg: Camera is 8 Inches above ground
        final float CAMERA_LEFT_DISPLACEMENT     = 0;     // eg: Camera is ON the robot's center line

        float phoneX_rotate = 0;
        float phoneY_rotate = 0;
        float phoneZ_rotate = 0;

        // We need to rotate the camera around its long axis to bring the correct camera forward.
        if (CAMERA_CHOICE == BACK) phoneY_rotate = -90;
        else phoneY_rotate = 90;

        // Rotate the phone vertical about the X axis if it's in portrait mode
        if (PHONE_IS_PORTRAIT) phoneX_rotate = 90 ;

        OpenGLMatrix robotFromCamera = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneY_rotate, phoneZ_rotate, phoneX_rotate));

        /**  Let all the trackable listeners know where the phone is.  */
        for (VuforiaTrackable trackable : allTrackablesInList_) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, vuforiaParameters_.cameraDirection);
        }
    }

    boolean determineRobotLocation() {
        // check all the trackable targets to see which one (if any) is visible.
        boolean exist_visible_target_flag = false;
        for (VuforiaTrackable target : allTrackablesInList_) {
            if (((VuforiaTrackableDefaultListener)target.getListener()).isVisible()) {
                lastTrackableId_ = convertTrackableNameToId(target.getName());
                exist_visible_target_flag = true;

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                OpenGLMatrix robot_location_transform = ((VuforiaTrackableDefaultListener)target.getListener()).getUpdatedRobotLocation();
                if (robot_location_transform != null) {
                    lastLocation_ = robot_location_transform;
                }

                break;
            }
        }

        if (exist_visible_target_flag == false) {
            telemetry_.addData("Visible target", "None");
            telemetry_.update();
            return false;
        }

        translateToLastLocationsToDistanceAndAngles();
        showRobotLocation();
        return true;
    }

    // find out if VuMark is visible to the phone camera.
    // @return True if VuMark found, false if not.
    boolean findTarget(int trackable_id) {
        if (trackable_id >=0 && trackable_id < NUM_TRACKABLES) {
            for (VuforiaTrackable target : allTrackablesInList_) {
                if (((VuforiaTrackableDefaultListener)target.getListener()).isVisible()) {
                    if (trackableNames_[trackable_id].equals(target.getName())==true) {
                        lastTrackableId_ = trackable_id;

                        // getUpdatedRobotLocation() will return null if no new information is available since
                        // the last time that call was made, or if the trackable is not currently visible.
                        OpenGLMatrix robot_location_transform = ((VuforiaTrackableDefaultListener) target.getListener()).getUpdatedRobotLocation();
                        if (robot_location_transform != null) {
                            lastLocation_ = robot_location_transform;
                        }

                        showRobotLocation();
                        translateToLastLocationsToDistanceAndAngles();

                        return true;
                    }
                }
            }
        }

        telemetry_.addData("Visible target", "None");
        telemetry_.update();
        return false;
    }

    private boolean findTarget(VuforiaTrackable target) {
        // See if any of the instances of the template are currently visible.
        VuMarkInstanceId instance_id = ((VuforiaTrackableDefaultListener) target.getListener()).getVuMarkInstanceId();
        OpenGLMatrix robot_location_transform = null;
        if (instance_id != null) {
            robot_location_transform = ((VuforiaTrackableDefaultListener) target.getListener()).getPose();
        }

        if (robot_location_transform == null) {
            telemetry_.addData("Visible target", "None");
            telemetry_.update();
            return false;
        }

        lastLocation_ = robot_location_transform;

        translateToLastLocationsToDistanceAndAngles();
        showRobotLocation();
        return true;
    }

    private void translateToLastLocationsToDistanceAndAngles() {
        // Extract the X, Y, and Z components of the offset of the target relative to the robot
        VectorF trans = lastLocation_.getTranslation();
        tX_ = trans.get(0) / mmPerInch;
        tY_ = trans.get(1) / mmPerInch;
        tZ_ = trans.get(2) / mmPerInch;

        // Extract the rotational components of the target relative to the robot
        Orientation rotation = Orientation.getOrientation(lastLocation_, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        rX_ = rotation.firstAngle;
        rY_ = rotation.secondAngle;
        rZ_ = rotation.thirdAngle;
    }

    private int convertTrackableNameToId(String name) {
        for (int i=0; i<NUM_TRACKABLES; ++i) {
            if (trackables_.get(i).getName().equals(name)==true) {
                return i;
            }
        }

        return NUM_TRACKABLES;
    }

    /**
     * Format OpenGLMatrix object for human viewing.
     * @return OpenGLMatrix description.
     */
    private String formatLocation(OpenGLMatrix location) {
        return (location != null) ? location.formatAsTransform() : "null";
    }

    private void showLocation(OpenGLMatrix location) {
        telemetry_.addData("Location", formatLocation(location));
    }

    public void showLastLocation() {
        telemetry_.addData("Location", formatLocation(lastLocation_));
    }

    public void showRobotLocation() {
        telemetry_.addData("Visible target", trackableNames_[lastTrackableId_]);

        if (lastTrackableId_ < NUM_TRACKABLES) {
            telemetry_.addData("Position (In)", "{X, Y, Z} = %.1f, %.1f, %.1f", tX_, tY_, tZ_);
            telemetry_.addData("Rotation (Degree)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rX_, rY_, rZ_);
        }

        telemetry_.update();
    }
}
