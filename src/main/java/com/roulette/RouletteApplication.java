package com.roulette;

import com.roulette.service.RouletteService;
import com.roulette.service.impl.RouletteServiceImpl;
import com.roulette.timertask.RouletteTimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Timer;

@SpringBootApplication
public class RouletteApplication {

    private RouletteService rouletteService;

    public static void main(String args[]) {
        ApplicationContext applicationContext = SpringApplication.run(RouletteApplication.class);
        RouletteService rouletteService = applicationContext.getBean(RouletteServiceImpl.class);
        rouletteService.initialiseGame();
        Timer timer = new Timer();
        timer.schedule(new RouletteTimerTask(), 30000);
    }
}
