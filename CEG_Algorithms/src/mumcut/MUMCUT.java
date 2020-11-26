package mumcut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import graph.CEG;
import graph.Effect;
import test.TestInput;
import test.TestSet;

public class MUMCUT {

    private CEG ceg;
    private ArrayList<UTP> utps;
    private ArrayList<NFP> nfps;
    private Set<TestInput> cutpnfpAll;
    private Set<TestInput> allTrueTest;
    private Set<TestInput> allFalseTest;

    private Set<TestInput> allTests;
    private TestSet testSet;

    private MUTP mutp;
    private MNFP mnfp;
    private CUTPNFP cutpnfp;

    private int count = 0;

    public MUMCUT(CEG ceg) {
        this.ceg = ceg;
        utps = new ArrayList<>();
        nfps = new ArrayList<>();
        cutpnfpAll = new HashSet<>();
        allTrueTest = new HashSet<>();
        allFalseTest = new HashSet<>();
        allTests = new HashSet<>();

        mutp = new MUTP(ceg);
        mnfp = new MNFP(ceg);
        cutpnfp = new CUTPNFP(ceg);

        combineAllTests();

    }

    private void combineAllTests() {
        allTests.addAll(mutp.getTestSet().getTestInputs());
        allTests.addAll(mnfp.getTestSet().getTestInputs());
        allTests.addAll(cutpnfp.getAllTests().getTestInputs());
        testSet = new TestSet(allTests);
    }

    public TestSet getTestSet() {
        return testSet;
    }

    public ArrayList<UTP> getUtps() {
        return utps;
    }

    public Set<TestInput> getAllTests() {
        return allTests;
    }

    public ArrayList<NFP> getNfps() {
        return nfps;
    }

    public Set<TestInput> getCutpnfpAll() {
        return cutpnfpAll;
    }

    public Set<TestInput> getAllTrueTest() {
        return allTrueTest;
    }

    public Set<TestInput> getAllFalseTest() {
        return allFalseTest;
    }

}
