package com.section9.rubbel.services;

import com.section9.rubbel.enums.BoardUpdateType;
import com.section9.rubbel.enums.PlayerActionType;
import com.section9.rubbel.models.*;
import com.section9.rubbel.tasks.PlayerTaskDTO;
import com.section9.rubbel.tasks.base.PlayerAction;
import com.section9.rubbel.tasks.base.PlayerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class AppService {

    @Autowired
    SimpMessagingTemplate messagingService;

    @Autowired
    WebSocketService webSocketService;

    @Autowired
    GameSessionManager gameSessionManager;

    @Autowired
    TaskManager taskManager;

    private Map<UUID, RubbelGameSession> rubbelGameSessions;

    public AppService() {
        rubbelGameSessions = new HashMap<>();
    }


    public DataContainer joinRubbelGameSession(UUID gameSessionId, Player joiningPlayer) {
        return gameSessionManager.joinRubbelGameSession(gameSessionId, joiningPlayer);
    }

    public DataContainer createRubbelGameSession(Player creatingPlayer) {
        return gameSessionManager.createRubbelGameSession(creatingPlayer);
    }

    public void processPlayerAction(UUID gameSessionId, PlayerAction playerAction) {
        if (gameSessionManager.authenticate(gameSessionId, playerAction.getExecutor())) {
            taskManager.processReceivedPlayerAction(gameSessionManager.getGameSession(gameSessionId), playerAction);
        }
    }

    public void processAutoCompletePlayerAction(UUID gameSessionId, PlayerAction playerAction) {
        if (gameSessionManager.authenticateHost(gameSessionId, playerAction.getExecutor())) {
            taskManager.processReceivedPlayerAction(gameSessionManager.getGameSession(gameSessionId), playerAction);
        }
    }

    public GameConfigOptions getGameConfigOptions(UUID gameSessionId) {
        if (gameSessionManager.hasGameSession(gameSessionId)) {
            return StaticValues.getGameConfigOptions();
        }
        return null;
    }

    public void applyUpdatedGameConfigSelection(UUID gameSessionId, UUID playerId, LobbyConfigSelection lobbyConfigSelection) {
        if (gameSessionManager.authenticateHost(gameSessionId, playerId)) {
            RubbelGameSession gameSession = gameSessionManager.getGameSession(gameSessionId);
            gameSession.setLobbyConfigSelection(lobbyConfigSelection);
            webSocketService.notifyPlayersAboutSingleChange(gameSessionId, gameSession.getPlayerIds(), PlayerGameStateMap.LOBBY_CONFIG_SELECTION_KEY, lobbyConfigSelection);
        }
    }

    public boolean temporaryRackToBoardUpdate(UUID gameSessionId, BoardUpdate boardUpdate) {
        if (gameSessionManager.authenticate(gameSessionId, boardUpdate.getExecutor())) {
            RubbelGameSession gameSession = gameSessionManager.getGameSession(gameSessionId);

            int clearIndex = gameSession.addLetterTokenToTempGameBoard(boardUpdate.getSquare());
            boardUpdate.setIndex(clearIndex);

            webSocketService.notifyPlayersAboutGameBoardUpdate(
                    gameSessionId,
                    gameSessionManager.getGameSession(gameSessionId).getPlayerIds(),
                    boardUpdate
            );

            if (boardUpdate.getType().equals(BoardUpdateType.R2GB)) {
                Rack playerRack = gameSession.getRackByPlayerId(boardUpdate.getExecutor());
                Rack updatedPlayerRack = new Rack(playerRack
                        .stream()
                        .filter(token -> !token.getId().equals(boardUpdate.getSquare().getLetterToken().getId()))
                        .collect(Collectors.toList()));
                gameSession.updateRackByPlayerId(boardUpdate.getExecutor(), updatedPlayerRack);

                webSocketService.notifySinglePlayerAboutLetterTokenRackUpdate(gameSession, boardUpdate.getExecutor(), updatedPlayerRack);
            }
            return true;
        }
        return false;
    }

    public void temporaryBoardToRackUpdate(UUID gameSessionId, BoardUpdate boardUpdate) {
        if (gameSessionManager.authenticate(gameSessionId, boardUpdate.getExecutor())) {
            RubbelGameSession gameSession = gameSessionManager.getGameSession(gameSessionId);
            LetterToken movingLetterToken = gameSession.removeLetterTokenFromTempGameBoard(boardUpdate.getSquare());
            Rack letterTokensByPlayer = gameSession.getRackByPlayerId(boardUpdate.getExecutor());

            if (boardUpdate.getIndex() > -1 && boardUpdate.getIndex() < letterTokensByPlayer.size()) {
                letterTokensByPlayer.add(boardUpdate.getIndex(), movingLetterToken);
            } else {
                letterTokensByPlayer.add(movingLetterToken);
            }

            boardUpdate.setIndex(-1);

            webSocketService.notifyPlayersAboutGameBoardUpdate(
                    gameSessionId,
                    gameSessionManager.getGameSession(gameSessionId).getPlayerIds(),
                    boardUpdate
            );
            //webSocketService.notifySinglePlayerAboutLetterTokenRackUpdate(gameSession, boardUpdate.getExecutor(), letterTokensByPlayer);
        }
    }

    public void processTimedTaskTick(UUID gameSessionId, Tick tick) {
        if (gameSessionManager.authenticateHost(gameSessionId, tick.getHostId())) {
            RubbelGameSession gameSession = gameSessionManager.getGameSession(gameSessionId);
            if (gameSession.isCurrentTask(tick.getTaskId())) {
                gameSession.updateTimerDelay(tick);
                webSocketService.notifyPlayersAboutSingleChange(
                        gameSession.getGameSessionId(),
                        gameSession.getPlayerIds(),
                        PlayerGameStateMap.CURRENT_TASK_KEY,
                        PlayerTaskDTO.from((PlayerTask) gameSession.getCurrentTask())
                );
            }
        }
    }

    public List<String> getAvatarList(UUID gameSessionId) {
        if (gameSessionManager.hasGameSession(gameSessionId)) {
            return StaticValues.getAvatarList();
        }
        return null;
    }

    public void applyNewPlayerName(UUID gameSessionId, Player player) {
        if (gameSessionManager.authenticate(gameSessionId, player.getId())) {
            RubbelGameSession gameSession = gameSessionManager.getGameSession(gameSessionId);
            gameSession.updatePlayerName(player.getId(), player.getName());
            webSocketService.notifyPlayersAboutPartialChange(gameSession, gameSession.getPlayerIds(), List.of(PlayerGameStateMap.LOCAL_PLAYER_KEY, PlayerGameStateMap.PLAYERS_KEY));
        }
    }

    public void applyPlayerReadyStatus(UUID gameSessionId, Player player) {
        if (gameSessionManager.authenticate(gameSessionId, player.getId())) {
            RubbelGameSession gameSession = gameSessionManager.getGameSession(gameSessionId);
            gameSession.updatePlayerReadyStatus(player.getId(), player.isReady());
            webSocketService.notifyPlayersAboutPartialChange(
                    gameSession,
                    gameSession.getPlayerIds(),
                    List.of(PlayerGameStateMap.LOCAL_PLAYER_KEY, PlayerGameStateMap.PLAYERS_KEY));
        }
    }

    public void updateAvatar(UUID gameSessionId, UUID playerId, String avatar) {
        if (gameSessionManager.authenticate(gameSessionId, playerId)) {
            gameSessionManager.updateAvatar(gameSessionId, playerId, avatar);
        }
    }

    public void updateRackOrder(UUID gameSessionId, UUID playerId, Rack letterTokens) {
        gameSessionManager.updateRackOrder(gameSessionId, playerId, letterTokens);
    }

    public List<String> getLettersWhitelist(UUID gameSessionId) {
        if (gameSessionManager.hasGameSession(gameSessionId)) {
            return gameSessionManager.getGameSession(gameSessionId).getLettersWhitelist();
        }
        return null;
    }

    public void restartGame(Player player, UUID gameSessionId) {
        if (gameSessionManager.authenticate(gameSessionId, player.getId())) {
            if (gameSessionManager.getGameSession(gameSessionId).getCurrentTask().getId().equals(PlayerTask.GAME_OVER_TASK)) {
                RubbelGameSession clonedGameSession = this.gameSessionManager.initGameSessionFromExisting(gameSessionId);
                PlayerAction mockHostPlayerAction = new PlayerAction(PlayerAction.START_GAME_ACTION_ID, PlayerTask.WAIT_AFTER_START_TASK_ID, PlayerActionType.COMPLETE);
                mockHostPlayerAction.setExecutor(clonedGameSession.getHost().getId());
                mockHostPlayerAction.setOriginTaskId(PlayerTask.LOBBY_TASK_ID);
                taskManager.processReceivedPlayerAction(clonedGameSession, mockHostPlayerAction);
            }
        }
    }

    public void restartGameIntermediate(Player player, UUID gameSessionId) {
        if (gameSessionManager.authenticateHost(gameSessionId, player.getId())) {

        }
    }
}
