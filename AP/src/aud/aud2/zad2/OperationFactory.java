package aud.aud2.zad2;

public class OperationFactory {
    private static final Operation ADD = (r, v) -> r + v;
    private static final Operation SUBTRACT = (r, v) -> r - v;
    private static final Operation MULTIPLY_OP = (r, v) -> r * v;
    private static final Operation DIVIDE_OP = (r, v) -> r / v;

    public static Operation getOperation(char operator) throws UnknownOperatorException {
        if (operator == '+') {
            return ADD;
        } else if (operator == '-') {
            return SUBTRACT;
        } else if (operator == '*') {
            return MULTIPLY_OP;
        } else if (operator == '/') {
            return DIVIDE_OP;
        } else {
            throw new UnknownOperatorException(operator);
        }
    }
}
