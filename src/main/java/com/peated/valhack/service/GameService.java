package com.peated.valhack.service;

import com.peated.valhack.model.DataFileStatus;
import com.peated.valhack.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    /**
     *
     * @param gameFileName the name of the game file, for instance "val_00676ae0-0a4d-4577-be1e-ab4d3a9889aa.json.gz"
     * @return whether or not the file was already processed
     */
    public DataFileStatus getStatus(String gameFileName) {
        try {
            String gameId = gameFileName.substring(0, gameFileName.indexOf(".")).replace("val_", "val:");
            if (gameRepository.existsByPlatformGameId(gameId)) {
                return DataFileStatus.DONE;
            } else {
                return DataFileStatus.TO_PROCESS;
            }
        } catch (Exception e) {
            return DataFileStatus.ERROR;
        }
    }
}
