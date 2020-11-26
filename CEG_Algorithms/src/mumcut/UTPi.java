package mumcut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import graph.CEG_Node_in_Exp;
import graph.Effect;
import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import org.openide.util.Exceptions;
import test.TestInput;

public class UTPi {

    private ArrayList<CEG_Node_in_Exp> component;
    private int compID;
    private Effect effect;
    private Set<TestInput> trueTI;
    private Set<TestInput> possibleTestInputs;

    public UTPi(int compID, Effect effect, ArrayList<CEG_Node_in_Exp> component, Set<TestInput> possibleTestInputs) {
        this.component = component;
        this.compID = compID;
        this.effect = effect;
        trueTI = new HashSet<>();
        this.possibleTestInputs = possibleTestInputs;
        setUTPi();
    }

    public Set<TestInput> getTrueTI() {
        return trueTI;
    }

    private ArrayList<ArrayList<CEG_Node_in_Exp>> getOtherTerms() {
        ArrayList<ArrayList<CEG_Node_in_Exp>> otherTerms = new ArrayList<>();
        int i = 0;
        for (ArrayList<CEG_Node_in_Exp> a : effect.getMin_DNF_Exp()) {
            if (i != compID) {
                otherTerms.add(a);
            }
            i++;
        }

        return otherTerms;
    }

    private String getStringComponent(ArrayList<CEG_Node_in_Exp> component) {
        String exp = "";

        for (CEG_Node_in_Exp c : component) {

            if (c.isNegated()) {
                exp = exp + "!" + c.getNode().getLabel() + " && ";
            } else {
                exp = exp + c.getNode().getLabel() + " && ";
            }
        }

        int last = exp.lastIndexOf("&& ");
        exp = exp.substring(0, last);

        return exp;
    }

    private void setUTPi() {

        String exp = getStringComponent(component);

        String modifiedExp;

        String effectExp = effect.getRelation().getExpression();
        String modifiedEffectExp = "";

        ArrayList<ArrayList<CEG_Node_in_Exp>> otherComponents = getOtherTerms();
        boolean flag;
        //for (Set<CEG_Node> set : selectedSet) {
        for (TestInput ti : possibleTestInputs) {
            flag = true;
            modifiedExp = exp;
            //modifiedEffectExp = effectExp;

            for (String s : ti.getTestInput().keySet()) {
                while (modifiedExp.contains(s + " ")) {
                    String check = s + " ";
                    modifiedExp = modifiedExp.replace(check, ti.valueToString(ti.getTestInput().get(s)) + " ");
                }
            }

            try {
                BooleanExpression be = BooleanExpression.readLeftToRight(modifiedExp);
                if (!be.booleanValue()) {
                    flag = false;
                }

            } catch (MalformedBooleanException ex) {
                Exceptions.printStackTrace(ex);
            }

            if (flag) {
                outerloop:
                for (ArrayList<CEG_Node_in_Exp> c : otherComponents) {
                    modifiedEffectExp = getStringComponent(c);

                    for (String nd : ti.getTestInput().keySet()) {

                        while (modifiedEffectExp.contains(nd + " ")) {
                            String check = nd + " ";
                            modifiedEffectExp = modifiedEffectExp.replace(check, ti.valueToString(ti.getTestInput().get(nd)) + " ");
                        }
                    }
                    try {

                        BooleanExpression be2 = BooleanExpression.readLeftToRight(modifiedEffectExp);

                        if (be2.booleanValue()) {
                            flag = false;
                            break outerloop;
                        }
                    } catch (MalformedBooleanException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
            if (flag) {
                trueTI.add(ti);
            }
        }
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

    public void setTrueTI(Set<TestInput> trueTI) {
        trueTI.clear();
        this.trueTI = trueTI;
    }

}
