package myProj;

import burlap.mdp.core.state.State;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.singleagent.model.RewardFunction;

public class PongRF implements RewardFunction {
	
	TerminalFunction tf;
	
	public PongRF(TerminalFunction _tf){
		this.tf = _tf;
	}
	
	@Override
	public double reward(State s, Action a, State sprime) {
		if(this.tf.isTerminal(sprime)) return -1000.0;
		return 0.0;
	}
	
}
