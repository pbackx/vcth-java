package com.peated.valhack;

import com.peated.valhack.service.GameDataProvider;
import com.peated.valhack.service.GameService;
import com.peated.valhack.val.ValParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@SpringBootApplication
@EnableAsync
public class Main implements CommandLineRunner {

    @Autowired
    private GameDataProvider gameDataProvider;

    @Autowired
    private ValParser valParser;

    @Autowired
    private GameService gameService;

    @Override
    public void run(String... args) throws Exception {
    }

    @GetMapping("/data/{id}")
    @ResponseBody
    public String getDataFile(@PathVariable String id) {
        return id + " is a valid id. Let's go.";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("dataFiles", gameService.getGameFiles());
        return "index";
    }

    @PostMapping("/process")
    public String processData(@RequestBody IndexDataFileRequest request, Model model) throws IOException {
        Resource resource = gameDataProvider.getDataFile(request.getDataFileId());

        var result = this.valParser.parse(resource);

        model.addAttribute("result", result);

        return "process-result :: main";
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
