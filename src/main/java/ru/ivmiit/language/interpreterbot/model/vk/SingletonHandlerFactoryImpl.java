package ru.ivmiit.language.interpreterbot.model.vk;

import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import ru.ivmiit.language.interpreterbot.model.BotMessage;

import java.util.List;
import java.util.function.Consumer;

public class SingletonHandlerFactoryImpl implements HandlerFactory {
    private List<Consumer<BotMessage>> handlers;
    private CallbackApiLongPollHandler callbackApiLongPollHandler = null;

    public SingletonHandlerFactoryImpl(List<Consumer<BotMessage>> handlers) {
        this.handlers = handlers;
    }

    @Override
    public CallbackApiLongPoll getCallbackApiLongPool(VkApiClient client, GroupActor actor) {
        if(callbackApiLongPollHandler == null){
            callbackApiLongPollHandler = new CallbackApiLongPollHandler(client, actor, handlers);
        }
        return callbackApiLongPollHandler;
    }
}
