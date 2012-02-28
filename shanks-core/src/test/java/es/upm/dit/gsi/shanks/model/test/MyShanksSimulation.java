package es.upm.dit.gsi.shanks.model.test;

import jason.JasonException;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.Set;

import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous3D;
import sim.field.grid.SparseGrid2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal3d.continuous.ContinuousPortrayal3D;
import sim.util.Double3D;
import es.upm.dit.gsi.shanks.ShanksSimulation;
import es.upm.dit.gsi.shanks.agent.JasonShanksAgent;
import es.upm.dit.gsi.shanks.agent.ShanksAgent;
import es.upm.dit.gsi.shanks.agent.exception.DuplicatedActionIDException;
import es.upm.dit.gsi.shanks.agent.test.MyJasonShanksAgent;
import es.upm.dit.gsi.shanks.agent.test.MySimpleShanksAgent;
import es.upm.dit.gsi.shanks.exception.DuplicatedAgentIDException;
import es.upm.dit.gsi.shanks.model.element.exception.TooManyConnectionException;
import es.upm.dit.gsi.shanks.model.element.exception.UnsupportedNetworkElementStatusException;
import es.upm.dit.gsi.shanks.model.failure.Failure;
import es.upm.dit.gsi.shanks.model.scenario.Scenario;
import es.upm.dit.gsi.shanks.model.scenario.exception.DuplicatedIDException;
import es.upm.dit.gsi.shanks.model.scenario.exception.ScenarioNotFoundException;
import es.upm.dit.gsi.shanks.model.scenario.exception.UnsupportedScenarioStatusException;
import es.upm.dit.gsi.shanks.model.scenario.portrayal.exception.DuplicatedPortrayalIDException;
import es.upm.dit.gsi.shanks.model.scenario.portrayal.test.MyHyperComplexScenario2DPortrayal;

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
                        if (agent instanceof JasonShanksAgent) {
                            logger.info("Total failures resolved by Agent: "
                                    + agent.getID()
                                    + ": "
                                    + ((MyJasonShanksAgent) agent)
                                            .getNumberOfResolvedFailures());
                        }
                    }
                }
            };
            schedule.scheduleRepeating(Schedule.EPOCH, 3, steppable, 500);
            break;
        case 2:
            Steppable steppable2 = new Steppable() {

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
            };
            Steppable steppable3 = new Steppable() {

                /**
                 * 
                 */
                private static final long serialVersionUID = -929835696282793943L;

                @Override
                public void step(SimState sim) {
                    ShanksSimulation simulation = (ShanksSimulation) sim;
                    Set<Failure> failures = simulation.getScenario()
                            .getCurrentFailures();

                    if (simulation.getScenario()
                            .getProperty(Scenario.SIMULATION_GUI)
                            .equals(Scenario.SIMULATION_2D)) {
                        try {
                            SparseGridPortrayal2D failuresPortrayal = (SparseGridPortrayal2D) simulation
                                    .getScenarioPortrayal()
                                    .getPortrayals()
                                    .get(MyHyperComplexScenario2DPortrayal.FAILURE_DISPLAY_ID)
                                    .get(MyHyperComplexScenario2DPortrayal.FAILURE_PORTRAYAL_ID);
                            SparseGrid2D grid = (SparseGrid2D) failuresPortrayal
                                    .getField();
                            grid.clear();
                            int pos = 20;
                            for (Failure f : failures) {
                                grid.setObjectLocation(f, 10, pos);
                                pos += 10;
                            }
                        } catch (DuplicatedPortrayalIDException e) {
                            e.printStackTrace();
                        } catch (ScenarioNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if (simulation.getScenario()
                            .getProperty(Scenario.SIMULATION_GUI)
                            .equals(Scenario.SIMULATION_3D)) {
                        try {
                            ContinuousPortrayal3D failuresPortrayal = (ContinuousPortrayal3D) simulation
                                    .getScenarioPortrayal()
                                    .getPortrayals()
                                    .get(MyHyperComplexScenario2DPortrayal.FAILURE_DISPLAY_ID)
                                    .get(MyHyperComplexScenario2DPortrayal.FAILURE_PORTRAYAL_ID);
                            Continuous3D grid = (Continuous3D) failuresPortrayal
                                    .getField();
                            grid.clear();
                            int pos = 20;
                            for (Failure f : failures) {
                                grid.setObjectLocation(f, new Double3D(-110,
                                        100 - pos, 0));
                                pos += 10;
                            }
                        } catch (DuplicatedPortrayalIDException e) {
                            e.printStackTrace();
                        } catch (ScenarioNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            schedule.scheduleRepeating(Schedule.EPOCH, 4, steppable3, 1);
            schedule.scheduleRepeating(Schedule.EPOCH, 3, steppable2, 500);
            break;
        default:
            logger.info("No configuration for MyShanksSimulation. Configuration 0 loaded -> default");
        }

    }

    @Override
    public void registerShanksAgents() throws DuplicatedAgentIDException,
            DuplicatedActionIDException {
        MyJasonShanksAgent agent = new MyJasonShanksAgent("resolverAgent1",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent1.asl");
        this.registerShanksAgent(agent);
        MyJasonShanksAgent agent2 = new MyJasonShanksAgent("resolverAgent2",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent2.asl");
        this.registerShanksAgent(agent2);
        MyJasonShanksAgent agent3 = new MyJasonShanksAgent("resolverAgent3",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent3.asl");
        this.registerShanksAgent(agent3);
        MySimpleShanksAgent agent4 = new MySimpleShanksAgent("simpleAgent1");
        this.registerShanksAgent(agent4);
    }

}
