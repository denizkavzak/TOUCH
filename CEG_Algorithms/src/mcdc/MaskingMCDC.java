package mcdc;

import graph.CEG;
import graph.Effect;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import test.TestInput;
import test.TestSet;

public class MaskingMCDC {

    private CEG ceg;
    private Set<TestInput> mcdc;
    private ArrayList<MaskingEffect> mes;
    private TestSet allTests;

    public MaskingMCDC(CEG ceg) {
        this.ceg = ceg;
        mcdc = new HashSet<>();
        mes = new ArrayList<>();
        allTests = new TestSet();
        choseMCDC();
    }

    public ArrayList<MaskingEffect> getUes() {
        return mes;
    }

    /**
     * Get all Test Inputs created for all effects
     *
     * @return
     */
    public Set<TestInput> getMcdc() {
        return mcdc;
    }

    private TestInput getSetElement(Set<TestInput> set, TestInput t) {
        for (TestInput ti : set) {
            if (ti.equals(t)) {
                return ti;
            }
        }
        return null;
    }

    /**
     * Chose unique MC/DC for each effect in the CEG
     */
    private void choseMCDC() {

        for (Effect effect : ceg.getEffectNodes()) {
            MaskingEffect me = new MaskingEffect(ceg, effect);
            mes.add(me);
        }

        for (MaskingEffect me : mes) {
            for (TestInput ti : me.getMcdc()) {
                mcdc.add(ti);
            }
        }

        for (TestInput ti : mcdc) {
            allTests.addTestInput(ti);
        }
    }

    public TestSet getAllTests() {
        return allTests;
    }

}
