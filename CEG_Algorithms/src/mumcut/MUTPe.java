package mumcut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import graph.CEG;
import graph.CEG_Node_in_Exp;
import graph.Cause;
import graph.Effect;
import java.util.HashMap;
import test.TestInput;

public class MUTPe {

    private CEG ceg;
    private Effect effect;
    private ArrayList<ArrayList<CEG_Node_in_Exp>> DNF_Expression;
    private UTP utp;
    private Set<TestInput> tests;

    public MUTPe(CEG ceg, Effect effect) {
        this.ceg = ceg;
        this.effect = effect;
        DNF_Expression = effect.getMin_DNF_Exp();
        utp = new UTP(ceg, effect);
        tests = new HashSet<>();
        choseTests();
    }

    private void choseTests() {
        for (int i = 0; i < DNF_Expression.size(); i++) {
            choseTestSeti(i);
        }

    }

    private void choseTestSeti(int compID) {

        ArrayList<CEG_Node_in_Exp> nodes = DNF_Expression.get(compID);
        HashMap<String, VariableCover> variableCovers = new HashMap<>();

        UTPi utpi = utp.getUTPi(compID);

        for (Cause c : ceg.getCauseNodes()) {

            boolean found = false;

            for (CEG_Node_in_Exp n : nodes) {
                if (n.getNode().getLabel().equals(c.getLabel())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                variableCovers.put(c.getLabel(), new VariableCover(c.getLabel()));
            }
        }

        Set<TestInput> tis = utpi.getTrueTI();

        boolean allCovered = false;

        for (TestInput ti : tis) {

            if (allCovered == false) {
                tests.add(ti);

                for (String s : variableCovers.keySet()) {
                    variableCovers.get(s).setValue(ti.getTestInput().get(s));
                }

                boolean flag = true;

                for (String s : variableCovers.keySet()) {
                    if (!variableCovers.get(s).isCovered()) {
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    allCovered = true;
                    break;
                }
            }

        }

    }

    public Set<TestInput> getTests() {
        return tests;
    }

}
