package com.miningmark48.oratio.handler;

import com.miningmark48.oratio.reference.Reference;
import com.miningmark48.oratio.util.UtilLogger;

import java.io.IOException;
import java.nio.file.*;

public class HandlerWatchService {

    public static void init() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(Reference.messageDir);

            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }

            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            UtilLogger.STATUS.log("Initialized watch service");

            handleWatchKey(watchService);
        } catch (IOException | InterruptedException e) {
            UtilLogger.FATAL.log("Watch service initialization failed!");
            e.printStackTrace();
        }
    }

    private static void handleWatchKey(WatchService watchService) throws InterruptedException {
        WatchKey key;
        while ((key = watchService.take()) != null) {
            key.pollEvents().forEach( q -> {
                UtilLogger.INFO.log("Changes detected, reloading messages...");
                HandlerMessages.init();
            });
            key.reset();
        }
    }

}
