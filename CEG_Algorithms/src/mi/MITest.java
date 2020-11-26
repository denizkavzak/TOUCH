package mi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import graph.CEG;
import graph.CEG_Node;
import test.TestInput;
import test.TestSet;
import test.Value;

public class MITest {

	private CEG ceg;
	private int testCaseID = 0;
	private MI mi;
	private TestSet testSet;
	
	public MITest(CEG ceg){
		this.ceg = ceg;
		mi = new MI(ceg);
		executeMI();
	}
	
	public void executeMI(){
		
		ArrayList<TestInput> tis = new ArrayList<>();

		for(SE se : mi.getSEs()){
			tis.addAll(createAllTestInputs(se.getFe().getFSE()));
			tis.addAll(createAllTestInputs(se.getTe().getTSE()));
		}
		
		testSet = new TestSet(tis);
	}
	
	public TestSet getTestSet(){
		return testSet;
	}
	
	private TestInput createTestInput(List<CEG_Node> set){
		
		TestInput ti = new TestInput(testCaseID+1,ceg);
		
		for(CEG_Node n : set){
			ti.addTestInput(n.getLabel(), Value.True);
		}
		
		return ti;
	}
	
	public ArrayList<TestInput> createAllTestInputs(Set<List<CEG_Node>> set){
		
		ArrayList<TestInput> tis = new ArrayList<>();
		
                System.out.println("size of set " + set.size());
		for(List<CEG_Node> testCase : set){
                        System.out.println("test " + testCase);
			tis.add(createTestInput(testCase));
		}
		
		return tis;
		
	}
	
}
