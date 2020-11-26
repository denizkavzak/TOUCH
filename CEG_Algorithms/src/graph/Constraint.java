package graph;

import org.gephi.graph.api.Node;

/**
 * Represents the constraint node in CEG
 * @author deniz.kavzak
 *
 */
public class Constraint extends CEG_Node{

	String constraintType; //i.e. XOR, I, M, R.
	String resultNodeLabel; //The Intermediate or effect node label which this constraint belongs to.
	
	public Constraint(Node node) {
		super(node);
		constraintType = (String) node.getAttributes().getValue("label");
		resultNodeLabel = (String) node.getAttributes().getValue("consInt");
	}
	
	public void setConstraintType(String type){
		constraintType = type;
	}	
	
	public void setResultNodeLabel(String label){
		resultNodeLabel = label;
	}	
	
	public String getConstraintType(){
		return constraintType;
	}
	
	public String getResultNodeLabel(){
		return resultNodeLabel;
	}
	
}
