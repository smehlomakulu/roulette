/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roulette.playerfile.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.roulette.service.RouletteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 *
 * @author shadrack
 */
@Component
public class PlayerFileUtil {

    private static final Logger log = LogManager.getLogger(PlayerFileUtil.class);

    public static List<String> loadPlayers() throws Exception {
        List<String> players = new ArrayList<>();
        String player = "";
        File file = new File(System.getProperty("user.home") + "/roulette/roulette.txt");
        log.debug("File path: {}", file.getAbsolutePath());
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        while((player = reader.readLine()) != null) {
            players.add(player);
        }
        
        return players;
    }
    
}
