package com.peated.valhack.controller;

import com.peated.valhack.model.Tournament;
import com.peated.valhack.service.BackgroundGameDataProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/process")
public class BackgroundGameDataProcessingController {

    @Autowired
    private BackgroundGameDataProcessingService backgroundProcessService;

    @PostMapping("/start")
    public String startProcess(String tournament, String year) {
        if (tournament == null || year == null) {
            return "Tournament and year must be provided.";
        }
        if (!backgroundProcessService.isRunning()) {
            System.out.println("Tournament: " + tournament);
            backgroundProcessService.startProcess(Tournament.valueOf(tournament.trim()), year);
            return "Background process started.";
        } else {
            return "Background process is already running.";
        }
    }

    @PostMapping("/stop")
    public String stopProcess() {
        if (backgroundProcessService.isRunning()) {
            backgroundProcessService.stopProcess();
            return "Background process stopped.";
        } else {
            return "Background process is not running.";
        }
    }

    @GetMapping("/status")
    public String getStatus() {
        return backgroundProcessService.getStatus();
    }
}