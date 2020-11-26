package mumcut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import graph.CEG;
import graph.Effect;
import test.TestInput;
import test.TestSet;

public class CUTPNFP {

    private CEG ceg;
    private Set<TestInput> cutpnfpAll;
    private UTP_NFP_All all;
    //private Set<TestInput> testInputs;
    private TestSet allTests;

    public CUTPNFP(CEG ceg) {
        this.ceg = ceg;
        all = new UTP_NFP_All(ceg);
        //testInputs = new HashSet<>();
        //allTests = new TestSet();
        cutpnfpAll = new HashSet<>();
        setCUTPNFPs();
        //convertToTestInputs();
    }

    public TestSet getAllTests() {
        return allTests;
    }

    private void setCUTPNFPs() {

        for (Effect e : ceg.getEffectNodes()) {
            Set<TestInput> c = CUTPNFPEffect(e);
            cutpnfpAll.addAll(c);
        }
        allTests = new TestSet(cutpnfpAll);
    }

    private Set<TestInput> CUTPNFPEffect(Effect effect) {

        Set<TestInput> cutpnfpE = new HashSet<>();

        for (int i = 0; i < effect.getMin_DNF_Exp().size(); i++) {
            cutpnfpE.addAll(CUTPNFPindex(effect, i));
        }

        return cutpnfpE;

    }

    private Set<TestInput> CUTPNFPindex(Effect effect, int compID) {

        UTP ut = null;
        NFP nf = null;

        for (UTP u : all.getUtps()) {
            if (u.getEffect().getLabel().equals(effect.getLabel())) {
                ut = u;
            }
        }
        for (NFP n : all.getNfps()) {
            if (n.getEffect().getLabel().equals(effect.getLabel())) {
                nf = n;
            }
        }

        UTPi utpi = ut.getUTPi(compID);
        NFPi nfpi = nf.getNFPi(compID);

        ArrayList<NFPij> nfpijs = nfpi.getNFPijs();

        //Set<List<CEG_Node>> cutpnfpis = new HashSet<>();
        Set<TestInput> cutpnfpis = new HashSet<>();

        for (NFPij nfpij : nfpijs) {
            cutpnfpis.addAll(chooseForOnej(utpi, nfpij));
        }

        return cutpnfpis;
    }

    private Set<TestInput> chooseForOnej(UTPi utpi, NFPij nfpij) {

        //Set<List<CEG_Node>> cutpnfpis = new HashSet<>();
        Set<TestInput> tests = new HashSet<>();

        outerloop:
        for (TestInput ti : utpi.getTrueTI()) {
            for (TestInput ti2 : nfpij.getFalseSet()) {
                if (ti.isSameExceptKey(ti2, nfpij.getNegNode().getNode().getLabel())) {
                    tests.add(ti);
                    tests.add(ti2);
                    break outerloop;
                }
            }
        }

        return tests;

        /*for(List<CEG_Node> l : utpi.getUtpAllSet()){
			//if(!nfpij.getFalseSet().isEmpty()){
				for(List<CEG_Node> l2 : nfpij.getFalseSet()){
					
					//System.out.println("--");
					//System.out.println(l);
					//System.out.println(l2);
					//System.out.println("--");
					if(sameSetExceptIndex(l, l2, nfpij.getNegID())){
						System.out.println("burday�m");
						cutpnfpis.add(l);
						cutpnfpis.add(l2);
						break;
					}
				}
			//}
		}*/
    }

    /*private void convertToTestInputs(){
		int id = 0;
		
		for(List<CEG_Node> list : cutpnfpAll){
			TestInput ti = new TestInput(id++);
			ti.createMap(ceg);
			
			for(CEG_Node c : list){
				ti.changeValue(c.getLabel(), Value.True);
			}
			
			testInputs.add(ti);
			allTests.addTestInput(ti);
		}
		
	}*/
}
