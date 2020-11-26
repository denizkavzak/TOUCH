package Fourier;

import java.util.ArrayList;
import test.TestInput;

/**
 *
 * @author deniz.kavzak
 */
public class Function {

    private ArrayList<Variable> variables;

    public Function() {
        variables = new ArrayList<>();
    }

    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public double calculateValue(TestInput ti) {

        double value = 0;

        for (String s : ti.getTestInput().keySet()) {
            for (Variable v : variables) {
                if (v.getCause().getLabel().equals(s)) {
                    value = value + v.calculateValue(ti.getTestInput().get(s));
                }
            }
        }
        return value;
    }

    @Override
    public String toString() {
        String s = "";

        for (Variable v : variables) {
            s = s + v.toString() + " ";
        }

        return s;
    }

}
