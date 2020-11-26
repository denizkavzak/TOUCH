package decisionTable;

import java.util.ArrayList;

//Rule in decision table with its condition and action part pair lists.
public class Rule {

    private int ID;
    private String name;
    private ArrayList<Pair> conditionPart;
    private ArrayList<Pair> actionPart;

	public Rule(int id, String name){
		ID = id;
		this.name=name;
	    conditionPart = new ArrayList<Pair>();
	    actionPart = new ArrayList<Pair>();
	}
	
	public int getID() {
		return ID;
	}

    public String getName() {
		return name;
	}

	public void fillCondition(Condition condition, boolean value){
		conditionPart.add(new Pair(condition, value));
	}

	public void fillAction(Action action, boolean value){
		actionPart.add(new Pair(action, value));
	}
	
	public ArrayList<Pair> getConditionPart()
	{
		return conditionPart;
	}
	@Override
    public String toString(){
    	String a = "";
    	for(Pair pair: conditionPart){
    		Condition condition = (Condition) pair.getKey();
    		boolean value = pair.getValue();
    		//a += condition.getName() + " : " + value + "\n";
    	}
    	for(Pair pair: actionPart){
    		Action action = (Action) pair.getKey();
    		boolean value = pair.getValue();
    		//a += action.getName() + " : " + value + "\n";
    	}
		return a;
    }
	
}
