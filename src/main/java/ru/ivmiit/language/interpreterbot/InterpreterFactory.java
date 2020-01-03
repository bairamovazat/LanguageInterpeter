package ru.ivmiit.language.interpreterbot;

import ru.ivmiit.language.interpreter.Interpreter;
import ru.ivmiit.language.interpreter.ProgramInterpreter;
import ru.ivmiit.language.interpreter.ProgramInterpreterImpl;

public interface InterpreterFactory {
    public ProgramInterpreter createProgramInterpreter();
}
