package com.section9.rubbel.tasks;

import com.section9.rubbel.tasks.base.PlayerAction;
import com.section9.rubbel.tasks.base.PlayerTask;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerTaskDTO {
    public String id;
    Map<String, Object> taskData;
    public boolean modal = false;
    public boolean takeOverTime;
    public List<UUID> owners;
    public List<PlayerAction> actions;
    public long endTimestamp = 0;
    public UUID instanceId;

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

    public boolean isModal() {
        return modal;
    }

    public void setModal(boolean modal) {
        this.modal = modal;
    }

    public boolean isTakeOverTime() {
        return takeOverTime;
    }

    public void setTakeOverTime(boolean takeOverTime) {
        this.takeOverTime = takeOverTime;
    }

    public List<UUID> getOwners() {
        return owners;
    }

    public void setOwners(List<UUID> owners) {
        this.owners = owners;
    }

    public List<PlayerAction> getActions() {
        return actions;
    }

    public void setActions(List<PlayerAction> actions) {
        this.actions = actions;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public UUID getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(UUID instanceId) {
        this.instanceId = instanceId;
    }

    public static PlayerTaskDTO from(PlayerTask playerTask) {
        PlayerTaskDTO dto = new PlayerTaskDTO();
        dto.setId(playerTask.getId());
        dto.setActions(playerTask.getActions());
        dto.setEndTimestamp(playerTask.getEndTimestamp());
        dto.setModal(playerTask.isModal());
        dto.setOwners(playerTask.getOwners());
        dto.setTaskData(playerTask.getTaskData());
        dto.setTakeOverTime(playerTask.isTakeOverTime());
        dto.setInstanceId(playerTask.getInstanceId());
        return dto;
    }


}
