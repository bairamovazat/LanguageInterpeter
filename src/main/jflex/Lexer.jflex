package ru.ivmiit.language.interpreter;

import java_cup.runtime.*;
import ru.ivmiit.language.interpreter.CupSymbol;
import java_cup.sym;

%%

%class Lexer

%unicode
%cup
%line
%column

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}


LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]

NUM = [0-9]+
IDENT = [A-Za-z_][A-Za-z_0-9]*
STRING = \"([^\\\"]|\\.)*\"

%%

<YYINITIAL> {

    /** Keywords. */
    "and"             { return symbol(CupSymbol.AND); }
    "or"              { return symbol(CupSymbol.OR); }
    "not"             { return symbol(CupSymbol.NOT); }
    "true"            { return symbol(CupSymbol.TRUE); }
    "false"           { return symbol(CupSymbol.FALSE); }

    "begin"           { return symbol(CupSymbol.BEGIN); }
    "end"             { return symbol(CupSymbol.END); }
    "exit"            { return symbol(CupSymbol.EXIT); }
    "if"              { return symbol(CupSymbol.IF); }
    "then"            { return symbol(CupSymbol.THEN); }
    "else"            { return symbol(CupSymbol.ELSE); }
    "while"           { return symbol(CupSymbol.WHILE); }
    "do"              { return symbol(CupSymbol.DO); }
    "docase"          { return symbol(CupSymbol.DOCASE); }
    "case"            { return symbol(CupSymbol.CASE); }
    "otherwise"       { return symbol(CupSymbol.OTHERWISE); }
    "endcase"         { return symbol(CupSymbol.ENDCASE); }

    "print"           { return symbol(CupSymbol.PRINT); }
    "readint"         { return symbol(CupSymbol.READINT); }
    "length"          { return symbol(CupSymbol.LENGTH); }
    "position"        { return symbol(CupSymbol.POSITION); }
    "readstr"         { return symbol(CupSymbol.READSTR); }
    "concatenate"     { return symbol(CupSymbol.CONCATENATE); }
    "substring"       { return symbol(CupSymbol.SUBSTRING); }

    ":="              {return symbol(CupSymbol.ASSIGN); }
    "="               { return symbol(CupSymbol.EQ); }
    "<"               { return symbol(CupSymbol.LT); }
    "<="              { return symbol(CupSymbol.LE); }
    ">"               { return symbol(CupSymbol.GT); }
    ">="              { return symbol(CupSymbol.GE); }
    "<>"              { return symbol(CupSymbol.NE); }

    "=="              { return symbol(CupSymbol.STREQ); }
    "!="              { return symbol(CupSymbol.STRNOTEQ); }

    ";"                { return symbol(CupSymbol.SEMI); }
    ","                { return symbol(CupSymbol.COMMA); }
    "("                { return symbol(CupSymbol.LPAREN); }
    ")"                { return symbol(CupSymbol.RPAREN); }
    "+"                { return symbol(CupSymbol.PLUS); }
    "-"                { return symbol(CupSymbol.MINUS); }
    "*"                { return symbol(CupSymbol.TIMES); }
    "%"                { return symbol(CupSymbol.MODE);  }
    "/"                { return symbol(CupSymbol.DIVIDE); }

    {NUM}      { return symbol(CupSymbol.NUM, new Integer(yytext())); }
    {IDENT}       { return symbol(CupSymbol.IDENT, new String(yytext()));}
    {STRING}      { return symbol(CupSymbol.STRING, new String(yytext())); }

    {WhiteSpace}       { /* do nothing */ }
    <<EOF>> { return symbol(CupSymbol.EOF); }
}


/* error */
[^]                    { throw new Error("Illegal character <"+yytext()+">"); }