package myers;

import java.util.ArrayList;
import java.util.HashMap;

import graph.*;
import test.TestInput;
import test.TestSet;
import test.Value;

public class InterValues {
	
	private static int id = 0;
	private CEG ceg;

	private HashMap<String, TestSet> map;

	public InterValues(CEG ceg) {
		this.ceg = ceg;
		map = new HashMap<>(); 
		addAll();
	}

	public CEG getCeg() {
		return ceg;
	}
	
	public HashMap<String, TestSet> getMap(){
		return map;
	}

	private void addANDTrue(Intermediate inter){
		
		String triple = "(" + inter.getLabel() + ",AND," + "True" + ")";
		
		Relation r = ceg.getInterMap().get(inter.getLabel()).get(0);
		
		ArrayList<CEG_Node> n = r.getRelatedNodes();
		TestSet testSet = new TestSet();
		TestInput ti = new TestInput(++id,ceg);
		
		for(CEG_Node c : n){
			if(!c.isNegated()){
				ti.addTestInput(c.getLabel(), Value.True);
			}else{
				ti.addTestInput(c.getLabel(), Value.False);
			}
		}
		testSet.addTestInput(ti);
		map.put(triple, testSet);
		
	}
	
	private void addORTrue(Intermediate inter){
		
		String triple = "(" + inter.getLabel() + ",OR," + "True" + ")";
		
		Relation r = ceg.getInterMap().get(inter.getLabel()).get(0);
		
		ArrayList<CEG_Node> n = r.getRelatedNodes();
		TestSet testSet = new TestSet();
		for(CEG_Node c : n){
			TestInput ti = new TestInput(++id,ceg);
			if(!c.isNegated()){
				ti.addTestInput(c.getLabel(), Value.True);
			}else{
				ti.addTestInput(c.getLabel(), Value.False);
			}
			for(CEG_Node cn : n){
				if(!c.equals(cn)){
					if(!cn.isNegated()){
						ti.addTestInput(cn.getLabel(), Value.False);
					}else{
						ti.addTestInput(cn.getLabel(), Value.True);
					}
				}	
			}
			testSet.addTestInput(ti);
		}
		
		map.put(triple, testSet);
	}
	
	private void addANDFalse(Intermediate inter){
		
		String triple = "(" + inter.getLabel() + ",AND," + "False" + ")";
		
		Relation r = ceg.getInterMap().get(inter.getLabel()).get(0);
		
		ArrayList<CEG_Node> n = r.getRelatedNodes();
		TestSet testSet = new TestSet();
		TestInput ti = new TestInput(++id,ceg);
		
		for(CEG_Node c : n){
			if(!c.isNegated()){
				ti.addTestInput(c.getLabel(), Value.False);
			}else{
				ti.addTestInput(c.getLabel(), Value.True);
			}
		}
		testSet.addTestInput(ti);
		
		
		for(CEG_Node c : n){
			ti = new TestInput(++id,ceg);
			
			if(!c.isNegated()){
				ti.addTestInput(c.getLabel(), Value.True);
			}else{
				ti.addTestInput(c.getLabel(), Value.False);
			}
			
			for(CEG_Node cn : n){
				if(!c.equals(cn)){
					if(!cn.isNegated()){
						ti.addTestInput(cn.getLabel(), Value.False);
					}else{
						ti.addTestInput(cn.getLabel(), Value.True);
					}
				}	
			}
			testSet.addTestInput(ti);
		}
	
		map.put(triple, testSet);
		
	}
		
	private void addORFalse(Intermediate inter){
		
		String triple = "(" + inter.getLabel() + ",OR," + "False" + ")";
		
		Relation r = ceg.getInterMap().get(inter.getLabel()).get(0);
		
		ArrayList<CEG_Node> n = r.getRelatedNodes();
		TestSet testSet = new TestSet();
		TestInput ti = new TestInput(++id,ceg);
		
		for(CEG_Node c : n){
			if(!c.isNegated()){
				ti.addTestInput(c.getLabel(), Value.False);
			}else{
				ti.addTestInput(c.getLabel(), Value.True);
			}
		}
		testSet.addTestInput(ti);
		map.put(triple, testSet);
		
	}
	
	private void addAll(){
		
		for(Intermediate inter : ceg.getInterNodes()){
			if(inter.getRelationType().equals("OR")){
				addORTrue(inter);
				addORFalse(inter);
			}else if(inter.getRelationType().equals("AND")){
				addANDTrue(inter);
				addANDFalse(inter);
			}
		}
		
	}
	
}
