/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mi;

import graph.CEG;
import graph.Effect;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import test.TestInput;
import test.TestSet;

/**
 *
 * @author Deniz
 */
public class MAXA_new {

    private CEG ceg;
    private ArrayList<SE_MAXA> SEs;
    private Set<TestInput> trTests;
    private Set<TestInput> flTests;
    private Set<TestInput> allTests;
    private TestSet testSet;

    public MAXA_new(CEG ceg) {
        this.ceg = ceg;
        SEs = new ArrayList<>();
        trTests = new HashSet<>();
        flTests = new HashSet<>();
        allTests = new HashSet<>();
        testSet = new TestSet();
        fillSEs();
        addAllTests();
    }

    public Set<TestInput> getTrTests() {
        return trTests;
    }

    public Set<TestInput> getFlTests() {
        return flTests;
    }

    public Set<TestInput> getAllTests() {
        return allTests;
    }

    private void fillSEs() {
        for (Effect effect : ceg.getEffectNodes()) {
            System.out.println("Original effect: " + effect.getLabel() + " = " + effect.getRelation().getExpression());
            System.out.println("Effect DNF Exp: " + effect.getMinDNF());
            SE_MAXA se = new SE_MAXA(ceg, effect);
            SEs.add(se);
            fillTests(se);
        }
    }

    private void fillTests(SE_MAXA se) {

        TE te = se.getTe();
        FE fe = se.getFe();

        for (TestInput ti : te.getInputsTSE()) {
            se.addTrueSet(ti);

        }

        for (TestInput ti : fe.getInputsFSE()) {
            se.addFalseSet(ti);

        }
    }

    private void addAllTests() {
        for (SE_MAXA se : SEs) {
            for (TestInput ti : se.getTrueSet()) {
                trTests.add(ti);
                allTests.add(ti);
            }
        }
        for (SE_MAXA se : SEs) {
            for (TestInput ti : se.getFalseSet()) {
                flTests.add(ti);
                allTests.add(ti);
            }
        }
    }

    /*
	public SE getSEofEffect(Effect effect){
		return (new SE(ceg, effect,selectedSet));
	}
     */
    public ArrayList<SE_MAXA> getSEs() {
        return SEs;
    }

    public void printTSE() {
        for (SE_MAXA se : SEs) {
            for (TestInput ti : se.getTe().getInputsTSE()) {
                System.out.println(ti);
            }
        }

    }

    public TestSet getTestSet() {

        for (TestInput ti : allTests) {
            testSet.addTestInput(ti);
        }
        return testSet;
    }

}
