package com.section9.rubbel.tasks.PlayerTasks;

import com.section9.rubbel.models.GameConfig;
import com.section9.rubbel.models.LanguageConfig;
import com.section9.rubbel.models.LobbyConfigSelection;
import com.section9.rubbel.models.RubbelGameSession;
import com.section9.rubbel.services.Constants;
import com.section9.rubbel.services.GameSessionManager;
import com.section9.rubbel.services.StaticValues;
import com.section9.rubbel.tasks.base.PlayerAction;
import com.section9.rubbel.tasks.base.PlayerTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LobbyTask extends PlayerTask {
    public LobbyTask(String id, UUID owner, PlayerAction action) {
        super(id, List.of(owner), List.of(action));
        setModal(false);
    }

    @Override
    public void executeStartOperations(RubbelGameSession gameSession) {

    }

    @Override
    public void executeEndOperations(RubbelGameSession gameSession, PlayerAction resolvedAction) {
        applyConfig(gameSession);
    }

    private void applyConfig(RubbelGameSession gameSession) {
        LobbyConfigSelection lobbyConfigSelection = gameSession.getLobbyConfigSelection();
        GameConfig gameConfig = new GameConfig();
        String langKey = (String) lobbyConfigSelection.getLanguageSelection().getValue();
        LanguageConfig selectedLanguageConfig = StaticValues
                .languageConfigOptions
                .stream()
                .filter(config -> config.getKey().equals(langKey))
                .findFirst()
                .orElseThrow();
        gameConfig.setLanguageConfig(selectedLanguageConfig);
        gameConfig.setProofTime((int) lobbyConfigSelection.getProofTimeSelection().getValue());
        gameConfig.setStartDelay((int) lobbyConfigSelection.getStartDelaySelection().getValue());
        gameConfig.setTimePerTurn((int) lobbyConfigSelection.getTimePerTurnSelection().getValue());
        gameConfig.setVetoTime((int) lobbyConfigSelection.getVetoTimeSelection().getValue());
        gameSession.setGameConfig(gameConfig);
        List<String> lettersWhitelist = new ArrayList<>(selectedLanguageConfig.getLetters().keySet());
        gameSession.setLettersWhitelist(lettersWhitelist);
    }



    @Override
    public PlayerAction resolveFinalPlayerAction(RubbelGameSession gameSession) {
        return actions.get(0);
    }



}
