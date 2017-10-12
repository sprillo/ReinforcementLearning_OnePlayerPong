package myProj;

import burlap.mdp.core.oo.state.generic.GenericOOState;
import burlap.mdp.core.state.State;
import burlap.mdp.core.TerminalFunction;

import static myProj.PongDomainGenerator.PAD_WIDTH;
import static myProj.PongDomainGenerator.BALL_RADIUS;
import static myProj.PongDomainGenerator.HALF_PAD_HEIGHT;

import static myProj.PongDomainGenerator.CLASS_AGENT;
import static myProj.PongDomainGenerator.CLASS_BALL;

public class PongTF implements TerminalFunction {
	
	public PongTF(){}
	
	@Override
	public boolean isTerminal(State s) {
		GenericOOState gs = (GenericOOState)s;
		PongAgent agent = (PongAgent)gs.touch(CLASS_AGENT);
		PongBall ball = (PongBall)gs.touch(CLASS_BALL);
		if(ball.ballPos_hor <= PAD_WIDTH + BALL_RADIUS){
			if(!(Math.abs(ball.ballPos_ver - agent.paddlePos) <= HALF_PAD_HEIGHT)){
				return true;
			}
		}
		return false;
	}
	
}
