package ru.ivmiit.language.interpreter;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Lexer lexer = new Lexer(null);
            ParserClassName p = new ParserClassName(lexer);
            Interpreter interpreter = p.getInterpreter();
            interpreter.setHandler(System.out::println);
            interpreter.setErrorHandler(System.err::println);

            for(int i = 0; i < 1; i++) {
                InputStream inputStream = Interpreter.class.getClassLoader().getResourceAsStream("test.txt");
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                interpreter.setInputStream(inputStream);
                lexer.yyreset(in);
                p.parse();
                interpreter.exec();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
