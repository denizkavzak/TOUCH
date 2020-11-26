package decisionTable;

import java.util.ArrayList;

import graph.*;

//Condition in the decision table, formed by the Cause and Intermediate nodes.
public class Condition {

	int id;
	String label;
	CEG_Node node;
	ArrayList<CEG_Node> causes;
	
	public Condition(int id, CEG_Node node){
		
		this.id = id;
		this.label = node.getLabel();
		this.node = node;
		causes = new ArrayList<>();
		
		if(node instanceof Cause){
			causes.add((Cause)node);
		}
		else if(node instanceof Intermediate){
			
			causes.add(node);
			
			for(CEG_Node relNode : ((Intermediate) node).getRelation().getRelatedNodes()){
				if(relNode instanceof Cause){
					causes.add((Cause) relNode);
				}else if(relNode instanceof Intermediate){
					causes.add((Intermediate)relNode);
				}	
			}
		}
	}
	
	public int getID(){
		return id;
	}
	
	public ArrayList<CEG_Node> getCauses(){
		return causes;
	}
	
	public CEG_Node getNode(){
		return node;
	}
	
}
