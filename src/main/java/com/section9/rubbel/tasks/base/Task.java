package com.section9.rubbel.tasks.base;

import com.section9.rubbel.enums.PlayerActionType;
import com.section9.rubbel.models.RubbelGameSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Task implements ITask{

    private String id;
    Map<String, Object> taskData;

    public Task(String id) {
        this.id = id;
        this.taskData = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getTaskData() {
        return taskData;
    }

    public void setTaskData(Map<String, Object> taskData) {
        this.taskData = taskData;
    }

    public void addTaskDataEntry(String key, Object entry) {
        taskData.put(key, entry);
    }
}
