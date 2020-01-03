package ru.ivmiit.language.interpreterbot.model;

import ru.ivmiit.language.interpreterbot.model.BotMessage;

import java.util.function.Consumer;

public interface Bot {
    public void sendMessage(BotMessage botMessage);
    public void addHandler(Consumer<BotMessage> vkMessageConsumer);
    public void removeHandler(Consumer<BotMessage> vkMessageConsumer);
    public void start();
    public void stop();
}
