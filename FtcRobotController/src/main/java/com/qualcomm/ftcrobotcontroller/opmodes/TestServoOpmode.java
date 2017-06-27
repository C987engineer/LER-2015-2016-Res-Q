package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.robotCoreFunctions.ServoHandler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

/**
 * Created on 2/14/2016.
 */
public class TestServoOpmode extends OpMode {

    float bumper;
    ServoHandler servo;

    @Override
    public void init(){

        servo = new ServoHandler(hardwareMap, "bumper");


    }
    @Override
    public void loop(){

        bumper  = Range.clip(gamepad1.right_stick_y, -1, 1);
        servo.SetServo("bumper", bumper);

    }
    @Override
    public void stop(){

    }
}
