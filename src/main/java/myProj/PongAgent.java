package myProj;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.StateUtilities;
import burlap.mdp.core.state.UnknownKeyException;
import burlap.mdp.core.state.annotations.DeepCopyState;

import java.util.Arrays;
import java.util.List;

import static myProj.PongDomainGenerator.CLASS_AGENT;
import static myProj.PongDomainGenerator.VAR_PADDLE_POS;
import static myProj.PongDomainGenerator.HEIGHT;

@DeepCopyState
public class PongAgent implements ObjectInstance, MutableState {
	
	private final static List<Object> keys = Arrays.<Object>asList(VAR_PADDLE_POS);
	
	public String name = "agent";
	
	public double paddlePos;
	
	public PongAgent(){
		this.paddlePos = HEIGHT / 2.0;
	}
	
	public PongAgent(double _paddlePos){
		this.paddlePos = _paddlePos;
	}
	
	public PongAgent(double _paddlePos
					, String _name){
		this.paddlePos = _paddlePos;
		this.name = _name;
	}
	
	@Override
	public String className() {
		return CLASS_AGENT;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public ObjectInstance copyWithName(String objectName) {
		return new PongAgent(paddlePos, objectName);
	}

	@Override
	public MutableState set(Object variableKey, Object value) {
		if(variableKey.equals(VAR_PADDLE_POS)){
			this.paddlePos = StateUtilities.stringOrNumber(value).doubleValue();
		}
		else{
			throw new UnknownKeyException(variableKey);
		}
		return this;
	}

	@Override
	public List<Object> variableKeys() {
		return keys;
	}

	@Override
	public Object get(Object variableKey) {
		if(variableKey.equals(VAR_PADDLE_POS)){
			return paddlePos;
		}
		throw new UnknownKeyException(variableKey);
	}

	@Override
	public PongAgent copy() {
		return new PongAgent(paddlePos);
	}

	@Override
	public String toString() {
		return StateUtilities.stateToString(this);
	}
}
