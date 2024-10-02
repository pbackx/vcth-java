package com.peated.valhack;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.peated.valhack.val.ValParser;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private GameDataProvider gameDataProvider;

    @Autowired
    private ValParser valParser;

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
        model.addAttribute("dataFiles", gameDataProvider.getDataFiles());
        return "index";
    }

    @PostMapping("/process")
    public String processData(@RequestBody IndexDataFileRequest request, Model model) throws IOException {
        Resource resource = gameDataProvider.getDataFile(request.getDataFileId());

        System.out.println("Processing data file: " + resource.getFile().getAbsolutePath());

        var result = this.valParser.parse(resource);

        model.addAttribute("result", result);

        return "process-result :: main";
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
