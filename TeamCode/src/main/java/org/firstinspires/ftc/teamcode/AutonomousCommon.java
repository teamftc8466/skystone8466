package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="AutonomousCommon", group="FS")
@Disabled
public class AutonomousCommon extends RobotHardware {
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

        driveTrain_.resetEncoder(0);
    }

    void cleanUpAtEndOfRun() {
        // TBD

        // getDetectSkystone().shutdownTfod();
    }

    AutoOperation.OpCode getCurrentOpcode() {
        if (numOpsInList_ == 0) return AutoOperation.OpCode.OP_STOP; //If the opList_ is empty, then the robot must stop

        if (currOpIdInList_ < 0) {
            currOpIdInList_ = -1; // If the current index is less than zero, set the index to -1 in order to restart from the beginning of the array
            if (moveToNextOpcode() == false) return AutoOperation.OpCode.OP_STOP; //
        } else if (currOpIdInList_ >= numOpsInList_) {
            return AutoOperation.OpCode.OP_STOP;
        }

        return opList_[currOpIdInList_].opcode();
    }

    double getCurrentOperand(int index){
        if (currOpIdInList_ >= 0 && currOpIdInList_ < numOpsInList_) {
            return opList_[currOpIdInList_].operand(index);
        }

        return 0;
    }

    void runCurrentOperation() {
        AutoOperation.OpCode opcode = getCurrentOpcode();
        double operand = getCurrentOperand(0);

        boolean finish_flag = false;
        switch (opcode) {
            case OP_DRIVE_TRAIN_RESET_ENCODER:
                finish_flag = runDriveTrainResetEncoder(operand);
                break;
            case OP_WAIT:
                finish_flag = runDriveTrainWait(operand);
                break;
            case OP_DRIVE_TRAIN_FORWARD:
                finish_flag = runDriveTrain(DriveTrainMode.FORWARD, operand);
                break;
            case OP_DRIVE_TRAIN_BACKWARD:
                finish_flag = runDriveTrain(DriveTrainMode.BACKWARD, operand);
                break;
            case OP_DRIVE_TRAIN_TURN_LEFT:
                finish_flag = runDriveTrain(DriveTrainMode.TURN_LEFT, operand);
                break;
            case OP_DRIVE_TRAIN_TURN_RIGHT:
                finish_flag = runDriveTrain(DriveTrainMode.TURN_RIGHT, operand);
                break;
            case OP_DRIVE_TRAIN_SHIFT_LEFT:
                finish_flag = runDriveTrain(DriveTrainMode.SHIFT_LEFT, operand);
                break;
            case OP_DRIVE_TRAIN_SHIFT_RIGHT:
                finish_flag = runDriveTrain(DriveTrainMode.SHIFT_RIGHT, operand);
                break;
            case OP_DRIVE_TRAIN_SHIFT_GEAR:
                driveTrain_.setPowerFactor(operand);
                finish_flag = true;
                break;
            case OP_MOVE_HOOK:
                moveHooks(operand, getCurrentOperand(1), getCurrentOperand(2));
                finish_flag = true;
                break;
            default:
                finish_flag = true;
        }

        if (finish_flag == true) {
            moveToNextOpcode();
        }
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

        AutoOperation.OpCode opcode = getCurrentOpcode();

        // Automatically reset encoders when a new driveTrain operation begins
        switch (opcode){
            case OP_DRIVE_TRAIN_FORWARD:
            case OP_DRIVE_TRAIN_BACKWARD:
            case OP_DRIVE_TRAIN_TURN_LEFT:
            case OP_DRIVE_TRAIN_TURN_RIGHT:
            case OP_DRIVE_TRAIN_SHIFT_LEFT:
            case OP_DRIVE_TRAIN_SHIFT_RIGHT:
                driveTrain_.resetEncoder(currTime_);
                break;
            default:
                break;
        }

        return true;
    }

    boolean runDriveTrainResetEncoder(double time_limit){
        do{
            double time=timer_.time();
            driveTrain_.resetEncoder(time);

            if (driveTrain_.allEncodersAreReset() == true) return true;
        } while (time <= currOpStartTime_+time_limit);

        telemetry.addLine("Reset encoder failed");
        telemetry.update();

        return true;
    }

    boolean runDriveTrain(DriveTrainMode drive_mode,
                          double drive_parameter){
        return driveTrain_.driveByMode(drive_mode, drive_parameter, currTime_);
    }

    boolean runDriveTrainWait(double time_limit){
        driveTrain_.driveByMode(DriveTrainMode.STOP, 0, timer_.time());

        if (time_limit > 0) {
            final long sleep_time_in_ms= (long)(time_limit *1000.0);
            sleep(sleep_time_in_ms);
        }

        return true;
    }

    void moveHooks(double left_hook_position_in_degree,
                   double right_hook_position_in_degree,
                   double waiting_time) {                     // Waiting time allows the servo to complete the instruction before the program moves to the next state? TODO: Wouldn't the program wait for the servos to reach their positions before moving to the next state anyways??
        leftHookServo_.setServoModePositionInDegree(left_hook_position_in_degree, false);
        rightHookServo_.setServoModePositionInDegree(right_hook_position_in_degree, false);

        if (waiting_time > 0) {
            do {
                double time = timer_.time();
            } while (time <= currOpStartTime_ + waiting_time);
        }
    }
}
