package myProj;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.oo.state.generic.GenericOOState;
import burlap.mdp.core.state.State;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;

import java.util.ArrayList;
import java.util.List;

import static myProj.PongDomainGenerator.BALL_RADIUS;
import static myProj.PongDomainGenerator.PAD_WIDTH;
import static myProj.PongDomainGenerator.HALF_PAD_HEIGHT;
import static myProj.PongDomainGenerator.HEIGHT;
import static myProj.PongDomainGenerator.WIDTH;
import static myProj.PongDomainGenerator.MAX_PADDLE_VEL;
import static myProj.PongDomainGenerator.BALL_SPEED_INCREASE;

import static myProj.PongDomainGenerator.CLASS_AGENT;
import static myProj.PongDomainGenerator.CLASS_BALL;

public class PongStateModel implements FullStateModel{
	
	public PongStateModel() {}

	@Override
	public List<StateTransitionProb> stateTransitions(State s, Action a) {

		List<StateTransitionProb> tps = new ArrayList<StateTransitionProb>(1);
		
		State sprime = sample(s,a);
		double prob = 1.0; // pong is deterministic!
		
		tps.add(new StateTransitionProb(sprime, prob));
		
		return tps;
	}
	
	@Override
	public State sample(State s, Action a) {
		
		s = s.copy();
		GenericOOState gs = (GenericOOState)s;
		PongAgent agent = (PongAgent)gs.touch(CLASS_AGENT);
		PongBall ball = (PongBall)gs.touch(CLASS_BALL);
		
		// update ball position
		ball.ballPos_hor += ball.ballSpeed_hor;
		ball.ballPos_ver += ball.ballSpeed_ver;
		
		// update paddle's vertical position, keep paddle on the screen
		double paddleVel = 0.0;
		if(a.actionName().equals("UP")){
			paddleVel = MAX_PADDLE_VEL;
		} else if(a.actionName().equals("DOWN")){
			paddleVel = -MAX_PADDLE_VEL;
		} else{assert(a.actionName().equals("NONE"));}
		
		if(agent.paddlePos + paddleVel >= HALF_PAD_HEIGHT && agent.paddlePos + paddleVel <= HEIGHT - 1 - HALF_PAD_HEIGHT){
			agent.paddlePos += paddleVel;
		}

		// check collision with top or bottom wall
		if(ball.ballPos_ver <= BALL_RADIUS || ball.ballPos_ver >= HEIGHT - 1 - BALL_RADIUS){
			ball.ballSpeed_ver *= -1;
		}
		
		// check collision with gutter
		if(ball.ballPos_hor <= PAD_WIDTH + BALL_RADIUS){
			if(Math.abs(ball.ballPos_ver - agent.paddlePos) <= HALF_PAD_HEIGHT){
				ball.ballSpeed_hor *= -BALL_SPEED_INCREASE;
				ball.ballSpeed_ver *= BALL_SPEED_INCREASE;
			}
		}
		
		// check collision with right wall
		if(ball.ballPos_hor >= WIDTH - 1 - PAD_WIDTH - BALL_RADIUS){
			ball.ballSpeed_hor *= -BALL_SPEED_INCREASE;
			ball.ballSpeed_ver *= BALL_SPEED_INCREASE;
		}
		
		return gs;
		
	}
	
}
