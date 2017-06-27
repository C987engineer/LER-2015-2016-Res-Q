package com.qualcomm.ftcrobotcontroller.opmodes;

public class AutoRed1 extends AbstractAutonomousOpMode {
    @Override
    public float getFirstForwardAmount() {
        return 30;
    }

    @Override
    public int getFirstTurnAngle() {
        return -80;
    }

    @Override
    public char getTeamColor() {
        return 'R';
    }

}
