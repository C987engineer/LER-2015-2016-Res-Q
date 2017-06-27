package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Chandler on 1/24/2016.
 */
public class ArmTest extends OpMode{

    DcMotor hookArm;
    float armSpeed;
    int goalBottom;
    int goalTop;

    public void init() {
        hookArm     = hardwareMap.dcMotor.get("hookArm");
        armSpeed    = 0;
        goalBottom  = 0;
        goalTop     = 0;
    }

    public void loop() {
        armSpeed    = Range.clip(gamepad1.right_stick_y, -1, 1);

        hookArm.setPower(armSpeed);

        if (gamepad1.a) {
            goalTop     = hookArm.getCurrentPosition();
        } else if (gamepad1.b) {
            goalBottom  = hookArm.getCurrentPosition();
        }

        telemetry.addData("Top Goal: ", goalTop);
        telemetry.addData("Bottom Goal: ", goalBottom);
    }

}
