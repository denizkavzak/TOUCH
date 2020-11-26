package mcdc;

import java.util.HashSet;
import java.util.Set;

import graph.CEG;
import graph.Effect;
import java.util.ArrayList;
import test.TestInput;
import test.TestSet;

public class UniqueMCDC {

    private CEG ceg;
    private Set<TestInput> mcdc;
    private ArrayList<UniqueEffect> ues;
    private TestSet allTests;

    public UniqueMCDC(CEG ceg) {
        this.ceg = ceg;
        mcdc = new HashSet<>();
        ues = new ArrayList<>();
        allTests = new TestSet();
        choseMCDC();
    }

    public ArrayList<UniqueEffect> getUes() {
        return ues;
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

            UniqueEffect ue = new UniqueEffect(ceg, effect);
            ues.add(ue);
        }

        for (UniqueEffect ue : ues) {
            for (TestInput ti : ue.getMcdc()) {

                mcdc.add(new TestInput(ti));
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
