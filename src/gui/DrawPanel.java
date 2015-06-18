package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import messaging.ChangeMessage;
import messaging.ChangeMessageBuffer;
import messaging.ClearGraphMessage;
import model.Agent;
import model.Model;
import model.mdp.Edge;
import model.mdp.Vertex;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.algorithms.layout.util.VisRunner;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;

/**
 * This is the VIEWPOINT
 * 
 * @author marc.vanzee
 *
 */
class DrawPanel extends JPanel implements Observer 
{	
	private static final long serialVersionUID = -5345319851341875800L;
	
	private final Graph<Vertex<?>,Edge<?,?>> ig = 
			Graphs.<Vertex<?>,Edge<?,?>>synchronizedDirectedGraph(new DirectedSparseMultigraph<Vertex<?>,Edge<?,?>>());

    private final Graph<Vertex<?>,Edge<?,?>> g = new ObservableGraph<Vertex<?>,Edge<?,?>>(ig);;
    
    private final AbstractLayout<Vertex<?>,Edge<?,?>> layout = new SpringLayout<Vertex<?>,Edge<?,?>>(g);
	
    private final Layout<Vertex<?>,Edge<?,?>> staticLayout = new StaticLayout<Vertex<?>,Edge<?,?>>(g, layout);
    
    private final VisualizationViewer<Vertex<?>,Edge<?,?>> vv = new JUNGRendering(layout, staticLayout);
    
    private final LayoutTransition<Vertex<?>,Edge<?,?>> lt =
			new LayoutTransition<Vertex<?>,Edge<?,?>>(vv, vv.getGraphLayout(),
					staticLayout);
    	
	private final MainGUI mainApp;
	private final Model model;
	
	private boolean animate = true;
	
	private DrawTaskScheduler taskScheduler = new DrawTaskScheduler(this);
	
	public DrawPanel(MainGUI mainApp) 
	{
		this.mainApp = mainApp;
		model = mainApp.getModel();
		// set a preferred size for the custom panel.
		setPreferredSize(new Dimension(400,250));
		
		//create a graphdraw
        layout.setSize(new Dimension(600,600));
			
        setLayout(new BorderLayout());
        setSize(new Dimension(600, 600));

        add(vv);
        vv.repaint();
   }

    /**
     * Receive messages, type is defined in MessageType
     */
    @Override
	public void update(Observable m, Object type) 
    {
    	System.out.println("received message");
    	// schedule to execute the message by adding it to the queue
    	taskScheduler.add((ChangeMessageBuffer)type);
    	
    }
        
    public void toggleAnimate() {
    	animate = !animate;
    	
    	if (layout != null)
    		layout.lock(animate);
    }
    
    /**
     * Remove all vertices and edges
     */
    public void clearGraph() 
    {
    	ChangeMessage gc = new ClearGraphMessage();
    	taskScheduler.add(gc);
    }
      
    public int countVertices() {
    	return g.getVertexCount();
    }
    
    /**
     * Callback method for the Task Scheduler
     * 
     * @param change
     */
    public void changeGraph(ChangeMessage change) 
    {
    	change.modifyGraph(g);
		
    	Agent ag = model.getAgent();
    	
    	if (ag != null)
    	{
    		mainApp.textFieldRewards.setText(model.getAgent().getReward()+"");
    		mainApp.textFieldDeliberations.setText(model.getAgent().getDeliberations()+"");
    		mainApp.textFieldActs.setText(model.getAgent().getActs()+"");
    	}
    	
    	mainApp.textFieldSteps.setText(model.getSteps()+"");
    	
    	layout.initialize();

		Relaxer relaxer = new VisRunner((IterativeContext)layout);
		relaxer.stop();
		relaxer.prerelax();
		
		
		StaticLayout<Vertex<?>,Edge<?,?>> staticLayout =
 			new StaticLayout<Vertex<?>,Edge<?,?>>(g, layout);
			LayoutTransition<Vertex<?>,Edge<?,?>> lt =
				new LayoutTransition<Vertex<?>,Edge<?,?>>(vv, vv.getGraphLayout(),
						staticLayout);
		
		Animator animator = new Animator(lt);
		animator.start();
		vv.repaint();
    	repaint();
    	    	
    	
    }
}