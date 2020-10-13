package com.section9.rubbel.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.section9.rubbel.GameSessionConfigs.GameBoardConfigSource;
import com.section9.rubbel.GameSessionConfigs.LanguageConfigSource;
import com.section9.rubbel.models.*;
import com.section9.rubbel.tasks.base.PlayerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.section9.rubbel.services.Constants.*;
import static com.section9.rubbel.models.PlayerGameState.*;


@Service
public class GameSessionManager {

    @Autowired
    WebSocketService webSocketService;

    private Map<UUID, RubbelGameSession> rubbelGameSessions;

    public GameSessionManager() {
        rubbelGameSessions = new HashMap<>();
    }

    public Map<UUID, RubbelGameSession> getRubbelGameSessions() {
        return rubbelGameSessions;
    }

    public void setRubbelGameSessions(Map<UUID, RubbelGameSession> rubbelGameSessions) {
        this.rubbelGameSessions = rubbelGameSessions;
    }

    public GameBoardConfig getGameBoardConfig() {
        return StaticValues.gameBoardConfig;
    }

    public boolean authenticate(UUID gameSessionId, UUID playerId) {
        if (hasRubbelGameSession(gameSessionId)) {
            RubbelGameSession gameSession = rubbelGameSessions.get(gameSessionId);
            return gameSession.hasPlayer(playerId);
        }
        return false;
    }

    public boolean authenticateHost(UUID gameSessionId, UUID playerId) {
        if (hasRubbelGameSession(gameSessionId)) {
            RubbelGameSession gameSession = rubbelGameSessions.get(gameSessionId);
            return gameSession.isHost(playerId);
        }
        return false;
    }

    private boolean hasRubbelGameSession(UUID rubbelGameSessionId) {
        return this.rubbelGameSessions.containsKey(rubbelGameSessionId);
    }


    public DataContainer createRubbelGameSession(Player creatingPlayer) {
        RubbelGameSession createdGameSession = RubbelGameSession.create();
        creatingPlayer.setHost(true);
        Player host = Player.create(creatingPlayer);
        setupRubbelGameSessionInitially(createdGameSession, host);
        PlayerGameState initialPlayerGameState = getPrivatePlayerGameState(createdGameSession, host.getId());
        DataContainer dataContainer = new DataContainer(
                RESPONSE_PURPOSE_CREATED_ROOM,
                createdGameSession.getGameSessionId(),
                host.getId()
        );

        dataContainer.addEntry(PLAYER_GAME_STATE_PARAM, initialPlayerGameState);

        return dataContainer;
    }

    public DataContainer joinRubbelGameSession(UUID gameSessionId, Player joiningPlayer) {
        if (!hasGameSession(gameSessionId)) {
            return null;
        }

        RubbelGameSession joiningGameSession = getGameSession(gameSessionId);

        if (joiningGameSession.hasPlayer(joiningPlayer.getId())) {
            joiningPlayer = joiningGameSession.getPlayerById(joiningPlayer.getId());//at the moment nothing to do
        } else if (joiningGameSession.isFull()) {
            return Util.getSessionLimitReachedDataContainer(gameSessionId);
        } else if (!Util.isInLobbyTask(joiningGameSession)) {
            return Util.getSessionIsAlreadyPlaying(gameSessionId);
        } else {
            joiningPlayer.setHost(false);
            joiningPlayer = Player.create( joiningPlayer);
            joiningGameSession.addPlayer(joiningPlayer);
        }


        DataContainer opponentsNotification = DataContainer.create(
                RESPONSE_PURPOSE_OPPONENT_JOINED,
                joiningPlayer.getId(),
                joiningGameSession.getGameSessionId()
        );

        DataContainer joiningPlayerNotification = DataContainer.create(
                RESPONSE_PURPOSE_JOIN_ROOM,
                joiningPlayer.getId(),
                joiningGameSession.getGameSessionId()
        );
        opponentsNotification.addEntry(PLAYERS_PARAM, joiningGameSession.getPlayers());


        webSocketService.notifyOpponents(joiningGameSession.getId(), joiningGameSession.getOpponentIds(joiningPlayer.getId()), opponentsNotification);
        joiningPlayerNotification.addEntry(PLAYER_GAME_STATE_PARAM, getPrivatePlayerGameState(joiningGameSession, joiningPlayer.getId()));
        return joiningPlayerNotification;

    }

    public RubbelGameSession getGameSession(UUID gameSessionId) {
        return rubbelGameSessions.get(gameSessionId);
    }

    public boolean hasGameSession(UUID gameSessionId) {
        return rubbelGameSessions.containsKey(gameSessionId);
    }

    private static List<com.section9.rubbel.models.LanguageConfig> loadLanguageConfigs() {
        ObjectMapper objMapper = new ObjectMapper();
        try {
            Map<String, Object> value = objMapper.readValue(LanguageConfigSource.LANGUAGE_CONFIG, Map.class);
            Map<String, Object> distributions = (Map<String, Object>) value.get("distributions");
            Map<String, Object> english = (Map<String, Object>) distributions.get("english");
            com.section9.rubbel.models.LanguageConfig englishConfig = objMapper.convertValue(english, com.section9.rubbel.models.LanguageConfig.class);

            Map<String, Object> hebrew = (Map<String, Object>) distributions.get("hebrew");
            com.section9.rubbel.models.LanguageConfig hebrewConfig = objMapper.convertValue(hebrew, com.section9.rubbel.models.LanguageConfig.class);

            return List.of(englishConfig, hebrewConfig);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static GameBoardConfig loadGameBoardConfig() {
        ObjectMapper objMapper = new ObjectMapper();
        try {
            GameBoardConfig gameboardConfig = objMapper.readValue(GameBoardConfigSource.GAMEBOARD_CONFIG, GameBoardConfig.class);
            return gameboardConfig;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addRubbelGameSession(RubbelGameSession rubbelGameSession) {
        rubbelGameSessions.put(rubbelGameSession.getId(), rubbelGameSession);
    }

    private void setupRubbelGameSessionInitially(RubbelGameSession gameSession, Player host) {
        GameBoardConfig gameBoardConfig = loadGameBoardConfig();
        gameSession.setSelectedGameBoardConfig(gameBoardConfig);
        gameSession.setGameBoard(GameBoardFactory.buildInitialSquares(gameBoardConfig));
        gameSession.setFixedGameBoard(GameBoardFactory.buildInitialSquares(gameBoardConfig));
        gameSession.setLobbyConfigSelection(StaticValues.getDefaultLobbyConfigSelection());
        gameSession.addPlayer(host);
        PlayerTask lobbyTask = (PlayerTask) TaskFactory.getNextTaskById(PlayerTask.LOBBY_TASK_ID, gameSession);
        gameSession.addTask(lobbyTask);
        addRubbelGameSession(gameSession);
    }

    public void updateAvatar(UUID gameSessionId, UUID playerId, String avatar) {
        RubbelGameSession gameSession = getGameSession(gameSessionId);
        gameSession.updatePlayerIcon(playerId, avatar);
        webSocketService.notifyPlayersAboutPartialChange(
                gameSession,
                gameSession.getPlayerIds(),
                List.of(PlayerGameStateMap.LOCAL_PLAYER_KEY, PlayerGameStateMap.PLAYERS_KEY));
    }

    public void updateRackOrder(UUID gameSessionId, UUID playerId, Rack letterTokens) {
        RubbelGameSession gameSession = getGameSession(gameSessionId);
        gameSession.updatePlayerRackOrder(playerId, letterTokens);
    }

    public RubbelGameSession initGameSessionFromExisting(UUID gameSessionId) {
        RubbelGameSession sourceGameSession = getGameSession(gameSessionId);
        if(sourceGameSession == null) {
            return null;
        }

        RubbelGameSession clonedGameSession = RubbelGameSession.create();
        clonedGameSession.setPlayers(
                sourceGameSession.getPlayers().stream()
                        .map(player -> Player.create(player, true))
                        .collect(Collectors.toList()));

        clonedGameSession.setGameSessionId(sourceGameSession.getGameSessionId());
        clonedGameSession.setSelectedGameBoardConfig(sourceGameSession.getSelectedGameBoardConfig());
        clonedGameSession.setLobbyConfigSelection(sourceGameSession.getLobbyConfigSelection());
        clonedGameSession.setLettersWhitelist(sourceGameSession.getLettersWhitelist());
        clonedGameSession.setGameBoard(GameBoardFactory.buildInitialSquares(sourceGameSession.getSelectedGameBoardConfig()));
        clonedGameSession.setFixedGameBoard(GameBoardFactory.buildInitialSquares(sourceGameSession.getSelectedGameBoardConfig()));
        PlayerTask lobbyTask = (PlayerTask) TaskFactory.getNextTaskById(PlayerTask.LOBBY_TASK_ID, clonedGameSession);
        clonedGameSession.addTask(lobbyTask);
        addRubbelGameSession(clonedGameSession);
        return clonedGameSession;
    }
}
