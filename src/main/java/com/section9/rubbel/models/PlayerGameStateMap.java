package com.section9.rubbel.models;

import com.section9.rubbel.tasks.PlayerTaskDTO;
import com.section9.rubbel.tasks.base.PlayerTask;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerGameStateMap extends HashMap<String, Object> {

    public static final String CURRENT_TASK_KEY = "currentTask";
    public static final String GAME_SESSION_ID_KEY = "gameSessionId";
    public static final String GAME_BOARD_KEY = "gameBoard";
    public static final String PLAYERS_KEY = "players";
    public static final String LOCAL_PLAYER_KEY = "localPlayer";
    public static final String LETTER_TOKENS_KEY = "letterTokens";
    public static final String REMAINING_LETTERS_KEY = "remainingLetters";
    public static final String LOBBY_CONFIG_SELECTION_KEY = "lobbyConfigSelection";
    public static final String ACTIVE_PLAYER_KEY = "activePlayer";
    public static final String NEW_WORDS_ON_BOARD_KEY = "newWordsOnBoard";
    public static final String LAST_VETO_PROOF_GIVEN_KEY = "votingContainer";

    public WordsOnBoardContainer getNewWordsOnBoard() {
        return (WordsOnBoardContainer) this.get(NEW_WORDS_ON_BOARD_KEY);
    }

    public void setNewWordsOnBoard(WordsOnBoardContainer newWordsOnBoard) {
        this.put(NEW_WORDS_ON_BOARD_KEY, newWordsOnBoard);
    }

    public void setVotingContainer(VotingContainer lastVetoProofGiven) {
        this.put(LAST_VETO_PROOF_GIVEN_KEY, lastVetoProofGiven);
    }

    public VotingContainer getVotingContainer() {
        return (VotingContainer) this.get(LAST_VETO_PROOF_GIVEN_KEY);
    }

    public PlayerTaskDTO getCurrentTask() {
        return (PlayerTaskDTO) this.get(CURRENT_TASK_KEY);
    }

    public void setCurrentTask(PlayerTaskDTO currentTask) {
        this.put(CURRENT_TASK_KEY, currentTask);
    }

    public UUID getGameSessionId() {
        return (UUID) this.get(GAME_SESSION_ID_KEY);
    }

    public void setGameSessionId(UUID gameSessionId) {
        this.put(GAME_SESSION_ID_KEY, gameSessionId);
    }

    public List<Square> getGameBoard() {
        return (List<Square>) this.get(GAME_BOARD_KEY);
    }

    public void setGameBoard(List<Square> gameBoard) {
        this.put(GAME_BOARD_KEY, gameBoard);
    }

    public List<Player> getPlayers() {
        return (List<Player>) this.get(PLAYERS_KEY);
    }

    public void setPlayers(List<Player> players) {
        this.put(PLAYERS_KEY, players);
    }

    public Player getLocalPlayer() {
        return (Player) this.get(LOCAL_PLAYER_KEY);
    }

    public void setLocalPlayer(Player localPlayer) {
        this.put(LOCAL_PLAYER_KEY, localPlayer);
    }

    public Rack getLetterTokens() {
        return (Rack) this.get(LETTER_TOKENS_KEY);
    }

    public void setLetterTokens(Rack letterTokens) {
        this.put(LETTER_TOKENS_KEY, letterTokens);
    }

    public int getRemainingLetters() {
        return (int) this.get(REMAINING_LETTERS_KEY);
    }

    public void setRemainingLetters(int remainingLetters) {
        this.put(REMAINING_LETTERS_KEY, remainingLetters);
    }

    public LobbyConfigSelection getLobbyConfigSelection() {
        return (LobbyConfigSelection) this.get(LOBBY_CONFIG_SELECTION_KEY);
    }

    public void setLobbyConfigSelection(LobbyConfigSelection lobbyConfigSelection) {
        this.put(LOBBY_CONFIG_SELECTION_KEY, lobbyConfigSelection);
    }

    public UUID getActivePlayer() {
        return (UUID) this.get(ACTIVE_PLAYER_KEY);
    }

    public void setActivePlayer(UUID activePlayer) {
        this.put(ACTIVE_PLAYER_KEY, activePlayer);
    }

    public void addEntry(String key, Object obj) {
        this.put(key, obj);
    }

    public void removeEntry(String key) {
        this.remove(key);
    }

    public static PlayerGameStateMap getPrivatePlayerGameState(RubbelGameSession gameSession, UUID playerId) {
        PlayerGameStateMap playerGameStateMap = new PlayerGameStateMap();
        playerGameStateMap.setGameSessionId(gameSession.getGameSessionId());
        playerGameStateMap.setCurrentTask(PlayerTaskDTO.from((PlayerTask) gameSession.getCurrentTask()));
        playerGameStateMap.setPlayers(gameSession.getPlayers());
        playerGameStateMap.setLocalPlayer(gameSession.getPlayerById(playerId));
        playerGameStateMap.setGameBoard(gameSession.getGameBoard());
        playerGameStateMap.setRemainingLetters(gameSession.getRemainingLetters());
        playerGameStateMap.setLobbyConfigSelection(gameSession.getLobbyConfigSelection());
        playerGameStateMap.setLetterTokens(gameSession.getRackByPlayerId(playerId));
        playerGameStateMap.setActivePlayer(gameSession.getActivePlayer());
        playerGameStateMap.setNewWordsOnBoard(gameSession.getNewWordsOnBoardContainer());
        playerGameStateMap.setVotingContainer(gameSession.getVotingContainer());
        return playerGameStateMap;
    }

    public static PlayerGameStateMap getPartialPlayerGameStateMap(RubbelGameSession gameSession, UUID playerId, List<String> propertyKeys) {
        PlayerGameStateMap playerGameStateMap = new PlayerGameStateMap();
        propertyKeys.forEach(key -> {
            playerGameStateMap.addEntry(key, getPrivateEntryByPropertyKey(gameSession, key, playerId));
        });
        return playerGameStateMap;
    }

    private static Object getPrivateEntryByPropertyKey(RubbelGameSession gameSession, String key, UUID playerId) {
        switch (key) {
            case LOCAL_PLAYER_KEY:
                return gameSession.getPlayerById(playerId);
            case LETTER_TOKENS_KEY:
                return gameSession.getRackByPlayerId(playerId);
            case GAME_SESSION_ID_KEY:
                return gameSession.getGameSessionId();
            case GAME_BOARD_KEY:
                return gameSession.getGameBoard();
            case PLAYERS_KEY:
                return gameSession.getPlayers();
            case REMAINING_LETTERS_KEY:
                return gameSession.getRemainingLetters();
            case LOBBY_CONFIG_SELECTION_KEY:
                return gameSession.getLobbyConfigSelection();
            case ACTIVE_PLAYER_KEY:
                return gameSession.getActivePlayer();
            case CURRENT_TASK_KEY:
                return PlayerTaskDTO.from((PlayerTask) gameSession.getCurrentTask());
            default:
                return null;
        }
    }


}
