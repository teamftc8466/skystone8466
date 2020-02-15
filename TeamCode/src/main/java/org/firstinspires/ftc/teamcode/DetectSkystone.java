/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;


import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

/**
 * This 2019-2020 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Skystone game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */

public class DetectSkystone {
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "AbCbPUz/////AAABmRlaEryonEVfsxxT+iHrRnIO+B0SFb6vzFX7lYpj3WD2pSxJG1pAEJeUJR3XWKQqKUbO8KUhq/4mnx2uvCcUM1Rg5/3f+qR0VytJNlyYBXAL9kvbpHVbHI/qjQziYKQ0/1SlKj4KX9nHDmPImH8Vd9vfXauFXJ8bnVE175BVln5MS6bYiK4vvxecGyrIvXpjojrYoHdynFVWcIiAtyy5pSjDbavzC/R12FO2uonKGuWNYfRDPUUnABkpSnObZGu6dxl+n1TznC/jBdWFACKJHaaxfqEiXdUkgXy3LUvUqSjhuYrYQAoL6hVlzkSEJs4AQkvybTeUCMRhCBO6cfheYDQuJnFFft8REdT6d5fyx4a1";
    private WebcamName webcamName_ = null;
    private int tfodMonitorViewId_ = 0;
    private Telemetry telemetry_ = null;

    /**
     * {@link #vuforia_} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia_ = null;

    /**
     * {@link #tfod_} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod_ = null;

    public DetectSkystone(WebcamName webcam_name,
                          int tfod_monitor_view_id,
                          Telemetry telemetry) {
        webcamName_=webcam_name;
        tfodMonitorViewId_=tfod_monitor_view_id;
        telemetry_=telemetry;
    }

    public boolean setupTfod() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry_.addData("Sorry!", "This device is not compatible with TFOD");
            telemetry_.update();
            return false;
        }

        if (tfod_ == null) return false;

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        tfod_.activate();
        return true;
    }

    public void shutdownTfod() {
        if (tfod_ != null) {
            tfod_.shutdown();
            tfod_=null;
        }
    }

    public boolean existTfod() {
        return (tfod_ != null);
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;

        if (webcamName_ != null) parameters.cameraName = webcamName_;
        else parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia_ = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId_);
        tfodParameters.minimumConfidence = 0.4;
        tfod_ = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia_);
        tfod_.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    public int detectSkystone(boolean is_red_team,
                              boolean show_stone_info) {
        if (tfod_ == null) return -1;

        int skystonePosition = -1;
        int skystoneleftorright = 0;
        Recognition skystoneBlock = null;

        // getUpdatedRecognitions() will return null if no new information is available since
        // the last time that call was made.
        List<Recognition> updatedRecognitions = tfod_.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            if (show_stone_info == true) {
                telemetry_.addData("# Object Detected", updatedRecognitions.size());
            }

            // step through the list of recognitions and display boundary info.
            int i = 0;
            for (Recognition recognition : updatedRecognitions) {
                if (recognition.getLabel().equals(LABEL_SECOND_ELEMENT)) {
                    skystoneBlock = recognition;
                    break;
                }
            }

            if (skystoneBlock != null) {
                if (skystoneBlock.getLeft() < 40) {
                    if (skystoneBlock.getRight() < 450) {
                        skystonePosition = 0;
                        if (is_red_team) {
                            skystonePosition = 2;
                        }
                        skystoneleftorright = 1;
                    }
                }

                if (skystoneBlock.getLeft() > 150 && skystoneBlock.getRight() > 500) {
                    skystonePosition = 2;
                    if (is_red_team) {
                        skystonePosition = 0;
                    }
                    skystoneleftorright = 1;
                }

                if (skystoneleftorright != 1) {
                    skystonePosition = 1; //add more accurate things later
                }
            }
        }

        if (show_stone_info == true) {
            if (skystoneBlock != null) {
                showStoneInfo(skystoneBlock, false);
                telemetry_.addData(">", skystonePosition);
            } else {
                telemetry_.addData("Not detected", -1);
            }

            telemetry_.update();
        }
        
        return skystonePosition;

    }

    void showStoneInfo(Recognition stone_block,
                       boolean update_flag) {
        telemetry_.addData(String.format("Label"), stone_block.getLabel());
        telemetry_.addData(String.format("  <Left, Right>"), "%.03f , %.03f",
                stone_block.getLeft(), stone_block.getRight());
        telemetry_.addData(String.format("  <Top, Bottom>"), "%.03f , %.03f",
                stone_block.getTop(), stone_block.getBottom());

        if (update_flag == true) telemetry_.update();
    }
}
