package com.peated.valhack.val;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ValParser {
    private final String filename;

    public ValParser(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void parse() throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filename);
             var gzipInputStream = new GZIPInputStream(fileInputStream);
             var reader = new InputStreamReader(gzipInputStream);
             var bufferedReader = new BufferedReader(reader)) {
            
            var jsonContent = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonContent.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonContent.toString());

            jsonNode.iterator().forEachRemaining(entry -> {
                entry.fieldNames().forEachRemaining(System.out::println);
            });
        }
    }
}
