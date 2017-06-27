package com.qualcomm.ftcrobotcontroller.opmodes;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Color_Sensor extends LinearOpMode {

    public enum ColorSensorDevice {ADAFRUIT, HITECHNIC_NXT, MODERN_ROBOTICS_I2C};

    //public ColorSensorDevice device = ColorSensorDevice.MODERN_ROBOTICS_I2C;
    public ColorSensorDevice device = ColorSensorDevice.HITECHNIC_NXT;

    ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        hardwareMap.logDevices();

        colorSensor = hardwareMap.colorSensor.get("nxt");

        waitForStart();

        float hsvValues[] = {0,0,0};
        final float values[] = hsvValues;
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);
        while (opModeIsActive()) {

            colorSensor.enableLed(true);

            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);

            telemetry.addData("argb", colorSensor.argb());
            telemetry.addData("Clear", colorSensor.alpha());
            telemetry.addData("Red  ", colorSensor.red());
            telemetry.addData("Green", colorSensor.green());
            telemetry.addData("Blue ", colorSensor.blue());
            telemetry.addData("Hue", hsvValues[0]);

            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
                }
            });
            waitOneFullHardwareCycle();
        }
    }

    private void enableLed(boolean value) {
                colorSensor.enableLed(value);
        }
    }
