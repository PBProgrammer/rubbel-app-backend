package com.section9.rubbel.controllers;

import com.section9.rubbel.models.*;
import org.springframework.lang.Nullable;

import com.section9.rubbel.tasks.base.PlayerAction;
import com.section9.rubbel.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.UUID;


@Controller
@CrossOrigin(origins = {"*", "http://localhost", "http://localhost:4200", "https://localhost:4200"})
public class WebSocketController {

    AppService appService;

    @Autowired
    public WebSocketController(AppService appService) {
        this.appService = appService;
    }

    @MessageMapping("/ws/session/{gameSessionId}/player-action-response")
    public void onReceivePlayerAction(@DestinationVariable UUID gameSessionId, @Nullable final PlayerAction playerAction) {
        appService.processPlayerAction(gameSessionId, playerAction);
    }

    @MessageMapping("/ws/session/{gameSessionId}/auto-complete")
    public void onReceiveAutoCompletePlayerAction(@DestinationVariable UUID gameSessionId, @Nullable final PlayerAction playerAction) {
        appService.processAutoCompletePlayerAction(gameSessionId, playerAction);
    }

    @MessageMapping("/ws/session/{gameSessionId}/update-player-name")
    public void onReceivePlayerNameChange(@DestinationVariable UUID gameSessionId, final Player player) {
        appService.applyNewPlayerName(gameSessionId, player);
    }

    @MessageMapping("/ws/session/{gameSessionId}/playerId/{playerId}/update-game-config-selection")
    public void onReceiveUpdateGameConfigSelection(@DestinationVariable UUID gameSessionId, @DestinationVariable UUID playerId, final LobbyConfigSelection lobbyConfigSelection) {
        appService.applyUpdatedGameConfigSelection(gameSessionId, playerId, lobbyConfigSelection);
    }

    @MessageMapping("/ws/session/{gameSessionId}/playerId/{playerId}/update-avatar")
    public void onReceiveUpdateAvatar(@DestinationVariable UUID gameSessionId, @DestinationVariable UUID playerId, final String avatar) {
        appService.updateAvatar(gameSessionId, playerId, avatar.replaceAll("\"", ""));
    }

    @MessageMapping("/ws/session/{gameSessionId}/playerId/{playerId}/update-rack-order")
    public void onReceiveUpdateRackOrder(@DestinationVariable UUID gameSessionId, @DestinationVariable UUID playerId, final List<LetterToken> letterTokens) {
        appService.updateRackOrder(gameSessionId, playerId, new Rack(letterTokens));
    }

    @MessageMapping("/ws/session/{gameSessionId}/player-ready")
    public void onReceivePlayerReadyChange(@DestinationVariable UUID gameSessionId, final Player player) {
        appService.applyPlayerReadyStatus(gameSessionId, player);
    }

    @MessageMapping("/ws/session/{gameSessionId}/tick")
    public void onReceiveTimedTaskTick(@DestinationVariable UUID gameSessionId, final Tick tick) {
        appService.processTimedTaskTick(gameSessionId, tick);
    }

    @MessageMapping("/ws/session/{gameSessionId}/temporary-rack-to-board-update")
    public void onReceiveTempRackToBoardChange(
            @DestinationVariable UUID gameSessionId,
            final BoardUpdate boardUpdate
    ) {
         appService.temporaryRackToBoardUpdate(gameSessionId, boardUpdate);
    }

    @MessageMapping("/ws/session/{gameSessionId}/temporary-in-between-board-update")
    public void onReceiveTempInBetweenBoardUpdate(
            @DestinationVariable UUID gameSessionId,
            final BoardUpdate boardUpdate
    ) {
        appService.temporaryRackToBoardUpdate(gameSessionId, boardUpdate);
    }

    @MessageMapping("/ws/session/{gameSessionId}/temporary-board-to-rack-update")
    public void onReceiveTempBoardToRackChange(
            @DestinationVariable UUID gameSessionId,
            final BoardUpdate boardUpdate
    ) {
        appService.temporaryBoardToRackUpdate(gameSessionId, boardUpdate);
    }

    @MessageMapping("/ws/session/{gameSessionId}/restart-game")
    public void onReceiveIntermediateRestart(
            @DestinationVariable UUID gameSessionId,
            final Player player) {
        appService.restartGameIntermediate(player, gameSessionId);
    }

    @MessageMapping("/ws/session/{gameSessionId}/restart-finished-game")
    public void onReceiveRestart(
            @DestinationVariable UUID gameSessionId,
            final Player player) {
        appService.restartGame(player, gameSessionId);
    }



}
