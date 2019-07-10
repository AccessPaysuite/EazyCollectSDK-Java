package uk.co.eazycollect.eazysdk;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class SettingsManager {

    public static Properties createSettings() {

        if(!Files.exists(Paths.get("./appSettings.xml").toAbsolutePath())) {
            SettingsWriter writer = new SettingsWriter();
        }

        Properties configuration = new Properties();

        try(BufferedInputStream stream = new BufferedInputStream(new FileInputStream(Paths.get("./appSettings.xml").toFile()))) {
            configuration.loadFromXML(stream);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return configuration;
    }

}
