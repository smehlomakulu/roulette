package com.roulette.service.impl;

import com.roulette.playerfile.util.PlayerFileUtil;
import com.roulette.service.RouletteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RouletteServiceImpl implements RouletteService {

    @Autowired
    private PlayerFileUtil fileUtil;

    private List<String> players = null;
    private Map<String, Map<String, Double>> playerBets = null;
    private Map<String, Map<String, Double>> playerWinnings = null;

    private static final Logger log = LogManager.getLogger(RouletteService.class);

    @Override
    public void initialiseGame() {
        players = Collections.emptyList();
        log.debug("Loading players file...");
        try {
            players = fileUtil.loadPlayers();
            log.debug("Successfully loaded players!");
            initialiseBets(players);
            requestBets();
        } catch (Exception e) {
            log.error("Failed to initialise roulette game", e);
            System.exit(1);
        }

    }

    private void initialiseBets(List<String> players) {
        playerBets = Collections.emptyMap();
        players.forEach(player -> {
            playerBets.put(player, new HashMap<>());
            log.debug("Initialised bet slot for: {}", player);
        });
    }

    private void requestBets() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please start placing your bet: ");
        String bet = scanner.nextLine();
        String[] bets = bet.split(" ");

        if(playerBets.containsKey(bets[0])) {
            Map<String, Double> playerBet = playerBets.get(bets[0]);
            playerBet.put(bets[1], Double.valueOf(bets[2]));
            playerBets.put(bets[0], playerBet);
        } else {
            log.error("Player has no bet slot created");
        }

        System.out.print("Do you want to place another bet? (yes/no)");
        String response = scanner.nextLine();

        if(response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
            requestBets();
        } else {

        }
    }

    private void drawWinners() {
        int lowerLimit = 1;
        int higherLimit = 37;
        Random random = new Random();
        int draw = random.nextInt(higherLimit - lowerLimit) + lowerLimit;
        Map<String, Double> winnings = null;
        double win = 0;
        Set<String> keyPlayers = playerBets.keySet();
        for(String player : keyPlayers){
            winnings = new HashMap<>();
            Map<String, Double> playerBet = playerBets.get(player);
            if(playerBet.containsKey(draw)) {
                win = playerBet.get(draw) * 36;
                winnings.put(String.valueOf(draw), win);
                playerWinnings.put(player, winnings);
            }else if((draw % 2) == 0 && playerBet.containsKey("EVEN")) {
                win = playerBet.get("EVEN") * 2;
                winnings.put("EVEN", win);
            }else if((draw % 2) > 0 && playerBet.containsKey("ODD")) {
                win = playerBet.get("ODD") * 2;
                winnings.put("ODD", win);
            }
        }
    }
}
