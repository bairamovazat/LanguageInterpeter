package ru.ivmiit.language.interpreterbot.model;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.ivmiit.language.interpreterbot.model.*;
import ru.ivmiit.language.interpreterbot.model.vk.SingletonHandlerFactoryImpl;
import ru.ivmiit.language.interpreterbot.model.vk.VkBotProperties;
import ru.ivmiit.language.interpreterbot.model.vk.VkConnection;
import ru.ivmiit.language.interpreterbot.model.vk.VkBotPropertiesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class VkBot implements Bot {

    private final VkConnection vkConnection;
    private final List<Consumer<BotMessage>> handlers = new ArrayList<>();
    private Runnable initConnectionHandler = () -> {};

    public VkBot(String vkConfigName) {
        VkBotProperties vkBotProperties = new VkBotPropertiesImpl(vkConfigName);

        vkConnection = new VkConnection(
                vkBotProperties,
                new SingletonHandlerFactoryImpl(handlers)
        );
    }

    @Override
    public void sendMessage(BotMessage botMessage) {
        try {
            vkConnection.sendMessage(botMessage);
        } catch (ClientException | ApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void addHandler(Consumer<BotMessage> vkMessageConsumer) {
        synchronized (handlers) {
            handlers.add(vkMessageConsumer);
        }
    }

    @Override
    public void removeHandler(Consumer<BotMessage> vkMessageConsumer) {
        synchronized (handlers) {
            handlers.remove(vkMessageConsumer);
        }
    }

    @Override
    public void setInitConnectionHandler(Runnable initConnectionHandler) {
        this.initConnectionHandler = initConnectionHandler;
    }

    @Override
    public void start() {
        try {
            vkConnection.initVkConnection();
            initConnectionHandler.run();
            vkConnection.startHandler();
        } catch (ClientException | ApiException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void stop() {
        vkConnection.stopHandler();
    }

}
