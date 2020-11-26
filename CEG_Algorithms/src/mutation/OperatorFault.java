package mutation;

import java.util.ArrayList;

import graph.Effect;

public abstract class OperatorFault extends Mutant{

	private ArrayList<Integer> andCount = new ArrayList<>();
	private ArrayList<Integer> orCount = new ArrayList<>();
	private ArrayList<Integer> notCount = new ArrayList<>();
	
	private String regAnd = "&&";
	private String regOr = "||";
	private String regNot = "!";;
	
	public OperatorFault(int id, Effect effect) {
		super(id, effect);
		createOperatorInfo();
                createMutant();
	}
	
        
        
	public abstract void createMutant();
	
	private void createOperatorInfo(){

		int i = 0;
		String modExp = super.getExpression();
		while(modExp.contains(regAnd)){
			if(i==0){
				andCount.add(0, modExp.indexOf(regAnd));// = modExp.indexOf(regAnd);
			}else{
				andCount.add(i, andCount.get(i-1) + 2 + modExp.indexOf(regAnd));
				//andCount[i] = andCount[i-1] + 2 + modExp.indexOf(regAnd);
			}
			modExp = modExp.substring(modExp.indexOf(regAnd)+2);
			i++;
		}
		
		i = 0;	
		modExp = super.getExpression();
		while(modExp.contains(regOr)){
			
			if(i==0){
				orCount.add(0, modExp.indexOf(regOr)); //orCount[0] = modExp.indexOf(regOr);
			}else{
				orCount.add(i, orCount.get(i-1) + 2 + modExp.indexOf(regOr));
				//orCount[i] = orCount[i-1] + 2 + modExp.indexOf(regOr);
			}
			modExp = modExp.substring(modExp.indexOf(regOr)+2);
			i++;
		}
		
		i = 0;
		modExp = super.getExpression();
		while(modExp.contains(regNot)){	
			if(i==0){
				notCount.add(0,modExp.indexOf(regNot));//notCount[0] = modExp.indexOf(regNot);
			}else{
				notCount.add(i, notCount.get(i-1) + 1 + modExp.indexOf(regNot));
				//notCount[i] = notCount[i-1] + 1 + modExp.indexOf(regNot);
			}
			modExp = modExp.substring(modExp.indexOf(regNot)+1);
			i++;
		}
	}
	
	
	
	/*
	public boolean isNegated(int index){
		if(expression.charAt(index-1)=='!'){
			return true;
		}else{
			return false;
		}
	}
	*/

	public ArrayList<Integer> getAndCount() {
		return andCount;
	}

	public ArrayList<Integer> getOrCount() {
		return orCount;
	}

	public ArrayList<Integer> getNotCount() {
		return notCount;
	}

	public String getRegAnd() {
		return regAnd;
	}

	public String getRegOr() {
		return regOr;
	}

	public String getRegNot() {
		return regNot;
	}

}
