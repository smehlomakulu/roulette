package com.roulette.service.impl;

import com.roulette.playerfile.util.PlayerFileUtil;
import com.roulette.service.RouletteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RouletteServiceImpl  implements RouletteService{

    private PlayerFileUtil fileUtil;

    private List<String> players = null;
    private static Map<String, Map<String, Double>> playerBets = null;
    private static Map<String, Map<String, Double>> playerWinnings = null;
    private static Map<String, Map<String, String>> playerOutcomes = null;
    private static final String WIN = "WIN";
    private static final String LOSE = "LOSE";
    private static final String EVEN = "EVEN";
    private static final String ODD = "ODD";

    private static final Logger log = LogManager.getLogger(RouletteService.class);

    @Override
    public void initialiseGame() {
        players = new ArrayList<>();
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
        playerBets = new HashMap<String, Map<String, Double>>();
        players.forEach(player -> {
            playerBets.put(player, new HashMap<>());
            log.debug("Initialised bet slot for: {}", player);
        });
    }

    private static void requestBets() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please start placing your bet: ");
        String bet = scanner.nextLine();
        String[] bets = bet.split(" ");

        try {
            if (playerBets.containsKey(bets[0])) {
                log.debug("Found player slot for: {}", bets[0]);
                Map<String, Double> playerBet = playerBets.get(bets[0]);
                log.debug("Player bet map: {}", playerBet.size());
                playerBet.put(bets[1], Double.valueOf(bets[2]));
                playerBets.put(bets[0], playerBet);
            } else {
                log.error("Player has no bet slot created");
            }
        }catch(Exception e) {
            log.error("Incorrect input supplied, please try again, format (Name number bet)");
            requestBets();
        }

        System.out.print("Do you want to place another bet? (yes/no)");
        String response = scanner.nextLine();

        if(response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
            requestBets();
        }
    }

    public static void drawWinners() {
        int lowerLimit = 1;
        int higherLimit = 37;
        Random random = new Random();
        int draw = random.nextInt(higherLimit - lowerLimit) + lowerLimit;
        Map<String, Double> winnings = null;
        Map<String, String> betOutcomes = null;
        playerWinnings = new HashMap<String, Map<String, Double>>();
        playerOutcomes = new HashMap<String, Map<String, String>>();
        double win = 0;
        String theBet = "";
        Set<String> keyPlayers = playerBets.keySet();
        for(String player : keyPlayers){
            winnings = new HashMap<>();
            betOutcomes = new HashMap<>();
            Map<String, Double> playerBet = playerBets.get(player);
            if(playerBet.containsKey(draw)) {
                theBet = String.valueOf(draw);
                win = playerBet.get(draw) * 36;
                winnings.put(theBet, win);
                betOutcomes.put(theBet, WIN);
            }else if((draw % 2) == 0 && playerBet.containsKey("EVEN")) {
                win = playerBet.get(EVEN) * 2;
                theBet = WIN;
                winnings.put(theBet, win);
                betOutcomes.put(theBet, WIN);
            }else if((draw % 2) > 0 && playerBet.containsKey("ODD")) {
                win = playerBet.get(ODD) * 2;
                theBet = ODD;
                winnings.put(theBet, win);
                betOutcomes.put(theBet, WIN);
            }else {
                for(String bet : playerBet.keySet()){
                    betOutcomes.put(bet, LOSE);
                }
            }
            playerWinnings.put(player, winnings);
            playerOutcomes.put(player, betOutcomes);
        }

        showResults(draw);
        System.out.println();
        requestBets();
    }

    private static void showResults(int draw) {
        System.out.println("Number: " + draw);
        System.out.print("Player\t");
        System.out.print("Bet\t");
        System.out.print("Outcome\t");
        System.out.println("Winnings");

        for (String player : playerBets.keySet()) {
            Map<String, Double> betMap = playerBets.get(player);
            betMap.keySet().forEach(bet -> {
                final Map<String, Double> win = playerWinnings.get(player);
                final Map<String, String> outcome = playerOutcomes.get(player);
                System.out.print(player + "\t");
                System.out.print(bet + "\t");
                System.out.print(outcome.get(bet) + "\t");
                System.out.println(win.get(bet) == null ? 0.0 : win.get(bet));
            });

        }
    }
}
