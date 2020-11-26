package mi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graph.CEG_Node;
import graph.CEG_Node_in_Exp;
import graph.Effect;
import test.TestInput;

public class FEi {

    private ArrayList<CEG_Node_in_Exp> component;
    private int compID;
    private Effect effect;
    private ArrayList<FEij> FEijs;
    private Set<List<CEG_Node>> falseSet;
    private Set<TestInput> possibleTestInputs;
    private Set<TestInput> falseIInputs;

    public FEi(ArrayList<CEG_Node_in_Exp> component, int compID, Effect effect, Set<TestInput> possibleTestInputs) {
        this.component = component;
        this.compID = compID;
        this.effect = effect;
        FEijs = new ArrayList<>();
        falseSet = new HashSet<>();
        this.possibleTestInputs = possibleTestInputs;
        falseIInputs = new HashSet<>();
        setFEijs();
        setFalseSetTotal();
    }

    private void setFalseSetTotal() {
        for (FEij feij : FEijs) {

            for (TestInput ti : feij.getFalseInputs()) {
                falseIInputs.add(ti);
            }
        }
    }

    private void setFEijs() {

        for (int i = 0; i < component.size(); i++) {
            setFEij(i);
        }

    }

    private void setFEij(int negID) {

        String exp = "";

        for (int i = 0; i < component.size(); i++) {
            CEG_Node_in_Exp c = component.get(i);

            if (i == negID) {
                if (!c.isNegated()) {
                    exp = exp + "!" + c.getNode().getLabel() + " && ";
                } else {
                    exp = exp + c.getNode().getLabel() + " && ";
                }
            } else {
                if (c.isNegated()) {
                    exp = exp + "!" + c.getNode().getLabel() + " && ";
                } else {
                    exp = exp + c.getNode().getLabel() + " && ";
                }
            }
        }
        int last = exp.lastIndexOf("&&");
        exp = exp.substring(0, last);

        FEij feij = new FEij(component, compID, negID, effect, exp, possibleTestInputs);
        FEijs.add(feij);

    }

    public Set<List<CEG_Node>> getFalseSet() {
        return falseSet;
    }

    public Set<TestInput> getFalseIInputs() {
        return falseIInputs;
    }

}
