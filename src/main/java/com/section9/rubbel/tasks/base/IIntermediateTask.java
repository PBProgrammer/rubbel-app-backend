package com.section9.rubbel.tasks.base;

import com.section9.rubbel.models.RubbelGameSession;

public interface IIntermediateTask {

    public Action getResolvedAction(RubbelGameSession gameSession);

}
