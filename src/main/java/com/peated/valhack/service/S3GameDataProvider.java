package com.peated.valhack.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Component
public class S3GameDataProvider implements GameDataProvider {
    private final S3Client s3Client;

    private static final String LEAGUE = "vct-international";

    public S3GameDataProvider(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public List<String> getDataFiles() {
//        var response = s3Client.listObjectsV2(request ->
//                request.bucket("vcthackathon-data")
//                        .delimiter("/")
//                        .prefix("")
//        );
//        return response.commonPrefixes().stream()
//                .map(CommonPrefix::prefix)
//                .filter(prefix -> !prefix.equals("fandom/"))
//                .toList();
        var response = s3Client.listObjectsV2(request ->
                request.bucket("vcthackathon-data")
                        .prefix("vct-international/games/2024/")
        );
        return response.contents().stream()
                .map(S3Object::key)
                .map(key -> key.substring(key.lastIndexOf('/') + 1))
                .filter(key -> key.endsWith(".json.gz"))
                .toList();
    }

    @Override
    public Resource getDataFile(String id) {
        try {
            var response = s3Client.getObject(request ->
                    request.bucket("vcthackathon-data")
                            .key("vct-international/games/2024/" + id)
            );

            File tempFile = File.createTempFile("s3file-", ".tmp");
            try (var inputStream = response) {
                Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            return new FileSystemResource(tempFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
