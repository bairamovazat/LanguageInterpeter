package ru.ivmiit.language.interpreterbot.model.vk;

import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.messages.Message;
import ru.ivmiit.language.interpreterbot.model.BotMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CallbackApiLongPollHandler extends CallbackApiLongPoll {
    public final List<Consumer<BotMessage>> messageHandlers;

    public CallbackApiLongPollHandler(VkApiClient client, GroupActor actor, List<Consumer<BotMessage>> messageHandlers) {
        super(client, actor);
        this.messageHandlers = messageHandlers;
    }

    @Override
    public void messageNew(Integer groupId, Message message) {
        synchronized (messageHandlers) {
            messageHandlers.forEach(e -> {
                e.accept(new VkBotMessage(message.getUserId(), message.getBody()));
            });
        }
    }

//    @Override
//    public void wallPostNew(Integer groupId, WallPost wallPost) {
//        System.out.println("wallPostNew: " + wallPost.toString());
//    }
}