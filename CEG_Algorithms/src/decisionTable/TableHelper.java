package decisionTable;

import java.util.ArrayList;

import graph.*;

//Helper class to create and manage a decision table.
public class TableHelper {
	
	DecisionTable DTable;
	CEG ceg;
	private ArrayList<Condition> causeConditions;
	private ArrayList<Condition> interConditions;
	
	public TableHelper(CEG ceg){
		this.DTable = new DecisionTable();
		this.ceg = ceg;
		causeConditions = new ArrayList<>();
		interConditions = new ArrayList<>();
	}
	
	//Prepares and fills given table.
	public DecisionTable prepareTable(){
		setConditions();
		setActions();
		fillRules();
		return this.DTable;
	}
	
	//Fills the lists of cause and intermediate conditions.
	private void setConditions(){
		int conditionCounter = 0;
		Condition condition;
		
		for(Cause cause : ceg.getCauseNodes()){
			condition = DTable.addCondition(conditionCounter,cause);
			causeConditions.add(condition);
			conditionCounter++;
		}
		
		for(Intermediate intermediate : ceg.getInterNodes()){
			condition = DTable.addCondition(conditionCounter,intermediate);
			interConditions.add(condition);
			conditionCounter++;
		}
	}
	
	//Fills all actions in the table.
	private void setActions(){
		for(Effect effect : ceg.getEffectNodes()){
			DTable.addAction(effect);
		}
	}
	
	//Fills all the rules in the table. 
	public void setRules(ArrayList<Rule> ruleSet){
		DTable.setRules(ruleSet);
	}

	//Fill the conditions in rules according to all possible truth values the conditions can take.
	private void fillRules(){
		int ruleCounter = 0;
		
		//all true
		for (int i = 0; i < causeConditions.size(); i++) {
			Rule rule = DTable.addRule(ruleCounter, "Rule " + ruleCounter);
			ruleCounter++;
			Condition condition = causeConditions.get(i);
			DTable.fillRule(rule, condition, true);
		}
		for(int i= causeConditions.size(); i<interConditions.size(); i++){
			
		}
		
		
		
		Rule rule = DTable.addRule(ruleCounter, "Rule " + ruleCounter);
		ruleCounter++;
		for (int i = 0; i < causeConditions.size(); i++) {
			DTable.fillRule(rule, causeConditions.get(i), true);
		}

	}
	
	/*private void setValueInter(Rule rule){
		
		ArrayList<Boolean> values;
		ArrayList<Pair> filledConditions = rule.getConditionPart();
		
		for(Condition cond : interConditions){
			Intermediate inter = (Intermediate) (cond.getNode());
			
			for(inter.getRelation().getRelatedNodes()){
				
			}
	
		}
		
		
	}
	*/
	

	
}
