package com.section9.rubbel.models;

import com.section9.rubbel.enums.PlayerActionType;
import com.section9.rubbel.tasks.IntermediateTasks.CheckWordTask;
import com.section9.rubbel.tasks.IntermediateTasks.IsGameFinishedTask;
import com.section9.rubbel.tasks.IntermediateTasks.RoundEvaluationTask;
import com.section9.rubbel.tasks.IntermediateTasks.VetoEvaluationTask;
import com.section9.rubbel.tasks.PlayerTasks.*;
import com.section9.rubbel.tasks.base.*;

import java.util.List;
import java.util.UUID;

public class TaskFactory {

    public static Task getNextTaskById(String TASK_ID, RubbelGameSession gameSession) {
        switch (TASK_ID) {
            case PlayerTask.LOBBY_TASK_ID:
                return getLobbyTask(gameSession.getHost().getId());
            case PlayerTask.WAIT_AFTER_START_TASK_ID:
                return getWaitAfterStartTask(gameSession.getHost().getId());
            case PlayerTask.PLAYER_TURN_TASK_ID:
                return getPlayerTurnTask(gameSession.getActivePlayer());
            case PlayerTask.SWITCH_LETTER_TOKENS_TASK_ID:
                return getSwitchLetterTokensTask(gameSession.getActivePlayer());
            case PlayerTask.VETO_OR_CONFIRM_WORD_TASK:
                return getVetoOrConfirmWordTask(gameSession);
            case IntermediateTask.IS_GAME_FINISHED_TASK_ID:
                return getIsGameFinishedTask();
            case IntermediateTask.ROUND_EVALUATION_ID:
                return getRoundEvaluationTask(gameSession);
            case PlayerTask.GAME_OVER_TASK:
                return getGameOverTask(gameSession.getPlayerIds());
            case PlayerTask.SHARE_PROOF_AFTER_VETO_TASK:
                return getShareProofAfterVetoTask(gameSession);
            case PlayerTask.VOTE_ON_PROVIDED_PROOF_TASK:
                return getVoteOnProvidedProofTask(gameSession);
//            case IntermediateTask.VETO_EVALUATION_ID :
//                return getVetoEvaluationTask(gameSession);
        }
        return gameSession.getCurrentTask();
        //TODO rollback stuff there was an error
    }

    private static Task getGameOverTask(List<UUID> owners) {
        return new GameOverTask(PlayerTask.GAME_OVER_TASK, owners, new PlayerAction());
    }

    private static LobbyTask getLobbyTask(UUID owner) {
        PlayerAction playerAction = new PlayerAction(PlayerAction.START_GAME_ACTION_ID, PlayerTask.WAIT_AFTER_START_TASK_ID, PlayerActionType.COMPLETE);
        return new LobbyTask(PlayerTask.LOBBY_TASK_ID,
                owner,
                playerAction
        );
    }

    private static WaitAfterStartTask getWaitAfterStartTask(UUID host) {
        final PlayerAction timerAction = new PlayerAction(PlayerAction.WAIT_AFTER_START_GAME_ACTION_ID, PlayerTask.PLAYER_TURN_TASK_ID, PlayerActionType.AUTO_COMPLETE);
        return new WaitAfterStartTask(PlayerTask.WAIT_AFTER_START_TASK_ID,
                host,
                timerAction
        );
    }

    private static PlayerTurnTask getPlayerTurnTask(UUID owner) {
        final List<PlayerAction> playerActions = List.of(
                new PlayerAction(PlayerAction.FINALIZE_PLACED_LETTERS_ACTION_ID, PlayerTask.VETO_OR_CONFIRM_WORD_TASK, PlayerActionType.COMPLETE),
                new PlayerAction(PlayerAction.OPEN_SWITCH_LETTERS_ACTION_ID, PlayerTask.SWITCH_LETTER_TOKENS_TASK_ID, PlayerActionType.COMPLETE),
                new PlayerAction(PlayerAction.PASS_TURN_ACTION_ID, IntermediateTask.IS_GAME_FINISHED_TASK_ID, PlayerActionType.COMPLETE)
        );
        return new PlayerTurnTask(PlayerTask.PLAYER_TURN_TASK_ID,
                List.of(owner),
                playerActions
        );
    }

    private static SwitchLetterTokensTask getSwitchLetterTokensTask(UUID owner) {
        final List<PlayerAction> playerActions = List.of(
                new PlayerAction(PlayerAction.CONFIRM_SWITCH_LETTERS_ACTION_ID, PlayerTask.PLAYER_TURN_TASK_ID, PlayerActionType.COMPLETE),
                new PlayerAction(PlayerAction.ABORT_ACTION_ID, PlayerTask.PLAYER_TURN_TASK_ID, PlayerActionType.ABORT)
        );
        return new SwitchLetterTokensTask(PlayerTask.SWITCH_LETTER_TOKENS_TASK_ID,
                owner,
                playerActions
        );
    }

//    private static VetoEvaluationTask getVetoEvaluationTask(RubbelGameSession gameSession) {
//        final List<Action> actions = List.of(new Action(Action.TO_ROUND_EVALUATION_ACTION_ID, IntermediateTask.ROUND_EVALUATION_ID));
//        return new VetoEvaluationTask(IntermediateTask.VETO_EVALUATION_ID, actions);
//    }

    private static RoundEvaluationTask getRoundEvaluationTask(RubbelGameSession gameSession) {
        final List<Action> actions = List.of(new Action(Action.TO_GAME_IS_FINISHED_ACTION_ID, IntermediateTask.IS_GAME_FINISHED_TASK_ID));
        return new RoundEvaluationTask(IntermediateTask.ROUND_EVALUATION_ID, actions);
    }

    private static VetoOrConfirmWordTask getVetoOrConfirmWordTask(RubbelGameSession gameSession) {
        final List<PlayerAction> playerActions = List.of(
                new PlayerAction(PlayerAction.CONFIRM_WORD_ACTION_ID, IntermediateTask.ROUND_EVALUATION_ID, PlayerActionType.COMPLETE),
                new PlayerAction(PlayerAction.VETO_ACTION_ID, PlayerTask.SHARE_PROOF_AFTER_VETO_TASK, PlayerActionType.COMPLETE)
        );
        return new VetoOrConfirmWordTask(PlayerTask.VETO_OR_CONFIRM_WORD_TASK, gameSession.getAllIdsButActivePlayer(), playerActions);
    }

    private static ShareProofAfterVetoTask getShareProofAfterVetoTask(RubbelGameSession gameSession) {
        final List<PlayerAction> playerActions = List.of(
                new PlayerAction(PlayerAction.PROVIDE_PROOF_ACTION_ID, PlayerTask.VOTE_ON_PROVIDED_PROOF_TASK, PlayerActionType.COMPLETE),
                new PlayerAction(PlayerAction.HAS_NO_PROOF_ACTION_ID, IntermediateTask.ROUND_EVALUATION_ID, PlayerActionType.COMPLETE)
        );
        return new ShareProofAfterVetoTask(PlayerTask.SHARE_PROOF_AFTER_VETO_TASK, gameSession.getActivePlayer(), playerActions);
    }

    private static VoteOnProvidedProofTask getVoteOnProvidedProofTask(RubbelGameSession gameSession) {
        final List<PlayerAction> playerActions = List.of(
                new PlayerAction(PlayerAction.ACCEPT_PROOF_ACTION_ID, IntermediateTask.ROUND_EVALUATION_ID, PlayerActionType.COMPLETE),
                new PlayerAction(PlayerAction.REJECT_PROOF_ACTION_ID, IntermediateTask.ROUND_EVALUATION_ID, PlayerActionType.COMPLETE)
        );
        return new VoteOnProvidedProofTask(PlayerTask.VOTE_ON_PROVIDED_PROOF_TASK, gameSession.getAllIdsButActivePlayer(), playerActions);
    }

    private static IsGameFinishedTask getIsGameFinishedTask() {
        final List<Action> actions = List.of(
                new Action(Action.GAME_CONTINUES_ACTION_ID, PlayerTask.PLAYER_TURN_TASK_ID),
                new Action(Action.FINISH_GAME_ACTION_ID, PlayerTask.GAME_OVER_TASK)
                );
        return new IsGameFinishedTask(IntermediateTask.IS_GAME_FINISHED_TASK_ID, actions);
    }

}
