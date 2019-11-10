package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="AutonomousCommon", group="FS")
@Disabled
public class AutonomousCommon extends RobotHardware {

    //Possible autonomous opcodes
    static final int OP_STOP = 0;
    static final int OP_DRIVE_TRAIN_RESET_ENCODER = 1;
    static final int OP_DRIVE_TRAIN_RESET_HEADING = 2;
    static final int OP_DRIVE_TRAIN_SHIFT_GEAR = 3;
    static final int OP_DRIVE_TRAIN_FORWARD = 4;
    static final int OP_DRIVE_TRAIN_BACKWARD = 5;
    static final int OP_DRIVE_TRAIN_TURN_LEFT = 6;
    static final int OP_DRIVE_TRAIN_TURN_RIGHT = 7;
    static final int OP_DRIVE_TRAIN_SHIFT_LEFT = 8;
    static final int OP_DRIVE_TRAIN_SHIFT_RIGHT = 9;

    AutoOperation [] opList_ = null;    // Lists all operations to be done in an autonomous
    int numOpsInList_ = 0;               // Array size of opList_, will change based on invdividual autonomous programs' array sizes
    int currOpIdInList_ = -1;            // Index (aka the id) of current opcode in opList_; the index of -1 indicates that the opList_ is still non-existent
    double currOpStartTime_ = 0.0;      // Start time to run current opcode

    @Override
    public void runOpMode() {
        initialize();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start autonomous");
        telemetry.update();

        waitForStart();

        initializeWhenStart();

        while (opModeIsActive()) {
            currTime_ = timer_.time();

            runCurrentOperation();
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        if (opList_ != null) numOpsInList_ = opList_.length; //the length function is already in the library

        super.initializeAutonomous();

        // Activate Tfod for detecting skystone
        // getDetectSkystone().setupTfod();
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;

        currOpIdInList_ = -1;
        currOpStartTime_ = 0.0;
    }

    void cleanUpAtEndOfRun() {
        // TBD

        // getDetectSkystone().shutdownTfod();
    }

    int getCurrentOpcode() {
        if (numOpsInList_ == 0) return OP_STOP; //If the opList_ is empty, then the robot must stop

        if (currOpIdInList_ < 0) {
            currOpIdInList_ = -1; // If the current index is less than zero, set the index to -1 in order to restart from the beginning of the array
            if (moveToNextOpcode() == false) return OP_STOP; //
        }

        return opList_[currOpIdInList_].opcode();
    }

    double getCurrentOperand(){
        if (currOpIdInList_ >= 0 && currOpIdInList_ < numOpsInList_) {
            return opList_[currOpIdInList_].operand();
        }

        return 0;
    }

    void runCurrentOperation() {

    }

    boolean moveToNextOpcode() {
        currOpStartTime_ = timer_.time();
        if (numOpsInList_ == 0) return false;           // If opList_ is empty, return false

        if (currOpIdInList_ < 0) currOpIdInList_ = 0;   // Begin from opList's first instruction
        else ++currOpIdInList_;                         // Move to the next instruction

        if (currOpIdInList_ >= numOpsInList_) {         // If the current index reaches the limit of the array, keep the index at the limit and return false
            currOpIdInList_ = numOpsInList_;
            return false;
        }

        return true;
    }
}
