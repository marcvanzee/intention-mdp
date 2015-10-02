package simulations;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import constants.MathOperations;
import mdps.Tileworld;
import mdps.elements.Action;
import mdps.elements.Agent;
import mdps.elements.QState;
import mdps.elements.State;
import mdps.factories.MDPType;
import mdps.generators.TileworldGenerator;
import settings.TileworldSettings;

/**
 * A TileWorldSimulation consists of a TileWorld (i.e. an MDP and an Agent) and models the evolution of this TileWorld over time.
 * 
 * @author marc.vanzee
 *
 */
public class TileworldSimulation extends BasicSimulation
{
	private final TileworldGenerator tileWorldGenerator = new TileworldGenerator();
	private final Agent agent;
	private final Tileworld tileworld;
	private double maxScore = 0;
	
	private int nextHole;
	
	public TileworldSimulation() 
	{
		super(MDPType.TILEWORLD);
		agent = ((Tileworld) mdp).getAgent();
		tileworld = (Tileworld) mdp;
	}
	
	//
	// GETTERS AND SETTERS
	//	
	public double getValue(State s) {
		return agent.getValue(s);
	}
	
	//
	// OTHER PUBLIC METHODS
	//
	
	public void buildNewModel() 
	{
		steps = 0;
		maxScore = 0;
		
		if (isRunning)
			timer.cancel();
		
		tileworld.reset();
		agent.reset();
		
		// generate a tileworld	
		this.tileWorldGenerator.run(mdp);
		
		// add agent
		tileworld.addAgentRandomly(new HashSet<State>(tileworld.getObstacles()));
				
		notifyGUI();
		
		// immediately create a hole and tell event to deliberate
		nextHole = 0; 
		agent.deliberateForEvent();
	}
		
	public void step() 
	{
		if (nextHole <= 0) {
			addHole();
		}
		
		if (steps % TileworldSettings.DYNAMISM == 0) {
			
			// get the next choice by the agent
			if (agent != null) {
				agent.step();
					
				// if the agent acted, move the agent and compute its reward
				if (agent.getNextAction() != null)
					moveAgent(agent.getCurrentState(), agent.getNextAction());
			}
		}
		
		steps++;
		nextHole--;
		
		// first clear the message buffer
		mdp.clearMessageBuffer();
			
		decreaseLifetimeHoles();	
		notifyGUI();
	}
	
	public double getAgentScore() {
		return agent.getScore();
	}
	
	public double getMaxScore() {
		return maxScore;
	}
	
	protected void moveAgent(State currentState, Action selectedAction) 
	{
		// in the tileworld, everything is completely deterministic so moving is quite easy.
		// simply select the unique qstate and state and move there.
		
		System.out.println("action at step " + steps + ": " + selectedAction);
		final QState qState = mdp.getQState(currentState, selectedAction);
		final State newState = mdp.getQEdges(qState).get(0).getToVertex();
				
		agent.getCurrentState().setVisited(false);
		agent.setCurrentState(newState);
		newState.setVisited(true);
		
		if (newState.isObstacle())
			System.out.println("moving to obstacle at step " + steps);
		
		agent.reward();
	}
	
	private void addHole() 
	{
		final State hole = tileworld.getRandomEmptyState();
		
		int lifetime = MathOperations.getRandomInt(
				TileworldSettings.HOLE_LIFE_EXP_MIN, TileworldSettings.HOLE_LIFE_EXP_MAX);
	
		int score = MathOperations.getRandomInt(
				TileworldSettings.HOLE_SCORE_MIN, TileworldSettings.HOLE_SCORE_MAX);
		
		hole.setReward(score);
		hole.setLifeTime(lifetime);
		hole.setHole(true);
		
		this.maxScore += score;		

		tileworld.addHole(hole);
						
		setNextHole();
	}
	
	
	private void setNextHole()
	{
		nextHole = MathOperations.getRandomInt(
				TileworldSettings.HOLE_GESTATION_TIME_MIN, TileworldSettings.HOLE_GESTATION_TIME_MAX);
		System.out.println("next hole in: " + nextHole);
	}
	
	private void decreaseLifetimeHoles() 
	{
		final List<State> toRemove = new LinkedList<State>();
		final State agState = agent.getCurrentState();
		final List<State> holes = tileworld.getHoles();
		
		for (State hole : holes) {
			hole.decreaseLifetime();
			
			if (hole.getLifetime() <= 0 || hole == agState) {
				toRemove.add(hole);
			}
		}

		for (State hole : toRemove) {
			hole.setHole(false);
			tileworld.removeHole(hole);
			
			hole.setReward(0.0);
		}	
	}
}
