package graph;

/**
 * Represents the nodes in expressions with the information whether it's negated or not in the expression
 * @author deniz.kavzak
 *
 */
public class CEG_Node_in_Exp {

	private CEG_Node node;
	private boolean negated;
	
	public CEG_Node_in_Exp(CEG_Node node) {
		this.node = node;
		negated = false;
	}
	
	public CEG_Node getNode() {
		return node;
	}
	
	public void setNode(CEG_Node node) {
		this.node = node;
	}
	
	public boolean isNegated() {
		return negated;
	}
	
	public void setNegated(boolean negated) {
		this.negated = negated;
	}
	
	public String toString(){
		if(isNegated())
			return "!" + node.getLabel();
		else
			return node.getLabel();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof CEG_Node_in_Exp)){
			return false;
		}
		CEG_Node_in_Exp cn = (CEG_Node_in_Exp)obj;
		
		if(cn.getNode().getLabel().equals(node.getLabel())){
			return true;
		}
		return false;
	}
	
	
	
}
