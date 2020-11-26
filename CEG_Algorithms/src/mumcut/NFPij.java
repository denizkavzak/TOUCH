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

public class NFPij {

    private ArrayList<CEG_Node_in_Exp> component;
    private int compID;
    private int negID;
    private Effect effect;
    private Set<TestInput> falseSet;
    private String exp;
    private Set<TestInput> possibleTestInputs;

    public NFPij(ArrayList<CEG_Node_in_Exp> component, int compID, int negID, Effect effect, String exp, Set<TestInput> possibleTestInputs) {
        this.component = component;
        this.compID = compID;
        this.negID = negID;
        this.effect = effect;
        this.exp = exp;
        this.possibleTestInputs = possibleTestInputs;
        falseSet = new HashSet<>();
        setFalseSet();
    }

    public CEG_Node_in_Exp getNegNode() {
        return component.get(negID);
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

    public ArrayList<CEG_Node_in_Exp> getComponent() {
        return component;
    }

    public int getCompID() {
        return compID;
    }

    public int getNegID() {
        return negID;
    }

    public Effect getEffect() {
        return effect;
    }

    public Set<TestInput> getFalseSet() {
        return falseSet;
    }

    public String getExp() {
        return exp;
    }

    private void setFalseSet() {

        String modifiedExp = exp;
        String modifiedEffectExp;

        ArrayList<ArrayList<CEG_Node_in_Exp>> otherComponents = getOtherTerms();
        boolean flag;
        for (TestInput ti : possibleTestInputs) {
            flag = true;
            modifiedExp = exp;

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
                        e.printStackTrace();
                    }

                }
            }
            if (flag) {
                falseSet.add(ti);
            }
        }
    }

}
