package com.roulette.timertask;

import com.roulette.service.RouletteService;
import com.roulette.service.impl.RouletteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class RouletteTimerTask extends TimerTask {

    @Override
    public void run() {
        RouletteServiceImpl.drawWinners();
    }
}
