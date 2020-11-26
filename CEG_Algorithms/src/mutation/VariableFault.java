package mutation;

import java.util.ArrayList;

import graph.Effect;

public abstract class VariableFault extends Mutant{

	public VariableFault(int id, Effect effect) {
		super(id, effect);
                createMutant();
	}

	public abstract void createMutant();
        
}
