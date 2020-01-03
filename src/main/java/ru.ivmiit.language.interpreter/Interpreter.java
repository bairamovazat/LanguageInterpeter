package ru.ivmiit.language.interpreter;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

abstract class BaseInstruction {
    private int line;
    private int column;
    private Interpreter interpreter;

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public IllegalArgumentException addPositionOnMessage(IllegalArgumentException throwable) {
        return new IllegalArgumentException(throwable.getMessage() + " on line: " + getLine() + ", on column: " + getColumn(), throwable);
    }
}

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

    @Getter
    private List<CupError> cupErrorList = new ArrayList<>();
    private HashMap<String, Object> hm = new HashMap<>();
    private InstructionList instructionList;
    //Нужно продублировать входящий поток для считывания числел (для input)
    @Getter
    @Setter
    private InputStream inputStream;

    @Getter
    @Setter
    private Consumer<String> handler = (s) -> {
    };
    @Getter
    @Setter
    private Consumer<String> errorHandler = (s) -> {
    };

    public Interpreter(InstructionList instructionList) {
        this.instructionList = instructionList;
    }

    public void exec() {
        if (cupErrorList.size() != 0) {
            throw new IllegalArgumentException("Запуск невозможен. Есть синтаксические ошибки");
        }
        instructionList.run(hm);
    }

    public void updateInstructions(InstructionList instructionList) {
        this.instructionList = instructionList;
    }

    private void addError(CupError cupError) {
//        errorHandler.accept(cupError.getError());
        cupErrorList.add(cupError);
    }

    public void addError(String message, Object info) {
        addError(new CupError(message, info));
    }
}

/**
 * VARS
 */
class ID extends BaseInstruction implements Expr {
    private String name;

    public ID(String s) {
        name = s;
    }

    public Object run(HashMap<String, Object> hm) {
        return hm.get(name);
    }
}

class AssignInstruction extends BaseInstruction implements SimpleInstruction {
    private String name;
    private Expr val;

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
class PlusOperator extends BaseInstruction implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 + (Integer) v2;
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }
}

class TimesOperator extends BaseInstruction implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {
        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);
        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 * (Integer) v2;
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }
}

class MinusOperator extends BaseInstruction implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {
        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);
        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 - (Integer) v2;
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }
}

class DivideOperator extends BaseInstruction implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {
        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);
        if (v1 instanceof Integer && v2 instanceof Integer) {
            if ((Integer) v2 == 0) {
                throw new ArithmeticException("Error: division by zero");

            }
            return (Integer) v1 / (Integer) v2;
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }
}

class ModeOperator extends BaseInstruction implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {
        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            if ((Integer) v2 == 0) {
                throw new ArithmeticException("Error: division by zero");
            }
            return (Integer) v1 % (Integer) v2;
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }
}

class OperatorExpression extends BaseInstruction implements Expr {

    private Expr e;
    private Expr e2;
    private Operator o;

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
class IntExpression extends BaseInstruction implements Expr {
    private int value;

    public IntExpression(int e) {
        value = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return value;
    }
}

class IntEnterExpression extends BaseInstruction implements Expr {
    public Object run(HashMap<String, Object> hm) {
        java.util.Scanner in = new java.util.Scanner(getInterpreter().getInputStream());
        return in.nextInt();
    }
}

class PIntExpression extends BaseInstruction implements Expr {
    private Expr expr;

    public PIntExpression(Expr e) {
        expr = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return expr.run(hm);
    }
}

class UMinusExpression extends BaseInstruction implements Expr {
    private Expr e;

    public UMinusExpression(Expr e) {
        this.e = e;
    }

    public Object run(HashMap<String, Object> hm) {

        Object v = e.run(hm);
        if (v instanceof Integer) {
            return -((Integer) v);
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }

}

class STRLengthExpression extends BaseInstruction implements Expr {
    private Expr e;

    public STRLengthExpression(Expr e) {
        this.e = e;
    }

    public Object run(HashMap<String, Object> hm) {

        Object v = e.run(hm);
        if (v instanceof String) {
            return ((String) v).length();
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }

    }

}

class STRPositionExpression extends BaseInstruction implements Expr {
    private Expr e;
    private Expr e2;

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
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }

}

/**
 * CONDITIONS
 */
class EqCond extends BaseInstruction implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return v1.equals(v2);
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }

    }
}

class LtCond extends BaseInstruction implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 < (Integer) v2;
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }
}

class LeCond extends BaseInstruction implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 <= (Integer) v2;
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }
}

class GtCond extends BaseInstruction implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 > (Integer) v2;
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }
}

class GeCond extends BaseInstruction implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 >= (Integer) v2;
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }
}

class NeCond extends BaseInstruction implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return !v1.equals(v2);
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }
}

class StrEqCond extends BaseInstruction implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof String && v2 instanceof String) {
            return v1.equals(v2);
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }
}

class StrNotEqCond extends BaseInstruction implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof String && v2 instanceof String) {
            return !v1.equals(v2);
        } else {
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }
}

/**
 * BOOLEAN OPERATIONS
 */
class BooleanExpression extends BaseInstruction implements Expr {
    private Boolean value;

    public BooleanExpression(Boolean e) {
        value = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return value;
    }
}

class ConditionBooleanExpression extends BaseInstruction implements Expr {

    private Expr e;
    private Expr e2;
    private Condition c;

    public ConditionBooleanExpression(Expr e, Condition c, Expr e2) {
        this.e = e;
        this.c = c;
        this.e2 = e2;
    }

    public Object run(HashMap<String, Object> hm) {
        return c.test(e, e2, hm);
    }
}

class PBooleanExpression extends BaseInstruction implements Expr {
    private Expr expr;

    public PBooleanExpression(Expr e) {
        expr = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return expr.run(hm);
    }
}

class NegationBooleanExpression extends BaseInstruction implements Expr {
    private Expr expr;

    public NegationBooleanExpression(Expr e) {
        expr = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return !((Boolean) expr.run(hm));
    }
}

class AndBooleanExpression extends BaseInstruction implements Expr {
    private Expr expr;
    private Expr expr2;

    public AndBooleanExpression(Expr e, Expr e2) {
        expr = e;
        expr2 = e2;
    }

    public Object run(HashMap<String, Object> hm) {
        return (Boolean) expr.run(hm) && (Boolean) expr2.run(hm);
    }
}

class OrBooleanExpression extends BaseInstruction implements Expr {
    private Expr expr;
    private Expr expr2;

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

class StringExpression extends BaseInstruction implements Expr {
    private String value;

    public StringExpression(String e) {
        value = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return value;
    }
}

class StrEnterExpression extends BaseInstruction implements Expr {
    public Object run(HashMap<String, Object> hm) {
        java.util.Scanner in = new java.util.Scanner(getInterpreter().getInputStream());
        return in.next();
    }
}

class ConcatStringExpression extends BaseInstruction implements Expr {
    private Expr s;
    private Expr s2;

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
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }
}

class SubStringExpression extends BaseInstruction implements Expr {
    private Expr sExpr;
    private Expr posExpr;
    private Expr lengthExpr;


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
            throw addPositionOnMessage(new IllegalArgumentException("Error: wrong objects type"));
        }
    }
}


class OutputInstruction extends BaseInstruction implements SimpleInstruction {
    private Expr expr;

    public OutputInstruction(Expr e) {
        expr = e;
    }

    public void run(HashMap<String, Object> hm) {
        getInterpreter().getHandler().accept(expr.run(hm).toString());
    }
}


/**
 * FLOW OPERATIONS
 */
class InstructionList extends BaseInstruction {
    private List<SimpleInstruction> simpleInstructions;

    public InstructionList() {
        simpleInstructions = new ArrayList<>();
    }

    public InstructionList(SimpleInstruction s) {
        simpleInstructions = new ArrayList<>();
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

class WhileInstruction extends BaseInstruction implements WhileInstructionI {
    private Expr cond;
    private SimpleInstruction si;

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

class DoWhileInstruction extends BaseInstruction implements WhileInstructionI {
    private Expr cond;
    private SimpleInstruction si;

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

class IfInstruction extends BaseInstruction implements IfInstructionI {

    private Expr condition;
    private SimpleInstruction simpleInstruction;

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

class IfElseInstruction extends BaseInstruction implements IfInstructionI {

    private Expr condition;
    private SimpleInstruction simpleInstruction;
    private SimpleInstruction simpleInstruction2;

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

class DoEndCaseInstruction extends BaseInstruction implements DoEndCaseInstructionI {
    private CaseInstruction caseInstruction;

    public DoEndCaseInstruction(CaseInstruction caseInstruction) {
        this.caseInstruction = caseInstruction;
    }

    public void run(HashMap<String, Object> hm) {
        caseInstruction.run(hm);
    }
}

class CaseInstruction extends BaseInstruction implements CaseInstructionI {

    private Expr condition;
    private InstructionList simpleInstruction;
    private CaseInstruction caseInstruction;
    private InstructionList otherwiseInstruction;


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

class BeginEndInstruction extends BaseInstruction implements SimpleInstruction {
    private InstructionList instructions;

    public BeginEndInstruction(InstructionList instructions) {
        this.instructions = instructions;
    }

    public void run(HashMap<String, Object> hm) {
        instructions.run(hm);
    }
}