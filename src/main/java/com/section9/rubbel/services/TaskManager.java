package com.section9.rubbel.services;

import com.section9.rubbel.models.*;
import com.section9.rubbel.tasks.base.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Stack;

@Service
public class TaskManager {

    @Autowired
    WebSocketService webSocketService;

    /*
    1. execute end operations from old task
    2. get next Task via action.targetTaskId/TaskFactory
    3. execute start operations from new task
    4. -> notify players, distribute next task
    5. if usertask/timertask wait for interaction
        else execute next task
     */

    public void processReceivedPlayerAction(RubbelGameSession gameSession, PlayerAction receivedPlayerAction) {
        Task currentTask = gameSession.getCurrentTask();
        if (currentTask instanceof PlayerTask && gameSession.isCurrentTask(receivedPlayerAction.getOriginTaskId())) {
            PlayerTask currentPlayerTask = (PlayerTask) currentTask;
            applyPlayerActionToPlayerTask(gameSession, receivedPlayerAction);
            if (currentPlayerTask.isAborted()) {
                rollbackToPrecedingPlayerTask(gameSession);
            } else {
                completeTaskIfPossible(gameSession);
            }
            webSocketService.notifyPlayersAboutGameState(gameSession);
        }
    }

    private void applyPlayerActionToPlayerTask(RubbelGameSession gameSession, PlayerAction receivedPlayerAction) {
        PlayerTask currentPlayerTask = (PlayerTask) gameSession.getCurrentTask();
        switch (receivedPlayerAction.getType()) {
            case COMPLETE: {
                if (currentPlayerTask.getOwners().contains(receivedPlayerAction.getExecutor())) {
                    currentPlayerTask.addReceivedPlayerAction(receivedPlayerAction);
                }
            }
            break;
            case AUTO_COMPLETE: {
                if (gameSession.isHost(receivedPlayerAction.getExecutor())) {
                    currentPlayerTask.setAutoComplete(true);
                    currentPlayerTask.addReceivedPlayerAction(receivedPlayerAction);
                }
            }
            break;
            case ABORT: {
                if (currentPlayerTask.getOwners().contains(receivedPlayerAction.getExecutor()) && currentPlayerTask.getOwners().size() == 1) {
                    currentPlayerTask.setAborted(true);
                }
            }
            break;
        }
    }

    private void completeTaskIfPossible(RubbelGameSession gameSession) {
        PlayerTask currentPlayerTask = (PlayerTask) gameSession.getCurrentTask();
        if (currentPlayerTask.taskCompletable(gameSession)) {
            String nextTaskId = completeCurrentPlayerTask(gameSession);
            Task nextTask = TaskFactory.getNextTaskById(nextTaskId, gameSession);
            gameSession.addTask(nextTask);
            if (nextTask instanceof PlayerTask) {
                nextTask.executeStartOperations(gameSession);
                PlayerTask nextPlayerTask = (PlayerTask) nextTask;
                if (nextPlayerTask.isTakeOverTime()) {
                    transferDelay(currentPlayerTask, (PlayerTask) nextTask);
                }
            } else {
                applyActionToIntermediateTask(gameSession, (IntermediateTask) nextTask);
            }
        }
    }

    private void rollbackToPrecedingPlayerTask(RubbelGameSession gameSession) {
        Stack<Task> tasks = gameSession.getTasks();
        PlayerTask abortedTask = (PlayerTask) tasks.pop();
        while (!(tasks.peek() instanceof PlayerTask)) {
            tasks.pop();
        }
        PlayerTask repeatingTask = (PlayerTask) tasks.pop();
        if (abortedTask.isTakeOverTime()) {
            PlayerTask nextTask = (PlayerTask) TaskFactory.getNextTaskById(repeatingTask.getId(), gameSession);
            nextTask.setEndTimestamp(abortedTask.getEndTimestamp());
            tasks.push(nextTask);
        }
    }

    private void transferDelay(PlayerTask sourceTask, PlayerTask targetTask) {
        targetTask.setEndTimestamp(sourceTask.getEndTimestamp());
    }

    private void applyActionToIntermediateTask(RubbelGameSession gameSession, IntermediateTask task) {
        task.executeStartOperations(gameSession);
        Action action = task.getResolvedAction(gameSession);
        Task nextTask = TaskFactory.getNextTaskById(action.getTargetTaskId(), gameSession);
        gameSession.addTask(nextTask);
        if (nextTask instanceof IntermediateTask) {
            applyActionToIntermediateTask(gameSession, (IntermediateTask) nextTask);
        } else {
            nextTask.executeStartOperations(gameSession);
        }
    }

    private boolean hasMatchingAction(PlayerTask task, PlayerAction action) {
        return task.actions.stream().filter(a ->
                a.getId().equals(action.getId()) && action.getExecutor().equals(a.getExecutor())
        ).count() > 0;
    }

    private String completeCurrentPlayerTask(RubbelGameSession gameSession) {
        PlayerTask currentPlayerTask = (PlayerTask) gameSession.getCurrentTask();
        PlayerAction resolvedPlayerAction = currentPlayerTask.resolveFinalPlayerAction(gameSession);
        currentPlayerTask.executeEndOperations(gameSession, resolvedPlayerAction);
        return resolvedPlayerAction.getTargetTaskId();
    }
}
