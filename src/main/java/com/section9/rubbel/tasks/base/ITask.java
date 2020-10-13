package com.section9.rubbel.tasks.base;

import com.section9.rubbel.models.RubbelGameSession;

public interface ITask {

    public boolean taskCompletable(RubbelGameSession gameSession);

    public void executeStartOperations(RubbelGameSession gameSession);

}
