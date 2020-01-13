/* Created by Melinda, last edited on 1/16/2020 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="AutonomousCommon", group="FS")
@Disabled
public class AutonomousCommon extends RobotHardware {
    AutoOperation [] opList_ = null;    // Lists all operations to be done in an autonomous
    int numOpsInList_ = 0;              // Array size of opList_, will change based on invdividual autonomous programs' array sizes
    int currOpIdInList_ = -1;           // Index (aka the id) of current opcode in opList_; the index of -1 indicates that the opList_ is still non-existent
    double currOpStartTime_ = 0.0;      // Start time to run current opcode

    boolean isRedTeam_ = true;          // This variale is for turning direction purposes
    int firstSkystonePos_ = -1;         // Skystone position is determined by TensorFlow. The -1 represents that the position of the Skystone is unknown because, as explained below, the detected Skystone can only be in positions 0, 1, or 2.
                                        // If the TensorFlow program returns a 0, the Skystone is the first farthest from the wall that the blocks are aligned against at a right angle
                                        // If the TensorFlow program returns a 1, the Skystone is the second farthest from the said wall.
                                        // If the TensorFlow program returns a 2, the Skystone is the third farthest from the said wall.

    /// A 3X3 array to contain the driving distance to reach the first skystone and deliver it to the foundation.
    /// It contents is depedent on red or blue team and will be defined in extended classes:
    ///   - AutonomousRedLoading
    ///   - AutomousBlueLoading
    ///
    /// Each row is associated with skystone position:
    ///   - Row 0 : skystone is the first farthest from the wall.
    ///   - Row 1 : skystone is the second farthest from the wall.
    ///   - Row 2 : skystone is the third farthest from the wall.
    ///
    //  At each row:
    ///   - First parameter: Distance to shift in order to align with the Skystone
    ///   - Second parameter: Distance forward to Skystone
    ///   - Third parameter: Distance to foundation after collecting Skystone (the turning angle towards the foundation is constant for all three positions of the Skystones, so we do not need a fourth parameter)
    double [][] grabFirstSkystone_ = null;

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
        getDetectSkystone().setupTfod();

        final int max_try_times_to_detect_skystone = 10;
        for(int i=0; i<max_try_times_to_detect_skystone; ++i) {
            int det_pos=getDetectSkystone().detectSkystone();
            if (det_pos >= 0) {
                firstSkystonePos_ = det_pos;
                break;
            }
        }

        // If the returned skystone position is not a valid number (0, 1, or 2), then assume that the Skystone is at the zero position
        if (firstSkystonePos_ < 0 || firstSkystonePos_ > 2) {
            firstSkystonePos_ = 0;
        }
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;

        currOpIdInList_ = -1;
        currOpStartTime_ = 0.0;

        driveTrain_.resetEncoder(0);
    }

    void cleanUpAtEndOfRun() {
        // TODO

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
                finish_flag = moveHooks(operand, getCurrentOperand(1), getCurrentOperand(2));
                break;
            case OP_DRIVE_TO_FIRST_SKYSTONE:
                finish_flag = driveToFirstSkystone();
                break;
            case OP_GRAB_FIRST_SKYSTONE:
                finish_flag = grabFirstSkystone();
                break;
            case OP_DRIVE_FROM_FIRST_SKYSTONE_TO_FOUNDATION:
                finish_flag = driveFromFirstSkystoneToFoundation();
                break;
            case OP_DROP_SKYSTONE_TO_FOUNDATION:
                finish_flag = dropSkystoneToFoundation();
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
        runDriveTrainResetEncoder(0.15); // Must allow 0.15 seconds for the encoders to reset to ensure that the encoders actually finish resetting before moving onto the next opMode

        return true;
    }

    boolean runDriveTrainResetEncoder(double time_limit){
        driveTrain_.resetEncoder(time);

        if (time_limit > 0) {
            sleep((long) (time_limit * 1000));
        }

        if (driveTrain_.allEncodersAreReset() == false) {
            telemetry.addLine("Reset encoder failed");
            telemetry.update();
        }

        return true;
    }

    boolean driveToFirstSkystone(){

        // Retrieve the value in the grabFirstSkystone_ two dimensional array in row firstSkystonePos_
        // (which can be 0, 1, or 2) and column 0 (aka the first column). This value will be the distance the robot shifts.
        double shift_distance_to_align_with_skystone = grabFirstSkystone_[firstSkystonePos_][0];

        if (shift_distance_to_align_with_skystone < 0) {
            runDriveTrainTillFinish(DriveTrainMode.SHIFT_LEFT, -shift_distance_to_align_with_skystone); // Negate the negative value because shifting distance cannot be negative
        } else if (shift_distance_to_align_with_skystone > 0) {
            runDriveTrainTillFinish(DriveTrainMode.SHIFT_RIGHT, shift_distance_to_align_with_skystone);
        }

        // Retrieve the value in the grabFirstSkystone_ two dimensional array in row firstSkystonePos_
        // (which can be 0, 1, or 2) and column 1 (aka the second column). This value will be the distance the robot drives forwards.
        double drive_forward_to_skystone = grabFirstSkystone_[firstSkystonePos_][1];

        runDriveTrainTillFinish(DriveTrainMode.FORWARD, drive_forward_to_skystone);

        return true;
    }

    boolean driveFromFirstSkystoneToFoundation(){
        /// Need to move back before making a turn to avoid colliding with stones
        // driveTrain_.driveByMode(DriveTrainMode.BACKWARD, 0.2, timer_.time());

        /* Turn away from skystone towards foundation */

        if (isRedTeam_ == true) {
            runDriveTrainTillFinish(DriveTrainMode.TURN_RIGHT, 90);
        } else {
            runDriveTrainTillFinish(DriveTrainMode.TURN_LEFT, 90);
        }

        /* Drive from Skystone area to foundation area */

        // Retrieve the value in the grabFirstSkystone_ two dimensional array in row firstSkystonePos_
        // (which can be 0, 1, or 2) and column 2 (aka the third column). This value will be the distance the robot drives forwards.
        double distance_from_skystone_to_foundation = grabFirstSkystone_[firstSkystonePos_][2];

        runDriveTrainTillFinish(DriveTrainMode.FORWARD, distance_from_skystone_to_foundation);

        /* Turn towards the foundation to prep for lowering the hooks */

        if (isRedTeam_ == true) {
            runDriveTrainTillFinish(DriveTrainMode.TURN_LEFT, 90);
        } else {
            runDriveTrainTillFinish(DriveTrainMode.TURN_RIGHT, 90);
        }

        /* Drive to foundation */
        runDriveTrainTillFinish(DriveTrainMode.FORWARD, 0.25);

        return true;
    }

    private void runDriveTrainTillFinish(DriveTrainMode drive_mode,
                                         double drive_parameter) {
        while (driveTrain_.driveByMode(drive_mode, drive_parameter, timer_.time()) == false) {
            // Run till finish
        }

        // Must allow 0.1 seconds for the encoders to reset to ensure that the encoders actually finish resetting before moving onto the next opMode
        runDriveTrainResetEncoder(0.1);
    }

    boolean runDriveTrain(DriveTrainMode drive_mode,
                          double drive_parameter){
        return driveTrain_.driveByMode(drive_mode, drive_parameter, currTime_);
    }

    boolean runDriveTrainWait(double time_limit){
        driveTrain_.driveByMode(DriveTrainMode.STOP, 0, timer_.time());

        if (time_limit > 0) {
            final long sleep_time_in_ms = (long)(time_limit *1000.0);  // long type is required because the parameter for the sleep function is type long
            sleep(sleep_time_in_ms);
        }

        return true;
    }

    boolean moveHooks(double left_hook_position_in_degree,
                   double right_hook_position_in_degree,
                   double waiting_time) {                     // Waiting time allows the servo to complete the instruction before the program moves to the next state
        leftHookServo_.setServoModePositionInDegree(left_hook_position_in_degree, false);
        rightHookServo_.setServoModePositionInDegree(right_hook_position_in_degree, false);

        if (waiting_time > 0) {
            sleep((long)(waiting_time * 1000));    // Declaration of the sleep function states that type long should be inputted
        }

        return true;
    }


    // TODO: Write this subsystem when the grabber hardware is finished
    boolean grabFirstSkystone() {
        sleep(3000);
        return true;
    }

    // TODO: Write this subsystem when the grabber hardware is finished
    boolean dropSkystoneToFoundation() {
        sleep(1000);
        return true;
    }
}
