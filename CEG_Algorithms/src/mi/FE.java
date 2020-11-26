package mi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graph.CEG;
import graph.CEG_Node;
import graph.CEG_Node_in_Exp;
import graph.Effect;
import test.TestInput;

public class FE {

    private ArrayList<FEi> FEis;
    private ArrayList<ArrayList<CEG_Node_in_Exp>> DNF_Expression;
    private Effect effect;

    private Set<List<CEG_Node>> FSE;

    private Set<TestInput> possibleTestInputs;

    private Set<TestInput> inputsFSE;

    public FE(CEG ceg, Effect effect) {

        this.effect = effect;
        DNF_Expression = effect.getMin_DNF_Exp(); //getDNF_Exp();//
        FEis = new ArrayList<>();
        FSE = new HashSet<List<CEG_Node>>();

        possibleTestInputs = ceg.getDc().getValidAllPossibleTests(effect);

        inputsFSE = new HashSet<>();
        setFEis();
        selectFSE();
    }

    private void setFEis() {

        int compID = 0;

        for (ArrayList<CEG_Node_in_Exp> component : DNF_Expression) {
            FEi fei = new FEi(component, compID, effect, possibleTestInputs);
            FEis.add(fei);
            compID++;
        }
    }

    private void selectFSE() {

        for (FEi fei : FEis) {

            for (TestInput ti : fei.getFalseIInputs()) {
                inputsFSE.add(ti);
            }
        }

    }

    public void eliminatePosFSE(TE te) {

        Set<TestInput> FSEcopy = new HashSet<>();

        for (TestInput ti : inputsFSE) {
            FSEcopy.add(ti);
        }

        for (TestInput ti : te.getInputsTSE()) {
            for (TestInput ti2 : inputsFSE) {
                if (ti.equals(ti2)) {
                    FSEcopy.remove(ti2);
                }
            }
        }

        inputsFSE.clear();
        for (TestInput ti : FSEcopy) {
            inputsFSE.add(ti);
        }

        FSEcopy.clear();

    }

    /*private boolean sameSet(List<CEG_Node> FSEc, List<CEG_Node> set) {

        ArrayList<CEG_Node> list1 = new ArrayList<CEG_Node>(FSEc);
        ArrayList<CEG_Node> list2 = new ArrayList<CEG_Node>(set);

        if (list1.size() != list2.size()) {
            return false;
        } else {

            for (CEG_Node c : list1) {
                boolean flag = false;
                for (CEG_Node n : list2) {
                    if (c.getLabel().equals(n.getLabel())) {
                        flag = true;
                        break;
                    }
                }
                if (flag == false) {
                    return false;
                }
            }

            return true;

        }
    }*/
    public ArrayList<FEi> getFEis() {
        return FEis;
    }

    public Set<List<CEG_Node>> getFSE() {
        return FSE;
    }

    public Effect getEffect() {
        return effect;
    }

    public Set<TestInput> getInputsFSE() {
        return inputsFSE;
    }

}
