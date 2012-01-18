package es.upm.dit.gsi.shanks.model;

/**
 * SecenarioManager class
 * 
 * This class generate the possibles errors
 * 
 * @author Daniel Lara
 * @author a.carrera
 * @version 0.1
 * 
 */

import java.util.List;
import java.util.logging.Logger;

import sim.engine.SimState;
import sim.engine.Steppable;
import es.upm.dit.gsi.shanks.ShanksSimulation;
import es.upm.dit.gsi.shanks.model.failure.Failure;
import es.upm.dit.gsi.shanks.model.failure.exception.NoCombinationForFailureException;
import es.upm.dit.gsi.shanks.model.failure.exception.UnsupportedElementInFailureException;
import es.upm.dit.gsi.shanks.model.scenario.Scenario;
import es.upm.dit.gsi.shanks.model.scenario.exception.UnsupportedScenarioStatusException;
import es.upm.dit.gsi.shanks.model.scenario.portrayal.ScenarioPortrayal;

public class ScenarioManager implements Steppable {

    public Logger logger = Logger.getLogger(ScenarioManager.class.getName());

    private static final long serialVersionUID = -7448202235281457216L;

    // DEFAULT STATES OF SCENARIO MANAGER
    private static final int CHECK_FAILURES = 0;
    private static final int GENERATE_FAILURES = 1;

    private Scenario scenario;
    private ScenarioPortrayal portrayal;
    private int simulationStateMachineStatus;

    /**
     * @param scenario
     * @param portrayal
     */
    public ScenarioManager(Scenario scenario, ScenarioPortrayal portrayal) {
        this.scenario = scenario;
        this.portrayal = portrayal;
    }

    /**
     * @param scenario
     */
    public ScenarioManager(Scenario scenario) {
        this.scenario = scenario;
        while (this.getPortrayal()==null) {
            this.portrayal = scenario.createScenarioPortrayal();
        }
    }
    
    /**
     * @return the scenario
     */
    public Scenario getScenario() {
        return this.scenario;
    }

    /**
     * @return the portrayal
     */
    public ScenarioPortrayal getPortrayal() {
        return portrayal;
    }

    /**
     * 
     */
    public void setPortrayal(ScenarioPortrayal portrayal) {
        this.portrayal = portrayal;
    }
    
    /* (non-Javadoc)
     * @see sim.engine.Steppable#step(sim.engine.SimState)
     */
    public void step(SimState state) {
        ShanksSimulation sim = (ShanksSimulation) state;
        try {
            this.stateMachine(sim);
        } catch (UnsupportedScenarioStatusException e) {
            logger.severe("UnsupportedScenarioStatusException: " + e.getMessage());
        } catch (NoCombinationForFailureException e) {
            logger.severe("NoCombinationForFailureException: " + e.getMessage());
        } catch (UnsupportedElementInFailureException e) {
            logger.severe("UnsupportedElementInFailureException: " + e.getMessage());
        } catch (InstantiationException e) {
            logger.severe("InstantiationException: " + e.getMessage());
        } catch (IllegalAccessException e) {
            logger.severe("IllegalAccessException: " + e.getMessage());
        }
    }

    /**
     * This method implements the state machine of the scneario manager
     * 
     * @param sim
     * @throws UnsupportedScenarioStatusException 
     * @throws NoCombinationForFailureException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws UnsupportedElementInFailureException 
     */
    public void stateMachine(ShanksSimulation sim) throws UnsupportedScenarioStatusException, NoCombinationForFailureException, UnsupportedElementInFailureException, InstantiationException, IllegalAccessException {
        logger.fine("Using default state machine for ScenarioManager");
        switch (this.simulationStateMachineStatus) {
        case CHECK_FAILURES:
            this.checkFailures(sim);
            this.simulationStateMachineStatus = GENERATE_FAILURES;
            break;
        case GENERATE_FAILURES:
            this.generateFailures(sim);
            this.simulationStateMachineStatus = CHECK_FAILURES;
            break;
        }
        long step = sim.getSchedule().getSteps();
        if (step%500==0) {
            logger.info("In step " + step + ", there are " + sim.getScenario().getCurrentFailures().size() + " current failures.");
            logger.info("In step " + step + ", there are " + sim.getNumOfResolvedFailures() + " resolved failures.");
        }
    }

    /**
     * @param sim
     * @throws UnsupportedScenarioStatusException 
     * @throws NoCombinationForFailureException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws UnsupportedElementInFailureException 
     */
    private void generateFailures(ShanksSimulation sim) throws UnsupportedScenarioStatusException, NoCombinationForFailureException, UnsupportedElementInFailureException, InstantiationException, IllegalAccessException {
        this.scenario.generateFailures();
    }

    /**
     * @param sim
     */
    private void checkFailures(ShanksSimulation sim) {
        List<Failure> resolved = this.scenario.checkResolvedFailures();
        sim.setNumOfResolvedFailures(sim.getNumOfResolvedFailures()
                + resolved.size());
    }
}