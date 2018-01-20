package myProj;

import burlap.mdp.core.oo.state.generic.GenericOOState;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.behavior.functionapproximation.dense.ConcatenatedObjectFeatures;
import burlap.behavior.functionapproximation.dense.NumericVariableFeatures;
import burlap.behavior.functionapproximation.sparse.tilecoding.TileCodingFeatures;
import burlap.behavior.functionapproximation.sparse.tilecoding.TilingArrangement;
import burlap.behavior.functionapproximation.DifferentiableStateActionValue;
import burlap.behavior.singleagent.learning.tdmethods.vfa.GradientDescentSarsaLam;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.behavior.singleagent.Episode;
import burlap.visualizer.Visualizer;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;

// For policy visualization
import burlap.mdp.singleagent.common.VisualActionObserver;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;

import java.util.ArrayList;
import java.util.List;

public class PongMain{
	public static void main(String[] args){
		PongSARSA();
	}
	
	public static void PongSARSA(){

		PongDomainGenerator pongGen = new PongDomainGenerator();
		OOSADomain domain = pongGen.generateDomain();
		
		ConcatenatedObjectFeatures inputFeatures = new ConcatenatedObjectFeatures()
				.addObjectVectorizion(PongDomainGenerator.CLASS_AGENT, new NumericVariableFeatures())
				.addObjectVectorizion(PongDomainGenerator.CLASS_BALL, new NumericVariableFeatures());

		int nTilings = 5;
		double resolution = 5.;

		double paddlePosWidth = PongDomainGenerator.HEIGHT / resolution;
		double ballPos_horWidth = PongDomainGenerator.WIDTH / resolution;
		double ballPos_verWidth = PongDomainGenerator.HEIGHT / resolution;
		double ballSpeed_horWidth = 2.0 * PongDomainGenerator.MAX_BALL_SPEED / resolution;
		double ballSpeed_verWitdh = 2.0 * PongDomainGenerator.MAX_BALL_SPEED / resolution;

		TileCodingFeatures tilecoding = new TileCodingFeatures(inputFeatures);
		tilecoding.addTilingsForAllDimensionsWithWidths(
				new double []{
					paddlePosWidth
					, ballPos_horWidth
					, ballPos_verWidth
					, ballSpeed_horWidth
					, ballSpeed_verWitdh
					},
				nTilings,
				TilingArrangement.RANDOM_JITTER);
				
		double defaultQ = 0.5;
		DifferentiableStateActionValue vfa = tilecoding.generateVFA(defaultQ/nTilings);
		GradientDescentSarsaLam agent = new GradientDescentSarsaLam(domain, 0.99, vfa, 0.02, 0.5);
		
		Visualizer v = pongGen.getVisualizer();
		VisualActionObserver vob = new VisualActionObserver(v);
		vob.initGUI();
		
		List episodes = new ArrayList();
		PongBall.rn.setSeed(1);// Still not fully reproducible: need to find out how to set GradientDescentSarsaLam's seed
		for(int i = 0; i < 5000; i++){
			State s = new GenericOOState(new PongAgent(), new PongBall());
			SimulatedEnvironment env = new SimulatedEnvironment(domain, s);
			if(i % 500 == 0){
				env.addObservers(vob);
			}
			System.out.print("Playing game # " + i + " ... ");
			Episode ea = agent.runLearningEpisode(env);
			episodes.add(ea);	
			System.out.println("Game finished. Length of game: " + ea.maxTimeStep());
			env.resetEnvironment();
		}
		
		System.out.println("Stopped training after 5000 iterations. Now just playing.");
		for(int i = 0; ; i++){
			State s = new GenericOOState(new PongAgent(), new PongBall());
			SimulatedEnvironment env = new SimulatedEnvironment(domain, s);
			
			env.addObservers(vob);
			
			Policy p = agent.planFromState(s);
			
			PolicyUtils.rollout(p, env);
			env.resetEnvironment();
		}
		
	}
}
