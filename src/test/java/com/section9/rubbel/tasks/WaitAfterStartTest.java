package com.section9.rubbel.tasks;

import com.section9.rubbel.models.LetterTokenFactory;
import com.section9.rubbel.models.Player;
import com.section9.rubbel.models.RubbelGameSession;
import com.section9.rubbel.services.GameSessionManager;
import com.section9.rubbel.tasks.PlayerTasks.WaitAfterStartTask;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class WaitAfterStartTest {

    private static Player player1;
    private static Player player2;
    private static Player player3;
    private static Player player4;
    private static RubbelGameSession gameSession;
    private static GameSessionManager sessionManager;

    @BeforeAll
    public static void initTestObjects() {
        Player host = new Player();
        host.setHost(true);
        Player basePlayer = new Player();
        player1 = Player.create(host);
        player2 = Player.create(basePlayer);
        player3 = Player.create(basePlayer);
        player4 = Player.create(basePlayer);

        gameSession = RubbelGameSession.create();
        sessionManager = new GameSessionManager();

    }


    @Test
    public void getStartingPlayer() {
        List<Player> players = List.of(player1, player2, player3, player4);
        gameSession.setPlayers(players);
        gameSession.setLetterTokenBank(LetterTokenFactory.getLetterTokensByConfig(gameSession.getGameConfig().getLanguageConfig()));

        WaitAfterStartTask waitAfterStartTask = new WaitAfterStartTask("MOCKID", UUID.randomUUID(), null);
        waitAfterStartTask.executeStartOperations(gameSession);
        UUID startingPlayer = gameSession.getActivePlayer();
        assertEquals(1, players.stream().filter(player -> player.getId().equals(startingPlayer)).count());
    }
}
