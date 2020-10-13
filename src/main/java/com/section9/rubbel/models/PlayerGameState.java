package com.section9.rubbel.models;

import com.section9.rubbel.tasks.base.Task;

import java.util.List;
import java.util.UUID;

public class PlayerGameState {

    Task currentTask;
    UUID gameSessionId;
    List<Square> gameBoard;
    List<Player> players;
    Player localPlayer;
    Rack letterTokens;
    int  remainingLetters = -1;
    LobbyConfigSelection lobbyConfigSelection;
    UUID activePlayer;
    WordsOnBoardContainer newWordsOnBoard;
    VotingContainer votingContainer;

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public UUID getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(UUID gameSessionId) {
        this.gameSessionId = gameSessionId;
    }

    public List<Square> getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(List<Square> gameBoard) {
        this.gameBoard = gameBoard;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public void setLocalPlayer(Player localPlayer) {
        this.localPlayer = localPlayer;
    }

    public Rack getLetterTokens() {
        return letterTokens;
    }

    public void setLetterTokens(Rack letterTokens) {
        this.letterTokens = letterTokens;
    }

    public int getRemainingLetters() {
        return remainingLetters;
    }

    public void setRemainingLetters(int remainingLetters) {
        this.remainingLetters = remainingLetters;
    }

    public LobbyConfigSelection getLobbyConfigSelection() {
        return lobbyConfigSelection;
    }

    public void setLobbyConfigSelection(LobbyConfigSelection lobbyConfigSelection) {
        this.lobbyConfigSelection = lobbyConfigSelection;
    }

    public UUID getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(UUID activePlayer) {
        this.activePlayer = activePlayer;
    }

    public WordsOnBoardContainer getNewWordsOnBoard() {
        return newWordsOnBoard;
    }

    public void setNewWordsOnBoard(WordsOnBoardContainer newWordsOnBoard) {
        this.newWordsOnBoard = newWordsOnBoard;
    }

    public VotingContainer getVotingContainer() {
        return votingContainer;
    }

    public void setVotingContainer(VotingContainer votingContainer) {
        this.votingContainer = votingContainer;
    }

    public static PlayerGameState getPrivatePlayerGameState(RubbelGameSession gameSession, UUID playerId) {
        PlayerGameState gameState = new PlayerGameState();
        gameState.setGameSessionId(gameSession.getGameSessionId());
        gameState.setCurrentTask(gameSession.getCurrentTask());
        gameState.setPlayers(gameSession.getPlayers());
        gameState.setNewWordsOnBoard(gameSession.getNewWordsOnBoardContainer());
        gameState.setLocalPlayer(gameSession.getPlayerById(playerId));
        gameState.setGameBoard(gameSession.getGameBoard());
        gameState.setRemainingLetters(gameSession.getRemainingLetters());
        gameState.setLobbyConfigSelection(gameSession.getLobbyConfigSelection());
        gameState.setLetterTokens(gameSession.getRackByPlayerId(playerId));
        gameState.setActivePlayer(gameSession.getActivePlayer());
        gameState.setVotingContainer(gameSession.getVotingContainer());
        return gameState;
    }

}
