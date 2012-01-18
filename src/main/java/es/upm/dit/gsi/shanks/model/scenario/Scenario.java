package es.upm.dit.gsi.shanks.model.scenario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import ec.util.MersenneTwisterFast;
import es.upm.dit.gsi.shanks.model.element.NetworkElement;
import es.upm.dit.gsi.shanks.model.element.exception.TooManyConnectionException;
import es.upm.dit.gsi.shanks.model.element.exception.UnsupportedNetworkElementStatusException;
import es.upm.dit.gsi.shanks.model.failure.Failure;
import es.upm.dit.gsi.shanks.model.failure.exception.NoCombinationForFailureException;
import es.upm.dit.gsi.shanks.model.failure.exception.UnsupportedElementInFailureException;
import es.upm.dit.gsi.shanks.model.scenario.exception.DuplicatedIDException;
import es.upm.dit.gsi.shanks.model.scenario.exception.UnsupportedScenarioStatusException;
import es.upm.dit.gsi.shanks.model.scenario.portrayal.ScenarioPortrayal;

/**
 * Scenarios class
 * 
 * This class create the different scenarios
 * 
 * @author Daniel Lara
 * @author a.carrera
 * @version 0.1.1
 * 
 */
/**
 * @author a.carrera
 * 
 */
public abstract class Scenario {

    Logger logger = Logger.getLogger(Scenario.class.getName());

    private String id;
    private List<String> possibleStates;
    private String currentStatus;
    private HashMap<String, NetworkElement> currentElements;
    private HashMap<Failure, Integer> currentFailures;
    private HashMap<Class<? extends Failure>, List<Set<NetworkElement>>> possibleFailures;
    private HashMap<Class<? extends Failure>, List<Integer>> generatedFailureConfigurations;

    /**
     * @param type
     * @throws UnsupportedNetworkElementStatusException
     * @throws TooManyConnectionException
     * @throws UnsupportedScenarioStatusException
     * @throws DuplicatedIDException
     */
    public Scenario(String id, String initialState)
            throws UnsupportedNetworkElementStatusException,
            TooManyConnectionException, UnsupportedScenarioStatusException,
            DuplicatedIDException {
        this.id = id;
        this.possibleStates = new ArrayList<String>();
        this.currentElements = new HashMap<String, NetworkElement>();
        this.currentFailures = new HashMap<Failure, Integer>();
        this.possibleFailures = new HashMap<Class<? extends Failure>, List<Set<NetworkElement>>>();
        this.generatedFailureConfigurations = new HashMap<Class<? extends Failure>, List<Integer>>();

        this.setPossibleStates();
        this.addNetworkElements();
        this.addPossibleFailures();

        this.setCurrentStatus(initialState);
        
        logger.info("Scenario " + this.getID() + " successfully created.");
    }

    /**
     * Create the scenario portrayal (2D o 3D).
     *
     * @return
     */
    abstract public ScenarioPortrayal createScenarioPortrayal();

    /**
     * @return the id
     */
    public String getID() {
        return id;
    }

    /**
     * @return the currentStatus
     */
    public String getCurrentStatus() {
        return currentStatus;
    }

    /**
     * @param desiredStatus
     *            the currentStatus to set
     * @return true if the status was set correctly and false if the status is
     *         not a possible status of the network element
     * @throws UnsupportedNetworkElementStatusException
     */
    public boolean setCurrentStatus(String desiredStatus)
            throws UnsupportedScenarioStatusException {
        if (this.isPossibleStatus(desiredStatus)) {
            this.currentStatus = desiredStatus;
            return true;
        } else {
            logger.warning("Impossible to set status: " + desiredStatus
                    + ". This network element " + this.getID()
                    + "does not support this status.");
            throw new UnsupportedScenarioStatusException();
        }
    }

    /**
     * @param possibleStatus
     * @return
     */
    private boolean isPossibleStatus(String possibleStatus) {
        if (this.possibleStates.contains(possibleStatus)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param possibleStatus
     */
    public void addPossibleStatus(String possibleStatus) {
        if (!this.possibleStates.contains(possibleStatus)) {
            this.possibleStates.add(possibleStatus);
            logger.fine("Status: " + possibleStatus
                    + " was added as possible status to Scenario "
                    + this.getID());
        } else {
            logger.fine("Status: " + possibleStatus
                    + " was already added as possible status to Scenario "
                    + this.getID());
        }
    }

    /**
     * @param possibleStatus
     */
    public void removePossibleStatus(String possibleStatus) {
        if (this.possibleStates.contains(possibleStatus)) {
            this.possibleStates.remove(possibleStatus);
            logger.fine("Status: " + possibleStatus
                    + " was removed as possible status to Scenario "
                    + this.getID());
        } else {
            logger.fine("Status: " + possibleStatus
                    + " was not removed as possible status to Scenario "
                    + this.getID()
                    + " because it was not a possible status previously.");
        }
    }

    /**
     * @param element
     * @throws DuplicatedIDException
     */
    public void addNetworkElement(NetworkElement element)
            throws DuplicatedIDException {
        if (!this.currentElements.containsKey(element.getID())) {
            this.currentElements.put(element.getID(), element);
        } else {
            throw new DuplicatedIDException(element);
        }
    }

    /**
     * @param element
     */
    public void removeNetworkElement(NetworkElement element) {
        this.currentElements.remove(element.getID());
    }

    /**
     * @return
     */
    public HashMap<String, NetworkElement> getCurrentElements() {
        return this.currentElements;
    }

    /**
     * 
     * 
     * @param failure
     *            Failure to add
     * @param configuration
     *            Configuration of the failure
     */
    public void addFailure(Failure failure, int configuration) {
        if (this.possibleFailures.containsKey(failure.getClass())) {
            this.currentFailures.put(failure, configuration);
        } else {
            logger.warning("Failure was not added, because this scenario does not support this type of Failure. Failure type: "
                    + failure.getClass().getName());
        }
    }

    /**
     * @param failure
     */
    public void removeFailure(Failure failure) {
        this.currentFailures.remove(failure);
    }

    /**
     * @return
     */
    public Set<Failure> getCurrentFailures() {
        return this.currentFailures.keySet();
    }

    /**
     * @param failure
     * @param possibleCombinations
     */
    public void addPossibleFailure(Class<? extends Failure> failure,
            List<Set<NetworkElement>> possibleCombinations) {
        this.possibleFailures.put(failure, possibleCombinations);
    }

    /**
     * @param failure
     * @param set
     */
    public void addPossibleFailure(Class<? extends Failure> failure,
            Set<NetworkElement> set) {
        List<Set<NetworkElement>> list = new ArrayList<Set<NetworkElement>>();
        list.add(set);
        this.possibleFailures.put(failure, list);
    }

    /**
     * @param failure
     * @param element
     */
    public void addPossibleFailure(Class<? extends Failure> failure,
            NetworkElement element) {
        List<Set<NetworkElement>> list = new ArrayList<Set<NetworkElement>>();
        Set<NetworkElement> set = new HashSet<NetworkElement>();
        set.add(element);
        list.add(set);
        this.possibleFailures.put(failure, list);
    }

    /**
     * @param failureType
     */
    public void removePossibleFailure(Class<Failure> failureType) {
        this.possibleFailures.remove(failureType);
    }

    /**
     * @return
     */
    public HashMap<Class<? extends Failure>, List<Set<NetworkElement>>> getPossibleFailures() {
        return this.possibleFailures;
    }

    /**
     *
     */
    abstract public void setPossibleStates();

    /**
     * @throws UnsupportedNetworkElementStatusException
     * @throws TooManyConnectionException
     * @throws DuplicatedIDException
     * 
     */
    abstract public void addNetworkElements()
            throws UnsupportedNetworkElementStatusException,
            TooManyConnectionException, DuplicatedIDException;

    /**
     * 
     */
    abstract public void addPossibleFailures();

    /**
     * 
     * Algorithm used to generate failures during the simulation
     * 
     * @throws UnsupportedScenarioStatusException
     * @throws NoCombinationForFailureException
     * @throws UnsupportedElementInFailureException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * 
     */
    public void generateFailures() throws UnsupportedScenarioStatusException,
            NoCombinationForFailureException,
            UnsupportedElementInFailureException, InstantiationException,
            IllegalAccessException {
        MersenneTwisterFast randomizer = new MersenneTwisterFast();
        String status = this.getCurrentStatus();
        HashMap<Class<? extends Failure>, Double> penalties = this
                .getPenaltiesInStatus(status);
        Iterator<Class<? extends Failure>> it = this.getPossibleFailures()
                .keySet().iterator();
        while (it.hasNext()) {
            Class<? extends Failure> type = it.next();
            double penalty = 0;
            double prob = 0;
            try {
                Failure failure = type.newInstance();
                List<Set<NetworkElement>> list = this.getPossibleFailures()
                        .get(type);
                int numberOfCombinations = list.size();
                int combinationNumber = randomizer.nextInt(numberOfCombinations);
                try {
                    // Apply penalty
                    penalty = penalties.get(type);
                    if (penalty > 0) {
                        prob = failure.getOccurrenceProbability()
                                * numberOfCombinations * penalty;
                    } else {
                        prob = -1.0; // Impossible failure
                    }
                } catch (Exception e) {
                    logger.fine("There is no penalty for failures: "
                            + type.getName() + " in status " + status);
                }
                if (randomizer.nextDouble() < prob) {
                    // Generate failure
                    Set<NetworkElement> elementsSet;
                    if (numberOfCombinations >= 1) {
                        elementsSet = list.get(combinationNumber);
                        this.setupFailure(failure, elementsSet,
                                combinationNumber);
                    } else if (this.generatedFailureConfigurations.get(type).size() == 0) {
                        throw new NoCombinationForFailureException(failure);
                    }
                }
            } catch (NoCombinationForFailureException e) {
                throw e;
            } catch (UnsupportedElementInFailureException e) {
                logger.severe("Impossible to instance failure: "
                        + type.getName()
                        + ". All failures must have a default constructor that calls other constructor Failure(String id, double occurrenceProbability)");
                logger.severe("Exception: " + e.getMessage());
                throw e;
            }
        }
    }

    /**
     * @param failure
     * @param elementsSet
     * @throws UnsupportedElementInFailureException
     */
    private void setupFailure(Failure failure, Set<NetworkElement> elementsSet,
            int configurationNumber)
            throws UnsupportedElementInFailureException {
        for (NetworkElement element : elementsSet) {
            String statusToSet = failure.getPossibleAffectedElements().get(
                    element.getClass());
            failure.addAffectedElement(element, statusToSet);
        }
        if (!this.generatedFailureConfigurations.containsKey(failure.getClass())) {
            this.generatedFailureConfigurations.put(failure.getClass(),
                    new ArrayList<Integer>());
        }
        List<Integer> numList = this.generatedFailureConfigurations.get(failure.getClass());
        if (!numList.contains(configurationNumber)) {
            numList.add(configurationNumber);
            this.generatedFailureConfigurations.put(failure.getClass(), numList);
            failure.activateFailure();
            this.addFailure(failure, configurationNumber);
            logger.fine("Generated Failure " + failure.getID() + " with configuration " + configurationNumber);
        }

    }

    /**
     * This method can return multipliers >1.0 (more probable failures) or
     * <1.0(less probable failure). CAUTION: If the multiplier is <=0.0, the
     * failure never happends.
     * 
     * @param status
     * @return Multiplier for each type of failure
     * @throws UnsupportedScenarioStatusException
     */
    abstract public HashMap<Class<? extends Failure>, Double> getPenaltiesInStatus(
            String status) throws UnsupportedScenarioStatusException;

    /**
     * @return resolved failures
     */
    public List<Failure> checkResolvedFailures() {
        List<Failure> resolvedFailures = new ArrayList<Failure>();
        for (Failure failure : this.currentFailures.keySet()) {
            if (failure.isResolved()) {
                resolvedFailures.add(failure);
                List<Integer> numList = this.generatedFailureConfigurations.get(failure
                        .getClass());
                Integer conf = this.currentFailures.get(failure);
                numList.remove((Integer) this.currentFailures.get(failure));
                this.generatedFailureConfigurations.put(failure.getClass(), numList);
                logger.fine("Resolved failure " + failure.getID() + ". Failure class: " + failure.getClass().getName() + " with configuration " + conf);
            }
        }
        for (Failure resolved : resolvedFailures) {
            this.currentFailures.remove(resolved);
        }
        return resolvedFailures;
    }

    /**
     * Return the network element with these id
     * 
     * @param id
     * @return
     */
    public NetworkElement getNetworkElement(String id) {
        return this.currentElements.get(id);
    }

}
