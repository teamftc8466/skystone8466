/* Created by Melinda, last edited on 1/16/2020 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="AutonomousCommon", group="FS")
@Disabled
public class AutonomousCommon extends RobotHardware {
    final double MAX_WAIT_TIME_FOR_CLOSE_GRABBER_CLAMP = 0.5;

    boolean enableShowDriveTrainInfo_ = false;
    boolean enableShowLiftInfo_ = false;
    boolean enableShowGrabberInfo_ = false;

    boolean useImu_ = false;            // If true, use IMU to correct heading when executing the operation OP_DRIVE_TRAIN_CORRECT_HEADING
    boolean controlTurnDegreeByEncoderCnt_ = true;
    boolean autoCorrectHeadingDuringDriving_ = true;
    boolean useShiftToDeliverStoneToFoundation_ = false;
    boolean initLiftGrabberToCatchPosition_ = true;

    AutoOperation [] opList_ = null;    // Lists all operations to be done in an autonomous
    int numOpsInList_ = 0;              // Array size of opList_, will change based on invdividual autonomous programs' array sizes
    int currOpIdInList_ = -1;           // Index (aka the id) of current opcode in opList_; the index of -1 indicates that the opList_ is still non-existent
    double currOpStartTime_ = 0.0;      // Start time to run current opcode

    boolean isRedTeam_ = true;          // This variable is for turning direction purposes

    boolean useTensorFlowToDetectSkystone_ = true;
    int firstSkystonePos_ = -1;         // Skystone position is determined by TensorFlow. The -1 represents that the position of the Skystone is unknown because, as explained below, the detected Skystone can only be in positions 0, 1, or 2.
                                        // If the TensorFlow program returns a 0, the Skystone is the first farthest from the wall that the blocks are aligned against at a right angle
                                        // If the TensorFlow program returns a 1, the Skystone is the second farthest from the said wall.
                                        // If the TensorFlow program returns a 2, the Skystone is the third farthest from the said wall.

    /// A 3X3 array to contain the driving distance to reach the first skystone and deliver it to the foundation.
    /// It contents is dependent on red or blue team and will be defined in extended classes:
    ///   - AutonomousRedLoadingFull
    ///   - AutonomousBlueLoadingFull
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

        /** Detect first skystone position and wait for the game to begin */
        if (getDetectSkystone() != null) detectFirstSkystoneAndWaitForStart();

        initializeWhenStart();

        while (opModeIsActive()) {
            currTime_ = timer_.time();

            runCurrentOperation();

            if (lift_ != null) {
                lift_.holdAtTargetPosition(currTime_);
            }

            if (grabber_ != null) grabber_.holdCraneAtTargetPosition();
        }

        cleanUpAtEndOfRun();
    }

    public void initialize() {
        if (opList_ != null) numOpsInList_ = opList_.length; //the length function is already in the library

        super.initializeAutonomous();

        // Activate Tfod for detecting skystone
        if (useTensorFlowToDetectSkystone_ == true &&
                getDetectSkystone() != null) {
            getDetectSkystone().setupTfod();
        }

        if (getImu() == null) {
            useImu_ = false;
        }

        if (useImu_ == false || autoCorrectHeadingDuringDriving_ == false) {
            getDriveTrain().disableToUseImu();
        } else {
            getDriveTrain().enableToUseImu();
        }

        if (useImu_ == false || controlTurnDegreeByEncoderCnt_ == true) {
            getDriveTrain().enableControlTurnDegreeByEncoderCount();
        } else {
            getDriveTrain().disableControlTurnDegreeByEncoderCount();
        }

        if (grabber_ != null) {
            grabber_.enforceCraneDrawBackToEnd(0.4, timer_, 2.5);
        }
    }
    int[] arr = {0,0,0};
    int arraynumber = -1;
    public synchronized void detectFirstSkystoneAndWaitForStart() {
        while (!isStarted()) {
            synchronized (this) {
                try {
                    if (getDetectSkystone() !=null) {
                        if (getDetectSkystone().existTfod() == true) {
                            int det_pos = getDetectSkystone().detectSkystone(isRedTeam_, true);
                            if (det_pos >= 0) {
                                firstSkystonePos_ = det_pos;
                                arraynumber++;
                            }
                        }
                        telemetry.addData("Waiting for Start: ",
                                "First_Skystone_Position=" + String.valueOf(firstSkystonePos_) +
                                        " Tfod_Active=" + String.valueOf(getDetectSkystone().existTfod()));
                    } else {
                        telemetry.addData("Waiting for Start: ", "No Tfod");
                    }

                    telemetry.update();

                    this.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    void initializeWhenStart() {
        timer_.reset();
        currTime_ = 0.0;

        currOpIdInList_ = -1;
        currOpStartTime_ = 0.0;

        driveTrain_.resetEncoder(0);

        // If the returned skystone position is not a valid number (0, 1, or 2), then assume that the Skystone is at the zero position
        if (firstSkystonePos_ < 0 || firstSkystonePos_ > 2) {
            firstSkystonePos_ = 1;
        }

        // firstSkystonePos_ = 0;  // For test purpose

        // Move grabber and lift to grab stone ready position
        if (initLiftGrabberToCatchPosition_ == true) {
            moveLiftAndGrabberToCatchStoneReadyPosition();
        }

        if (enableShowDriveTrainInfo_ == true) {
            driveTrain_.enableShowDriveTrainInfo();  // Disable it after finish debugging
        } else if (enableShowGrabberInfo_ == true) {
            if (grabber_ != null) grabber_.enableShowGrabberInfo();
        } else if (enableShowLiftInfo_ == true) {
            if (lift_ != null) lift_.enableShowLiftInfo();
        }
    }

    void cleanUpAtEndOfRun() {
        if (getDetectSkystone() != null) getDetectSkystone().shutdownTfod();
    }

    boolean moveLiftAndGrabberToCatchStoneReadyPosition() {
        if (lift_ == null || grabber_ == null) return true;

        lift_.moveToPosition(Lift.Position.LIFT_GRAB_STONE_READY, timer_.time());
        grabber_.moveCraneToPosition(Grabber.CranePosition.CRANE_GRAB_STONE);
        grabber_.clampOpen();

        return true;
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
                finish_flag = runDriveTrain(DriveTrainMode.FORWARD, operand, getCurrentOperand(1));
                break;
            case OP_DRIVE_TRAIN_BACKWARD:
                finish_flag = runDriveTrain(DriveTrainMode.BACKWARD, operand, getCurrentOperand(1));
                break;
            case OP_DRIVE_TRAIN_TURN_LEFT:
                finish_flag = runDriveTrain(DriveTrainMode.TURN_LEFT, operand, getCurrentOperand(1));
                break;
            case OP_DRIVE_TRAIN_TURN_RIGHT:
                finish_flag = runDriveTrain(DriveTrainMode.TURN_RIGHT, operand, getCurrentOperand(1));
                break;
            case OP_DRIVE_TRAIN_SHIFT_LEFT:
                finish_flag = runDriveTrain(DriveTrainMode.SHIFT_LEFT, operand, getCurrentOperand(1));
                break;
            case OP_DRIVE_TRAIN_SHIFT_RIGHT:
                finish_flag = runDriveTrain(DriveTrainMode.SHIFT_RIGHT, operand, getCurrentOperand(1));
                break;
            case OP_DRIVE_TRAIN_CORRECT_HEADING:
                if (imu_ != null && useImu_ == true) {
                    finish_flag = driveTrain_.applyImuToControlTurningToTargetHeading(operand, getCurrentOperand(2), getCurrentOperand(1), timer_.time());
                } else {
                    finish_flag = true;
                }
                break;
            case OP_DRIVE_TRAIN_SHIFT_GEAR:
                driveTrain_.setPowerFactor(operand);
                finish_flag = true;
                break;
            case OP_MOVE_HOOK:
                finish_flag = moveHooks((int)operand, getCurrentOperand(1));
                break;
            case OP_DRIVE_TO_FIRST_SKYSTONE:
                finish_flag = driveToFirstSkystone();
                break;
            case OP_GRAB_STONE:
                finish_flag = grabStone(operand);
                break;
            case OP_DRIVE_FROM_FIRST_SKYSTONE_TO_FOUNDATION:
                finish_flag = driveFromFirstSkystoneToFoundation();
                break;
            case OP_DROP_SKYSTONE_TO_FOUNDATION:
                finish_flag = dropSkystoneToFoundation(operand);
                break;
            case OP_DROP_STONE_TO_GROUND:
                finish_flag = dropSkystoneToGround(operand);
                break;
            case OP_GRABBER_CRANE_FULL_DRAW_BACK:
                grabber_.moveCraneToPosition(Grabber.CranePosition.CRANE_DRAW_BACK_POSITION);
                finish_flag = true;
                break;
            case OP_LIFT_MOVE_TO_BOTTOM_POSITION:
                lift_.moveToPosition(Lift.Position.LIFT_BOTTOM_POSITION, timer_.time());
                finish_flag = true;
                break;
            case OP_GRAB_STONE_READY_POSITION:
                finish_flag = moveLiftAndGrabberToCatchStoneReadyPosition();
                break;
            case OP_DETECT_SKYSTONE:
                finish_flag = detectSkyStoneDuringAutonomous(operand);
                break;
            case OP_DRIVE_SKYSTONE_CLOSE_TO_WALL:
                finish_flag = driveToSkystoneCloseToWallAndPark();
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

        switch (opcode) {
            case OP_DRIVE_TRAIN_TURN_LEFT:
                if (getImu() != null) getImu().setTargetHeading(getImu().targetHeading() + getCurrentOperand(0));
                break;
            case OP_DRIVE_TRAIN_TURN_RIGHT:
                if (getImu() != null) getImu().setTargetHeading(getImu().targetHeading() - getCurrentOperand(0));
                break;
            default:
                break;
        }

        // Automatically reset encoders when a new driveTrain operation begins
        runDriveTrainResetEncoder(0.1); // Must allow 0.1 seconds for the encoders to reset to ensure that the encoders actually finish resetting before moving onto the next opMode

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
            // Negate the negative value because shifting distance cannot be negative
            runDriveTrainTillFinish(DriveTrainMode.SHIFT_LEFT,
                    -shift_distance_to_align_with_skystone,
                    true);
        } else if (shift_distance_to_align_with_skystone > 0) {
            runDriveTrainTillFinish(DriveTrainMode.SHIFT_RIGHT,
                    shift_distance_to_align_with_skystone,
                    true);
        }

        // Retrieve the value in the grabFirstSkystone_ two dimensional array in row firstSkystonePos_
        // (which can be 0, 1, or 2) and column 1 (aka the second column). This value will be the distance the robot drives forwards.
        double drive_forward_to_skystone = grabFirstSkystone_[firstSkystonePos_][1];

        runDriveTrainTillFinish(DriveTrainMode.FORWARD,
                drive_forward_to_skystone,
                true);

        return true;
    }

    boolean driveFromFirstSkystoneToFoundation() {
        final double saved_power_factor = driveTrain_.powerFactor();

        /// Need to move back before making a turn to avoid colliding with stones
        double move_back_from_stone_distance=0.1;
        if (useShiftToDeliverStoneToFoundation_ == true) move_back_from_stone_distance=0.15;
        runDriveTrainTillFinish(DriveTrainMode.BACKWARD,
                move_back_from_stone_distance,
                true);

        // Retrieve the value in the grabFirstSkystone_ two dimensional array in row firstSkystonePos_
        // (which can be 0, 1, or 2) and column 2 (aka the third column). This value will be the distance the robot drives forwards.
        final double distance_from_skystone_to_foundation = grabFirstSkystone_[firstSkystonePos_][2];

        if (useShiftToDeliverStoneToFoundation_ == true) {
            if (isRedTeam_ == true) {
                runDriveTrainTillFinish(DriveTrainMode.SHIFT_RIGHT,
                        distance_from_skystone_to_foundation,
                        true);
            } else {
                runDriveTrainTillFinish(DriveTrainMode.SHIFT_LEFT,
                        distance_from_skystone_to_foundation,
                        true);
            }

            // Wait lift move to drop position
            if (lift_ != null) {
                final double max_wait_time=0.3;
                lift_.moveToPosition(Lift.Position.LIFT_DROP_TO_FOUNDATION, timer_.time());
                currOpStartTime_ = timer_.time();
                do {
                    currTime_ = timer_.time();
                    if (currTime_  >= (currOpStartTime_ + max_wait_time)) break;

                    if (lift_ != null) lift_.holdAtTargetPosition(currTime_);
                    if (grabber_ != null) grabber_.holdCraneAtTargetPosition();
                } while (lift_.reachToTargetEncoderCount() == false);
            }
        } else {
            /* Turn away from skystone towards foundation */

            if (isRedTeam_ == true) {
                runDriveTrainTillFinish(DriveTrainMode.TURN_RIGHT,
                        90,
                        true);
            } else {
                runDriveTrainTillFinish(DriveTrainMode.TURN_LEFT,
                        90,
                        true);
            }

            /* Drive from Skystone area to foundation area */
            driveTrain_.setPowerFactor(1.35);    // Make long forward driving faster
            runDriveTrainTillFinish(DriveTrainMode.FORWARD,
                    distance_from_skystone_to_foundation,
                    true);
            if (saved_power_factor != driveTrain_.powerFactor()) {
                driveTrain_.setPowerFactor(saved_power_factor);
            }

            /* Turn towards the foundation to prep for lowering the hooks */

            lift_.moveToPosition(Lift.Position.LIFT_DROP_TO_FOUNDATION, timer_.time());

            if (isRedTeam_ == true) {
                runDriveTrainTillFinish(DriveTrainMode.TURN_LEFT,
                        90,
                        true);
            } else {
                runDriveTrainTillFinish(DriveTrainMode.TURN_RIGHT,
                        90,
                        true);
            }
        }

        grabber_.moveCraneToPosition(Grabber.CranePosition.CRANE_DROP_STONE);

        /* Drive to foundation (Numbers TBR) */
        final double move_forward_to_foundation_distance = 0.2;
        if (useShiftToDeliverStoneToFoundation_ == true) move_back_from_stone_distance=0.25;
        runDriveTrainTillFinish(DriveTrainMode.FORWARD,
                move_forward_to_foundation_distance,
                true);

        return true;
    }

    private void runDriveTrainTillFinish(DriveTrainMode drive_mode,
                                         double drive_parameter,
                                         boolean update_imu_target_heading_flag) {
        if (useImu_ == true &&
                update_imu_target_heading_flag == true) {
            switch (drive_mode) {
                case TURN_LEFT:
                    getImu().setTargetHeading(getImu().targetHeading() + drive_parameter);
                    break;
                case TURN_RIGHT:
                    getImu().setTargetHeading(getImu().targetHeading() - drive_parameter);
                    break;
                default:
                    break;
            }
        }

        // Run till finish
        currOpStartTime_ = timer_.time();
        while (driveTrain_.driveByMode(drive_mode, drive_parameter, 1.0, timer_.time()) == false) {
            currTime_ = timer_.time();
            if (lift_ != null) lift_.holdAtTargetPosition(currTime_);
            if (grabber_ != null) grabber_.holdCraneAtTargetPosition();
        }

        // Must allow 0.1 seconds for the encoders to reset to ensure that the encoders actually finish resetting before moving onto the next opMode
        runDriveTrainResetEncoder(0.1);
    }

    boolean runDriveTrain(DriveTrainMode drive_mode,
                          double drive_parameter,
                          double power_factor) {
        return driveTrain_.driveByMode(drive_mode, drive_parameter, power_factor, currTime_);
    }

    boolean runDriveTrainWait(double time_limit){
        driveTrain_.driveByMode(DriveTrainMode.STOP, 0, 0, timer_.time());

        if (time_limit > 0) {
            final long sleep_time_in_ms = (long)(time_limit *1000.0);  // long type is required because the parameter for the sleep function is type long
            sleep(sleep_time_in_ms);
        }

        return true;
    }

    boolean moveHooks(int position,
                      double waiting_time) {                     // Waiting time allows the servo to complete the instruction before the program moves to the next state
        hooks_.moveHooksToPosition(hooks_.convertToPosition(position));

        if (waiting_time > 0) {
            sleep((long)(waiting_time * 1000));    // Declaration of the sleep function states that type long should be inputted
        }

        return true;
    }

    boolean grabStone(double max_allowed_time) {
        if (lift_ == null || grabber_ == null) return true;

        if (lift_.isMoveToPositionApplied(Lift.Position.LIFT_GRAB_STONE_READY) == false) {
            lift_.moveToPosition(Lift.Position.LIFT_GRAB_STONE_READY, timer_.time());
        }

        if (grabber_.isCraneWithMoveToPositionApplied(Grabber.CranePosition.CRANE_GRAB_STONE) == false) {
            grabber_.moveCraneToPosition(Grabber.CranePosition.CRANE_GRAB_STONE);
        }

        if (grabber_.clampPosition() != Grabber.ClampPosition.CLAMP_OPEN) {
            grabber_.clampOpen();
            // sleep(200);   // Wait for opening clamp
        }

        if (max_allowed_time < 1) max_allowed_time = 1;

        if (lift_.reachToTargetEncoderCount() == false ||
               grabber_.craneReachToTargetEncoderCount() == false) {
            final double used_time = timer_.time() - currOpStartTime_;
            if (used_time < (max_allowed_time - 0.7)) return false;
        }

        lift_.moveToPosition(Lift.Position.LIFT_GRAB_STONE_CATCH, timer_.time());

        while (lift_.reachToTargetEncoderCount() == false) {
            currTime_ = timer_.time();
            grabber_.holdCraneAtTargetPosition();
            lift_.holdAtTargetPosition(currTime_);
            if ((currTime_ - currOpStartTime_) >= max_allowed_time) break;
        }

        grabber_.clampClose();
        grabber_.moveCraneToPosition(Grabber.CranePosition.CRANE_GRAB_STONE);

        // Wait for clamp fully close
        final double beg_clamp_close_time = timer_.time();
        while (true) {
            currTime_ = timer_.time();
            if ((currTime_ - beg_clamp_close_time) >= MAX_WAIT_TIME_FOR_CLOSE_GRABBER_CLAMP) break;

            lift_.holdAtTargetPosition(currTime_);
            grabber_.holdCraneAtTargetPosition();
        }

        lift_.moveToPosition(Lift.Position.LIFT_DELIVER_STONE, timer_.time());

        return true;
    }

    boolean dropSkystoneToFoundation(double max_allowed_time) {
        if (lift_ == null || grabber_ == null) return true;

        if (max_allowed_time < 0.5) max_allowed_time = 0.5;

        if (lift_.isMoveToPositionApplied(Lift.Position.LIFT_DROP_TO_FOUNDATION) == false) {
            lift_.moveToPosition(Lift.Position.LIFT_DROP_TO_FOUNDATION, timer_.time());
        }


        grabber_.clampOpen();

        final double used_time = timer_.time() - currOpStartTime_;
        return (used_time >= max_allowed_time);
    }

    boolean dropSkystoneToGround(double max_allowed_time){
        if (lift_ == null || grabber_ == null) return true;

        if (max_allowed_time < 0.5) max_allowed_time = 0.5;

        grabber_.clampOpen();

        if (lift_.isMoveToPositionApplied(Lift.Position.LIFT_GRAB_STONE_READY) == false) {
            lift_.moveToPosition(Lift.Position.LIFT_GRAB_STONE_READY, timer_.time());
        }

        final double used_time = timer_.time() - currOpStartTime_;
        return (used_time >= max_allowed_time);
    }

    boolean detectSkyStoneDuringAutonomous(double time_limit) {
        if (getDetectSkystone() == null) return true;

        int det_pos = getDetectSkystone().detectSkystone(isRedTeam_, false);
        if (det_pos >= 0) {
            firstSkystonePos_ = det_pos;
            return true;
        }

        double curr_time = timer_.time();
        if ((curr_time - currOpStartTime_) >= time_limit) {
            firstSkystonePos_ = 1; return true;
        }

        return false;
    }

    boolean driveToSkystoneCloseToWallAndPark() {
        double shift_distance_to_align_with_skystone = grabFirstSkystone_[firstSkystonePos_][0];
            runDriveTrainTillFinish(DriveTrainMode.SHIFT_LEFT,
                    -shift_distance_to_align_with_skystone,
                    true);


        double drive_forward_to_skystone = grabFirstSkystone_[firstSkystonePos_][1];

        runDriveTrainTillFinish(DriveTrainMode.FORWARD,
                drive_forward_to_skystone,
                true);

        grabStone(0.5);

        runDriveTrainTillFinish(DriveTrainMode.BACKWARD,
                drive_forward_to_skystone,
                true);

        return true;
    }

}
