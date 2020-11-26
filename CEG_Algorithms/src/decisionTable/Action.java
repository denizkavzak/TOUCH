package decisionTable;

import java.util.ArrayList;

import graph.CEG_Node;
import graph.Effect;

//Action in decision table, formed by the effect nodes.
public class Action {

	Effect effect;
	
	public Action(Effect effect){
		this.effect = effect;
	}
	
	public String getLabel(){
		return effect.getLabel();
	}
	
	public ArrayList<CEG_Node> getNodes(){
		return effect.getRelation().getRelatedNodes();
	}
	
	public String getRelationType(){
		return effect.getRelationType();
	}
	
}
