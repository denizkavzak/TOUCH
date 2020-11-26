package mcdc;

import graph.CEG;
import graph.CEG_Node;
import graph.Cause;
import graph.Effect;
import graph.Intermediate;
import graph.Relation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import org.openide.util.Exceptions;
import test.TestInput;
import test.Value;

public class MaskingEffect {

    private CEG ceg;
    private Effect effect;
    private Set<TestInput> selectedTestInputs;
    private Set<TestInput> selectedTestInputsForEffect;
    private Set<TestInput> mcdc;

    public MaskingEffect(CEG ceg, Effect effect) {
        this.ceg = ceg;
        this.effect = effect;
        selectedTestInputs = ceg.getDc().getValidAllPossibleTests(effect);

        selectedTestInputsForEffect = new HashSet<>();

        mcdc = new HashSet<>();
        fillTestInputs();
        fillEffectValues();
        choseAllTests();
    }

    public Set<TestInput> getMcdc() {
        return mcdc;
    }

    /**
     * Generates all possible test inputs by using selected test inputs in CEG
     */
    private void fillTestInputs() {

        Set<TestInput> tis = new HashSet<>();

        for (TestInput ti : selectedTestInputs) {
            tis.add(new TestInput(ti));
        }

        for (TestInput ti : tis) {
            for (Intermediate i : ceg.getInterNodes()) {

                Relation r = ceg.getInterMap().get(i.getLabel()).get(0);
                String exp = r.getExpression();

                for (String s : ti.getTestInput().keySet()) {
                    String check = s + " ";
                    if (exp.contains(check)) {
                        exp = exp.replace(check, String.valueOf(ti.getTestInput().get(s)).toLowerCase() + " ");
                    }
                }

                if (!exp.contains("C")) {
                    exp = exp.replaceAll("AND", "&&");

                    exp = exp.replaceAll("OR", "||");
                    exp = exp.replaceAll("~", "!");

                    try {
                        BooleanExpression be = BooleanExpression.readLeftToRight(exp);
                        Value val;

                        if (be.booleanValue() == true) {
                            val = Value.True;
                        } else if (be.booleanValue() == false) {
                            val = Value.False;
                        } else {
                            val = Value.NA;
                        }

                        ti.addInterToMap(i.getLabel(), val);

                    } catch (MalformedBooleanException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
                //}

            }

            selectedTestInputsForEffect.add(ti);
        }
    }

    /**
     * Test inputs are selected for each node in the effects' expression
     */
    private void choseAllTests() {

        for (CEG_Node n : effect.getExpressionNodes()) {
            //choseTests(n);
            choseTestCases(n);
        }

    }

    private void choseTestCases(CEG_Node cause) {

        Set<TestInput> set1 = new HashSet<>();
        Set<TestInput> set2 = new HashSet<>();

        ArrayList<Pair> independentPairs = new ArrayList<>();

        for (TestInput ti : selectedTestInputsForEffect) {
            set1.add(new TestInput(ti));
            set2.add(new TestInput(ti));
        }

        boolean flag = true;

        outerloop:
        for (TestInput ti : set2) {
            flag = true;

            for (TestInput ti2 : set1) {

                if (ti.getTestInput().get(cause.getLabel()).equals(ti2.getTestInput().get(cause.getLabel()))) {
                    flag = false;
                    break;
                }

                if (ti.getCorrespondingEffectValue(effect) == ti2.getCorrespondingEffectValue(effect)) {
                    flag = false;
                    break;
                }

                inloop:
                for (CEG_Node n : effect.getRelation().getRelatedNodes()) {
                    if (n instanceof Intermediate) {

                        if (isContainedinInter(cause, n.getLabel())) {

                            for (CEG_Node cn : ceg.getIntermediateNode(n.getLabel()).getExpressionNodes(ceg)) {
                                if (!cn.getLabel().equals(cause.getLabel())) {
                                    if (ti.getTestInput().get(cn.getLabel()) != ti2.getTestInput().get(cn.getLabel())) {
                                        flag = false;
                                        break inloop;
                                    }
                                }
                            }

                        } else if (ti.getInterMap().get(n.getLabel()) != ti2.getInterMap().get(n.getLabel())) {
                            flag = false;
                            break;
                        }
                    } else if (n instanceof Cause) {

                        if (!n.getLabel().equals(cause.getLabel())) {

                            if (ti.getTestInput().get(n.getLabel()) != ti2.getTestInput().get(n.getLabel())) {
                                flag = false;
                                break;
                            }
                        }
                    }
                }

                if (flag) {
                    //mcdc.add(ti); //eklenecek
                    //mcdc.add(ti2); // eklenecek
                    independentPairs.add(new Pair(effect, (Cause) cause, new TestInput(ti), new TestInput(ti2)));
                    //break outerloop;// eklenecek
                }
                //}
                //}
            }

            //if (flag) {//eklenecek
            //    break;//eklenecek
            //}//eklenecek
            //}
        }

        Random r = new Random();
        if (independentPairs.size() > 0) {
            int i = r.nextInt(independentPairs.size());
            mcdc.add(independentPairs.get(i).getTi());
            mcdc.add(independentPairs.get(i).getTi2());
        }

    }

    private boolean isContainedinInter(CEG_Node cause, String inter) {

        if (ceg.getIntermediateNode(inter).getExpressionNodes(ceg).contains(cause)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Fills the effect truth value in the generated test inputs
     */
    private void fillEffectValues() {

        String exp = effect.getRelation().getExpression();

        for (TestInput ti : selectedTestInputsForEffect) {

            String modifiedExp = exp;

            for (String s : ti.getTestInput().keySet()) {
                if (s.startsWith("C")) {
                    String key = s + " ";
                    modifiedExp = modifiedExp.replace(key, ti.getTestInput().get(s).toString().toLowerCase() + " ");
                }
            }

            if (!modifiedExp.contains("C")) {
                try {
                    BooleanExpression be = BooleanExpression.readLeftToRight(modifiedExp);
                    ti.setEffectValue(String.valueOf(be.booleanValue()));
                    ti.setEffect(effect.getLabel());

                } catch (MalformedBooleanException e) {

                    e.printStackTrace();
                }
            }
        }

    }
}
