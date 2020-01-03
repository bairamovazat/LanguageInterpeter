package ru.ivmiit.language.interpreter;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.GroupFull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ru.ivmiit.language.interpreterbot.model.*;
import ru.ivmiit.language.interpreterbot.model.vk.CallbackApiLongPollHandler;
import ru.ivmiit.language.interpreterbot.model.vk.VkBotProperties;
import ru.ivmiit.language.interpreterbot.model.vk.VkConnection;
import ru.ivmiit.language.interpreterbot.model.vk.VkBotPropertiesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class VkApiTests extends AbstractProperties {
    private static String vkConfigName = "vk.config.properties";

    public VkApiTests() {
        super(vkConfigName);
    }

    @Test
    void testProperties() {
        Assertions.assertNotEquals(getProperties(), null);
    }

    @Test
    void testVkConfig() {
        VkBotProperties vkBotProperties = new VkBotPropertiesImpl(vkConfigName);
        Assertions.assertNotNull(vkBotProperties.getVkApiId());
        Assertions.assertNotNull(vkBotProperties.getVkClientSecret());
        Assertions.assertNotNull(vkBotProperties.getVkCode());
        Assertions.assertNotNull(vkBotProperties.getVkRedirectUrl());
        Assertions.assertNotNull(vkBotProperties.getVkGroupId());
    }

    @Test
    void testVkConnection() throws ClientException, ApiException {
        VkBotProperties vkBotProperties = new VkBotPropertiesImpl(vkConfigName);
        List<Consumer<BotMessage>> handlers = new ArrayList<>();

        handlers.add(System.out::println);

        VkConnection vkConnection = new VkConnection(
                vkBotProperties,
                (client, actor) -> (new CallbackApiLongPollHandler(client, actor, handlers))
        );
        vkConnection.initVkConnection();
        List<GroupFull> groupsInfo = vkConnection.getCurrentGroupInfo();
        Assertions.assertTrue(groupsInfo.size() == 1);
        System.out.println(groupsInfo.get(0));
    }
}
