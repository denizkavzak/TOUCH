package Fourier;

import graph.CEG_Node;
import test.Value;

/**
 *
 * @author deniz.kavzak
 */
public class Variable {

    private double coefficient;
    private CEG_Node cause;

    public Variable(double coefficient, CEG_Node cause) {
        this.coefficient = coefficient;
        this.cause = cause;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public CEG_Node getCause() {
        return cause;
    }

    public double calculateValue(Value value) {
        if (value == Value.True) {
            return coefficient;
        } else {
            return -1 * coefficient;
        }
    }

    @Override
    public String toString() {
        if (coefficient > 0) {
            return "+" + coefficient + "*" + cause.getLabel();
        } else {
            return coefficient + "*" + cause.getLabel();
        }
    }
}
