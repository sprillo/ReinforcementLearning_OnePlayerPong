package myProj;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.StateUtilities;
import burlap.mdp.core.state.UnknownKeyException;
import burlap.mdp.core.state.annotations.DeepCopyState;

import java.util.Arrays;
import java.util.List;

import static myProj.PongDomainGenerator.CLASS_BALL;
import static myProj.PongDomainGenerator.VAR_BALL_POS_HOR;
import static myProj.PongDomainGenerator.VAR_BALL_POS_VER;
import static myProj.PongDomainGenerator.VAR_BALL_SPEED_HOR;
import static myProj.PongDomainGenerator.VAR_BALL_SPEED_VER;
import static myProj.PongDomainGenerator.WIDTH;
import static myProj.PongDomainGenerator.HEIGHT;
import static myProj.PongDomainGenerator.INITIAL_BALL_SPEED_HOR;
import static myProj.PongDomainGenerator.INITIAL_BALL_SPEED_VER;

import java.util.Random;

@DeepCopyState
public class PongBall implements ObjectInstance, MutableState {
	
	public static final Random rn = new Random();
	
	private final static List<Object> keys = Arrays.<Object>asList(	VAR_BALL_POS_HOR
																	,VAR_BALL_POS_VER
																	,VAR_BALL_SPEED_HOR
																	,VAR_BALL_SPEED_VER);
	
	public String name = "ball";
	
	public double ballPos_hor;
	public double ballPos_ver;
	public double ballSpeed_hor;
	public double ballSpeed_ver;
	
	public PongBall(){
		this.ballPos_hor = WIDTH / 2.0;
		this.ballPos_ver = HEIGHT / 2.0;
		
		int n = 2000;
		int i = (rn.nextInt() % 500) + 1500;
		int j = (rn.nextInt() % 500) + 1500;
		
		this.ballSpeed_hor = INITIAL_BALL_SPEED_HOR * i / 1.0 / n;
		this.ballSpeed_ver = INITIAL_BALL_SPEED_VER * j / 1.0 / n;
	}
	
	public PongBall(double _ballPos_hor
					,double _ballPos_ver
					,double _ballSpeed_hor
					,double _ballSpeed_ver){
		this.ballPos_hor = _ballPos_hor;
		this.ballPos_ver = _ballPos_ver;
		this.ballSpeed_hor = _ballSpeed_hor;
		this.ballSpeed_ver = _ballSpeed_ver;
	}
	
	public PongBall(double _ballPos_hor
					,double _ballPos_ver
					,double _ballSpeed_hor
					,double _ballSpeed_ver
					,String _name){
		this.ballPos_hor = _ballPos_hor;
		this.ballPos_ver = _ballPos_ver;
		this.ballSpeed_hor = _ballSpeed_hor;
		this.ballSpeed_ver = _ballSpeed_ver;
		this.name = _name;
	}
	
	@Override
	public String className() {
		return CLASS_BALL;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public ObjectInstance copyWithName(String objectName) {
		return new PongBall(ballPos_hor
							,ballPos_ver
							,ballSpeed_hor
							,ballSpeed_ver
							,objectName);
	}

	@Override
	public MutableState set(Object variableKey, Object value) {
		if(variableKey.equals(VAR_BALL_POS_HOR)){
			this.ballPos_hor = StateUtilities.stringOrNumber(value).doubleValue();
		} else if(variableKey.equals(VAR_BALL_POS_VER)){
			this.ballPos_ver = StateUtilities.stringOrNumber(value).doubleValue();
		} else if(variableKey.equals(VAR_BALL_SPEED_HOR)){
			this.ballSpeed_hor = StateUtilities.stringOrNumber(value).doubleValue();
		} else if(variableKey.equals(VAR_BALL_SPEED_VER)){
			this.ballSpeed_ver = StateUtilities.stringOrNumber(value).doubleValue();
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
		if(variableKey.equals(VAR_BALL_POS_HOR)){
			return ballPos_hor;
		} else if(variableKey.equals(VAR_BALL_POS_VER)){
			return ballPos_ver;
		} else if(variableKey.equals(VAR_BALL_SPEED_HOR)){
			return ballSpeed_hor;
		} else if(variableKey.equals(VAR_BALL_SPEED_VER)){
			return ballSpeed_ver;
		}
		throw new UnknownKeyException(variableKey);
	}

	@Override
	public PongBall copy() {
		return new PongBall(ballPos_hor
							,ballPos_ver
							,ballSpeed_hor
							,ballSpeed_ver);
	}

	@Override
	public String toString() {
		return StateUtilities.stateToString(this);
	}
}
