package com.qualcomm.ftcrobotcontroller.opmodes;

import android.util.Log;

import com.qualcomm.ftccommon.DbgLog;
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
public class PushBotSquare extends LinearOpMode {
    //DcMotor leftMotor;
    //DcMotor rightMotor;
    MotorHandler DriveTrain;
    ServoHandler Arms;
    DirectionHandler dH;
    final double Leftarm_start     = 0;
    final double Rightarm_start    = 1;
    int[] encoderPositions = new int[4];

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

        sleep(DriveTrain.moveDistanceEstimation(72f));

        DriveTrain.setAllMotorsPower(0);

        sleep(1000);

        DriveTrain.setAllMotorsPower(1);

        int encoderGoal = ((int) (36f/(4*Math.PI))*1680);
        int[] encoderGoals = new int[]{encoderGoal+DriveTrain.readEncoder("rf"), encoderGoal+DriveTrain.readEncoder("rb"), encoderGoal+DriveTrain.readEncoder("lf"), encoderGoal+DriveTrain.readEncoder("lb")};

        while (true) {
            if (DriveTrain.checkMotor("rf", encoderGoals[0]) && DriveTrain.checkMotor("rb", encoderGoals[1]) && DriveTrain.checkMotor("lf", encoderGoals[2]) && DriveTrain.checkMotor("lb", encoderGoals[3])) {
                break;
            }
            DriveTrain.setAllMotorsPower(1);
            encoderPositions = DriveTrain.readEncoders(new String[]{"rf", "rb", "lf", "lb"});

            telemetry.addData("RF", encoderPositions[0]);
            telemetry.addData("RF Goal", encoderGoal+DriveTrain.readEncoder("rf"));
            telemetry.addData("If RF", DriveTrain.checkMotor("rf", encoderGoals[0]));
        }

        DriveTrain.setAllMotorsPower(0);

    }
}
