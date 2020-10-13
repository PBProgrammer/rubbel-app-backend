package com.section9.rubbel.tasks.PlayerTasks;

import com.section9.rubbel.models.*;
import com.section9.rubbel.services.StaticValues;
import com.section9.rubbel.tasks.base.PlayerAction;
import com.section9.rubbel.tasks.base.PlayerTask;

import java.util.*;
import java.util.stream.Collectors;

public class GameOverTask extends PlayerTask {
    public GameOverTask(String id, List<UUID> owners, PlayerAction action) {
        super(id, owners, List.of(action));
        setModal(true);
    }

    @Override
    public void executeStartOperations(RubbelGameSession gameSession) {
        GameOverScoreBoard gameOverScoreBoard = new GameOverScoreBoard();
        gameOverScoreBoard.winner = determineWinner(gameSession.getPlayers());
        getTaskData().put("gameOverScoreBoard" , gameOverScoreBoard);
    }

    @Override
    public void executeEndOperations(RubbelGameSession gameSession, PlayerAction resolvedAction) {
    }


    @Override
    public PlayerAction resolveFinalPlayerAction(RubbelGameSession gameSession) {
        return actions.get(0);
    }


    private List<UUID> determineWinner(List<Player> players) {
        int highestScore = 0;
        List<Player> playersWon = new ArrayList<>();
        for(Player player : players) {
            if(player.getScore() > highestScore) {
                playersWon = List.of(player);
                highestScore = player.getScore();
            }else if(player.getScore() == highestScore) {
                playersWon.add(player);
            }
        }
        return playersWon
                .stream()
                .map(player -> player.getId())
                .collect(Collectors.toList());
    }

}
