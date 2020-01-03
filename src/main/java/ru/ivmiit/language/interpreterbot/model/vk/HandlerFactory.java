package ru.ivmiit.language.interpreterbot.model.vk;

import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;

public interface HandlerFactory {
    public CallbackApiLongPoll getCallbackApiLongPool(VkApiClient client, GroupActor actor);
}
