package ru.ivmiit.language.interpreter;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            OutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = Interpreter.class.getClassLoader().getResourceAsStream("test.txt");

            BufferedReader in  = new BufferedReader(new InputStreamReader(inputStream));
            ParserClassName p = new ParserClassName(new Lexer(in));
            p.parse();
            Interpreter interpreter = p.action_obj.getInterpreter();
            interpreter.exec();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
