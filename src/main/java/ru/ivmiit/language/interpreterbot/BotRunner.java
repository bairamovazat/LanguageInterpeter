package ru.ivmiit.language.interpreterbot;

import ru.ivmiit.language.interpreterbot.model.Bot;
import ru.ivmiit.language.interpreterbot.model.VkBot;
import ru.ivmiit.language.interpreterbot.model.vk.VkBotMessage;

public class BotRunner {

    public static void main(String[] args) {
        String vkConfigName = "vk.config.properties";
        Bot bot = new VkBot(vkConfigName);
        BotInterpreterManagerImpl botInterpreterManager = new BotInterpreterManagerImpl();
        botInterpreterManager.setHandler((userId, message) -> bot.sendMessage(new VkBotMessage(userId, message)));
        botInterpreterManager.setErrorHandler((userId, message) -> bot.sendMessage(new VkBotMessage(userId, message)));
        bot.addHandler(botInterpreterManager::newMessage);
        bot.start();
    }
}
