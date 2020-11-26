package mumcut;

import java.util.Set;

import graph.CEG;
import graph.Effect;
import java.util.ArrayList;
import java.util.HashSet;
import test.TestInput;
import test.TestSet;

public class MNFP {

    private CEG ceg;
    private Set<TestInput> tests;
    private TestSet testSet;
    private ArrayList<MNFPe> mnfpes;

    public MNFP(CEG ceg) {
        this.ceg = ceg;
        tests = new HashSet<>();
        mnfpes = new ArrayList<>();
        choseAllTests();
    }

    private void choseAllTests() {
        for (Effect e : ceg.getEffectNodes()) {
            MNFPe mnfpe = new MNFPe(ceg, e);

            mnfpes.add(mnfpe);

        }

        for (MNFPe mnfpe : mnfpes) {
            for (TestInput ti : mnfpe.getTests()) {
                tests.add(ti);
            }
        }

        testSet = new TestSet(tests);
    }

    public TestSet getTestSet() {
        return testSet;
    }

}
