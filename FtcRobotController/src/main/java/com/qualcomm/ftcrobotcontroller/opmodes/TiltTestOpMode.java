package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.res.Configuration;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcrobotcontroller.opmodes.robotCoreFunctions.TiltDetector;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class TiltTestOpMode extends OpMode {
    TiltDetector td;

    @Override
    public void init() {
        td = new TiltDetector(this.hardwareMap, Configuration.ORIENTATION_LANDSCAPE);
    }


    @Override
    public void loop() {
        float roll = td.getRoll();
        telemetry.addData("tilt", roll);
        DbgLog.msg("tilt = " + roll); // in case telemetry not working, we can check the logs
    }
}
