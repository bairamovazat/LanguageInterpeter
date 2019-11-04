package ru.ivmiit.language.interpreter;

import jflex.GeneratorException;
import jflex.Options;
import jflex.Out;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File(
                Main.class.getClassLoader().getResource("test.txt").getFile()
        );
        FileReader fileReader = new FileReader(file);
        Docase docase = new Docase(fileReader);
        docase.next_token();
    }
}
