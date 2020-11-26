package Fourier;

import graph.CEG;
import graph.CEG_Node;
import graph.Constraint;
import graph.Effect;
import graph.Relation;
import java.util.ArrayList;
import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import mutation.ClauseConjunction;
import mutation.ClauseDisjunction;
import mutation.ExpressionNegation;
import mutation.MissingVariable;
import mutation.Mutant;
import mutation.OperatorReference;
import test.TestInput;
import test.TestSet;
import test.Value;

/**
 * Implementation of Spectral Testing method
 * @author deniz.kavzak
 */
public class Fourier {

    public static int toplamSay = 0;

    private double fourierCoef[] = new double[65535];

    private CEG ceg;
    private ArrayList<Relation> constraintRelations;
    TestSet ts;
           
    public Fourier(CEG ceg) {
        this.ceg = ceg;
        constraintRelations = new ArrayList<>();
        ts = new TestSet();
        setConstraintRelations();
    }

    public TestSet findSpectralTestWithLinearApproximationFunction(Effect effect, Mutant mutant){
        
        TestSet ts = new TestSet();

        int id = 0;

        for (String mut : mutant.getMutants()) {
            TestInput ti = new TestInput(id++);
            int i, j, m;
            double a;

            double nx;
            short ii;

            int input = effect.getExpressionNodes().size();

            double th; 
            
            th = (1.0) / Math.pow(2.0, input);         

            for (ii = 0; ii < input; ii++) {

                i = (int) Math.pow(2, ii);
                a = 0;

                for (j = 0; j < Math.pow(2, input); j++) {

                    nx = 1.0;

                    for (m = 0; m < input; m++) {
                        int s = (i >> m) & (0x1);
                        int t = (j >> m) & (0x1);
                        if (s == 1 && t == 1) {
                            nx *= (-1.0);
                        }
                    }

                    TestInput tiM = new TestInput(0);
                    tiM.setEffect(effect.getLabel());
                    tiM.fillWithBits(j, ceg, effect);

                    int x = mutant.m(mut, tiM);
                    String v = tiM.valueToString(tiM.getCorrespondingEffectValue(effect));

                    int y = 0;
                    if (v.equals("true")) {
                        y = 1;
                    }
                    int res = x ^ y; 
                    
                    if (res == 1) {
                        res = -1;
                    } else if (res == 0) {
                        res = 1;
                    }
                    a += nx * res;
                    
                }
                
                fourierCoef[i] = a / Math.pow(2, input);
            }

            Function function = new Function(); 
             
            for (ii = 0; ii < input; ii++) {
                i = (int) Math.pow(2, ii);
                               
                Variable variable = new Variable(fourierCoef[i], effect.getExpressionNodes().get(ii));
                function.addVariable(variable);
                
            }
        
            ti.fillWithBits(0, ceg, effect);
            double max = function.calculateValue(ti);
            short best = 0;
            
            for(ii = 1; ii< Math.pow(2, input); ii++){
                
                ti = new TestInput(id);
                ti.fillWithBits(ii, ceg, effect);
                
                double val = function.calculateValue(ti);
                
                if(val>=max){
                    max=val;
                    best=ii;
                }
            }
            
            ti = new TestInput(id);
            ti.fillWithBits(best, ceg, effect);

            toplamSay++;
            ts.addTestInput(ti);
        }

        return ts;
        
    }

    public TestSet findAllSpectralTestsForEffectbyFunction(Effect effect, Mut mut) {

        ArrayList<TestInput> tests = new ArrayList<>();

        TestSet set = new TestSet();

        ArrayList<Mutant> mutants = mut.getMutantsOfEffect().get(effect.getLabel());

        for (Mutant m : mutants) {
            tests.addAll(findSpectralTestWithLinearApproximationFunction(effect, m).getTestInputs());

        }

        set.addTestInputList(tests);

        return set;
    }
    
        public TestSet findAllSpectralTestsForEffectbyFunction_4FaultClassesCover(Effect effect, Mut mut) {

        ArrayList<TestInput> tests = new ArrayList<>();

        TestSet set = new TestSet();

        ArrayList<Mutant> mutants = mut.getMutantsOfEffect().get(effect.getLabel());

        for (Mutant m : mutants) {
            if(m instanceof OperatorReference){
                tests.addAll(findSpectralTestWithLinearApproximationFunction(effect, m).getTestInputs());
            }else if(m instanceof ClauseConjunction){
                tests.addAll(findSpectralTestWithLinearApproximationFunction(effect, m).getTestInputs());
            }else if(m instanceof ClauseDisjunction){
                tests.addAll(findSpectralTestWithLinearApproximationFunction(effect, m).getTestInputs());
            }else if(m instanceof MissingVariable){
                tests.addAll(findSpectralTestWithLinearApproximationFunction(effect, m).getTestInputs());
            }
        }

        set.addTestInputList(tests);

        return set;
    }
    

    public TestSet getTestsForAllEffectsbyFunction(Mut mut) {
        TestSet ts = new TestSet();

        for (Effect e : ceg.getEffectNodes()) {
            ArrayList<TestInput> tests = new ArrayList<>();
            tests.addAll(findAllSpectralTestsForEffectbyFunction(e, mut).getTestInputs());
            ts.addTestInputList(tests);
        }
        this.ts = ts;
        checkConstraints();
        return ts;
    }
    
    public TestSet getTestsForAllEffectsbyFunction_4Faults(Mut mut) {
        TestSet ts = new TestSet();

        for (Effect e : ceg.getEffectNodes()) {
            ArrayList<TestInput> tests = new ArrayList<>();
            //tests.addAll(findAllSpectralTestsForEffectbyFunction(e, mut).getTestInputs());
            tests.addAll(findAllSpectralTestsForEffectbyFunction_4FaultClassesCover(e, mut).getTestInputs());
            ts.addTestInputList(tests);
        }
        this.ts = ts;
        checkConstraints();
        return ts;
    }

    public TestSet findSpectralTest(Effect effect, Mutant mutant) {

        TestSet ts = new TestSet();

        int id = 0;

        for (String mut : mutant.getMutants()) {
            TestInput ti = new TestInput(id++);
            int i, j, m;
            double a;
            //char fn;
            double nx;
            short ii;

            int input = effect.getExpressionNodes().size();

            double th; //treshold value

            th = (1.0) / Math.pow(2.0, input);

            for (ii = 0; ii < input; ii++) {

                i = (int) Math.pow(2, ii);
                a = 0;

                for (j = 0; j < Math.pow(2, input); j++) {

                    nx = 1.0;

                    for (m = 0; m < input; m++) {
                        int s = (i >> m) & (0x1);
                        int t = (j >> m) & (0x1);
                        if (s == 1 && t == 1) {
                            nx *= (-1.0);
                        }
                    }

                    TestInput tiM = new TestInput(0);
                    tiM.setEffect(effect.getLabel());
                    tiM.fillWithBits(j, ceg, effect);

                    int x = mutant.m(mut, tiM);
                    String v = tiM.valueToString(tiM.getCorrespondingEffectValue(effect));

                    int y = 0;
                    if (v.equals("true")) {
                        y = 1;
                    }
                    int res = x ^ y;

                    if (res == 1) {
                        res = -1;
                    } else if (res == 0) {
                        res = 1;
                    }
                    a += nx * res;

                }

                fourierCoef[i] = a / Math.pow(2, input);
            }

            for (ii = 0; ii < input; ii++) {
                i = (int) Math.pow(2, ii);
                if (fourierCoef[i] < ((-1.0) * th)) {

                    ti.addTestInput(effect.getExpressionNodes().get(effect.getExpressionNodes().size() - ii - 1).getLabel(), Value.True);

                } else if (fourierCoef[i] > ((1.0) * th)) {
                    ti.addTestInput(effect.getExpressionNodes().get(effect.getExpressionNodes().size() - ii - 1).getLabel(), Value.False);

                } else {
                    ti.addTestInput(effect.getExpressionNodes().get(effect.getExpressionNodes().size() - ii - 1).getLabel(), Value.False);
                }

            }

            ts.addTestInput(ti);
        }

        return ts;
    }

    public TestSet findAllSpectralTestsForEffect(Effect effect) {

        ArrayList<TestInput> tests = new ArrayList<>();

        TestSet set = new TestSet();

        Mut mut = new Mut(ceg);

        ArrayList<Mutant> mutants = mut.getMutantsOfEffect().get(effect.getLabel());

        for (Mutant m : mutants) {
            tests.addAll(findSpectralTest(effect, m).getTestInputs());
        }

        set.addTestInputList(tests);

        return set;
    }

    public TestSet getTestsForAllEffects() {
        TestSet ts = new TestSet();

        for (Effect e : ceg.getEffectNodes()) {
            ArrayList<TestInput> tests = new ArrayList<>();
            tests.addAll(findAllSpectralTestsForEffect(e).getTestInputs());

            ts.addTestInputList(tests);
        }

        return ts;
    }
    
    private void setConstraintRelations() {

        for (Relation r : ceg.getRelations()) {
            if (r.getConstraint() != null) {
                constraintRelations.add(r);

            }
        }
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

}
