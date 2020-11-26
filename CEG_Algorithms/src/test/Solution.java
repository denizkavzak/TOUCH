package test;

import graph.*;

public class Solution {

	CEG_Node node;
	boolean truthValue;
	
	public Solution(CEG_Node node, boolean truthValue) {
		super();
		this.node = node;
		this.truthValue = truthValue;
	}

	public CEG_Node getNode() {
		return node;
	}

	public void setNode(CEG_Node node) {
		this.node = node;
	}

	public boolean isTruthValue() {
		return truthValue;
	}

	public void setTruthValue(boolean truthValue) {
		this.truthValue = truthValue;
	}
	
}
