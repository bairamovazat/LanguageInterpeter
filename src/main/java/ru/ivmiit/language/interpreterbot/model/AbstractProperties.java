package ru.ivmiit.language.interpreterbot.model;


import java.io.*;
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
            InputStream propertiesInputStream = AbstractProperties.class.getClassLoader()
                    .getResourceAsStream(propertiesName);
            if(propertiesInputStream == null) {
                throw new NullPointerException("Не найден файл с конфигурацией " + propertiesName);
            }
            properties.load(new InputStreamReader(propertiesInputStream));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
