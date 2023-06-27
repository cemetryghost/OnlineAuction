package com.example.onlineauction;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogManager {
    private static final String LOG_FILE = "application_logging.txt";
    private static final Logger logger = Logger.getLogger(LogManager.class.getName());
    private static FileHandler fileHandler;

    static {
        try {
            String projectDir = System.getProperty("user.dir");
            String logFilePath = projectDir + File.separator + LOG_FILE;

            File logFile = new File(logFilePath);
            if (!logFile.exists()) {
                boolean created = logFile.createNewFile();
                if (created) {
                    System.out.println("Файл логов успешно создан: " + logFile.getAbsolutePath());
                } else {
                    System.out.println("Не удалось создать файл логов.");
                }
            } else {
                System.out.println("Файл логов уже существует: " + logFile.getAbsolutePath());
            }
            if (logFile.delete()) {
                System.out.println("Файл логов успешно очищен.");
            } else {
                System.out.println("Не удалось очистить файл логов.");
            }

            fileHandler = new FileHandler(logFile.getAbsolutePath(), true);
            logger.addHandler(fileHandler);

            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Logger getLogger() {
        return logger;
    }
    public static void closeLogger() {
        fileHandler.close();
    }
}
