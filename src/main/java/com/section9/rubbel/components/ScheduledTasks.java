package com.section9.rubbel.components;

import com.section9.rubbel.models.RubbelGameSession;
import com.section9.rubbel.services.GameSessionManager;
import com.section9.rubbel.tasks.base.PlayerTask;
import com.section9.rubbel.tasks.base.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@EnableScheduling
@Component
public class ScheduledTasks {


    @Autowired
    GameSessionManager gameSessionManager;

    //@Scheduled(cron = "0 0/1 * * * *")
    //@Scheduled(cron = "0 0/15 * * * *") every 15 minutes // @Scheduled(fixedRate = 30000)

    /*@Scheduled(fixedRate = 1000)
    public void decrementTimers() {
        Map<UUID, RubbelGameSession> gameSessions = gameSessionManager.getRubbelGameSessions();
        gameSessions.values().forEach(gameSession -> {
            Task task = gameSession.getCurrentTask();
            if (task instanceof PlayerTask) {
                PlayerTask playerTask = (PlayerTask) task;
                if(playerTask.getDelay() >= 0) {
                    playerTask.setDelay(playerTask.getDelay() - 1000);
                    System.out.println("gameSession: " + gameSession.getGameSessionId() + " | task: " + playerTask.getId() + " | delay:" + playerTask.getDelay());
                }
            }

        });

    }*/

}
