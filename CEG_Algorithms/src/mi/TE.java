package mi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import graph.CEG;
import graph.CEG_Node;
import graph.CEG_Node_in_Exp;
import graph.Effect;
import test.TestInput;

public class TE {

    private Effect effect;
    private ArrayList<ArrayList<CEG_Node_in_Exp>> DNF_Expression;

    private ArrayList<TEi> TEis;
    private Set<List<CEG_Node>> TSE;
    private Set<TestInput> possibleTestInputs;
    private Set<TestInput> inputsTSE;

    public TE(CEG ceg, Effect effect) {
        this.effect = effect;

        DNF_Expression = effect.getMin_DNF_Exp();

        possibleTestInputs = ceg.getDc().getValidAllPossibleTests(effect);

        inputsTSE = new HashSet<>();
        TEis = new ArrayList<>();
        TSE = new HashSet<>();
        setTEis();
        //selectTSE();
    }

    private void setTEis() {

        int compID = 0;

        for (ArrayList<CEG_Node_in_Exp> component : DNF_Expression) {
            TEi tei = new TEi(compID, effect, component, possibleTestInputs);
            TEis.add(tei);

            compID++;
        }

        eliminateMutualTEis(); //eklenecek

    }

    private void eliminateMutualTEis() {

        for (int i = 0; i < TEis.size() - 1; i++) {
            for (int j = i + 1; j < TEis.size(); j++) {
                Set<TestInput> s1 = new HashSet<>();
                Set<TestInput> s2 = new HashSet<>();

                for (TestInput ti : TEis.get(i).getTrueInputs()) {
                    s1.add(ti);
                }
                for (TestInput ti : TEis.get(j).getTrueInputs()) {
                    s2.add(ti);
                }

                for (TestInput ti : TEis.get(i).getTrueInputs()) {
                    for (TestInput ti2 : TEis.get(j).getTrueInputs()) {

                        if (ti.equals(ti2)) {
                            s1.remove(ti);
                            s2.remove(ti2);
                        }
                    }
                }

                TEis.get(i).setTrueInputs(s1);
                TEis.get(j).setTrueInputs(s2);

            }
        }

    }

    /*private boolean sameSet(List<CEG_Node> l1, List<CEG_Node> l2) {

        if (l1.size() != l2.size()) {
            return false;
        }
        int count = 0;
        for (CEG_Node c1 : l1) {
            for (CEG_Node c2 : l2) {
                if (c1.getLabel().equals(c2.getLabel())) {
                    count++;
                    break;
                }
            }
        }
        if (count == l1.size()) {
            return true;
        }

        return false;
    }*/
    public void selectTSE() {

        for (TEi tei : TEis) {
            Random r = new Random();

            if (!tei.getTrueInputs().isEmpty()) {
                int i = r.nextInt(tei.getTrueInputs().size());
                int c = 0;
                for (TestInput ti : tei.getTrueInputs()) {
                    if (c == i) {
                        inputsTSE.add(ti);
                    } else {
                        c++;
                    }
                }
            }
        }
    }

    public void fillInputsTSEwithAll() {
        for (TEi tei : TEis) {
            for (TestInput ti : tei.getTrueInputs()) {
                inputsTSE.add(ti);
            }
        }
    }

    public Effect getEffect() {
        return effect;
    }

    public ArrayList<ArrayList<CEG_Node_in_Exp>> getDNF_Expression() {
        return DNF_Expression;
    }

    public ArrayList<TEi> getTEis() {
        return TEis;
    }

    public Set<List<CEG_Node>> getTSE() {
        return TSE;
    }

    public Set<TestInput> getInputsTSE() {
        return inputsTSE;
    }

}
