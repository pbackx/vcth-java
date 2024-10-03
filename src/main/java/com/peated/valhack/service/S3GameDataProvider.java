package com.peated.valhack.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class S3GameDataProvider implements GameDataProvider {
    private final S3Client s3Client;

//    private static final String LEAGUE = "vct-international";

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

            try (var inputStream = response; var outputStream = new ByteArrayOutputStream()) {
                inputStream.transferTo(outputStream);
                return new ByteArrayResource(outputStream.toByteArray());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
