package ru.ivmiit.language.interpreterbot;

import lombok.Setter;
import ru.ivmiit.language.interpreter.Interpreter;
import ru.ivmiit.language.interpreter.ProgramInterpreter;
import ru.ivmiit.language.interpreterbot.model.BotMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class BotInterpreterManagerImpl implements BotInterpreterManager {

    @Setter
    private BiConsumer<Integer, String> handler = (message, string) -> {
    };

    @Setter
    private BiConsumer<Integer, String> errorHandler = (message, string) -> {
    };

    private final Map<Integer, ProgramInterpreter> usersInterpreter = new HashMap<>();

    private InterpreterFactory interpreterFactory = new InterpreterFactoryImpl();

    @Override
    public void setHandler(BiConsumer<Integer, String> handler) {
        this.handler = handler;
    }

    @Override
    public void setErrorHandler(BiConsumer<Integer, String> handler) {
        this.errorHandler = handler;
    }

    public ProgramInterpreter getInterpreter(Integer userId) {
        int copiedUserId = userId;
        synchronized (usersInterpreter) {
            return usersInterpreter.computeIfAbsent(userId, k -> {
                ProgramInterpreter interpreter = interpreterFactory.createProgramInterpreter();
                interpreter.addHandler((string) -> {
                    this.handler.accept(copiedUserId, string);
                });
                interpreter.addErrorHandler((string) -> {
                    this.errorHandler.accept(copiedUserId, string);
                });
                return interpreter;
            });
        }
    }

    @Override
    public void newMessage(BotMessage botMessage) {
        getInterpreter(botMessage.getUserId()).addProgramToExecute(botMessage.getMessage());
    }

}
