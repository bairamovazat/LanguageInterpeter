package ru.ivmiit.language.interpreterbot.model.vk;

import ru.ivmiit.language.interpreterbot.model.AbstractProperties;

public class VkBotPropertiesImpl extends AbstractProperties implements VkBotProperties {

    public VkBotPropertiesImpl(String propertiesName) {
        super(propertiesName);
    }

    public String getVkApiId() {
        return getProperties().getProperty("VK_APP_ID");
    }

    public String getVkClientSecret() {
        return getProperties().getProperty("VK_CLIENT_SECRET");
    }

    public String getVkRedirectUrl() {
        return getProperties().getProperty("VK_REDIRECT_URI");
    }

    public String getVkCode() {
        return getProperties().getProperty("VK_CODE");
    }

    public Integer getVkGroupId() {
        return getProperties().getProperty("VK_GROUP_ID") == null ? null : Integer.parseInt(getProperties().getProperty("VK_GROUP_ID"));
    }

    @Override
    public String getVkGroupToken() {
        return getProperties().getProperty("VK_GROUP_TOKEN");
    }
}
