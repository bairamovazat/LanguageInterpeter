package ru.ivmiit.language.interpreter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProgramInterpreterImpl implements ProgramInterpreter {

    private final List<Consumer<String>> handlers = new ArrayList<>();
    private final List<Consumer<String>> errorHandlers = new ArrayList<>();

    private ParserClassName parserClassName;
    private Lexer lexer;

    public ProgramInterpreterImpl() {
        lexer = new Lexer(null);
        parserClassName = new ParserClassName(lexer);
        parserClassName.getInterpreter().setHandler(this::handleEvent);
        parserClassName.getInterpreter().setErrorHandler(this::handleErrorEvent);
    }

    private void handleEvent(String message) {
        synchronized (handlers) {
            handlers.forEach(h -> h.accept(message));
        }
    }

    private void handleErrorEvent(String message) {
        synchronized (errorHandlers) {
            errorHandlers.forEach(h -> h.accept(message));
        }
    }

    @Override
    public void addHandler(Consumer<String> handler) {
        synchronized (handlers) {
            handlers.add(handler);
        }
    }

    @Override
    public void removeHandler(Consumer<String> handler) {
        synchronized (handlers) {
            handlers.remove(handler);
        }
    }

    @Override
    public void addErrorHandler(Consumer<String> handler) {
        synchronized (errorHandlers) {
            errorHandlers.add(handler);
        }
    }

    @Override
    public void removeErrorHandler(Consumer<String> handler) {
        synchronized (errorHandlers) {
            errorHandlers.remove(handler);
        }
    }

    @Override
    public void addProgramToExecute(String program) {
        try {
            InputStream is = new ByteArrayInputStream(program.getBytes("UTF-8"));
            parserClassName.getInterpreter().setInputStream(is);
            lexer.yyreset(new InputStreamReader(is));
            runParser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runParser() throws Exception {
        try {
            parserClassName.parse();
        }catch (Throwable e) {
            String errorMessage = "Ошибки: Ошибка синтаксиса неизвестный синтаксис";
            handleErrorEvent(errorMessage);
            return;
        }
        Interpreter interpreter = parserClassName.getInterpreter();
        List<CupError> errorList = interpreter.getCupErrorList();
        if (errorList.size() != 0) {
            String errorMessage = "Ошибки: " + errorList.stream().map(CupError::getError).collect(Collectors.joining(";"));
            handleErrorEvent(errorMessage);
            errorList.clear();
        }else {
            interpreter.exec();
        }
    }

}
