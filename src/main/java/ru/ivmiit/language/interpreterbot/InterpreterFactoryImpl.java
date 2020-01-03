package ru.ivmiit.language.interpreterbot;

import ru.ivmiit.language.interpreter.ProgramInterpreter;
import ru.ivmiit.language.interpreter.ProgramInterpreterImpl;

public class InterpreterFactoryImpl implements InterpreterFactory {
    @Override
    public ProgramInterpreter createProgramInterpreter() {
        return new ProgramInterpreterImpl();
    }
}
