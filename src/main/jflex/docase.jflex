package ru.ivmiit.language.interpreter;

import java_cup.runtime.*;
import jflex.sym;
%%
%class Docase
%unicode
%cup
%line
%column
%states IN_CASE WAIT_END_CASE EXECUTE_CASE
DO = "DO"
CASE = "CASE"
END_CASE = "ENDCASE"
OTHERWISE = "OTHERWISE"
DIGIT = "[0-9]*[.[0-9]{1}]{0,1}"
LEXPR = "[+-*/0-9. \\(\\)]*"
OPERATION_PLUS = "\\+"
OPERATION_MINUS = "\\-"
OPERATION_MULT = "\\*"
OPERATION_DIV = "\\/"
%%
{DO} {
          System.out.print(yytext());
      }
{CASE} {
          System.out.print(yytext());
      }
{END_CASE} {
          System.out.print(yytext());
      }
{DIGIT} {
          System.out.print(yytext());
      }
{LEXPR} {
          System.out.print(yytext());
      }
{END_CASE} {
          System.out.print(yytext());
      }
{CASE} {
          System.out.print(yytext());
      }