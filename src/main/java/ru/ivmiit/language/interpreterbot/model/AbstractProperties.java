package ru.ivmiit.language.interpreterbot.model;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public abstract class AbstractProperties {
    private Properties properties;

    protected Properties getProperties() {
        return properties;
    }

    protected AbstractProperties(String propertiesName) {
        properties = new Properties();
        try {
            URL propertiesUrl = AbstractProperties.class.getClassLoader()
                    .getResource(propertiesName);
            if(propertiesUrl == null) {
                throw new NullPointerException("Не найден файл с конфигурацией " + propertiesName);
            }
            String propertiesPath = propertiesUrl.getPath();
            properties.load(new FileReader(new File(propertiesPath)));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
