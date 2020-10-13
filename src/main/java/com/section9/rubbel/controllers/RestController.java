package com.section9.rubbel.controllers;


import com.section9.rubbel.models.DataContainer;
import com.section9.rubbel.models.GameConfigOptions;
import com.section9.rubbel.models.Player;
import com.section9.rubbel.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@org.springframework.web.bind.annotation.RestController
@CrossOrigin(origins = {"http://localhost", "http://localhost:4200", "https://localhost:4200"})
@RequestMapping("backend/rest")
public class RestController {

    @Autowired
    AppService appService;

    @PostMapping(path = {"/start-new-rubbel-game-session"})
    public ResponseEntity<DataContainer> requestNewRubbelGameSession(@RequestBody Player creatingPlayer) {
        return asResponseEntity(appService.createRubbelGameSession(creatingPlayer));
    }


    @PostMapping(path = {"/join-rubbel-game-session/{gameSessionId}"})
    public ResponseEntity<DataContainer> joinRubbelGameSession(@PathVariable("gameSessionId") UUID gameSessionId, @RequestBody Player joiningPlayer) {
        return asResponseEntity(appService.joinRubbelGameSession(gameSessionId, joiningPlayer));
    }

    @GetMapping(path = {"/game-config-options/{gameSessionId}"})
    public ResponseEntity<GameConfigOptions> getGameConfigOptions(@PathVariable("gameSessionId") UUID gameSessionId) {
        return asResponseEntity(appService.getGameConfigOptions(gameSessionId));
    }

    @GetMapping(path = {"/avatar-list/{gameSessionId}"})
    public ResponseEntity<List<String>> getAvatarList(@PathVariable("gameSessionId") UUID gameSessionId) {
        return asResponseEntity(appService.getAvatarList(gameSessionId));
    }

    @GetMapping(path = {"/letters-whitelist/{gameSessionId}"})
    public ResponseEntity<List<String>> getLettersWhitelist(@PathVariable("gameSessionId") UUID gameSessionId) {
        return asResponseEntity(appService.getLettersWhitelist(gameSessionId));
    }

    private static <T> ResponseEntity<T> asResponseEntity(T obj) {
        if (obj != null) {
            return ResponseEntity.ok().body(obj);
        }
        return ResponseEntity.badRequest().build();
    }
}
