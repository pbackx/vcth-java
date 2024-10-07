package com.peated.valhack.service;

import com.peated.valhack.model.DataFile;
import com.peated.valhack.model.Tournament;
import org.springframework.core.io.Resource;

import java.util.List;

public interface GameDataProvider {
    List<String> getAvailableYears(Tournament tournament);

    List<String> getDataFiles(Tournament tournament, String year);

    Resource getDataResource(DataFile dataFile);
}
