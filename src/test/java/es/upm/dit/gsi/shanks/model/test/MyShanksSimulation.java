package es.upm.dit.gsi.shanks.model.test;

import jason.JasonException;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import es.upm.dit.gsi.shanks.ShanksSimulation;
import es.upm.dit.gsi.shanks.agent.ShanksAgent;
import es.upm.dit.gsi.shanks.agent.exception.DuplicatedActionIDException;
import es.upm.dit.gsi.shanks.agent.test.MyShanksAgent;
import es.upm.dit.gsi.shanks.exception.DuplicatedAgentIDException;
import es.upm.dit.gsi.shanks.model.element.exception.TooManyConnectionException;
import es.upm.dit.gsi.shanks.model.element.exception.UnsupportedNetworkElementStatusException;
import es.upm.dit.gsi.shanks.model.scenario.Scenario;
import es.upm.dit.gsi.shanks.model.scenario.exception.DuplicatedIDException;
import es.upm.dit.gsi.shanks.model.scenario.exception.ScenarioNotFoundException;
import es.upm.dit.gsi.shanks.model.scenario.exception.UnsupportedScenarioStatusException;
import es.upm.dit.gsi.shanks.model.scenario.portrayal.exception.DuplicatedPortrayalIDException;

/**
 * @author a.carrera
 * 
 */
public class MyShanksSimulation extends ShanksSimulation {

    private static final long serialVersionUID = 1778288778609950190L;
    private Properties configuration;

    public static final String CONFIGURATION = "Configuration";

    /**
     * @param seed
     * @param scenarioClass
     * @param scenarioID
     * @param initialState
     * @param properties
     * @param configPropertiesMyShanksSimulation
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws UnsupportedNetworkElementStatusException
     * @throws TooManyConnectionException
     * @throws UnsupportedScenarioStatusException
     * @throws DuplicatedIDException
     * @throws DuplicatedPortrayalIDException
     * @throws ScenarioNotFoundException
     * @throws DuplicatedActionIDException
     * @throws DuplicatedAgentIDException
     * @throws JasonException
     */
    public MyShanksSimulation(long seed,
            Class<? extends Scenario> scenarioClass, String scenarioID,
            String initialState, Properties properties,
            Properties configPropertiesMyShanksSimulation)
            throws SecurityException, IllegalArgumentException,
            NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            UnsupportedNetworkElementStatusException,
            TooManyConnectionException, UnsupportedScenarioStatusException,
            DuplicatedIDException, DuplicatedPortrayalIDException,
            ScenarioNotFoundException, DuplicatedAgentIDException,
            DuplicatedActionIDException {
        super(seed, scenarioClass, scenarioID, initialState, properties);
        this.configuration = configPropertiesMyShanksSimulation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.upm.dit.gsi.shanks.ShanksSimulation#addSteppables()
     */
    @Override
    public void addSteppables() {
        int conf = new Integer(
                this.configuration
                        .getProperty(MyShanksSimulation.CONFIGURATION));
        switch (conf) {
        case 0:
            logger.fine("Nothing to do here... No more steppables");
            break;
        case 1:
            Steppable steppable = new Steppable() {

                /**
                 * 
                 */
                private static final long serialVersionUID = 2669002521740395423L;

                @Override
                public void step(SimState sim) {
                    ShanksSimulation simulation = (ShanksSimulation) sim;
                    for (ShanksAgent agent : simulation.getAgents()) {
                        logger.info("Total failures resolved by Agent: "
                                + agent.getID()
                                + ": "
                                + ((MyShanksAgent) agent)
                                        .getNumberOfResolvedFailures());
                    }
                }
            };
            schedule.scheduleRepeating(Schedule.EPOCH, 3, steppable, 500);
            break;
        default:
            logger.info("No configuration for MyShanksSimulation. Configuration 0 loaded -> default");
        }

    }

    @Override
    public void registerShanksAgents() throws DuplicatedAgentIDException,
            DuplicatedActionIDException {
        MyShanksAgent agent = new MyShanksAgent("resolverAgent1",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent1.asl");
        this.registerShanksAgent(agent);
        MyShanksAgent agent2 = new MyShanksAgent("resolverAgent2",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent2.asl");
        this.registerShanksAgent(agent2);
        MyShanksAgent agent3 = new MyShanksAgent("resolverAgent3",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent3.asl");
        this.registerShanksAgent(agent3);
        MyShanksAgent agent4 = new MyShanksAgent("resolverAgent4",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent3.asl");
        this.registerShanksAgent(agent4);
        MyShanksAgent agent5 = new MyShanksAgent("resolverAgent5",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent3.asl");
        this.registerShanksAgent(agent5);
        MyShanksAgent agent6 = new MyShanksAgent("resolverAgent6",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent3.asl");
        this.registerShanksAgent(agent6);
        MyShanksAgent agent7 = new MyShanksAgent("resolverAgent7",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent3.asl");
        this.registerShanksAgent(agent7);
        MyShanksAgent agent8 = new MyShanksAgent("resolverAgent8",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent3.asl");
        this.registerShanksAgent(agent8);
        MyShanksAgent agent9 = new MyShanksAgent("resolverAgent9",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent3.asl");
        this.registerShanksAgent(agent9);
        MyShanksAgent agent10 = new MyShanksAgent("resolverAgent10",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent3.asl");
        this.registerShanksAgent(agent10);
    }

}
