package com.section9.rubbel.services;

import com.section9.rubbel.models.DataContainer;
import com.section9.rubbel.models.LetterToken;
import com.section9.rubbel.models.RubbelGameSession;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.section9.rubbel.services.Constants.*;
import static com.section9.rubbel.tasks.base.PlayerTask.LOBBY_TASK_ID;

public class Util {

    String id;

    //TASK NAMES
    private static final String PLAYER_TURN_NAME = "PLAYER_TURN";

    //TASK IDS
    public static final UUID PLAYER_TURN_ID = UUID.randomUUID();

    public static String getRandomPlayerName() {
        return String.format("Player%d", (int) (Math.random() * 100));
    }

    public static DataContainer getSessionLimitReachedDataContainer(UUID gameSessionId) {
        DataContainer isFullDTC = new DataContainer(RESPONSE_PURPOSE_SESSION_FULL, gameSessionId);
        return isFullDTC;
    }

    public static DataContainer getSessionIsAlreadyPlaying(UUID gameSessionId) {
        DataContainer alreadyPlayingDTC = new DataContainer(RESPONSE_PURPOSE_SESSION_ALREADY_PLAYING, gameSessionId);
        return alreadyPlayingDTC;
    }

    public static long getEndTimestamp(long delay) {
        long dateTime = new Date().getTime();
        return dateTime + delay;
    }

    public static boolean isInLobbyTask(RubbelGameSession gameSession) {
        if (gameSession != null) {
            return gameSession.getCurrentTask().getId().equals(LOBBY_TASK_ID);
        }
        return false;
    }

    public static LetterToken getAndRemoveRandomLetterTokenFromBank(List<LetterToken> letterTokenBank) {
        if (letterTokenBank.size() == 0)
            return null;
        if (letterTokenBank.size() == 1)
            return letterTokenBank.get(0);
        Random random = new Random();
        int index = random.nextInt(letterTokenBank.size());
        LetterToken letterToken = letterTokenBank.get(index);
        letterTokenBank.remove(index);
        return letterToken;
    }

    public static Object deepCopy(Object o) {
        Object obj = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(baos).writeObject(o);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

            obj = new ObjectInputStream(bais).readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
