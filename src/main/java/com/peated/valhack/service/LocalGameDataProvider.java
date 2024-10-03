package com.peated.valhack.service;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Can be used for testing purposes.
 */
public class LocalGameDataProvider implements ResourceLoaderAware, GameDataProvider {

    private ResourceLoader resourceLoader;
    private List<String> dataFiles;

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    @PostConstruct
    public void loadData() throws IOException {
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        Resource[] resources = resolver.getResources("classpath:data/val*");
        
        dataFiles = new ArrayList<>();
        for (Resource resource : resources) {
            String fileName = resource.getFilename();
            dataFiles.add(fileName);
        }
    }

    @Override
    public List<String> getDataFiles() {
        return dataFiles;
    }

    @Override
    public Resource getDataFile(String id) {
        String matchingFile = dataFiles.stream()
                .filter(file -> file.contains(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No matching data file found for ID: " + id));

        Resource resource = resourceLoader.getResource("classpath:data/" + matchingFile);
        if (!resource.exists()) {
            throw new IllegalArgumentException("Data file does not exist: " + matchingFile);
        }
        
        return resourceLoader.getResource("classpath:data/" + id);
    }
}
