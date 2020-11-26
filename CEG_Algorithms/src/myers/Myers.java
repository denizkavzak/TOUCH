package myers;

import java.util.ArrayList;
import java.util.Set;

import graph.CEG;
import graph.CEG_Node;
import graph.Effect;
import graph.Intermediate;
import graph.Relation;
import graph.Constraint;
import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import test.TestInput;
import test.TestSet;
import test.Value;

public class Myers {

    private CEG ceg;
    private EffectValues ev;
    private InterValues iv;
    private TestSet ts;
    private ArrayList<Relation> constraintRelations;

    public Myers(CEG ceg) {
        this.ceg = ceg;
        ev = new EffectValues(ceg);
        iv = new InterValues(ceg);
        ts = new TestSet();
        constraintRelations = new ArrayList<>();
        createTestSets();
        setConstraintRelations();
        checkConstraints();

    }

    public EffectValues getEv() {
        return ev;
    }

    public TestSet getTs() {
        return ts;
    }

    private void createTestSets() {
        for (Effect e : ceg.getEffectNodes()) {
            fillTestInputs(e.getLabel());

            // fillFalseTestInputs(e.getLabel());
        }

        for (String s : ev.getMap().keySet()) {

            for (TestInput ti : ev.getMap().get(s).getTestInputs()) {
                ts.getTestInputs().add(ti);
            }
        }

    }

    private void setConstraintRelations() {

        for (Relation r : ceg.getRelations()) {
            if (r.getConstraint() != null) {
                constraintRelations.add(r);

            }
        }
    }

    private boolean isValid(TestInput ti) {

        boolean flag = true;

        if (!constraintRelations.isEmpty()) {
            for (Relation r : constraintRelations) {
                if (r.getConstraint().getLabel().equals("E")) {

                    ArrayList<CEG_Node> c = r.getRelatedNodes();

                    int count = 0;

                    for (int i = 0; i < c.size(); i++) {
                        if (ti.getTestInput().containsKey(c.get(i).getLabel()) && ti.getTestInput().get(c.get(i).getLabel()) == Value.True) {
                            count++;
                        }
                    }

                    if (c.size() % 2 == 0) {
                        if (count != 1) {
                            flag = false;
                        }
                    } else if (c.size() % 2 != 0) {
                        if (count > 1 && count < c.size()) {
                            flag = false;
                        }
                        if (count == 0) {
                            flag = false;
                        }
                    }

                } else if (r.getConstraint().getLabel().equals("R")) {
                    CEG_Node n = r.getRelatedNodes().get(0);
                    if (ti.getTestInput().containsKey(n.getLabel()) && ti.getTestInput().get(n.getLabel()) == Value.True) {
                        boolean tmp = true;
                        for (String s : ti.getTestInput().keySet()) {
                            if (s.equals(r.getConstraint().getResultNodeLabel()) && ti.getTestInput().get(s) == Value.True) {
                                tmp = false;
                            }
                        }

                        if (tmp) {
                            flag = false;
                        }
                    }
                } else if (r.getConstraint().getLabel().equals("O")) {

                    ArrayList<CEG_Node> c = r.getRelatedNodes();

                    int count = 0;

                    for (int i = 0; i < c.size(); i++) {
                        if (ti.getTestInput().containsKey(c.get(i).getLabel()) && ti.getTestInput().get(c.get(i).getLabel()) == Value.True) {
                            count++;
                        }
                    }

                    if (count != 1) {
                        flag = false;
                    }

                } else if (r.getConstraint().getLabel().equals("I")) {

                    ArrayList<CEG_Node> c = r.getRelatedNodes();

                    int count = 0;

                    for (int i = 0; i < c.size(); i++) {
                        if (ti.getTestInput().containsKey(c.get(i).getLabel()) && ti.getTestInput().get(c.get(i).getLabel()) == Value.True) {
                            count++;
                        }
                    }

                    if (count == 0) {
                        flag = false;
                    }
                }
            }
        }
        for (Constraint con : ceg.getConsNodes()) {

            if (con.getConstraintType().equals("M")) {

                String[] c = con.getResultNodeLabel().split("\\-");

                Effect e1 = ceg.getEffectNode(c[0]);
                Effect e2 = ceg.getEffectNode(c[1]);

                String exp1 = e1.getRelation().getExpression();
                String exp2 = e2.getRelation().getExpression();

                for (String s : ti.getTestInput().keySet()) {
                    String check = s + " ";
                    exp1 = exp1.replace(check, ti.valueToString(ti.getTestInput().get(s)) + " ");
                    exp2 = exp2.replace(check, ti.valueToString(ti.getTestInput().get(s)) + " ");
                }

                //System.out.println("once once : " + exp1);
                //System.out.println("once once : " + exp2);
                //if any of Cause nodes is not in one of the expressions, it is valued NA, so lets take false
                /* while (exp1.contains("C")) {
                    int st = exp1.indexOf("C");
                    int e = exp1.indexOf(" ", st);
                    exp1 = exp1.replaceFirst(exp1.substring(st, e), "false");
                }

                while (exp2.contains("C")) {
                    int st = exp2.indexOf("C");
                    int e = exp2.indexOf(" ", st);
                    exp2 = exp2.replaceFirst(exp2.substring(st, e), "false");
                }

                /*
                while (exp1.contains("C")) {
                    int st = exp1.indexOf("C");
                    int e = exp1.indexOf(" ", st);
                    exp1 = exp1.replaceFirst(exp1.substring(st, e), "false");
                }

                while (exp2.contains("C")) {
                    int st = exp2.indexOf("C");
                    int e = exp2.indexOf(" ", st);
                    exp2 = exp2.replaceFirst(exp2.substring(st, e), "false");
                }
                 */
                //if (!exp1.contains("C") && !exp2.contains("C")) {
                if (!exp1.contains("C") && !exp2.contains("C")) {

                    try {
                        BooleanExpression be1 = BooleanExpression.readLeftToRight(exp1);
                        BooleanExpression be2 = BooleanExpression.readLeftToRight(exp2);

                        if (be1.booleanValue()) {
                            if (be2.booleanValue()) {
                                flag = false;
                            }
                        }
                    } catch (MalformedBooleanException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //}
                }
            }
        }

        return flag;

    }

    private void checkConstraints() {

        TestSet ts2 = new TestSet();

        for (TestInput ti : ts.getTestInputs()) {

            if (isValid(ti)) {

                ts2.addTestInput(ti);
            }
        }

        ts.getTestInputs().clear();
        ts.getTestInputs().addAll(ts2.getTestInputs());
    }

    private Intermediate getInter(String label) {
        for (Intermediate inter : ceg.getInterNodes()) {
            if (inter.getLabel().equals(label)) {
                return inter;
            }
        }
        return null;
    }

    private boolean isAllCause(Set<String> keySet) {
        for (String s : keySet) {
            if (s.startsWith("I")) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllTestSetCauses(TestSet set) {
        for (TestInput t : set.getTestInputs()) {
            if (!isAllCause(t.getTestInput().keySet())) {
                return false;
            }
        }
        return true;
    }

    public void fillTestInputs(String effect) {

        TestSet t = ev.getMap().get(effect);
        TestSet t2 = new TestSet();
        int count = 0;
        for (TestInput ti : t.getTestInputs()) {
            //System.out.println(ti.getEffectValue());
            for (String s : ti.getTestInput().keySet()) {
                if (s.startsWith("I")) {
                    String key = "(" + s + "," + getInter(s).getRelationType() + "," + ti.getTestInput().get(s) + ")";
                    //System.out.println(key);
                    for (TestInput ti2 : iv.getMap().get(key).getTestInputs()) {
                        TestInput tNew = new TestInput(count++, ceg);

                        tNew.getTestInput().putAll(ti.getTestInput());
                        tNew.getTestInput().putAll(ti2.getTestInput());
                        tNew.getTestInput().remove(s);
                        //System.out.println(ti.getEffectValue());
                        //System.out.println(ti.getEffect());
                        tNew.setResult(ti.getEffect(), Value.True);
                        tNew.addEffectToMap(ti.getEffect(), Value.True);
                        //System.out.println(tNew);
                        t2.addTestInput(tNew);
                    }
                    break;
                }
            }
        }

        if (!t2.getTestInputs().isEmpty()) {
            ev.getMap().put(effect, t2);
        }

        if (!isAllTestSetCauses(ev.getMap().get(effect))) {
            fillTestInputs(effect);
        }

        //if(countInterNodes())
        //if(isAllCause(ev.getMap().get(effect).getTestInputs().get(0).getTestInput().keySet()))
    }

    /*  public void fillFalseTestInputs(String effect) {
        TestSet t = ev.getFalseMap().get(effect);
        TestSet t2 = new TestSet();
        int count = 0;
        for (TestInput ti : t.getTestInputs()) {
            //System.out.println(ti.getEffectValue());
            for (String s : ti.getTestInput().keySet()) {
                if (s.startsWith("I")) {
                    String key = "(" + s + "," + getInter(s).getRelationType() + "," + ti.getTestInput().get(s) + ")";
                    for (TestInput ti2 : iv.getMap().get(key).getTestInputs()) {
                        TestInput tNew = new TestInput(count++, ceg);
                        tNew.getTestInput().putAll(ti.getTestInput());
                        tNew.getTestInput().putAll(ti2.getTestInput());
                        tNew.getTestInput().remove(s);
                        //System.out.println(ti.getEffectValue());
                        tNew.setResult(ti.getEffect(), Value.False);
                        t2.addTestInput(tNew);
                    }
                    break;
                }
            }
        }

        ev.getFalseMap().put(effect, t2);

        if (!isAllTestSetCauses(ev.getFalseMap().get(effect))) {
            fillFalseTestInputs(effect);
        }

        //if(countInterNodes())
        //if(isAllCause(ev.getMap().get(effect).getTestInputs().get(0).getTestInput().keySet()))
    }
     */
}
