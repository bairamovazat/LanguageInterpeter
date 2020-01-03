package ru.ivmiit.language.interpreterbot.model.vk;

import lombok.ToString;
import ru.ivmiit.language.interpreterbot.model.BotMessage;

@ToString
public class VkBotMessage implements BotMessage {
    private Integer userId;
    private String message;

    public VkBotMessage(Integer userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    @Override
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
