package com.peated.valhack.service;

import com.peated.valhack.model.Tournament;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Note that in the following code, the fixtures also include the mapping data file. The documentation separates these
 * into two categories.
 */
@Service
public class EsportsFixtureService {
    private final String dataFolder;
    private final S3Client s3Client;

    private static final String[] FIXTURE_FILES = {
            "leagues.json.gz",
            "tournaments.json.gz",
            "players.json.gz",
            "teams.json.gz",
            "mapping_data_v2.json.gz",
    };

    public EsportsFixtureService(
            @Value("${com.peated.valhack.data-folder}") String dataFolder,
            S3Client s3Client
    ) {
        this.dataFolder = dataFolder;
        this.s3Client = s3Client;

        for (Tournament tournament : Tournament.values()) {
            var folder = this.getFixtureFolder(tournament);
            System.out.println("Creating folder: " + folder.getAbsolutePath());
            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    throw new RuntimeException("Failed to create folder: " + folder.getAbsolutePath());
                }
            }
        }
    }

    private File getFixtureFolder(Tournament tournament) {
        return new File(dataFolder + "/" + tournament.getFolderName());
    }

    public void downloadFixtures(Tournament tournament) throws IOException {
        // download fixtures from S3 to local folder
        for (String fixtureFileName : FIXTURE_FILES) {
            var localFile = new File(this.getFixtureFolder(tournament), fixtureFileName);
            var response = s3Client.getObject(
                    builder -> builder.bucket("vcthackathon-data").key(tournament.getFolderName() + "/esports-data/" + fixtureFileName)
            );
            try (var inputStream = response; var outputStream = new FileOutputStream(localFile)) {
                inputStream.transferTo(outputStream);
            }
        }
    }
}
