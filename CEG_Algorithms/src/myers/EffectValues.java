package myers;

import java.util.ArrayList;
import java.util.HashMap;

import graph.CEG;
import graph.CEG_Node;
import graph.Effect;
import test.TestInput;
import test.TestSet;
import test.Value;

public class EffectValues {
	
	private CEG ceg;
	private static int id = 0;
	private HashMap<String, TestSet> map;
        //private HashMap<String, TestSet> falseMap;
	
	public EffectValues(CEG ceg){
		this.ceg = ceg;
		//InterValues iv = new InterValues(ceg);
		map = new HashMap<>();
                //falseMap = new HashMap<>();
		createAllEffectValues();
                
  
	}

	public HashMap<String, TestSet> getMap() {
		return map;
	}

	private void createAllEffectValues(){
		for(Effect e : ceg.getEffectNodes()){
			createEffectValue(e);
                        //createFalseEffectValue(e);
		}
	}
	
	private void createEffectValue(Effect effect){
		
		ArrayList<CEG_Node> nodes = effect.getRelation().getRelatedNodes();
		String type = effect.getRelationType();
		//System.out.println("effect: " + effect.getLabel() + "tip: " + type);
		TestSet testSet = new TestSet();
		if(type == null){ //Bu durum sadece Effect direkt bir Intermediate'a ya da cause'a bagl�yken olur
			TestInput ti = new TestInput(++id,ceg);
			ti.addTestInput(nodes.get(0).getLabel(), Value.True);
                        ti.setResult(effect.getLabel(),Value.True);
                        //System.out.println(effect.getLabel());                       
                        testSet.addTestInput(ti);
		}else if(type.equals("NOT")){ //Bu durum sadece Effect direkt bir Intermediate'a ya da cause'a negated bir edge ile bagl�yken olur
			TestInput ti = new TestInput(++id,ceg);
			ti.addTestInput(nodes.get(0).getLabel(), Value.False);
                        ti.setResult(effect.getLabel(),Value.True);
			testSet.addTestInput(ti);
		}else if(type.equals("AND")){
			TestInput ti = new TestInput(++id,ceg);
			for(CEG_Node n : nodes){
				if(!n.isNegated()){
					ti.addTestInput(n.getLabel(), Value.True);
				}else{
					ti.addTestInput(n.getLabel(), Value.False);
				}	
			}
                        ti.setResult(effect.getLabel(),Value.True);
                        //System.out.println(ti.getEffectValue());
			testSet.addTestInput(ti);
			
		}else if(type.equals("OR")){
			
			for(CEG_Node c : nodes){
					TestInput ti = new TestInput(++id,ceg);
					if(!c.isNegated()){
						ti.addTestInput(c.getLabel(), Value.True);
					}else{
						ti.addTestInput(c.getLabel(), Value.False);
					}
					for(CEG_Node cn : nodes){
						if(!c.equals(cn)){
							if(!cn.isNegated()){
								ti.addTestInput(cn.getLabel(), Value.False);
							}else{
								ti.addTestInput(cn.getLabel(), Value.True);
							}
						}	
					}
                                        ti.setResult(effect.getLabel(),Value.True);
					testSet.addTestInput(ti);	
			}
		}		
		
		map.put(effect.getLabel(), testSet);
	}
	
	
   /*     private void createFalseEffectValue(Effect effect){
		
		ArrayList<CEG_Node> nodes = effect.getRelation().getRelatedNodes();
		String type = effect.getRelationType();
		//System.out.println("effect: " + effect.getLabel() + "tip: " + type);
		TestSet testSet = new TestSet();
		if(type == null){ //Bu durum sadece Effect direkt bir Intermediate'a bagl�yken olur
			TestInput ti = new TestInput(++id,ceg);
			ti.addTestInput(nodes.get(0).getLabel(), Value.False);
                        ti.setResult(effect.getLabel(),Value.False);
                        //System.out.println(effect.getLabel());                       
                        testSet.addTestInput(ti);
		}else if(type.equals("NOT")){ //Bu durum sadece Effect direkt bir Intermediate'a negated bir edge ile bagl�yken olur
			TestInput ti = new TestInput(++id,ceg);
			ti.addTestInput(nodes.get(0).getLabel(), Value.True);
                        ti.setResult(effect.getLabel(),Value.False);
			testSet.addTestInput(ti);
		}else if(type.equals("AND")){
			TestInput ti = new TestInput(++id,ceg);
			for(CEG_Node n : nodes){ //All 0
				if(!n.isNegated()){
                                    ti.addTestInput(n.getLabel(), Value.False);
				}else{
                                    ti.addTestInput(n.getLabel(), Value.True);
				}	
			}
                        ti.setResult(effect.getLabel(),Value.False);
                        //System.out.println(ti.getEffectValue());
			testSet.addTestInput(ti); //All 0 added
                        
                        for(CEG_Node c : nodes){
					TestInput ti2 = new TestInput(++id,ceg);
					if(!c.isNegated()){
						ti2.addTestInput(c.getLabel(), Value.False);
					}else{
						ti2.addTestInput(c.getLabel(), Value.True);
					}
					for(CEG_Node cn : nodes){
						if(!c.equals(cn)){
							if(!cn.isNegated()){
								ti2.addTestInput(cn.getLabel(), Value.True);
							}else{
								ti2.addTestInput(cn.getLabel(), Value.False);
							}
						}	
					}
                                        ti2.setResult(effect.getLabel(),Value.False);
					testSet.addTestInput(ti2);	
			}
                        
			
		}else if(type.equals("OR")){ //All 0
                    TestInput ti = new TestInput(++id,ceg);
                    for(CEG_Node n : nodes){ //All 0
			if(!n.isNegated()){
                            ti.addTestInput(n.getLabel(), Value.False);
			}else{
                            ti.addTestInput(n.getLabel(), Value.True);
			}	
                    }
                        
                    ti.setResult(effect.getLabel(),Value.False);
                    testSet.addTestInput(ti);	
			
		}		
		
		falseMap.put(effect.getLabel(), testSet);
	}
        */

}
