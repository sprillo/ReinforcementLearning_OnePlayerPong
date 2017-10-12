package myProj;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.core.oo.state.generic.GenericOOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.StateUtilities;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.shell.visual.VisualExplorer;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.visualizer.StateRenderLayer;
import burlap.visualizer.Visualizer;
import burlap.visualizer.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class PongDomainGenerator implements DomainGenerator {
	
	public static final String CLASS_AGENT = "agent";
	public static final String CLASS_BALL = "ball";
	
	public static final String VAR_PADDLE_POS = "paddlePos";
	public static final String VAR_BALL_POS_HOR = "ballPos_hor";
	public static final String VAR_BALL_POS_VER = "ballPos_ver";
	public static final String VAR_BALL_SPEED_HOR = "ballSpeed_hor";
	public static final String VAR_BALL_SPEED_VER = "ballSpeed_ver";
	
	public static final int WIDTH = 600;
	public static final int HEIGHT = 400;       
	public static final int BALL_RADIUS = 20;
	public static final int PAD_WIDTH = 8;
	public static final int PAD_HEIGHT = 120;
	public static final int HALF_PAD_WIDTH = PAD_WIDTH / 2;
	public static final int HALF_PAD_HEIGHT = PAD_HEIGHT / 2;
	public static final double MAX_PADDLE_VEL = 10.0;
	public static final double BALL_SPEED_INCREASE = 1.1;
	public static final double MAX_BALL_SPEED = 50.0;
	public static final double INITIAL_BALL_SPEED_HOR = 20.0;
	public static final double INITIAL_BALL_SPEED_VER = 10.0;
	
	public static final String ACTION_NONE = "NONE";
	public static final String ACTION_UP = "UP";
	public static final String ACTION_DOWN = "DOWN";
	
	@Override
	public OOSADomain generateDomain(){
		OOSADomain domain = new OOSADomain();

		domain.addStateClass(CLASS_AGENT, PongAgent.class)
				.addStateClass(CLASS_BALL, PongBall.class);

		domain.addActionTypes(
				new UniversalActionType(ACTION_NONE),
				new UniversalActionType(ACTION_UP),
				new UniversalActionType(ACTION_DOWN));

		PongStateModel smodel = new PongStateModel();
		TerminalFunction tf = new PongTF();
		RewardFunction rf = new PongRF(tf);

		domain.setModel(new FactoredModel(smodel, rf, tf));

		return domain;
	}
	
	public class BallPainter implements ObjectPainter{
		@Override
		public void paintObject(Graphics2D g2, OOState s, ObjectInstance ob,
								float cWidth, float cHeight) {
			g2.setColor(Color.GRAY);
			
			///(0,0) is topleft corner in java canvas
			
			double leftCoordinate = StateUtilities.stringOrNumber(ob.get(VAR_BALL_POS_HOR)).doubleValue() - BALL_RADIUS;
			double topCoordinate = HEIGHT - (StateUtilities.stringOrNumber(ob.get(VAR_BALL_POS_VER)).doubleValue() + BALL_RADIUS); /// 'inverted' math because of java canvas
			double width = 2.0 * BALL_RADIUS;
			double height = 2.0 * BALL_RADIUS;
			double scale_hor_factor = cWidth / WIDTH;
			double scale_ver_factor = cHeight / HEIGHT;
			leftCoordinate *= scale_hor_factor;
			topCoordinate *= scale_ver_factor;
			width *= scale_hor_factor;
			height *= scale_ver_factor;

			//paint the ball
			g2.fill(new Ellipse2D.Float((float)leftCoordinate, (float)topCoordinate, (float)width, (float)height));

		}
	}
	
	public class AgentPainter implements ObjectPainter{
		@Override
		public void paintObject(Graphics2D g2, OOState s, ObjectInstance ob,
								float cWidth, float cHeight) {
			g2.setColor(Color.GRAY);
			
			///(0,0) is topleft corner in java canvas
			
			double leftCoordinate = 0;
			double topCoordinate = HEIGHT -(StateUtilities.stringOrNumber(ob.get(VAR_PADDLE_POS)).doubleValue() + HALF_PAD_HEIGHT); /// 'inverted' math because of java canvas
			double width = PAD_WIDTH;
			double height = PAD_HEIGHT;
			double scale_hor_factor = cWidth / WIDTH;
			double scale_ver_factor = cHeight / HEIGHT;
			leftCoordinate *= scale_hor_factor;
			topCoordinate *= scale_ver_factor;
			width *= scale_hor_factor;
			height *= scale_ver_factor;

			//paint the paddle
			g2.fill(new Rectangle2D.Float((float)leftCoordinate, (float)topCoordinate, (float)width, (float)height));
		}
	}
	
	public StateRenderLayer getStateRenderLayer(){
		StateRenderLayer rl = new StateRenderLayer();
		OOStatePainter ooStatePainter = new OOStatePainter();
		ooStatePainter.addObjectClassPainter(CLASS_AGENT, new AgentPainter());
		ooStatePainter.addObjectClassPainter(CLASS_BALL, new BallPainter());
		rl.addStatePainter(ooStatePainter);
		return rl;
	}
	
	public Visualizer getVisualizer(){
		return new Visualizer(this.getStateRenderLayer());
	}
	
	public static void main(String[] args){
		PongDomainGenerator gen = new PongDomainGenerator();
		OOSADomain domain = gen.generateDomain();
		State initialState = new GenericOOState(new PongAgent(), 
												new PongBall());
		SimulatedEnvironment env = new SimulatedEnvironment(domain, initialState);

		Visualizer v = gen.getVisualizer();
		VisualExplorer exp = new VisualExplorer(domain, env, v);

		exp.addKeyAction("a", ACTION_NONE, "");
		exp.addKeyAction("w", ACTION_UP, "");
		exp.addKeyAction("s", ACTION_DOWN, "");

		exp.initGUI();
	}
}
