package com.qualcomm.ftcrobotcontroller.opmodes;


import android.content.res.Configuration;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.ftcrobotcontroller.opmodes.robotCoreFunctions.DirectionHandler;
import com.qualcomm.ftcrobotcontroller.opmodes.robotCoreFunctions.MotorHandler;
import com.qualcomm.ftcrobotcontroller.opmodes.robotCoreFunctions.MountainDetector;
import com.qualcomm.ftcrobotcontroller.opmodes.robotCoreFunctions.ServoHandler;
import com.qualcomm.ftcrobotcontroller.opmodes.robotCoreFunctions.TiltDetector;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.text.MessageFormat;

public abstract class AutonomousTestingOpMode extends LinearOpMode {

    MotorHandler driveTrain;
    ServoHandler arms;
    DirectionHandler dH;
    final double leftarm_start = 0;
    final double rightarm_start = 1;
    int[] encoderPositions = new int[4];
    private String[] allMotors = new String[]{"rf", "rb", "lf", "lb"};
    private final String[] leftMotors = new String[]{"lf", "lb"};
    private final String[] rightMotors = new String[]{"rf", "rb"};
    private final String[] armNames = new String[]{"leftArm", "rightArm"};

    public abstract void DriveInstructions();

    @Override
    public void runOpMode() throws InterruptedException {
        waitOneFullHardwareCycle();
        DbgLog.msg("OPMODE: Starting initialization...");
        // DbgLog.msg("hardwareMap.appContext: "+hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE));

        initElectronics();

        waitForStart();
        DbgLog.msg("OPMODE: Beginning drive instructions...");

        DriveInstructions();

    }

    protected void attemptDrive(float distance) {
        try {
            drive(distance); // implemented in subclasses
        } catch (Exception e) {
            DbgLog.error(MessageFormat.format("OPMODE: Problem detected ({0}), stopping...", e.getMessage()));
            DbgLog.logStacktrace(e);
            driveTrain.setAllMotorsPower(0);
        }
    }

    protected void drive(float distance) throws InterruptedException {
        int goal = driveTrain.moveDistanceEncoder(distance, allMotors);
        driveGuaranteed(allMotors, goal, 10);
    }

    protected void turn(int angle) throws InterruptedException {
        dH.turnByAngle(angle, true);
        turnGuaranteed(rightMotors, driveTrain.rightGoal, leftMotors, driveTrain.leftGoal, 10);
    }

    protected void initElectronics() {

        driveTrain = new MotorHandler(hardwareMap, allMotors, 4.5875f);
        arms = new ServoHandler(hardwareMap, new String[]{"leftArm", "rightArm"});
        dH = new DirectionHandler(driveTrain, allMotors);

        driveTrain.SetDirection(new String[]{"lf", "lb"}, DcMotor.Direction.FORWARD);
        driveTrain.SetDirection(new String[]{"rf", "rb"}, DcMotor.Direction.REVERSE);

        arms.SetServo(armNames, new double[]{leftarm_start, rightarm_start});
    }

    protected void driveGuaranteed(String[] motorNames, int goal, int range) throws InterruptedException {
        DbgLog.msg("OPMODE: Goal set to " + goal);
        while (!driveTrain.checkAvg(motorNames, goal, range)) {
            driveTrain.setAllMotorsPower(1);
            DbgLog.msg(compareGoals(motorNames, goal, "OPMODE: Not at position, "));
            telemetry.addData("Motor Set: ", driveTrain.checkAvg(motorNames, goal, range));
            sleep(20);
        }
        DbgLog.msg(compareGoals(motorNames, goal, "OPMODE: AT POSITION, "));
        driveTrain.setAllMotorsPower(0);
    }

    protected void turnGuaranteed(String[] motorNames1, int goal1, String[] motorNames2, int goal2, int range) throws InterruptedException {
        while (!(driveTrain.checkAvg(motorNames1, goal1, range) && driveTrain.checkAvg(motorNames2, goal2, range))) {
            driveTrain.setAllMotorsPower(1);
            //DbgLog.msg(MessageFormat.format("waitForMotors: Not at position, Goal1 is {0}, Goal2 is {1}", driveTrain.checkAvg(motorNames1, goal1, range), driveTrain.checkAvg(motorNames2, goal2, range)));
            //telemetry.addData("Motor Set 1: ", driveTrain.checkAvg(motorNames1, goal1, range));
            //telemetry.addData("Motor Set 2: ", driveTrain.checkAvg(motorNames2, goal2, range));
            sleep(20);
        }
        DbgLog.msg(MessageFormat.format("waitForMotors: AT POSITION; Goal1 is {0}, Goal2 is {1}", driveTrain.checkAvg(motorNames1, goal1, range), driveTrain.checkAvg(motorNames2, goal2, range)));
        DbgLog.msg(MessageFormat.format("waitForMotors: Giving tests; {0} is at {1} and {2} is at {3}", motorNames1[0], driveTrain.readEncoder(motorNames1[0]), motorNames2[0], driveTrain.readEncoder(motorNames2[0])));
        driveTrain.setAllMotorsPower(0);
    }

    private String compareGoals(String[] motorNames, int goal, String intro) {
        Object[] toFormat = new Object[motorNames.length*2];
        String toReturn;

        for (int i = 0; i/2 < motorNames.length; i+=2) {
            toFormat[i] = MessageFormat.format("{0} ({1})", driveTrain.readEncoder(motorNames[i]), motorNames[i]);
            toFormat[i+1] = goal;
        }

        String formatString = intro;

        for (int i = 0; i/2 < motorNames.length; i+=2) {
            formatString = formatString + "{" + i + "} < {" + (i+1) + "}, ";
        }

        toReturn = MessageFormat.format(formatString, toFormat);

        return toReturn;
    }

}
