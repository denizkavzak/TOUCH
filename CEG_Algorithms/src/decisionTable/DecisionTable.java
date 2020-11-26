package decisionTable;

import java.util.ArrayList;

import decisionTable.Action;
import decisionTable.Condition;
import decisionTable.Rule;
import graph.CEG_Node;
import graph.Effect;

//Decision table with its conditions,actions and rules.
public class DecisionTable {

	private ArrayList<Condition> conditions;
    private ArrayList<Action> actions;
    private ArrayList<Rule> rules;
	
    public DecisionTable(){
    	conditions = new ArrayList<>();
    	actions = new ArrayList<>();
    	rules = new ArrayList<>();
    }
    
    public void setRules(ArrayList<Rule> rules){
    	this.rules = rules;
    }
    
    //Creates and adds a condition to the table from a given node (Cause or Intermediate node).
    public Condition addCondition(int id, CEG_Node node) {
        Condition condition = new Condition(id, node);
        conditions.add(condition);
        return condition;
    }
    
    //Creates and adds an action to the table from a given effect node.
    public Action addAction(Effect effect){
    	Action action = new Action(effect);
    	actions.add(action);
    	return action;
    }
    
    //Creates and adds a rule to the table from a given effect node.
    public Rule addRule(int id, String str) {
    	Rule rule = new Rule(id, str);
        rules.add(rule);
        return rule;
    }
    
    //Fill a given rule's condition part.
    public void fillRule(Rule rule, Condition condition, boolean value) {
    	rule.fillCondition(condition, value);
    }

    //Fill a rule's action part.
    public void fillRule(Rule rule, Action action, boolean value) {
    	//checkRuleExistence
    	rule.fillAction(action, value);
    }

	public ArrayList<Condition> getConditions() {
		return conditions;
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public ArrayList<Rule> getRules() {
		return rules;
	}
    
}
