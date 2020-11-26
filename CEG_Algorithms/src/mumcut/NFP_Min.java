package mumcut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import graph.CEG;
import graph.CEG_Node_in_Exp;
import graph.Effect;
import test.TestInput;

public class NFP_Min {

    private CEG ceg;
    private ArrayList<NFPi> NFPis;
    private ArrayList<ArrayList<CEG_Node_in_Exp>> DNF_Expression;
    private Effect effect;
    private Set<TestInput> possibleTestInputs;
    private Set<TestInput> NFP;
    private Set<TestInput> tests;

    public NFP_Min(CEG ceg, Effect effect) {
        this.effect = effect;
        DNF_Expression = effect.getMin_DNF_Exp();
        this.ceg = ceg;
        NFPis = new ArrayList<>();
        NFP = new HashSet<>();
        possibleTestInputs = ceg.getDc().getValidAllPossibleTests(effect);

        tests = new HashSet<>();
        setNFPis();
    }

    public Set<TestInput> getTests() {
        return tests;
    }

    public Effect getEffect() {
        return effect;
    }

    public NFPi getNFPi(int compID) {
        for (NFPi nfpi : NFPis) {
            if (nfpi.getCompID() == compID) {
                return nfpi;
            }
        }
        return null;
    }

    public int getVarSize(int compID) {
        return DNF_Expression.get(compID).size();
    }

    public int getComponentSize() {
        return DNF_Expression.size();
    }

    private void setNFPis() {

        int compID = 0;

        for (ArrayList<CEG_Node_in_Exp> component : DNF_Expression) {
            NFPi nfpi = new NFPi(component, compID, effect, possibleTestInputs);
            NFPis.add(nfpi);
            compID++;
            NFP.addAll(nfpi.getFalseSet());
        }

        for (NFPi nfpi : NFPis) {

            for (TestInput t : nfpi.getFalseSet()) {
                tests.add(t);
            }
        }

    }

    public ArrayList<NFPi> getNFPis() {
        return NFPis;
    }

    public Set<TestInput> getNFP() {
        return NFP;
    }

}
