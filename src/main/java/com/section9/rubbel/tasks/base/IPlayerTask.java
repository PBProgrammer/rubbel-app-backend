package com.section9.rubbel.tasks.base;

import com.section9.rubbel.models.RubbelGameSession;

public interface IPlayerTask {

    public void executeEndOperations(RubbelGameSession gameSession, PlayerAction resolvedAction);

    public PlayerAction resolveFinalPlayerAction(RubbelGameSession gameSession);

}
