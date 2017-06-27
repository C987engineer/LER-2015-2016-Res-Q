package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.res.Configuration;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.ftcrobotcontroller.opmodes.robotCoreFunctions.MountainDetector;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class MountainDetectorOpMode extends OpMode {

    MountainDetector md;

    @Override
    public void init() {
        FtcRobotControllerActivity a = (FtcRobotControllerActivity) hardwareMap.appContext;
        md = new MountainDetector(a, this, Configuration.ORIENTATION_LANDSCAPE);
    }

    @Override
    public void loop() {
        int loc = md.getRedLocation();
        telemetry.addData("mtn location", loc);
        DbgLog.msg("location = " + loc); // in case telemetry not working, we can check the logs
    }
}