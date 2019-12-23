package ru.ivmiit.language.interpreter;

import java.io.*;
import java.util.*;

interface Expr {
    Object run(HashMap<String, Object> hm);
}

interface Condition {
    boolean test(Expr e1, Expr e2, HashMap<String, Object> hm);
}

interface Operator {
    int count(Expr e1, Expr e2, HashMap<String, Object> hm);
}

interface SimpleInstruction {
    void run(HashMap<String, Object> hm);
}

interface WhileInstructionI extends SimpleInstruction {
}

interface IfInstructionI extends SimpleInstruction {
}

interface DoEndCaseInstructionI extends SimpleInstruction {
}

interface CaseInstructionI {
    void run(HashMap<String, Object> hm);
}

public class Interpreter {

    private List<CupError> cupErrorList = new ArrayList<>();
    private HashMap<String, Object> hm = new HashMap<>();
    private InstructionList instructionList;

    public Interpreter(InstructionList instructionList) {
        this.instructionList = instructionList;
    }

    public void exec() {
        if(cupErrorList.size() != 0) {
            throw new IllegalArgumentException("Запуск невозможен. Есть синтаксические ошибки");
        }
        instructionList.run(hm);
    }

    public void updateInstructions(InstructionList instructionList) {
        this.instructionList = instructionList;
    }

    public void addError(CupError cupError) {
        System.err.println(cupError.getError());
        cupErrorList.add(cupError);
    }

    public void addError(String message, Object info) {
        addError(new CupError(message, info));
    }
}

/**
 * VARS
 */
class ID implements Expr {
    String name;

    public ID(String s) {
        name = s;
    }

    public Object run(HashMap<String, Object> hm) {
        return hm.get(name);
    }
}

class AssignInstruction implements SimpleInstruction {
    String name;
    Expr val;

    public AssignInstruction(String i, Expr e) {
        name = i;
        val = e;
    }

    public void run(HashMap<String, Object> hm) {
        hm.put(name, val.run(hm));
    }
}


/**
 * OPERATORS
 */
class PlusOperator implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 + (Integer) v2;
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }
}

class TimesOperator implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {
        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);
        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 * (Integer) v2;
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }
}

class MinusOperator implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {
        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);
        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 - (Integer) v2;
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }
}

class DivideOperator implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {
        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);
        if (v1 instanceof Integer && v2 instanceof Integer) {
            if ((Integer) v2 == 0) {
                throw new ArithmeticException("Error: division by zero");

            }
            return (Integer) v1 / (Integer) v2;
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }
}

class ModeOperator implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {
        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            if ((Integer) v2 == 0) {
                throw new ArithmeticException("Error: division by zero");
            }
            return (Integer) v1 % (Integer) v2;
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }
}

class OperatorExpression implements Expr {

    Expr e, e2;
    Operator o;

    public OperatorExpression(Expr e, Operator o, Expr e2) {
        this.e = e;
        this.e2 = e2;
        this.o = o;
    }

    public Object run(HashMap<String, Object> hm) {
        return o.count(e, e2, hm);
    }
}

/**
 * INT OPERATIONS
 */
class IntExpression implements Expr {
    int value;

    public IntExpression(int e) {
        value = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return value;
    }
}

class IntEnterExpression implements Expr {
    public Object run(HashMap<String, Object> hm) {
        java.util.Scanner in = new java.util.Scanner(System.in);
        return in.nextInt();
    }
}

class PIntExpression implements Expr {
    Expr expr;

    public PIntExpression(Expr e) {
        expr = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return expr.run(hm);
    }
}

class UMinusExpression implements Expr {
    Expr e;

    public UMinusExpression(Expr e) {
        this.e = e;
    }

    public Object run(HashMap<String, Object> hm) {

        Object v = e.run(hm);
        if (v instanceof Integer) {
            return -((Integer) v);
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }

}

class STRLengthExpression implements Expr {
    Expr e;

    public STRLengthExpression(Expr e) {
        this.e = e;
    }

    public Object run(HashMap<String, Object> hm) {

        Object v = e.run(hm);
        if (v instanceof String) {
            return ((String) v).length();
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }

    }

}

class STRPositionExpression implements Expr {
    Expr e, e2;

    public STRPositionExpression(Expr e, Expr e2) {
        this.e = e;
        this.e2 = e2;
    }

    public Object run(HashMap<String, Object> hm) {

        Object v1 = e.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof String && v2 instanceof String) {
            String s = (String) v1;
            String s2 = (String) v2;

            int pos = s.indexOf(s2);
            return (pos != -1) ? pos + 1 : 0;
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }

}

/**
 * CONDITIONS
 */
class EqCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 == (Integer) v2;
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }

    }
}

class LtCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 < (Integer) v2;
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }
}

class LeCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 <= (Integer) v2;
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }
}

class GtCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 > (Integer) v2;
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }
}

class GeCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 >= (Integer) v2;
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }
}

class NeCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 != (Integer) v2;
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }
}

class StrEqCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof String && v2 instanceof String) {
            return ((String) v1).equals((String) v2);
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }
}

class StrNotEqCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof String && v2 instanceof String) {
            return !((String) v1).equals((String) v2);
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }
}

/**
 * BOOLEAN OPERATIONS
 */
class BooleanExpression implements Expr {
    Boolean value;

    public BooleanExpression(Boolean e) {
        value = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return value;
    }
}

class ConditionBooleanExpression implements Expr {

    Expr e, e2;
    Condition c;

    public ConditionBooleanExpression(Expr e, Condition c, Expr e2) {
        this.e = e;
        this.c = c;
        this.e2 = e2;
    }

    public Object run(HashMap<String, Object> hm) {
        return c.test(e, e2, hm);
    }
}

class PBooleanExpression implements Expr {
    Expr expr;

    public PBooleanExpression(Expr e) {
        expr = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return expr.run(hm);
    }
}

class NegationBooleanExpression implements Expr {
    Expr expr;

    public NegationBooleanExpression(Expr e) {
        expr = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return !((Boolean) expr.run(hm));
    }
}

class AndBooleanExpression implements Expr {
    Expr expr, expr2;

    public AndBooleanExpression(Expr e, Expr e2) {
        expr = e;
        expr2 = e2;
    }

    public Object run(HashMap<String, Object> hm) {
        return (Boolean) expr.run(hm) && (Boolean) expr2.run(hm);
    }
}

class OrBooleanExpression implements Expr {
    Expr expr, expr2;

    public OrBooleanExpression(Expr e, Expr e2) {
        expr = e;
        expr2 = e2;
    }

    public Object run(HashMap<String, Object> hm) {
        return (Boolean) expr.run(hm) || (Boolean) expr2.run(hm);
    }
}

/**
 * STRING OPERATIONS
 */

class StringExpression implements Expr {
    String value;

    public StringExpression(String e) {
        value = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return value;
    }
}

class StrEnterExpression implements Expr {
    public Object run(HashMap<String, Object> hm) {
        java.util.Scanner in = new java.util.Scanner(System.in);
        return in.next();
    }
}

class ConcatStringExpression implements Expr {
    Expr s, s2;

    public ConcatStringExpression(Expr s, Expr s2) {
        this.s = s;
        this.s2 = s2;
    }

    public Object run(HashMap<String, Object> hm) {
        Object v1 = s.run(hm);
        Object v2 = s2.run(hm);

        if (v1 instanceof String && v2 instanceof String) {
            return (String) v1 + (String) v2;
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }
}

class SubStringExpression implements Expr {
    Expr sExpr, posExpr, lengthExpr;

    public SubStringExpression(Expr s, Expr pos, Expr length) {
        sExpr = s;
        posExpr = pos;
        lengthExpr = length;
    }

    public Object run(HashMap<String, Object> hm) {

        Object v1 = sExpr.run(hm);
        Object v2 = posExpr.run(hm);
        Object v3 = lengthExpr.run(hm);

        if (v1 instanceof String && v2 instanceof Integer && v3 instanceof Integer) {
            String s = (String) v1;
            int pos = (Integer) v2;
            int length = (Integer) v3;

            if (pos + length - 1 > s.length()) {
                length = s.length() - pos + 1;
            }
            if (pos < 1 || pos > s.length() || length < 1) {
                return "";
            } else {
                return s.substring(pos - 1, pos + length - 1);
            }
        } else {
            throw new IllegalArgumentException("Error: wrong objects type");
        }
    }
}


class OutputInstruction implements SimpleInstruction {
    Expr expr;

    public OutputInstruction(Expr e) {
        expr = e;
    }

    public void run(HashMap<String, Object> hm) {
        System.out.println(expr.run(hm));
    }
}


/**
 * FLOW OPERATIONS
 */
class InstructionList {
    private List<SimpleInstruction> simpleInstructions;

    public InstructionList() {
        simpleInstructions = new ArrayList<SimpleInstruction>();
    }

    public InstructionList(SimpleInstruction s) {
        simpleInstructions = new ArrayList<SimpleInstruction>();
        simpleInstructions.add(s);
    }

    public void add(SimpleInstruction s) {
        simpleInstructions.add(s);
    }

    public void run(HashMap<String, Object> hm) {
        for (SimpleInstruction si : simpleInstructions) {
            si.run(hm);
        }
    }
}

class WhileInstruction implements WhileInstructionI {
    Expr cond;
    SimpleInstruction si;

    public WhileInstruction(Expr c, SimpleInstruction s) {
        cond = c;
        si = s;
    }

    public void run(HashMap<String, Object> hm) {
        while ((Boolean) cond.run(hm)) {
            si.run(hm);
        }
    }
}

class DoWhileInstruction implements WhileInstructionI {
    Expr cond;
    SimpleInstruction si;

    public DoWhileInstruction(Expr c, SimpleInstruction s) {
        cond = c;
        si = s;
    }

    public void run(HashMap<String, Object> hm) {
        do
            si.run(hm);
        while ((Boolean) cond.run(hm));
    }
}

class IfInstruction implements IfInstructionI {

    Expr condition;
    SimpleInstruction simpleInstruction;

    public IfInstruction(Expr condition, SimpleInstruction simpleInstruction) {
        this.condition = condition;
        this.simpleInstruction = simpleInstruction;
    }

    public void run(HashMap<String, Object> hm) {
        if ((Boolean) condition.run(hm)) {
            simpleInstruction.run(hm);
        }
    }
}

class IfElseInstruction implements IfInstructionI {

    Expr condition;
    SimpleInstruction simpleInstruction;
    SimpleInstruction simpleInstruction2;

    public IfElseInstruction(Expr condition, SimpleInstruction simpleInstruction, SimpleInstruction simpleInstruction2) {
        this.condition = condition;
        this.simpleInstruction = simpleInstruction;
        this.simpleInstruction2 = simpleInstruction2;
    }

    public void run(HashMap<String, Object> hm) {
        if ((Boolean) condition.run(hm)) {
            simpleInstruction.run(hm);
        } else {
            simpleInstruction2.run(hm);
        }
    }
}

class DoEndCaseInstruction implements DoEndCaseInstructionI {
    CaseInstruction caseInstruction;

    public DoEndCaseInstruction(CaseInstruction caseInstruction) {
        this.caseInstruction = caseInstruction;
    }

    public void run(HashMap<String, Object> hm) {
        caseInstruction.run(hm);
    }
}

class CaseInstruction implements CaseInstructionI {

    Expr condition;
    InstructionList simpleInstruction;
    CaseInstruction caseInstruction;
    InstructionList otherwiseInstruction;


    public CaseInstruction(Expr condition, InstructionList simpleInstruction) {
        this.condition = condition;
        this.simpleInstruction = simpleInstruction;
    }

    public CaseInstruction(Expr condition, InstructionList simpleInstruction, InstructionList otherwiseInstruction) {
        this.condition = condition;
        this.simpleInstruction = simpleInstruction;
        this.otherwiseInstruction = otherwiseInstruction;
    }

    public CaseInstruction(Expr condition, InstructionList simpleInstruction, CaseInstruction caseInstruction) {
        this.condition = condition;
        this.simpleInstruction = simpleInstruction;
        this.caseInstruction = caseInstruction;
    }

    public void run(HashMap<String, Object> hm) {
        if ((Boolean) condition.run(hm)) {
            simpleInstruction.run(hm);
        } else if (caseInstruction != null) {
            caseInstruction.run(hm);
        } else if (otherwiseInstruction != null) {
            otherwiseInstruction.run(hm);
        }
    }
}

class BeginEndInstruction implements SimpleInstruction {
    private InstructionList instructions;

    public BeginEndInstruction(InstructionList instructions) {
        this.instructions = instructions;
    }

    public void run(HashMap<String, Object> hm) {
        instructions.run(hm);
    }
}