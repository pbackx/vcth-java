package com.peated.valhack.service;

import com.peated.valhack.model.DataFile;
import com.peated.valhack.model.Tournament;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Component
@Profile("local")
public class LocalGameDataProvider implements GameDataProvider {
    private final String localCopyFolder;

    public LocalGameDataProvider(
            @Value("${com.peated.valhack.local-copy-folder}") String localCopyFolder
    ) {
        this.localCopyFolder = localCopyFolder;
    }

    @Override
    public List<String> getAvailableYears(Tournament tournament) {
        String baseFolder = localCopyFolder + "/" + tournament.getFolderName() + "/games/";
        try (var paths = Files.list(Paths.get(baseFolder))) {
            return paths.filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString())
                    .toList();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> getDataFiles(Tournament tournament, String year) {
        String gameFolder = localCopyFolder + "/" + tournament.getFolderName() + "/games/" + year;
        try (var paths = Files.list(Paths.get(gameFolder))) {
            return paths.filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .toList();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Resource getDataResource(DataFile dataFile) {
        String filePath = localCopyFolder + "/" + dataFile.tournament().getFolderName() + "/games/" + dataFile.year() + "/" + dataFile.fileName();
        return new InputStreamResource(() -> Files.newInputStream(Paths.get(filePath)));
    }
}
