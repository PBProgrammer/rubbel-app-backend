package com.section9.rubbel.models;

import com.section9.rubbel.services.Constants;
import com.section9.rubbel.services.Util;
import com.section9.rubbel.tasks.base.PlayerTask;
import com.section9.rubbel.tasks.base.Task;

import java.util.*;
import java.util.stream.Collectors;


public class RubbelGameSession {

    private UUID gameSessionId;
    private List<Player> players;
    private Stack<Task> tasks;
    private List<Square> gameBoard;
    private List<Square> fixedGameBoard;
    private Map<UUID, Rack> playerLetterTokenRacks;
    private List<LetterToken> letterTokenBank;
    private int consecutivePasses = 0;
    private LobbyConfigSelection lobbyConfigSelection;
    private GameConfig gameConfig;
    private GameBoardConfig selectedGameBoardConfig;
    private int activePlayerIndex = -1;
    private WordsOnBoardContainer newWordsOnBoardContainer;
    private VotingContainer votingContainer;
    private List<String> lettersWhitelist;

    public RubbelGameSession() {
        players = new ArrayList<>();
        tasks = new Stack<>();
    }

    public List<String> getLettersWhitelist() {
        return lettersWhitelist;
    }

    public void setLettersWhitelist(List<String> lettersWhitelist) {
        this.lettersWhitelist = lettersWhitelist;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<UUID> getPlayerIds() {
        return players.stream().map(Player::getId).collect(Collectors.toList());
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public UUID getId() {
        return gameSessionId;
    }

    public void setGameSessionId(UUID gameSessionId) {
        this.gameSessionId = gameSessionId;
    }

    public boolean hasPlayer(UUID playerId) {
        for (Player player : players) {
            if (player.getId().equals(playerId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isHost(UUID playerId) {
        for (Player player : players) {
            if (player.getId().equals(playerId) && player.isHost()) {
                return true;
            }
        }
        return false;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }


    public Task getCurrentTask() {
        return tasks.peek();
    }

    public LobbyConfigSelection getLobbyConfigSelection() {
        return lobbyConfigSelection;
    }

    public void setLobbyConfigSelection(LobbyConfigSelection lobbyConfigSelection) {
        this.lobbyConfigSelection = lobbyConfigSelection;
    }

    public boolean isFull() {
        return this.players.size() == Constants.MAX_PLAYER_LIMIT;
    }

    public List<Player> getOpponents(UUID ownId) {
        return players
                .stream()
                .filter(p -> !p.getId().equals(ownId))
                .collect(Collectors.toList());
    }

    public List<UUID> getOpponentIds(UUID ownId) {
        return players
                .stream()
                .filter(p -> !p.getId().equals(ownId))
                .map(Player::getId)
                .collect(Collectors.toList());
    }

    public UUID getGameSessionId() {
        return gameSessionId;
    }

    public List<Square> getGameBoard() {
        return gameBoard;
    }

    public List<Square> getFixedGameBoard() {
        return fixedGameBoard;
    }

    public void setFixedGameBoard(List<Square> fixedGameBoard) {
        this.fixedGameBoard = fixedGameBoard;
    }

    public void setGameBoard(List<Square> gameBoard) {
        this.gameBoard = gameBoard;
    }

    public GameBoardConfig getSelectedGameBoardConfig() {
        return selectedGameBoardConfig;
    }

    public void setSelectedGameBoardConfig(GameBoardConfig selectedGameBoardConfig) {
        this.selectedGameBoardConfig = selectedGameBoardConfig;
    }

    public Stack<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Stack<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        this.tasks.push(task);
    }


    public int getConsecutivePasses() {
        return consecutivePasses;
    }

    public void setConsecutivePasses(int consecutivePasses) {
        this.consecutivePasses = consecutivePasses;
    }

    public void resetConsecutivePasses() {
        consecutivePasses = 0;
    }

    public void incrementConsecutivePasses() {
        consecutivePasses++;
    }

    public Player getPlayerById(UUID playerId) {
        return players
                .stream()
                .filter(player -> player.getId().equals(playerId))
                .findFirst()
                .orElseThrow();
    }

    public List<LetterToken> getLetterTokenBank() {
        return letterTokenBank;
    }

    public void setLetterTokenBank(List<LetterToken> letterTokenBank) {
        this.letterTokenBank = letterTokenBank;
    }

    public UUID getActivePlayer() {
        if (activePlayerIndex < 0) {
            return null;
        }
        return players.get(activePlayerIndex).getId();
    }

    public int getRemainingLetters() {
        return letterTokenBank != null ? letterTokenBank.size() : 0;
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public void setGameConfig(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public WordsOnBoardContainer getNewWordsOnBoardContainer() {
        return newWordsOnBoardContainer;
    }

    public void updateNewWordsOnBoardContainer(UUID owner, List<Word> newWordsOnBoard) {
            this.newWordsOnBoardContainer = new WordsOnBoardContainer(owner, newWordsOnBoard);
    }

    public VotingContainer getVotingContainer() {
        return votingContainer;
    }

    public void setVotingContainer(VotingContainer votingContainer) {
        this.votingContainer = votingContainer;
    }

    public Rack getRackByPlayerId(UUID playerId) {
        if (hasPlayer(playerId) && playerLetterTokenRacks != null) {
            return playerLetterTokenRacks.get(playerId);
        }
        return null;
    }

    public Player getHost() {
        return players
                .stream()
                .filter(Player::isHost)
                .findFirst()
                .get();
    }

    public List<Player> getAllButHost() {
        return players
                .stream()
                .filter(player -> !player.isHost())
                .collect(Collectors.toList());
    }

    public List<Player> getAllButActivePlayer() {
        return players
                .stream()
                .filter(player -> !player.getId().equals(getActivePlayer()))
                .collect(Collectors.toList());
    }

    public List<UUID> getAllIdsButActivePlayer() {
        return players
                .stream()
                .filter(player -> !player.getId().equals(getActivePlayer()))
                .map(player -> player.getId())
                .collect(Collectors.toList());
    }

    public Map<UUID, Rack> getPlayerLetterTokenRacks() {
        return playerLetterTokenRacks;
    }

    public void setPlayerLetterTokenRacks(Map<UUID, Rack> playerLetterTokenRacks) {
        this.playerLetterTokenRacks = playerLetterTokenRacks;
    }

    public void updatePlayerName(UUID playerId, String newName) {
        getPlayerById(playerId).setName(newName);

    }

    public void updatePlayerIcon(UUID playerId, String icon) {
        getPlayerById(playerId).setIcon(icon);
    }

    public void updatePlayerRackOrder(UUID playerId, Rack newTokens) {
        List<UUID> tokenIds = newTokens.stream().map(token -> token.getId()).collect(Collectors.toList());
        Rack playerRack = getRackByPlayerId(playerId);
        if(newTokens.size() == playerRack.size() && playerRack.stream().allMatch(letterToken -> tokenIds.contains(letterToken.getId()))) {
            updateRackByPlayerId(playerId, newTokens);
        }
    }

    public void updatePlayerReadyStatus(UUID playerId, boolean ready) {
        getPlayerById(playerId).setReady(ready);
    }

    public boolean isCurrentTask(String taskId) {
        if (getCurrentTask() != null) {
            return getCurrentTask().getId().equals(taskId);
        }
        return false;
    }

    public void updateTimerDelay(Tick tick) {
        ((PlayerTask) getCurrentTask()).setEndTimestamp(tick.timerDelay);
    }

    public void updateRackByPlayerId(UUID playerId, Rack letterTokens) {
        playerLetterTokenRacks.put(playerId, letterTokens);
    }

    public void determineAndSetNextPlayer() {
        setActivePlayerIndex((getActivePlayerIndex() + 1) % players.size());
    }

    public int getActivePlayerIndex() {
        return activePlayerIndex;
    }

    public void setActivePlayerIndex(int activePlayerIndex) {
        this.activePlayerIndex = activePlayerIndex;
    }

    public int addLetterTokenToTempGameBoard(Square updatedSquare) {
        int clearIndex = -1;
        Square foundGameBoardSquare = getGameBoard().get(updatedSquare.getIndex());
        for (Square gameBoardSquare : getGameBoard()) {
            if (gameBoardSquare.getLetterToken() != null && gameBoardSquare.getLetterToken().getId().equals(updatedSquare.getLetterToken().getId())) {
                gameBoardSquare.setLetterToken(null);
                clearIndex = gameBoardSquare.getIndex();
            }
        }
        foundGameBoardSquare.setLetterToken(updatedSquare.getLetterToken());
        return clearIndex;
    }

    public LetterToken removeLetterTokenFromTempGameBoard(Square updatedSquare) {
        Square foundGameBoardSquare = getGameBoard().get(updatedSquare.getIndex());
        LetterToken movingLetterToken = foundGameBoardSquare.getLetterToken();
        foundGameBoardSquare.setLetterToken(null);
        return movingLetterToken;
    }

    public void resetGameBoardToFixedState() {
        List<Square> copiedGameBoard = (List<Square>) Util.deepCopy(getFixedGameBoard());
        setGameBoard(copiedGameBoard);
    }

    public void transferUnfixedLetterTokensOnBoardBackToActivePlayer() {
        List<LetterToken> unfixedLetterTokens = getGameBoard()
                .stream()
                .filter(square -> square.getLetterToken() != null && !square.isFixed())
                .map(Square::getLetterToken)
                .collect(Collectors.toList());
        getRackByPlayerId(getActivePlayer()).addAll(unfixedLetterTokens);
    }

    public List<Square> getSquaresWithUnfixedLetterTokens() {
        return getGameBoard()
                .stream()
                .filter(Square::hasUnfixedLetterToken)
                .collect(Collectors.toList());
    }

    public static RubbelGameSession create() {
        RubbelGameSession gameSession = new RubbelGameSession();
        gameSession.setGameSessionId(UUID.randomUUID());
        return gameSession;
    }

    public void fixLetterTokens() {
        getSquaresWithUnfixedLetterTokens().forEach(square -> square.setFixed(true));
    }

    public void updateSquareSymbol(int index, String symbol) {
        gameBoard.get(index).getLetterToken().setSymbol(symbol);
    }

    public void setNewWordsOnBoardContainer(WordsOnBoardContainer newWordsOnBoardContainer) {
        this.newWordsOnBoardContainer = newWordsOnBoardContainer;
    }
}
