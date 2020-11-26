package mumcut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import graph.CEG_Node_in_Exp;
import graph.Effect;
import test.TestInput;

public class NFPi {

    private ArrayList<CEG_Node_in_Exp> component;
    private int compID;
    private Effect effect;
    private ArrayList<NFPij> NFPijs;
    private Set<TestInput> falseSet;

    private Set<TestInput> possibleTestInputs;

    public NFPi(ArrayList<CEG_Node_in_Exp> component, int compID, Effect effect, Set<TestInput> possibleTestInputs) {
        this.component = component;
        this.compID = compID;
        this.effect = effect;
        this.possibleTestInputs = possibleTestInputs;
        NFPijs = new ArrayList<>();
        falseSet = new HashSet<>();

        setNFPijs();
        setFalseSetTotal();

    }

    private void setFalseSetTotal() {
        for (NFPij nfpij : NFPijs) {
            falseSet.addAll(nfpij.getFalseSet());
        }
    }

    public NFPij getNFPij(int negID) {
        for (NFPij nfpij : NFPijs) {
            if (nfpij.getNegID() == negID) {
                return nfpij;
            }
        }
        return null;
    }

    public int getCompID() {
        return compID;
    }

    private void setNFPijs() {

        for (int i = 0; i < component.size(); i++) {
            setNFPij(i);
        }

    }

    public ArrayList<NFPij> getNFPijs() {
        return NFPijs;
    }

    private void setNFPij(int negID) {

        String exp = "";

        for (int i = 0; i < component.size(); i++) {
            CEG_Node_in_Exp c = component.get(i);

            if (i == negID) {
                if (!c.isNegated()) {
                    exp = exp + "!" + c.getNode().getLabel() + " && ";
                } else {
                    exp = exp + c.getNode().getLabel() + " && ";
                }
            } else if (c.isNegated()) {
                exp = exp + "!" + c.getNode().getLabel() + " && ";
            } else {
                exp = exp + c.getNode().getLabel() + " && ";
            }
        }
        int last = exp.lastIndexOf("&& ");
        exp = exp.substring(0, last);

        NFPij nfpij = new NFPij(component, compID, negID, effect, exp, possibleTestInputs);

        NFPijs.add(nfpij);

    }

    public Set<TestInput> getFalseSet() {
        return falseSet;
    }

}
