package ru.ivmiit.language.interpreter;

import java.io.InputStream;
import java.util.function.Consumer;

public interface ProgramInterpreter {
    public void addHandler(Consumer<String> handler);
    public void removeHandler(Consumer<String> handler);

    public void addErrorHandler(Consumer<String> handler);
    public void removeErrorHandler(Consumer<String> handler);


    public void addProgramToExecute(String program);
}
