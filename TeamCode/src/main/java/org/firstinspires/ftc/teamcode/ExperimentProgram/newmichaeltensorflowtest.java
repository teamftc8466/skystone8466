
package org.firstinspires.ftc.teamcode.ExperimentProgram;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;

import java.util.List;


@Autonomous(name = "michaelTensorflowtesttfkuufuf", group = "Concept")
public class newmichaeltensorflowtest extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;
    private static final int POINTING_TOLERANCE = 50;

    private static final double LOW_POWER = 0.15;
    private static final double MID_POWER = 0.25;
    private static final int CLICKS_TO_TARGET = 500; //change later
    private static final int COUNTS_PER_ROTATION = 670; //change this later

    private int tfodMonitorViewId1;
    public enum RobotState {
        TARGET_SKYSTONE,
        MOVE_SKYSTONE,
        DONE,
        TEST,
        ERROR,
    }
    private static final String VUFORIA_KEY =
            "AbCbPUz/////AAABmRlaEryonEVfsxxT+iHrRnIO+B0SFb6vzFX7lYpj3WD2pSxJG1pAEJeUJR3XWKQqKUbO8KUhq/4mnx2uvCcUM1Rg5/3f+qR0VytJNlyYBXAL9kvbpHVbHI/qjQziYKQ0/1SlKj4KX9nHDmPImH8Vd9vfXauFXJ8bnVE175BVln5MS6bYiK4vvxecGyrIvXpjojrYoHdynFVWcIiAtyy5pSjDbavzC/R12FO2uonKGuWNYfRDPUUnABkpSnObZGu6dxl+n1TznC/jBdWFACKJHaaxfqEiXdUkgXy3LUvUqSjhuYrYQAoL6hVlzkSEJs4AQkvybTeUCMRhCBO6cfheYDQuJnFFft8REdT6d5fyx4a1";
    //    private static final String VUFORIA_KEY = "AbCbPUz/////AAABmRlaEryonEVfsxxT+iHrRnIO+B0SFb6vzFX7lYpj3WD2pSxJG1pAEJeUJR3XWKQqKUbO8KUhq/4mnx2uvCcUM1Rg5/3f+qR0VytJNlyYBXAL9kvbpHVbHI/qjQziYKQ0/1SlKj4KX9nHDmPImH8Vd9vfXauFXJ8bnVE175BVln5MS6bYiK4vvxecGyrIvXpjojrYoHdynFVWcIiAtyy5pSjDbavzC/R12FO2uonKGuWNYfRDPUUnABkpSnObZGu6dxl+n1TznC/jBdWFACKJHaaxfqEiXdUkgXy3LUvUqSjhuYrYQAoL6hVlzkSEJs4AQkvybTeUCMRhCBO6cfheYDQuJnFFft8REdT6d5fyx4a1";
    private RobotState myRobotState = RobotState.TARGET_SKYSTONE;

    private VuforiaLocalizer Vuforia = null;

    private TFObjectDetector myTfod = null;

    DcMotor topleftmotor = null;
    DcMotor toprightmotor = null;
    DcMotor bottomleftmotor = null;
    DcMotor bottomrightmotor = null;

    @Override
    public void runOpMode() {
        initrobot();

        //wait for start
        telemetry.addData(">", "press play");
        telemetry.update();
        waitForStart();
        telemetry.addData("asdhfl", "askdfadsfk");
        telemetry.update();
        while (opModeIsActive()) {

            switch (myRobotState) {
                case TARGET_SKYSTONE:
                    targetSkystone();
                    break;
                case MOVE_SKYSTONE:
                    movetoSkystone();
                    break;
                case DONE:
                    shutdown();
                    break;
                case TEST:
                    moveFor (200, 200, 200, 200);
                    myRobotState = RobotState.DONE;
                case ERROR:
                    myRobotState = RobotState.DONE;
                    break;
                default: {
                    telemetry.addData("Error", "error");
                    myRobotState = RobotState.ERROR;
                }
            }
        }
    }

    private void initrobot() {
        //init vuforia
        //  int tfodMonitorViewId1 = hardwareMap.appContext.getResources().getIdentifier("tforMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        initVuforia();


        //init tensorflow
        if (myRobotState != RobotState.ERROR) {
            if (ClassFactory.getInstance().canCreateTFObjectDetector())
            {
                initTfod();
            }
            else
            {
                telemetry.addData("Sorry!", "This device is not compatible with TFOD");
                telemetry.update();
            }
            if (myTfod != null) {
                myTfod.activate();
            }
            else {
                telemetry.addData("ERROR", "tensorflow no activate");
                //    myRobotState = RobotState.ERROR;
            }


        }//init motors
        if (myRobotState != RobotState.ERROR) {
            initMotors();
        }

        telemetry.addData("Status", "Initialized but with no motors"); //change later
    }
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;
        telemetry.addData("1", "1");

        //  Instantiate the Vuforia engine
        Vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodparameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodparameters.minimumConfidence = 0.8;
        myTfod = ClassFactory.getInstance().createTFObjectDetector(tfodparameters, Vuforia);
        myTfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        telemetry.addData(">", tfodMonitorViewId);
    }
    private void initMotors() {
        topleftmotor = hardwareMap.dcMotor.get("topleft");
        toprightmotor = hardwareMap.dcMotor.get("topright");
        bottomleftmotor = hardwareMap.dcMotor.get("bottomleft");
        bottomrightmotor = hardwareMap.dcMotor.get("bottomright");
        topleftmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        toprightmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bottomleftmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bottomrightmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        topleftmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        toprightmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bottomrightmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bottomleftmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        topleftmotor.setPower(0);
        toprightmotor.setPower(0);
        bottomrightmotor.setPower(0);
        bottomleftmotor.setPower(0);
    }

    private void targetSkystone() {
        //telemetry.addData("State", "Target skystone");
        //telemetry.update();
        Recognition skystoneBlock = null;

        //return without changing state if there is no new info
        List<Recognition> updatedRecognitions = myTfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("State:", "Targeting Skystone");
            for (Recognition recognition : updatedRecognitions) {
                if (recognition.getLabel().equals(LABEL_SECOND_ELEMENT)) {
                    skystoneBlock = recognition;
                    break;
                }
            }
            //found a skystone
            if (skystoneBlock != null) {
                int skystoneBlockLeftX = (int) skystoneBlock.getLeft();
                int skystoneBlockRightX = (int) skystoneBlock.getRight();
                int skystoneBlockTop = (int) skystoneBlock.getTop();
                int skystoneBlockBottom = (int) skystoneBlock.getBottom();
                int detectionWidth = (int) skystoneBlock.getHeight();
//              telemetry.addData("detectionWidth or height", detectionWidth);
//              telemetry.addData("LeftX",  skystoneBlockLeftX);
//              telemetry.addData("RightX", skystoneBlockRightX);
//              telemetry.addData("top", skystoneBlockTop);
//              telemetry.addData("bottom", skystoneBlockBottom);

                if (detectionWidth < 870 ) { //change later maybe
                    telemetry.addData("Position", "left or right");
                    if ((skystoneBlockTop+skystoneBlockBottom) < 1200 ) {
                        telemetry.addData(">", "at left");
                    }
                    if( (skystoneBlockTop + skystoneBlockBottom) > 1200) {
                        telemetry.addData(">", "at right");
                    }
                }else {
                    telemetry.addData("position", "at mid");
                }
                telemetry.update();
            }else {
                telemetry.addData("status","no skystone found");
                telemetry.update();
            }
        }//else {
        //    idle();
        //}
    }
    private void movetoSkystone() {
        telemetry.addData("state","movingtoskystone");
        telemetry.update();
        moveFor(CLICKS_TO_TARGET, CLICKS_TO_TARGET, CLICKS_TO_TARGET, CLICKS_TO_TARGET);
        myRobotState = RobotState.DONE;
    }
    private void moveFor (int topleftCount, int toprightCount, int bottomleftCount, int bottomrightCount) {
        topleftmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        toprightmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bottomleftmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bottomrightmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        topleftmotor.setTargetPosition(topleftCount);
        bottomrightmotor.setTargetPosition(bottomrightCount);
        toprightmotor.setTargetPosition(toprightCount);
        bottomleftmotor.setTargetPosition(bottomleftCount);
        topleftmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        toprightmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bottomleftmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bottomrightmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        topleftmotor.setPower(LOW_POWER);
        toprightmotor.setPower(LOW_POWER);
        bottomleftmotor.setPower(LOW_POWER);
        bottomrightmotor.setPower(LOW_POWER);
        while (opModeIsActive() && (topleftmotor.isBusy() || toprightmotor.isBusy() || bottomleftmotor.isBusy() || bottomrightmotor.isBusy())) {
            idle();
        }


    }
    private void moveSkystoneLeft() {
        moveFor( 1, 1, 1, 1); //change later for turning
        moveFor( 1, 1, 1, 1); //change later for moving skystone
    }
    private void moveSkystoneright() {
        moveFor( 1, 1, 1 ,1); //change later for turning
        moveFor( 1, 1, 1, 1); //change later for moving skystone
    }
    private void moveSkystoneCenter() {
        moveFor( 1, 1, 1 ,1); //change later for moving to skystone
    }
    private void shutdown() {


        telemetry.addData("state", "done");
        toprightmotor.setPower(0);
        topleftmotor.setPower(0);
        bottomleftmotor.setPower(0);
        bottomrightmotor.setPower(0);
        if (myTfod != null) {
            myTfod.shutdown();
        }
        telemetry.update();
    }
}

