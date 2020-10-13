package com.section9.rubbel.models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataContainer extends HashMap<String, Object> {

    private static String PURPOSE_KEY = "purpose";
    private static String GAME_SESSION_ID_KEY = "gameSessionId";
    private static String RECEIVER_ID_KEY = "receiverId";


    Map<String, Object> payload;

    public DataContainer(String purpose, UUID gameSessionId) {
        this.payload = new HashMap<>();
        addEntry(PURPOSE_KEY, purpose);
        addEntry(GAME_SESSION_ID_KEY, gameSessionId);
    }

    public DataContainer(String purpose, UUID gameSessionId, UUID receiverId) {
        this.payload = new HashMap<>();
        addEntry(PURPOSE_KEY, purpose);
        addEntry(GAME_SESSION_ID_KEY, gameSessionId);
        addEntry(RECEIVER_ID_KEY, receiverId);
    }

    public void addEntry(String key, Object obj) {
        this.put(key, obj);
    }

    public void removeEntry(String key) {
        this.remove(key);
    }

    public UUID getReceiverId() {
        return (UUID) get(RECEIVER_ID_KEY);
    }
    public static DataContainer create(String purpose, UUID gameSessionId, UUID receiverId) {
        return new DataContainer(purpose, gameSessionId, receiverId);
    }
}
