package com.peated.valhack.service;

import org.springframework.core.io.Resource;

import java.util.List;

public interface GameDataProvider {
    List<String> getDataFiles();

    Resource getDataFile(String id);
}
