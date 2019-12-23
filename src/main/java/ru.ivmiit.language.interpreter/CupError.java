package ru.ivmiit.language.interpreter;


public class CupError {
    private String message;
    private Object info;

    public CupError(String message, Object info) {
        this.message = message;
        this.info = info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    public String getError() {
        StringBuilder m = new StringBuilder("Error");
        if (getInfo() instanceof java_cup.runtime.Symbol) {
            java_cup.runtime.Symbol s = ((java_cup.runtime.Symbol) info);
            m.append(" in line ").append(s.left + 1);
            m.append(", column ").append(s.right + 1);
        }
        m.append(" : ").append(message);
        return m.toString();
    }
}
