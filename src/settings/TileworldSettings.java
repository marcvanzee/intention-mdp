package settings;

import mdp.agent.ReactionStrategy;
import mdp.algorithms.AlgorithmType;

public class TileworldSettings 
{
	
	/*
	 * Environment Settings
	 */
	public static int WORLD_SIZE =	 				10;
	public static int HOLE_GESTATION_TIME_MIN = 	60;
	public static int HOLE_GESTATION_TIME_MAX = 	210;
	public static int HOLE_LIFE_EXP_MIN = 			240;
	public static int HOLE_LIFE_EXP_MAX = 			860;
	public static int HOLE_SCORE_MIN = 				20;
	public static int HOLE_SCORE_MAX =				80; 			
	public static int INITIAL_NR_HOLES = 			4;
	public static int WALL_SIZE_MIN =				2;
	public static int WALL_SIZE_MAX = 				4;
	public static int INITIAL_NR_WALLS =			10;
	
	/*
	 * Agent Settings
	 */
	
	// Dynamism = nr. Env steps / nr. Agent steps
	// so d=5 means the agent takes 1 step every 5 steps of the environment
	public static int DYNAMISM = 					9;
	
	// Planning time is the cost for making a plan.
	// So p=4 means the reward of the agent is decreased by 4 whenever it forms a plan
	public static double PLANNING_TIME = 			3;
	
	// Commitment degree is the number of steps the agent keeps its plan
	// When cd=-1, the agent keeps its plan until it's finished
	// For value iteration, this means the agent replans when it has reached a state where it cannot optimize further
	public static int BOLDNESS = 					1;
	public static boolean USE_REACTION_STRATEGY = 	true;
	public static ReactionStrategy 
						REACTION_STRATEGY =			ReactionStrategy.TARGET_DIS_OR_ANY_HOLE;
	
	public static AlgorithmType ALGORITHM = AlgorithmType.LEARNING;
	
	public static ReactionStrategy parseReactionStrategy(String str) {
		if (str.equals("TARGET_DISAPPEARS")) return ReactionStrategy.TARGET_DISAPPEARS;
		if (str.equals("TARGET_DIS_OR_NEARER_HOLE")) return ReactionStrategy.TARGET_DIS_OR_NEARER_HOLE;
		
		return ReactionStrategy.TARGET_DIS_OR_ANY_HOLE;
	}
	
	/*
	 * Angel Settings
	 */
	
	
	public static int HYPOTHESIS_DEPTH = 6;
	
	public static int HYPOTHESIS_REPETITIONS = 5;
	
	public static boolean TEST_ENV = false;
	
	public static boolean PRINT_NOTHING = true;
}
