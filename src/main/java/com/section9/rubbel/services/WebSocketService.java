package com.section9.rubbel.services;


import com.section9.rubbel.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.section9.rubbel.services.Constants.*;


@Service
public class WebSocketService {

    @Autowired
    SimpMessagingTemplate messagingService;

    public void notifyPlayer(UUID gameSessionId, UUID playerId, DataContainer dtc) {
        this.messagingService.convertAndSend("/client/" + gameSessionId + "/playerId/" + playerId, dtc);
    }

    public void notifyAllPlayers(UUID gameSessionID, DataContainer dtc) {
        this.messagingService.convertAndSend("/client/" + gameSessionID, dtc);
    }

    public void notifyOpponents(UUID gameSessionID, List<UUID> userIds, DataContainer dtc) {
        userIds.stream().forEach(userId -> notifyPlayer(gameSessionID, userId, dtc));
    }

    public void notifyPlayersAboutGameState(RubbelGameSession gameSession, String purpose) {
        gameSession.getPlayers()
                .stream()
                .map(player -> {
                    DataContainer dataContainer = DataContainer.create(
                            purpose,
                            gameSession.getGameSessionId(),
                            player.getId()
                    );
                    dataContainer.addEntry(PLAYER_GAME_STATE_PARAM, PlayerGameStateMap.getPrivatePlayerGameState(gameSession, player.getId()));
                    return dataContainer;
                })
                .forEach(dataContainer -> notifyPlayer(gameSession.getGameSessionId(), dataContainer.getReceiverId(), dataContainer));
    }

    public void notifyPlayersAboutGameState(RubbelGameSession gameSession) {
        notifyPlayersAboutGameState(gameSession, RESPONSE_PURPOSE_UPDATE_GAME_STATE);
    }

    public void notifyPlayersAboutSingleChange(UUID gameSessionId, List<UUID> playerIds, String playerGameStatePropertyKey, Object value) {
        playerIds.stream()
                .map(playerId -> {
                    DataContainer dataContainer = DataContainer.create(
                            RESPONSE_PURPOSE_UPDATE_SINGLE_GAME_STATE_ENTRY,
                            gameSessionId,
                            playerId
                    );
                    PlayerGameStateMap playerGameStateMap = new PlayerGameStateMap();
                    playerGameStateMap.addEntry(playerGameStatePropertyKey, value);

                    dataContainer.addEntry(PLAYER_GAME_STATE_PARAM, playerGameStateMap);
                    return dataContainer;
                })
                .forEach(dataContainer -> notifyPlayer(gameSessionId, dataContainer.getReceiverId(), dataContainer));
    }

    public void notifyPlayersAboutGameBoardUpdate(UUID gameSessionId, List<UUID> playerIds, BoardUpdate boardUpdate) {
        playerIds.stream()
                .map(playerId -> {
                    DataContainer dataContainer = DataContainer.create(
                            RESPONSE_PURPOSE_UPDATE_GAME_BOARD,
                            gameSessionId,
                            playerId
                    );
                    dataContainer.addEntry(BOARD_UPDATE_PARAM, boardUpdate);
                    return dataContainer;
                })
                .forEach(dataContainer -> notifyPlayer(gameSessionId, dataContainer.getReceiverId(), dataContainer));
    }

    public void notifyPlayersAboutPartialChange(RubbelGameSession gameSession, List<UUID> playerIds, List<String> propertyKeys) {
        playerIds.stream()
                .map(playerId -> {
                    DataContainer dataContainer = DataContainer.create(
                            RESPONSE_PURPOSE_UPDATE_PARTIAL_GAME_STATE,
                            gameSession.getGameSessionId(),
                            playerId
                    );
                    dataContainer.addEntry(PLAYER_GAME_STATE_PARAM, PlayerGameStateMap.getPartialPlayerGameStateMap(gameSession, playerId, propertyKeys));
                    return dataContainer;
                })
                .forEach(dataContainer -> notifyPlayer(gameSession.getGameSessionId(), dataContainer.getReceiverId(), dataContainer));
    }

    public void notifySinglePlayerAboutLetterTokenRackUpdate(RubbelGameSession gameSession, UUID playerId, Rack letterTokens) {
        DataContainer dataContainer = new DataContainer(
                RESPONSE_PURPOSE_UPDATE_LETTER_TOKENS,
                gameSession.getGameSessionId(),
                playerId);
        dataContainer.addEntry(PlayerGameStateMap.LETTER_TOKENS_KEY, gameSession.getRackByPlayerId(playerId));
        notifyPlayer(gameSession.getGameSessionId(), playerId, dataContainer);
    }
}
