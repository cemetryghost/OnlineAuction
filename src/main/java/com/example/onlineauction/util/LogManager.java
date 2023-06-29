package com.example.onlineauction.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogManager {
    private static final String LOG_FILE = "application-log.txt";
    private static final Logger logger = Logger.getLogger(LogManager.class.getName());
    private static FileHandler fileHandler;

    static {
        initializeLogger();
    }

    private static void initializeLogger() {
        try {
            String projectDir = System.getProperty("user.dir");
            String logFilePath = projectDir + File.separator + LOG_FILE;

            File logFile = new File(logFilePath);

            if (logFile.exists()) {
                if (logFile.delete()) {
                    logger.info("Файл логов успешно очищен: " + logFile.getAbsolutePath());
                } else {
                    logger.warning("Не удалось очистить файл логов: " + logFile.getAbsolutePath());
                }
            }

            if (logFile.createNewFile()) {
                logger.info("Файл логов успешно создан: " + logFile.getAbsolutePath());
            } else {
                logger.warning("Не удалось создать файл логов: " + logFile.getAbsolutePath());
            }

            fileHandler = new FileHandler(logFile.getAbsolutePath(), true);
            logger.addHandler(fileHandler);

            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при инициализации логгера.", e);
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void closeLogger() {
        if (fileHandler != null) {
            fileHandler.close();
        }
    }
}
