package com.section9.rubbel.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.section9.rubbel.GameSessionConfigs.GameBoardConfigSource;
import com.section9.rubbel.GameSessionConfigs.LanguageConfigSource;
import com.section9.rubbel.models.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StaticValues {

    public static final String DEBUG_LABEL = "DEBUG";
    public static final int DEBUG_TIME = 10000; // 2000000;
    public static final TemplateConfigSelectOption DEBUG_OPTION = new TemplateConfigSelectOption(DEBUG_LABEL, DEBUG_TIME, true);


    enum ConfigType {
        START_DELAY,
        TIME_PER_TURN,
        VETO_TIME,
        PROOF_TIME
    }

    //CONFIG VALUES
    public static LanguageConfig englishConfig;
    public static LanguageConfig hebrewConfig;

    public static List<LanguageConfig> languageConfigOptions = loadLanguageConfigs();
    public static GameBoardConfig gameBoardConfig = loadGameBoardConfig();
    public static TemplateConfigSelectOption languagePreSelection = languageConfigOptions
            .stream()
            .map(LanguageConfig::getKey)
            .findFirst()
            .map(key -> new TemplateConfigSelectOption(key, key, true))
            .get();


    public static LobbyConfigSelection defaultLobbyConfigSelection;

    public static List<TemplateConfigSelectOption> START_DELAYS_OPTIONS = List.of(
            new TemplateConfigSelectOption("1s", 1000, true),
            new TemplateConfigSelectOption("3s", 3000),
            new TemplateConfigSelectOption("5s", 5000),
            new TemplateConfigSelectOption("10s", 1000),
            new TemplateConfigSelectOption("15s", 15000));

    public static List<TemplateConfigSelectOption> TIME_PER_TURN_OPTIONS = List.of(
            new TemplateConfigSelectOption("10s", 10000),
            new TemplateConfigSelectOption("30s", 30000),
            new TemplateConfigSelectOption("45s", 45000),
            new TemplateConfigSelectOption("1min", 60000),
            new TemplateConfigSelectOption("2min", 120000),
            new TemplateConfigSelectOption("3min", 180000, true),
            new TemplateConfigSelectOption("5min", 300000),
            new TemplateConfigSelectOption("10min", 600000),
            DEBUG_OPTION
    );
    public static List<TemplateConfigSelectOption> VETO_TIMES_OPTIONS = List.of(
            new TemplateConfigSelectOption("10s", 10000),
            new TemplateConfigSelectOption("20s", 20000),
            new TemplateConfigSelectOption("40s", 40000),
            new TemplateConfigSelectOption("50s", 50000),
            new TemplateConfigSelectOption("1min", 60000),
            new TemplateConfigSelectOption("3min", 180000, true),
            new TemplateConfigSelectOption("5min", 300000),
            new TemplateConfigSelectOption("10min", 600000),
            DEBUG_OPTION
    );

    public static List<TemplateConfigSelectOption> PROOF_TIMES_OPTIONS = List.of(
            new TemplateConfigSelectOption("10s", 10000),
            new TemplateConfigSelectOption("20s", 20000),
            new TemplateConfigSelectOption("30s", 30000),
            new TemplateConfigSelectOption("40s", 40000),
            new TemplateConfigSelectOption("50s", 50000),
            new TemplateConfigSelectOption("1min", 60000),
            new TemplateConfigSelectOption("3min", 180000, true),
            new TemplateConfigSelectOption("5min", 300000),
            new TemplateConfigSelectOption("10min", 600000),
            DEBUG_OPTION);


    public static LobbyConfigSelection getDefaultLobbyConfigSelection() {
        LobbyConfigSelection lobbyConfigSelection = new LobbyConfigSelection();
        lobbyConfigSelection.setLanguageSelection(languagePreSelection);
        lobbyConfigSelection.setProofTimeSelection(getLobbyConfigPreSelection(ConfigType.PROOF_TIME));
        lobbyConfigSelection.setStartDelaySelection(getLobbyConfigPreSelection(ConfigType.START_DELAY));
        lobbyConfigSelection.setTimePerTurnSelection(getLobbyConfigPreSelection(ConfigType.TIME_PER_TURN));
        lobbyConfigSelection.setVetoTimeSelection(getLobbyConfigPreSelection(ConfigType.VETO_TIME));
        return lobbyConfigSelection;
    }

    public static ConfigSelectOption getLobbyConfigPreSelection(ConfigType configType) {
        switch (configType) {
            case TIME_PER_TURN:
                return getPreSelectedConfigOption(TIME_PER_TURN_OPTIONS);
            case START_DELAY:
                return getPreSelectedConfigOption(START_DELAYS_OPTIONS);
            case PROOF_TIME:
                return getPreSelectedConfigOption(PROOF_TIMES_OPTIONS);
            case VETO_TIME:
                return getPreSelectedConfigOption(VETO_TIMES_OPTIONS);
        }
        return DEBUG_OPTION;
    }

    public static ConfigSelectOption getPreSelectedConfigOption(List<TemplateConfigSelectOption> configSelectOptions) {
        List<TemplateConfigSelectOption> filteredConfigSelectOptions = configSelectOptions
                .stream()
                .filter(option -> option.isPreSelect() || option.getLabel().equals(DEBUG_LABEL))
                .collect(Collectors.toList());

        Optional<TemplateConfigSelectOption> debugOption = filteredConfigSelectOptions
                .stream()
                .filter(option -> option.getLabel().equals(DEBUG_LABEL))
                .findFirst();

        if (debugOption.isPresent()) {
            return debugOption.get();
        }

        return filteredConfigSelectOptions
                .stream()
                .findFirst()
                .orElseThrow();
    }

    public static GameConfigOptions getGameConfigOptions() {
        GameConfigOptions gameConfigOptions = new GameConfigOptions();
        List<LanguageConfig> languageConfig = languageConfigOptions;
        gameConfigOptions.setLanguageOptions(
                languageConfig
                        .stream()
                        .map(option -> new ConfigSelectOption(option.getKey(), option.getKey()))
                        .collect(Collectors.toList())
        );
        gameConfigOptions.setStartDelays(getStartDelaysOptions()
                .stream()
                .map(templateConfigSelectOption -> templateConfigSelectOption.toConfigSelectOption())
                .collect(Collectors.toList()));

        gameConfigOptions.setProofTimes(getProofTimesOptions().stream()
                .map(templateConfigSelectOption -> templateConfigSelectOption.toConfigSelectOption())
                .collect(Collectors.toList()));

        gameConfigOptions.setTimePerTurns(getTimePerTurnOptions().stream()
                .map(templateConfigSelectOption -> templateConfigSelectOption.toConfigSelectOption())
                .collect(Collectors.toList()));

        gameConfigOptions.setVetoTimes(getVetoTimesOptions().stream()
                .map(templateConfigSelectOption -> templateConfigSelectOption.toConfigSelectOption())
                .collect(Collectors.toList()));
        return gameConfigOptions;

    }

    private static List<com.section9.rubbel.models.LanguageConfig> loadLanguageConfigs() {
        ObjectMapper objMapper = new ObjectMapper();
        try {
            Map<String, Object> value = objMapper.readValue(LanguageConfigSource.LANGUAGE_CONFIG, Map.class);
            Map<String, Object> distributions = (Map<String, Object>) value.get("distributions");
            Map<String, Object> english = (Map<String, Object>) distributions.get("english");
            com.section9.rubbel.models.LanguageConfig englishConfig = objMapper.convertValue(english, com.section9.rubbel.models.LanguageConfig.class);

            Map<String, Object> hebrew = (Map<String, Object>) distributions.get("hebrew");
            com.section9.rubbel.models.LanguageConfig hebrewConfig = objMapper.convertValue(hebrew, com.section9.rubbel.models.LanguageConfig.class);

            return List.of(englishConfig, hebrewConfig);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static GameBoardConfig loadGameBoardConfig() {
        ObjectMapper objMapper = new ObjectMapper();
        try {
            GameBoardConfig gameboardConfig = objMapper.readValue(GameBoardConfigSource.GAMEBOARD_CONFIG, GameBoardConfig.class);
            return gameboardConfig;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<TemplateConfigSelectOption> getStartDelaysOptions() {
        return START_DELAYS_OPTIONS;
    }

    public static List<TemplateConfigSelectOption> getTimePerTurnOptions() {
        return TIME_PER_TURN_OPTIONS;
    }

    public static List<TemplateConfigSelectOption> getVetoTimesOptions() {
        return VETO_TIMES_OPTIONS;
    }

    public static List<TemplateConfigSelectOption> getProofTimesOptions() {
        return PROOF_TIMES_OPTIONS;
    }

    public static List<String> getAvatarList() {
        return List.of(
                "001-modern.svg",
                "002-hipster.svg",
                "003-mod.svg",
                "004-seapunk.svg",
                "005-afrofuturism.svg",
                "006-gang member.svg",
                "007-punk.svg",
                "008-otaku.svg",
                "009-heavy.svg",
                "010-grunge.svg",
                "011-cosplayer.svg",
                "012-pimp.svg",
                "013-regular.svg",
                "014-e girl.svg",
                "015-afro.svg",
                "016-punk.svg",
                "017-yuppie.svg",
                "018-formal.svg",
                "019-e girl.svg",
                "020-bodybuilder.svg",
                "021-gothic.svg",
                "022-posh.svg",
                "023-rapper.svg",
                "024-rave.svg",
                "025-gothic.svg",
                "026-rockabilly.svg",
                "027-chonga.svg",
                "028-bohemian.svg",
                "029-biker.svg",
                "030-rastafari.svg",
                "031-boy.svg",
                "032-regular.svg",
                "033-rocker.svg",
                "034-rocker.svg",
                "035-ganguro girl.svg",
                "036-eighties.svg",
                "037-beatnik.svg",
                "038-mod.svg",
                "039-lady.svg",
                "040-bosozoku.svg",
                "041-punk.svg",
                "042-hipster.svg",
                "043-emo.svg",
                "044-surfer.svg",
                "045-grunge.svg",
                "046-preppy.svg",
                "047-Hippie.svg",
                "048-nerd.svg",
                "049-cosplayer.svg",
                "050-rapper.svg"
        );
    }

    public static String getRandomIcon() {
        List<String> avatarList = getAvatarList();
        return avatarList.get((int) (avatarList.size() * Math.random()));
    }
}
