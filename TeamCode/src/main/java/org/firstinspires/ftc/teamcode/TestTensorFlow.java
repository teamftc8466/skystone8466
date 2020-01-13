package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TestTensorFlow", group = "FS")
// @Disabled
public class TestTensorFlow extends RobotHardware {

    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start test color program");
        telemetry.update();

        waitForStart();

        initializeWhenStart();

        int max_call_times=10;
        int num_calls=0;
        int [] cnt={0, 0, 0};
        while (opModeIsActive() ) {
            int pos=getDetectSkystone().detectSkystone();
            if (pos >= 0) cnt[pos]+=1;
            num_calls++;
            if (num_calls >= max_call_times) break;

            sleep(3000);
            telemetry.addData("=", toString().valueOf(cnt[0]) + " " + toString().valueOf(cnt[1]) + " " + toString().valueOf(cnt[2]));
            telemetry.update();
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        createDetectSkystone();

        getDetectSkystone().setupTfod();
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        // TBD
    }
}
