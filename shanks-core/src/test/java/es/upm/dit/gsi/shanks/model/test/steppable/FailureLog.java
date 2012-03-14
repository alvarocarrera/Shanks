/**
 * 
 */
package es.upm.dit.gsi.shanks.model.test.steppable;

import java.util.logging.Logger;

import es.upm.dit.gsi.shanks.ShanksSimulation;
import es.upm.dit.gsi.shanks.agent.JasonShanksAgent;
import es.upm.dit.gsi.shanks.agent.ShanksAgent;
import es.upm.dit.gsi.shanks.agent.test.MyJasonShanksAgent;
import sim.engine.SimState;
import sim.engine.Steppable;

/**
 * @author a.carrera
 *
 */
public class FailureLog implements Steppable {


    public Logger logger = Logger.getLogger(FailureLog.class.getName());
    
    /**
     * 
     */
    private static final long serialVersionUID = 2669002521740395423L;

    @Override
    public void step(SimState sim) {
        ShanksSimulation simulation = (ShanksSimulation) sim;
        for (ShanksAgent agent : simulation.getAgents()) {
            if (agent instanceof JasonShanksAgent) {
                logger.info("Total failures resolved by Agent: "
                        + agent.getID()
                        + ": "
                        + ((MyJasonShanksAgent) agent)
                                .getNumberOfResolvedFailures());
            }
        }
    }

}
