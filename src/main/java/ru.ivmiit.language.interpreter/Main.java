package ru.ivmiit.language.interpreter;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            InputStream inputStream = Interpreter.class.getClassLoader().getResourceAsStream("test.txt");
//            InputStream inputStream = System.in;

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            ParserClassName p = new ParserClassName(new Lexer(in));
            p.parse();
            Interpreter interpreter = p.action_obj.getInterpreter();
            interpreter.exec();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
