package com.thanhdc.GUI.Main;

import java.util.Timer;
import java.util.TimerTask;

public class EndTask extends TimerTask {

    @Override
    public void run() {
        Timer timertoEnd = new Timer();
        timertoEnd.cancel();
        timertoEnd.purge();

    }

}