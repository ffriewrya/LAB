package ru.itmo.moona.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itmo.moona.service.StockManager;

import java.io.File;
import java.io.IOException;

public class FileStorage {
    private final ObjectMapper mapper;
    private final StockManager manager;

    public FileStorage(ObjectMapper mapper, StockManager manager) {
        this.mapper = mapper;
        this.manager = manager;
    }

    public void saveToJson(String path) {
        try {
            StockSnapshot s = new StockSnapshot(manager.getReagents(), manager.getBatches(), manager.getMoves());
            mapper.writeValue(new File(path), s);
        } catch (IOException e) {
            throw new RuntimeException("unable to save data to file. ensure that the file is not open and the path is correct. " + e);
        }
    }

    public StockSnapshot loadFromJson(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                throw new IllegalArgumentException("can't find any file");
            }
            return mapper.readValue(file, StockSnapshot.class);
        } catch (IOException e) {
            throw new RuntimeException("unable to load data from file. ensure that the file is not open and the path is correct. " + e);
        }
    }
}
