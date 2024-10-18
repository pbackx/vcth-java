package com.peated.valhack;

import com.peated.valhack.model.DataFile;
import com.peated.valhack.model.DataFileStatus;
import com.peated.valhack.model.Tournament;
import com.peated.valhack.service.EsportsFixtureService;
import com.peated.valhack.service.GameDataProvider;
import com.peated.valhack.service.GameService;
import com.peated.valhack.val.ValParser;
import com.peated.valhack.service.BedrockAgentService;
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

    private final GameDataProvider gameDataProvider;
    private final ValParser valParser;
    private final GameService gameService;
    private final EsportsFixtureService esportsFixtureService;
    private final BedrockAgentService bedrockAgentService;

    public Main(GameDataProvider gameDataProvider, ValParser valParser, GameService gameService, EsportsFixtureService esportsFixtureService, BedrockAgentService bedrockAgentService) {
        this.gameDataProvider = gameDataProvider;
        this.valParser = valParser;
        this.gameService = gameService;
        this.esportsFixtureService = esportsFixtureService;
        this.bedrockAgentService = bedrockAgentService;
    }

    @GetMapping("/data/{id}")
    @ResponseBody
    public String getDataFile(@PathVariable String id) {
        return id + " is a valid id. Let's go.";
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    record UserMessageRequest(String userMessage) { }

    @PostMapping("/message")
    public String answer(@RequestBody UserMessageRequest body, Model model) {
        model.addAttribute("message", body.userMessage());
        model.addAttribute("response", bedrockAgentService.getResponse(body.userMessage(), "TODO"));

//        var r = """
//The team I would assemble consists of:
//
//Sentinel: FNC Alfajer
//Initiator 1: DRX stax
//Initiator 2: NRG crashies
//Duelist: LEV aspas
//Controller: DRX MaKo
//
//This composition balances the key roles needed in competitive matches. The two elite Initiators stax and crashies can effectively gather information and set up plays for the aggressive Duelist aspas to get opening picks. Alfajer as the Sentinel can anchor sites with his utility, while MaKo controls areas with his Controller smokes and abilities.\s
//""";
//        model.addAttribute("response", r);
        return "message :: main";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("tournaments", Arrays.stream(Tournament.values()).map(Tournament::name).toArray());
        return "admin";
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

        var result = this.valParser.parse(tournament, Integer.parseInt(request.year()), resource);

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

