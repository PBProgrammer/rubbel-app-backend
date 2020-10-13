package com.section9.rubbel.tasks.PlayerTasks;

import com.section9.rubbel.models.*;
import com.section9.rubbel.services.Util;
import com.section9.rubbel.tasks.base.PlayerAction;
import com.section9.rubbel.tasks.base.PlayerTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SwitchLetterTokensTask extends PlayerTask {

    public SwitchLetterTokensTask(String id, UUID owner, List<PlayerAction> actions) {
        super(id, List.of(owner), actions);
        takeOverTime = true;
        setModal(true);
    }

    @Override
    public void executeStartOperations(RubbelGameSession gameSession) {

    }

    @Override
    public void executeEndOperations(RubbelGameSession gameSession, PlayerAction resolvedAction) {
        if (!isAutoComplete()) {
            List<LetterToken> letterTokensToSwitch = ((List<Map<String, Object>>) resolvedAction.getPayload())
                    .stream()
                    .map(LetterTokenFactory::map)
                    .collect(Collectors.toList());
            switchLetterTokens(gameSession, resolvedAction.getExecutor(), letterTokensToSwitch);
            gameSession.resetConsecutivePasses();
        }
        gameSession.determineAndSetNextPlayer();
    }

    @Override
    public PlayerAction resolveFinalPlayerAction(RubbelGameSession gameSession) {
        if(isAutoComplete()) {
            return actions.stream().findFirst().orElseThrow();
        }
        return getReceivedActions().values().stream().findFirst().orElseThrow();
    }

    @Override
    public boolean taskCompletable(RubbelGameSession gameSession) {

        if (isAutoComplete()) {
            return true;
        }
        PlayerAction receivedAction = getReceivedActions()
                .values()
                .stream()
                .findFirst()
                .orElseThrow();
        return receivedAction.getPayload() != null;
    }

    private void switchLetterTokens(RubbelGameSession gameSession, UUID executor, List<LetterToken> tokensToSwitch) {
        List<LetterToken> newLetterTokens = new ArrayList<>();
        List<LetterToken> bank = gameSession.getLetterTokenBank();
        for (int i = 0; i < tokensToSwitch.size(); i++) {
            newLetterTokens.add(Util.getAndRemoveRandomLetterTokenFromBank(bank));
        }
        bank.addAll(tokensToSwitch);
        Rack playerLetterToken = gameSession.getRackByPlayerId(executor);
        Rack updatedPlayerLetterTokens = new Rack(playerLetterToken
                .stream()
                .filter(token -> {
                    for (LetterToken letterToken : tokensToSwitch) {
                        if (letterToken.getId().equals(token.getId())) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList()));
        updatedPlayerLetterTokens.addAll(newLetterTokens);
        gameSession.updateRackByPlayerId(executor, updatedPlayerLetterTokens);
    }
}
