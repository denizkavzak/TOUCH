/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcdc;

import graph.Cause;
import graph.Effect;
import test.TestInput;

/**
 *
 * @author Deniz
 */
public class Pair {

    Effect effect;
    Cause cause;
    TestInput ti;
    TestInput ti2;

    public Pair(Effect effect, Cause cause, TestInput ti, TestInput ti2) {
        this.effect = effect;
        this.cause = cause;
        this.ti = ti;
        this.ti2 = ti2;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public Cause getCause() {
        return cause;
    }

    public void setCause(Cause cause) {
        this.cause = cause;
    }

    public TestInput getTi() {
        return ti;
    }

    public void setTi(TestInput ti) {
        this.ti = ti;
    }

    public TestInput getTi2() {
        return ti2;
    }

    public void setTi2(TestInput ti2) {
        this.ti2 = ti2;
    }

    @Override
    public boolean equals(Object obj) {

        if (ti.equals(((Pair) obj).getTi()) || ti.equals(((Pair) obj).getTi2())) {
            if (ti2.equals(((Pair) obj).getTi()) || ti2.equals(((Pair) obj).getTi2())) {
                return true;
            }
        }

        return false;
    }

}
