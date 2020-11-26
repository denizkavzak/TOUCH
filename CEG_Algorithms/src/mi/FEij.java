package mi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graph.CEG_Node;
import graph.CEG_Node_in_Exp;
import graph.Effect;
import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import test.TestInput;

public class FEij {

    private ArrayList<CEG_Node_in_Exp> component;
    private int compID;
    private int negID;
    private Effect effect;
    private Set<List<CEG_Node>> falseSet;
    private String exp;
    private Set<TestInput> possibleTestInputs;
    private Set<TestInput> falseInputs;

    public FEij(ArrayList<CEG_Node_in_Exp> component, int compID, int negID, Effect effect, String exp, Set<TestInput> possibleTestInputs) {
        this.component = component;
        this.compID = compID;
        this.negID = negID;
        this.effect = effect;
        this.exp = exp;
        falseSet = new HashSet<>();
        this.possibleTestInputs = possibleTestInputs;
        falseInputs = new HashSet<>();
        setFalseSet();
    }

    public ArrayList<CEG_Node_in_Exp> getComponent() {
        return component;
    }

    public int getCompID() {
        return compID;
    }

    public int getNegID() {
        return negID;
    }

    public Effect getEffect() {
        return effect;
    }

    public Set<List<CEG_Node>> getFalseSet() {
        return falseSet;
    }

    public String getExp() {
        return exp;
    }

    private void setFalseSet() {

        String modifiedExp = exp;

        for (TestInput ti : possibleTestInputs) {

            modifiedExp = exp;

            for (String nd : ti.getTestInput().keySet()) {
                while (modifiedExp.contains(nd + " ")) {
                    String check = nd + " ";
                    modifiedExp = modifiedExp.replace(check, ti.valueToString(ti.getTestInput().get(nd)) + " ");
                }
            }

            try {
                BooleanExpression be = BooleanExpression.readLeftToRight(modifiedExp);

                if (be.booleanValue()) {
                    falseInputs.add(ti);

                }
            } catch (MalformedBooleanException e) {

                e.printStackTrace();
            }
        }
    }

    public Set<TestInput> getFalseInputs() {
        return falseInputs;
    }

}
