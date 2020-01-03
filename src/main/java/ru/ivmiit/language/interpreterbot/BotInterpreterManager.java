package ru.ivmiit.language.interpreterbot;

import ru.ivmiit.language.interpreterbot.model.BotMessage;

import java.util.function.BiConsumer;

public interface BotInterpreterManager {
    void setHandler(BiConsumer<Integer, String> handler);

    void setErrorHandler(BiConsumer<Integer, String> handler);

    void newMessage(BotMessage botMessage);
}
