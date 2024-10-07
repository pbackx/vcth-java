package com.peated.valhack.service;

import com.peated.valhack.model.DataFile;
import com.peated.valhack.model.Tournament;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.CommonPrefix;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class S3GameDataProvider implements GameDataProvider {
    private final S3Client s3Client;

    public S3GameDataProvider(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public List<String> getAvailableYears(Tournament tournament) {
        var response = s3Client.listObjectsV2(request ->
                request.bucket("vcthackathon-data")
                        .prefix(tournament.getFolderName() + "/games/")
                        .delimiter("/")
        );
        return response.commonPrefixes().stream()
                .map(CommonPrefix::prefix)
                .map(prefix -> prefix.substring(prefix.lastIndexOf("games/") + 6, prefix.length() - 1))
                .toList();
    }

    private String getPrefix(Tournament tournament, String year) {
        return tournament.getFolderName() + "/games/" + year + "/";
    }

    private String getPrefix(DataFile dataFile) {
        return getPrefix(dataFile.tournament(), dataFile.year()) + dataFile.fileName();
    }

    @Override
    public List<String> getDataFiles(Tournament tournament, String year) {
        var response = s3Client.listObjectsV2(request ->
                request.bucket("vcthackathon-data")
                        .prefix(getPrefix(tournament, year))
        );
        return response.contents().stream()
                .map(S3Object::key)
                .map(key -> key.substring(key.lastIndexOf('/') + 1))
                .filter(key -> key.endsWith(".json.gz"))
                .toList();
    }

    @Override
    public Resource getDataResource(DataFile dataFile) {
        try {
            var response = s3Client.getObject(request ->
                    request.bucket("vcthackathon-data")
                            .key(getPrefix(dataFile))
            );

            try (var inputStream = response; var outputStream = new ByteArrayOutputStream()) {
                inputStream.transferTo(outputStream);
                return new ByteArrayResource(outputStream.toByteArray());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
