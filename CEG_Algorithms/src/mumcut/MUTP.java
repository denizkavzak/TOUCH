package mumcut;

import java.util.Set;

import graph.CEG;
import graph.Effect;
import java.util.ArrayList;
import java.util.HashSet;
import test.TestInput;
import test.TestSet;

public class MUTP {

    private CEG ceg;
    private Set<TestInput> tests;
    private TestSet testSet;
    private ArrayList<MUTPe> mutpes;

    public MUTP(CEG ceg) {
        this.ceg = ceg;
        tests = new HashSet<>();
        testSet = new TestSet();
        mutpes = new ArrayList<>();
        choseAllTests();
    }

    private void choseAllTests() {
        for (Effect e : ceg.getEffectNodes()) {
            MUTPe mutpe = new MUTPe(ceg, e);

            mutpes.add(mutpe);
        }

        for (MUTPe mutpe : mutpes) {
            for (TestInput ti : mutpe.getTests()) {
                tests.add(ti);
            }
        }

        testSet = new TestSet(tests);
    }

    public TestSet getTestSet() {
        return testSet;
    }

}
