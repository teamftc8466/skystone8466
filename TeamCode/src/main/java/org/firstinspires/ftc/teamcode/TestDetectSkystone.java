package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TestDetectSkystone", group = "FS")

public class TestDetectSkystone extends RobotHardware {
    public boolean isRedteam = false;
    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start test color program");
        telemetry.update();

        waitForStart();

        initializeWhenStart();

        int [] cnt={0, 0, 0};
        while (opModeIsActive() ) {
            int pos=getDetectSkystone().detectSkystone(isRedteam, true);
            if (pos >= 0) cnt[pos]+=1;

            sleep(200);
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
