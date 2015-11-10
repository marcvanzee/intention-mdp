# mdp-plan-revision

Read a more detailed description of the conceptual underpinnings in the following paper:

> [Intention Reconsideration as Metareasoning](http://www.marcvanzee.nl/publications/2015/borm2015_metareasoning.pdf) ([Marc van Zee](http://www.marcvanzee.nl), [Thomas Icard](http://stanford.edu/~icard/)), [In Bounded Optimality and Rational Metareasoning NIPS 2015 Workshop](https://sites.google.com/site/boundedoptimalityworkshop/home), 2015. 

### Background: Intention Reconsideration
In this project we are interested in understanding a specific aspect of bounded optimality and metareasoning, namely the control of _plan_ or _intention reconsideration_. This problem is more circumscribed than the general problem of metareasoning, but it also inherits many of the interesting and characteristic features. The basic problem is as follows: Suppose an agent has devised a (partial) plan of action for a particular environment, as it appeared to the agent at some time _t_. But then at some later time _t'>t_---perhaps in the course of executing the plan---the agent's view on the world changes. When should the agent _replan_, and when should the agent keep its current (perhaps improvable, possibly dramatically) plan? In other words, in the specific context of a planning agent who is learning new relevant facts about the world, when should this agent stop to _rethink_, and when should it go ahead and _act_ according to its current plan? 

Our work builds on earlier, largely forgotten (regrettably, in our view) work in the _belief-desire-intention_ (BDI) agent literature, by Kinny and Georgeff. They compare some rudimentary reconsideration strategies, as a function of several environmental parameters, in simple _Tileworld_ experiments. We reproduce their results, and also compare their reconsideration strategies to the \emph{optimal} reconsideration strategies for these environmental parameter settings. Interestingly, even the very simple agents Kinny and Georgeff considered behave nearly optimally in certain environments. However, no agent performs optimally across environments. Our results suggest that meta-meta-reasoning may indeed be called for in this setting, so that an agent might tune its reconsideration strategy flexibly to different environments.

## Experiments

Computing an optimal policy for the meta-level MDP is difficult in general. In this section, we present experimental simulation results on specific classes of environments and agents. We have implemented the general framework from the previous section in Java.\footnote{The source code is available on Github: \url{https://github.com/marcvanzee/mdp-plan-revision}. An example MDP visualization is depicted in Figure~\ref{fig:mdp} of Appendix A.}  %, where red circles denote states, blue triangles denote Q-states, and green arrows denote the optimal policy that is computed using value iteration. 
While we have also been investigating this general setting, in this abstract we focus on one set of experiments reproducing the aforementioned Tileworld experiments by Kinny and Georgeff, with comparison to an ``angelic'' metareasoner, who solves the think/act tradeoff approximately optimally. 

\subsection{Experimental Setup}

Kinny and Georgeff present the Tileworld as a 2-dimensional grid on which the time between two subsequent hole appearances is characterized by a gestation period $g$, and holes have a life-expectancy $l$, both taken from a uniform distribution. Planning cost $p$ is operationalized as a time delay.  The ratio of clock rates between the agent's action capabilities and changes in the environment is set by a \emph{rate of world change} parameter $\gamma$. This parameter determines the \emph{dynamism} of the world. When an agent plans, it selects the plan that maximizes hole score divided by distance (an approximation to computing an optimal policy in this setting). The performance of an agent is characterized by its \emph{effectiveness} $\epsilon$, which is its score divided by the maximum possible score it could have achieved. The setup is easily seen as a specific case of our meta-decision problem (see Fig. \ref{fig:tileworld}).

Kinny and Georgeff propose two families of intention reconsideration strategies: bold agents, who inflexibly replan after a fixed number of steps, and reactive agents, who respond to specific events in the environment. For us, a \emph{bold} agent only reconsiders its intentions when it has reached the target hole; and a \emph{reactive} agent is a bold agent that also replans when a hole closer than its current target appears, or when its target disappears. 

In addition, we consider an \emph{angelic} agent, who approximates the value of computation calculations that would allow always selecting \textsf{think} or \textsf{act} in an optimal way. It does so by recursively running a large number of simulations for the meta-level actions from a given state, approximating the expected value of both, and choosing the better. Because we are interested in the theoretically best policy, the angelic agent is not charged for any of this computation: time stops, and the agent can spend as much time as it needs to determine the best meta-level action (hence the term `angelic').

\subsection{Results}

Graphs of the results can be found in Appendix A. In Figure~\ref{fig:result1} we compare the bold agent with the angelic planner with the same parameter settings as Kinny and Georgeff and a planning time of 2. Unsurprisingly, the angelic planner outperforms the bold agent. In Figure~\ref{fig:result2}, we increase the planning time to 4, which increases the difference in performance between the angelic planner and the bold agent, while the reactive planner does equally well. However, in Figure~\ref{fig:result3}, we see that when we change the parameters settings such that the world is significantly smaller and holes appear as quickly as they come, the angelic planner outperforms the reactive agent as well. Finally, in Figure~\ref{fig:result4} we consider a highly dynamic domain in which holes appear and disappear very fast. Here the bold agent outperforms the reactive strategy, and does nearly as well as the angelic agent. In such an environment, agents that replan too often never have a chance to make it toward their goals.

Intriguingly, even these very simple agents---bold agents and rudimentary reactive agents---come very close to ideal in certain environments. This suggests that if we fix a given environment, near-optimal intention/plan reconsideration can actually be done quite tractably. However, since these optimal meta-level strategies differ from environment to environment, this seems to be a natural setting in which meta-meta-level reasoning can be useful. One would like a method for determining which of a family of meta-level strategies one ought to use, given some (statistical or other) information about the current environment, its dynamics and the relative (opportunity) cost of planning.
