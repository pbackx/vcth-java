package com.peated.valhack;

import com.peated.valhack.model.DataFile;
import com.peated.valhack.model.DataFileStatus;
import com.peated.valhack.model.Tournament;
import com.peated.valhack.service.EsportsFixtureService;
import com.peated.valhack.service.GameDataProvider;
import com.peated.valhack.service.GameService;
import com.peated.valhack.val.ValParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@SpringBootApplication
@EnableAsync
public class Main {

    @Autowired
    private GameDataProvider gameDataProvider;

    @Autowired
    private ValParser valParser;

    @Autowired
    private GameService gameService;

    @Autowired
    private EsportsFixtureService esportsFixtureService;

    @GetMapping("/data/{id}")
    @ResponseBody
    public String getDataFile(@PathVariable String id) {
        return id + " is a valid id. Let's go.";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tournaments", Arrays.stream(Tournament.values()).map(Tournament::name).toArray());
        return "index";
    }

    @PostMapping("/process")
    public String processData(@RequestBody IndexDataFileRequest request, Model model) throws IOException {
        Tournament tournament = Tournament.valueOf(request.tournament());
        DataFile dataFile = new DataFile(
                request.dataFileId(),
                DataFileStatus.TO_PROCESS,
                tournament,
                request.year()
        );
        Resource resource = gameDataProvider.getDataResource(dataFile);

        var result = this.valParser.parse(tournament, resource);

        model.addAttribute("result", result);

        return "process-result :: main";
    }

    @PostMapping("/fixture/download")
    @ResponseBody
    public String downloadFixtures(@RequestBody DownloadFixturesRequest request) throws IOException {
        esportsFixtureService.downloadFixtures(request.getTournament());
        return "Fixtures downloaded for " + request.getTournament() + ". ";
    }

    @GetMapping("/fixture/{tournament}/years")
    public String getAvailableYears(@PathVariable String tournament, Model model) {
        List<String> years = gameDataProvider.getAvailableYears(Tournament.valueOf(tournament));
        model.addAttribute("years", years);
        return "tournament-years";
    }

    @GetMapping("/fixture/{tournament}/{year}")
    public String getGames(@PathVariable String tournament, @PathVariable String year, Model model) {
        List<DataFile> games = gameService.getGameFiles(Tournament.valueOf(tournament), year);
        model.addAttribute("dataFiles", games);
        return "games";
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

