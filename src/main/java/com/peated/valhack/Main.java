package com.peated.valhack;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.zip.GZIPInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.peated.valhack.model.Player;
import com.peated.valhack.repository.PlayerRepository;
import com.peated.valhack.val.ValParser;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;
import java.util.List;

@RestController
@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private GameDataProvider gameDataProvider;

    @Autowired
    private Database database;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Data files: " + gameDataProvider.getDataFiles());

        playerRepository.save(new Player(null, "John Doe"));
        var players = playerRepository.findAll();
        System.out.println("Players: " + players);
    }

    @GetMapping("/data")
    public List<String> getDataFiles() {
        return gameDataProvider.getDataFiles();
    }

    @GetMapping("/data/{id}")
    public String getDataFile(@PathVariable String id) {
        return id + " is a valid id. Let's go.";
    }

    @GetMapping("/date")
    public String getCurrentDuckDbDate() {
        try {
            return database.getCurrentDuckDbDate();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error getting current date";
        }
    }

    @PostMapping("/data")
    public String loadData(@RequestBody IndexDataFileRequest request) throws IOException {

        System.out.println("Loading data file ID: " + request.getDataFileId());

        Resource resource = gameDataProvider.getDataFile(request.getDataFileId());

        System.out.println("Matching resource: " + resource.getFile().getAbsolutePath());

        // ValParser valParser = new ValParser(request.getDataFileId());
        // valParser.parse();


        // // Print the data file ID for debugging
        // System.out.println("Received data file ID: " + dataFileId);
        return "Data loaded successfully";
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
