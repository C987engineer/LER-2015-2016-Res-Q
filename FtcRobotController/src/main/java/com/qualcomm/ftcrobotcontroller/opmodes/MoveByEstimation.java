package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.robotCoreFunctions.DirectionHandler;
import com.qualcomm.ftcrobotcontroller.opmodes.robotCoreFunctions.MotorHandler;
import com.qualcomm.ftcrobotcontroller.opmodes.robotCoreFunctions.ServoHandler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;


/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep()
 * and a for loop.
 */
public class MoveByEstimation extends LinearOpMode {
    //DcMotor leftMotor;
    //DcMotor rightMotor;
    MotorHandler DriveTrain;
    ServoHandler Arms;
    DirectionHandler dH;
    final double Leftarm_start     = 0;
    final double Rightarm_start    = 1;

    @Override
    public void runOpMode() throws InterruptedException {
        waitOneFullHardwareCycle();
        /*
        Log.d("PushBotSquare", "testing Logging");
        DbgLog.msg("starting PushBotSquare");
        try {
            leftMotor = hardwareMap.dcMotor.get("left1");
        }
        catch (NullPointerException ne) {
            DbgLog.error("Can't get left motor");
        }
        try {
            rightMotor = hardwareMap.dcMotor.get("right1");
        }
        catch (NullPointerException ne) {
            DbgLog.error("Can't get right motor");
        }
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        //leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        //rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        */

        DriveTrain  = new MotorHandler(hardwareMap, new String[]{"rf", "rb", "lf", "lb"}, 4);
        Arms        = new ServoHandler(hardwareMap, new String[]{"leftArm", "rightArm"});
        dH          = new DirectionHandler(DriveTrain, new String[]{"rf", "rb", "lf", "lb"});

        DriveTrain.SetDirection(new String[]{"lf", "lb"}, DcMotor.Direction.FORWARD);
        DriveTrain.SetDirection(new String[]{"rf", "rb"}, DcMotor.Direction.REVERSE);

        Arms.SetServo(new String[]{"leftArm", "rightArm"}, new double[]{Leftarm_start, Rightarm_start});

        waitForStart();

        DriveTrain.setAllMotorsPower(1);

        sleep(DriveTrain.moveDistanceEstimation(36f));

        DriveTrain.setAllMotorsPower(0);

        sleep(1000);

        sleep(dH.turnRightEstimate());

        DriveTrain.setAllMotorsPower(0);

        sleep(dH.turnRightEstimate());

        DriveTrain.setAllMotorsPower(0);

        sleep(dH.turnLeftEstimate());

        DriveTrain.setAllMotorsPower(0);

        sleep(dH.turnByAngleEstimate(270, true));

        DriveTrain.setAllMotorsPower(0);

    }
}
