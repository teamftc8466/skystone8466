package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="TestHooks", group="FS")
@Disabled
public class TestHooks extends RobotHardware {

    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start test GoBuilda servo program");
        telemetry.update();

        waitForStart();

        initializeWhenStart();

        hooks_.showPosition(true);

        sleep(3000);

        for (int i=0; i<3; ++i) {
            hooks_.moveHooksToPosition(Hooks.Position.PULL);
            hooks_.showPosition(true);

            sleep(3000);

            hooks_.moveHooksToPosition(Hooks.Position.RELEASE);
            hooks_.showPosition(true);

            sleep(3000);
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        createHooks(Hooks.Position.INIT);
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        // TBD
    }
}
