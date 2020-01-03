package ru.ivmiit.language.interpreterbot;

import ru.ivmiit.language.interpreterbot.model.Bot;
import ru.ivmiit.language.interpreterbot.model.VkBot;
import ru.ivmiit.language.interpreterbot.model.vk.VkBotMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class BotRunner {

    public static void main(String[] args) {
        String vkConfigName = "vk.config.properties";
        Bot bot = new VkBot(vkConfigName);
        bot.setInitConnectionHandler(() -> {
            String message = "language-interpreter инициализирован " + new Date().toString();

            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getLocalHost();
                message += "; IP Address:- " + inetAddress.getHostAddress();
                message += "; Host Name:- " + inetAddress.getHostName();
            } catch (UnknownHostException e) {
                message += "; UnknownHostException: " + e.getMessage();
            }
            bot.sendMessage(new VkBotMessage(158377194, message));
        });
        BotInterpreterManagerImpl botInterpreterManager = new BotInterpreterManagerImpl();
        botInterpreterManager.setHandler((userId, message) -> bot.sendMessage(new VkBotMessage(userId, message)));
        botInterpreterManager.setErrorHandler((userId, message) -> bot.sendMessage(new VkBotMessage(userId, message)));
        bot.addHandler(botInterpreterManager::newMessage);
        bot.start();
    }
}
