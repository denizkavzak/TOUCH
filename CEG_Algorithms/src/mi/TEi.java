package mi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graph.*;
import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import test.TestInput;

public class TEi {

    private ArrayList<CEG_Node_in_Exp> component;
    private int compID;
    private Effect effect;

    private Set<List<CEG_Node>> trueSet;
    private Set<TestInput> possibleTestInputs;
    public Set<TestInput> trueInputs;

    public TEi(int compID, Effect effect, ArrayList<CEG_Node_in_Exp> component, Set<TestInput> possibleTestInputs) {
        this.compID = compID;
        this.effect = effect;
        this.component = component;
        trueSet = new HashSet<>();
        trueInputs = new HashSet<>();
        this.possibleTestInputs = possibleTestInputs;

        setTEi();
    }

    private void setTEi() {

        String exp = "";

        for (CEG_Node_in_Exp c : component) {

            if (c.isNegated()) {
                exp = exp + "!" + c.getNode().getLabel() + " && ";
            } else {
                exp = exp + c.getNode().getLabel() + " && ";
            }
        }
        int last = exp.lastIndexOf("&&");
        exp = exp.substring(0, last);

        String modifiedExp;

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

                    trueInputs.add(ti);

                }
            } catch (MalformedBooleanException e) {

                e.printStackTrace();
            }
        }

    }

    public void setTrueSet(Set<List<CEG_Node>> trueSet) {
        this.trueSet.clear();
        this.trueSet = trueSet;
    }

    public ArrayList<CEG_Node_in_Exp> getComponent() {
        return component;
    }

    public int getCompID() {
        return compID;
    }

    public Effect getEffect() {
        return effect;
    }

    public Set<List<CEG_Node>> getTrueSet() {
        return trueSet;
    }

    public Set<TestInput> getTrueInputs() {
        return trueInputs;
    }

    public void setTrueInputs(Set<TestInput> trueInputs) {
        trueInputs.clear();
        this.trueInputs = trueInputs;
    }

}
