package com.section9.rubbel.services;

import com.section9.rubbel.models.ConfigSelectOption;
import com.section9.rubbel.models.GameBoardConfig;
import com.section9.rubbel.models.LanguageConfig;
import com.section9.rubbel.models.LobbyConfigSelection;

import java.util.List;
import java.util.UUID;

public class Constants {

    //DTC OUTGOING PURPOSES
    public static final String RESPONSE_PURPOSE_CREATED_ROOM = "created_room";
    public static final String RESPONSE_PURPOSE_JOIN_ROOM = "join_room";
    public static final String RESPONSE_PURPOSE_SESSION_FULL = "session_full";
    public static final String RESPONSE_PURPOSE_SESSION_ALREADY_PLAYING = "already_playing";
    public static final String RESPONSE_PURPOSE_OPPONENT_JOINED = "opponent_joined";
    public static final String RESPONSE_PURPOSE_UPDATE_GAME_STATE = "update_game_state";
    public static final String RESPONSE_PURPOSE_UPDATE_SINGLE_GAME_STATE_ENTRY = "update__single_game_state_entry";
    public static final String RESPONSE_PURPOSE_UPDATE_PARTIAL_GAME_STATE = "update_partial_game_state";
    public static final String RESPONSE_PURPOSE_UPDATE_GAME_BOARD = "update_game_board";
    public static final String RESPONSE_PURPOSE_UPDATE_LETTER_TOKENS = "update_letter_tokens";
    public static final String RESPONSE_PURPOSE_SUBMIT_PROOF = "submit-proof";



    public static final int MAX_PLAYER_LIMIT = 4;

    //PARAMETER KEYS
    public static final String PLAYER_GAME_STATE_PARAM = "playerGameState";
    public static final String PLAYERS_PARAM = "players";
    public static final String BOARD_UPDATE_PARAM = "boardUpdate";

}
