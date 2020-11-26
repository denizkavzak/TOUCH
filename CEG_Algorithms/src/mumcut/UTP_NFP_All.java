/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mumcut;

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
public class UTP_NFP_All {

    private CEG ceg;
    private ArrayList<UTP> utps;
    private ArrayList<NFP> nfps;
    private TestSet allTests;
    private Set<TestInput> testInputs;

    public UTP_NFP_All(CEG ceg) {
        this.ceg = ceg;
        utps = new ArrayList<>();
        nfps = new ArrayList<>();
        allTests = new TestSet();
        testInputs = new HashSet<>();
        setAllTests();
    }

    private void setAllTests() {

        for (Effect effect : ceg.getEffectNodes()) {

            UTP utp = new UTP(ceg, effect);
            NFP nfp = new NFP(ceg, effect);

            utps.add(utp);
            nfps.add(nfp);

            testInputs.addAll(utp.getTests());
            testInputs.addAll(nfp.getNFP());

        }

        for (TestInput ti : testInputs) {
            allTests.addTestInput(ti);
        }
    }

    public TestSet getAllTests() {
        return allTests;
    }

    public ArrayList<UTP> getUtps() {
        return utps;
    }

    public ArrayList<NFP> getNfps() {
        return nfps;
    }

}
