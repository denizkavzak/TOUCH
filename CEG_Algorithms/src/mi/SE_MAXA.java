/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mi;

import graph.CEG;
import graph.Effect;
import java.util.ArrayList;
import test.TestInput;

/**
 *
 * @author Deniz
 */
public class SE_MAXA {

    private CEG ceg;
    private Effect effect;
    private TE te;
    private FE fe;
    private ArrayList<TestInput> trueSet;
    private ArrayList<TestInput> falseSet;

    public SE_MAXA(CEG ceg, Effect effect) {
        this.ceg = ceg;
        this.effect = effect;
        trueSet = new ArrayList<>();
        falseSet = new ArrayList<>();

        setTEFE();
    }

    public ArrayList<TestInput> getTrueSet() {
        return trueSet;
    }

    public void addTrueSet(TestInput testInput) {
        trueSet.add(testInput);
    }

    public ArrayList<TestInput> getFalseSet() {
        return falseSet;
    }

    public void addFalseSet(TestInput testInput) {
        falseSet.add(testInput);
    }

    private void setTEFE() {

        TE te = new TE(ceg, effect);
        FE fe = new FE(ceg, effect);

        this.te = te;
        te.fillInputsTSEwithAll();
        this.fe = fe;

        fe.eliminatePosFSE(te);	//eklenecek
    }

    public CEG getCeg() {
        return ceg;
    }

    public Effect getEffect() {
        return effect;
    }

    public TE getTe() {
        return te;
    }

    public FE getFe() {
        return fe;
    }

}
