package org.firstinspires.ftc.teamcode.ExperimentProgram;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "michaelTensorflowtest", group = "Concept")
@Disabled
public class michaelTensorflowTest extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;
    private static final int POINTING_TOLERANCE = 60;

    private static final double LOW_POWER = 0.15;
    private static final double MID_POWER = 0.25;
    private static final int CLICKS_TO_TARGET = 500; //change later
    private static final int COUNTS_PER_ROTATION = 670; //change this later

    public enum RobotState {
        TARGET_SKYSTONE,
        MOVE_SKYSTONE,
        DONE,
        TEST,
        ERROR,
    }

    private static final String VUFORIA_KEY = "AbCbPUz/////AAABmRlaEryonEVfsxxT+iHrRnIO+B0SFb6vzFX7lYpj3WD2pSxJG1pAEJeUJR3XWKQqKUbO8KUhq/4mnx2uvCcUM1Rg5/3f+qR0VytJNlyYBXAL9kvbpHVbHI/qjQziYKQ0/1SlKj4KX9nHDmPImH8Vd9vfXauFXJ8bnVE175BVln5MS6bYiK4vvxecGyrIvXpjojrYoHdynFVWcIiAtyy5pSjDbavzC/R12FO2uonKGuWNYfRDPUUnABkpSnObZGu6dxl+n1TznC/jBdWFACKJHaaxfqEiXdUkgXy3LUvUqSjhuYrYQAoL6hVlzkSEJs4AQkvybTeUCMRhCBO6cfheYDQuJnFFft8REdT6d5fyx4a1";
    private RobotState myRobotState = RobotState.TARGET_SKYSTONE;

    private VuforiaLocalizer myVuforia = null;

    private TFObjectDetector myTfod = null;

    DcMotor topleftmotor = null;
    DcMotor toprightmotor = null;
    DcMotor bottomleftmotor = null;
    DcMotor bottomrightmotor = null;

    public void runOpMode() {
        initrobot();

        //wait for start
        telemetry.addData(">", "press play");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            switch (myRobotState) {
                case TARGET_SKYSTONE:
                    targetSkystone();
            }
        }
    }

    private void initrobot() {
        //init vuforia
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        myVuforia = ClassFactory.getInstance().createVuforia(parameters);

        if (myVuforia == null) {
            myRobotState = RobotState.ERROR;
            telemetry.addData("ERROR", "Vuforia engine no initialize");
        }

        //init tensorflow
        if (myRobotState != RobotState.ERROR) {
            if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
                int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tforMonitorViewId", "id", hardwareMap.appContext.getPackageName());

                TFObjectDetector.Parameters tfodparameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
                myTfod = ClassFactory.getInstance().createTFObjectDetector(tfodparameters, myVuforia);
                myTfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
            }

            if (myTfod != null) {
                myTfod.activate();
            } else {
                telemetry.addData("ERROR", "tensorflow no activate");
                myRobotState = RobotState.ERROR;
            }
            //init motors
            if (myRobotState != RobotState.ERROR) {
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
            telemetry.addData("Status", "Initialized");
        }

    }


    private void targetSkystone() {
        Recognition skystoneBlock = null;

        //return without changing state if there is no new info
        List<Recognition> updatedRecognitions = myTfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("State:", "Target Skystone");
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
                int skystoneBlockCenterX = (skystoneBlockLeftX+skystoneBlockRightX)/2;
                int error = skystoneBlockCenterX - SCREEN_WIDTH/2;

                if (Math.abs(error) < POINTING_TOLERANCE) {
                    myRobotState = RobotState.MOVE_SKYSTONE;
                } else {
                    telemetry.addData("Action", "Turn"+error);
                    telemetry.update();
                    int turnclicks = error /8;
                    // moveFor(turn_clicks, -)
                }
            }
        }
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
}


