package mumcut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import graph.CEG;
import graph.CEG_Node_in_Exp;
import graph.Effect;
import test.TestInput;

public class UTP_Min {

    private CEG ceg;
    private ArrayList<UTPi> utpis;
    private Effect effect;
    private ArrayList<ArrayList<CEG_Node_in_Exp>> DNF_Expression;

    private Set<TestInput> tests;
    private Set<TestInput> possibleTestInputs;

    public UTP_Min(CEG ceg, Effect effect) {
        this.ceg = ceg;
        this.effect = effect;
        DNF_Expression = effect.getMin_DNF_Exp();

        possibleTestInputs = ceg.getDc().getValidAllPossibleTests(effect);

        utpis = new ArrayList<>();
        tests = new HashSet<>();

        setUtpis();

    }

    public Set<TestInput> getTests() {
        return tests;
    }

    public ArrayList<UTPi> getUtpis() {
        return utpis;
    }

    public Effect getEffect() {
        return effect;
    }

    public UTPi getUTPi(int compID) {
        for (UTPi utpi : utpis) {
            if (utpi.getCompID() == compID) {
                return utpi;
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

    private void setUtpis() {

        int compID = 0;
        for (ArrayList<CEG_Node_in_Exp> component : DNF_Expression) {

            UTPi utpi = new UTPi(compID, effect, component, possibleTestInputs);

            utpis.add(utpi);
            compID++;
        }

        eliminateMutualUtpis();

        for (UTPi utpi : utpis) {

            for (TestInput t : utpi.getTrueTI()) {
                tests.add(t);
            }

        }
    }

    private void eliminateMutualUtpis() {

        for (int i = 0; i < utpis.size() - 1; i++) {
            for (int j = i + 1; j < utpis.size(); j++) {

                Set<TestInput> iC = new HashSet<>();
                Set<TestInput> jC = new HashSet<>();

                iC.addAll(utpis.get(i).getTrueTI());
                jC.addAll(utpis.get(j).getTrueTI());

                for (TestInput ti : utpis.get(i).getTrueTI()) {
                    for (TestInput ti2 : utpis.get(j).getTrueTI()) {

                        if (ti.equals(ti2)) {
                            iC.remove(ti);
                            jC.remove(ti2);
                        }
                    }
                }

                utpis.get(i).setTrueTI(iC);
                utpis.get(j).setTrueTI(jC);
            }
        }
    }

}
