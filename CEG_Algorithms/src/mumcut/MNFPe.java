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

public class MNFPe {

    private CEG ceg;
    private Effect effect;
    private ArrayList<ArrayList<CEG_Node_in_Exp>> DNF_Expression;
    private NFP nfp;
    private Set<TestInput> tests;

    public MNFPe(CEG ceg, Effect effect) {
        this.ceg = ceg;
        this.effect = effect;
        DNF_Expression = effect.getMin_DNF_Exp();
        nfp = new NFP(ceg, effect);
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

        NFPi nfpi = nfp.getNFPi(compID);

        for (NFPij nfpij : nfpi.getNFPijs()) {

            variableCovers = new HashMap<>();

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

            Set<TestInput> tis = nfpij.getFalseSet();

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
                        }
                    }

                    if (flag) {
                        allCovered = true;
                        break;
                    }
                }

            }

        }

    }

    /*
    private void choseTestSeti(int compID) {

        ArrayList<CEG_Node_in_Exp> nodes = DNF_Expression.get(compID);
        //ArrayList<CEG_Node> notContained = new ArrayList<>();
        HashMap<String, VariableCover> variableCovers = new HashMap<>();

        NFPi nfpi = nfp.getNFPi(compID);

        for (Cause c : ceg.getCauseNodes()) {

            boolean found = false;

            for (CEG_Node_in_Exp n : nodes) {
                if (n.getNode().getLabel().equals(c.getLabel())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                //notContained.add(c);
                variableCovers.put(c.getLabel(), new VariableCover(c.getLabel()));
            }
        }

        Set<TestInput> tis = nfpi.getFalseSet();

        //System.out.println("tis size: " + tis.size());
        boolean allCovered = false;

        // System.out.println("tis size :" + tis.size());
        for (TestInput ti : tis) {
            //if (!trueSet.contains(ti)) {
            // if (tests.isEmpty()) {
            // tests.add(ti);
            //System.out.println("Added ti : " + ti);
            //  for (String s : variableCovers.keySet()) {
            //    variableCovers.get(s).setValue(ti.getTestInput().get(s));
            // }
            //} else 
            if (allCovered == false) {
                //System.out.println("ti : " + ti);
                tests.add(ti);
                //System.out.println("Added ti : " + ti);
                for (String s : variableCovers.keySet()) {
                    variableCovers.get(s).setValue(ti.getTestInput().get(s));
                }

                boolean flag = true;

                for (String s : variableCovers.keySet()) {
                    if (!variableCovers.get(s).isCovered()) {
                        flag = false;
                    }
                }

                if (flag) {
                    allCovered = true;
                    break;
                }
            }
            //}
            //}
        }
    }*/

    public Set<TestInput> getTests() {
        return tests;
    }

}
