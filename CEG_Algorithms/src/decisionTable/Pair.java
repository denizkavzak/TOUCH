package decisionTable;

//Pair formed by the node with its truth value, which is used in a rule.
public class Pair {

	Object key;
	boolean value;
	
    public Pair(Object key, boolean value){
    	this.key = key;
    	this.value = value;
    }
	
    public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

    public String toString() {
		return key.toString()+" : "+ value +" ,";
	}

    
}
