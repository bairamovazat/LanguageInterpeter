package ru.ivmiit.language.interpreterbot.model.vk;

import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.GroupFull;
import ru.ivmiit.language.interpreterbot.model.BotMessage;

import java.util.List;

public class VkConnection {

    private VkBotProperties vkBotProperties;

    private VkApiClient vkApiClient;

    private GroupActor groupActor;

    //Реаширует на пришёдшие запросы от vk
    private CallbackApiLongPoll handler;

    private Thread handlerThread;
    //Получаем CalLBackApi
    private HandlerFactory handlerFactory;


    public VkConnection(VkBotProperties vkBotProperties, HandlerFactory handlerFactory) {
        this.vkBotProperties = vkBotProperties;
        this.handlerFactory = handlerFactory;
    }

    public void initVkConnection() throws ClientException, ApiException {
        TransportClient transportClient = HttpTransportClient.getInstance();
        vkApiClient = new VkApiClient(transportClient);

        groupActor = new GroupActor(vkBotProperties.getVkGroupId(), vkBotProperties.getVkGroupToken());
        vkApiClient.groups().setLongPollSettings(groupActor)
                .enabled(true)
                .messageNew(true)
                .execute();
        handler = handlerFactory.getCallbackApiLongPool(vkApiClient, groupActor);
    }

    public List<GroupFull> getCurrentGroupInfo() {
        try {
            return vkApiClient.groups().getById(groupActor).execute();
        } catch (ApiException | ClientException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Integer sendMessage(BotMessage message) throws ClientException, ApiException {
        return vkApiClient.messages()
                .send(groupActor)
                .userId(message.getUserId())
                .message(message.getMessage())
                .execute();
    }

    public void startHandler() {
        handlerThread = new Thread(() -> {
            try {
                handler.run();
            } catch (ClientException | ApiException e) {
                e.printStackTrace();
            }
        });
        handlerThread.run();
    }

    public void stopHandler() {
        if (handlerThread != null && handlerThread.isAlive()) {
            handlerThread.stop();
        }
    }
}
