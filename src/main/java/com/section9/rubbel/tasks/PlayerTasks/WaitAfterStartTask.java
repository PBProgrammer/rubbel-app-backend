package com.section9.rubbel.tasks.PlayerTasks;

import com.section9.rubbel.models.*;
import com.section9.rubbel.services.Util;
import com.section9.rubbel.tasks.base.PlayerAction;
import com.section9.rubbel.tasks.base.PlayerTask;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class WaitAfterStartTask extends PlayerTask {

    public WaitAfterStartTask(String id, UUID owner, PlayerAction action) {
        super(id, List.of(owner), List.of(action));
        setModal(false);
    }

    @Override
    public void executeStartOperations(RubbelGameSession gameSession) {
        initLetterTokenBank(gameSession);
        initRandomRackForEachPlayer(gameSession);
        chooseStartingPlayerRandomly(gameSession);
        setTaskTimer(gameSession);
    }

    @Override
    public void executeEndOperations(RubbelGameSession gameSession, PlayerAction resolvedAction) {

    }

    private Rack getRandomRackAndUpdateBank(UUID playerID, List<LetterToken> letterTokenBank) {
        int rackSize = 7; // TODO in Config auslagern ?
        Rack rack = new Rack(letterTokenBank.stream().limit(rackSize).collect(Collectors.toList()));
        rack.forEach(letterTokenBank::remove);
        return rack;
    }

    private void initRandomRackForEachPlayer(RubbelGameSession gameSession) {
        List<LetterToken> letterTokenBank = gameSession.getLetterTokenBank();
        List<Player> players = gameSession.getPlayers();
        Map<UUID, Rack> playerLetterTokenRacks = new HashMap<>();
        players.forEach(player -> {
            Collections.shuffle(letterTokenBank);
            playerLetterTokenRacks.put(player.getId() , getRandomRackAndUpdateBank(player.getId(), letterTokenBank));
        });
        gameSession.setPlayerLetterTokenRacks(playerLetterTokenRacks);
    }

    private void chooseStartingPlayerRandomly(RubbelGameSession gameSession) {
        Random random = new Random();
        List<Player> players = gameSession.getPlayers();
        int randomIndex = random.nextInt(players.size());
        gameSession.setActivePlayerIndex(randomIndex);
    }

    private void setTaskTimer(RubbelGameSession gameSession) {
        this.setEndTimestamp(Util.getEndTimestamp(gameSession.getGameConfig().getStartDelay()));
    }

    private void initLetterTokenBank(RubbelGameSession gameSession) {
        List<LetterToken> letterTokenBank = LetterTokenFactory.getLetterTokensByConfig(gameSession.getGameConfig().getLanguageConfig());
        gameSession.setLetterTokenBank(letterTokenBank);
    }

    @Override
    public PlayerAction resolveFinalPlayerAction(RubbelGameSession gameSession) {
        return getReceivedActions()
                .values()
                .stream()
                .findFirst()
                .orElseThrow();
    }
}
