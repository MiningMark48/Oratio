package com.miningmark48.tidalchatbot.handler;

import com.miningmark48.tidalchatbot.reference.Reference;
import com.miningmark48.tidalchatbot.util.UtilLogger;

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

            UtilLogger.log(UtilLogger.LogType.INFO, "Initialized watch service!");

            handleWatchKey(watchService);
        } catch (IOException | InterruptedException e) {
            UtilLogger.log(UtilLogger.LogType.FATAL, "WATCH SERVICE INITIALIZATION FAILED");
            e.printStackTrace();
        }
    }

    private static void handleWatchKey(WatchService watchService) throws InterruptedException {
        WatchKey key;
        while ((key = watchService.take()) != null) {
            key.pollEvents().forEach( q -> {
                UtilLogger.log(UtilLogger.LogType.INFO, "Changes detected, reloading messages...");
                HandlerMessages.init();
            });
            key.reset();
        }
    }

}
