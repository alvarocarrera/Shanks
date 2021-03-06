/*******************************************************************************
 * Copyright  (C) 2014 Álvaro Carrera Barroso
 * Grupo de Sistemas Inteligentes - Universidad Politecnica de Madrid
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
/** es.upm.dit.gsi.shanks
 20/12/2011

 */
package es.upm.dit.gsi.shanks.model.scenario;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import es.upm.dit.gsi.shanks.ShanksSimulation;
import es.upm.dit.gsi.shanks.exception.ShanksException;
import es.upm.dit.gsi.shanks.model.element.NetworkElement;
import es.upm.dit.gsi.shanks.model.element.device.Device;
import es.upm.dit.gsi.shanks.model.element.exception.TooManyConnectionException;
import es.upm.dit.gsi.shanks.model.element.exception.UnsupportedNetworkElementFieldException;
import es.upm.dit.gsi.shanks.model.element.link.Link;
import es.upm.dit.gsi.shanks.model.event.Event;
import es.upm.dit.gsi.shanks.model.event.failure.Failure;
import es.upm.dit.gsi.shanks.model.scenario.exception.AlreadyConnectedScenarioException;
import es.upm.dit.gsi.shanks.model.scenario.exception.DuplicatedIDException;
import es.upm.dit.gsi.shanks.model.scenario.exception.NonGatewayDeviceException;
import es.upm.dit.gsi.shanks.model.scenario.exception.ScenarioNotFoundException;
import es.upm.dit.gsi.shanks.model.scenario.exception.UnsupportedScenarioStatusException;

/**
 * @author a.carrera
 * 
 */
public abstract class ComplexScenario extends Scenario {

    private HashMap<Scenario, List<Link>> scenarios;

    /**
     * Create a complex scenario to compose the whole scenario adding other
     * scenarios.
     * 
     * @param type
     * @param initialState
     * @param properties
     * @param logger
     * @throws ShanksException
     */
    public ComplexScenario(String type, String initialState,
            Properties properties, Logger logger) throws ShanksException {

        // throws UnsupportedNetworkElementFieldException,
        // TooManyConnectionException, UnsupportedScenarioStatusException,
        // DuplicatedIDException, NonGatewayDeviceException,
        // AlreadyConnectedScenarioException, SecurityException,
        // IllegalArgumentException, NoSuchMethodException,
        // InstantiationException, IllegalAccessException,
        // InvocationTargetException {
        super(type, initialState, properties, logger);
        this.scenarios = new HashMap<Scenario, List<Link>>();

        this.addScenarios();
        this.addPossiblesFailuresComplex();
        this.addPossiblesEventsComplex();
    }

    /**
     * Add all scenarios to the simulation using addScenario method
     * 
     * @throws ShanksException
     */
    abstract public void addScenarios() throws ShanksException;

    // throws UnsupportedNetworkElementFieldException,
    // TooManyConnectionException, UnsupportedScenarioStatusException,
    // DuplicatedIDException, NonGatewayDeviceException,
    // AlreadyConnectedScenarioException, SecurityException,
    // IllegalArgumentException, NoSuchMethodException, InstantiationException,
    // IllegalAccessException, InvocationTargetException;

    /**
     * Add the scenario to the complex scenario.
     * 
     * @param scenarioClass
     * @param scenarioID
     * @param initialState
     * @param properties
     * @param gatewayDeviceID
     * @param externalLinkID
     * @throws ShanksException
     */
    public void addScenario(Class<? extends Scenario> scenarioClass,
            String scenarioID, String initialState, Properties properties,
            String gatewayDeviceID, String externalLinkID)
            throws ShanksException {
        // throws NonGatewayDeviceException, TooManyConnectionException,
        // DuplicatedIDException, AlreadyConnectedScenarioException,
        // SecurityException, NoSuchMethodException, IllegalArgumentException,
        // InstantiationException, IllegalAccessException,
        // InvocationTargetException {
        Constructor<? extends Scenario> c;
        Scenario scenario;
        try {
            c = scenarioClass.getConstructor(new Class[] { String.class,
                    String.class, Properties.class, Logger.class });
            scenario = c.newInstance(scenarioID, initialState, properties, this.getLogger());
        } catch (SecurityException e) {
            throw new ShanksException(e);
        } catch (NoSuchMethodException e) {
            throw new ShanksException(e);
        } catch (IllegalArgumentException e) {
            throw new ShanksException(e);
        } catch (InstantiationException e) {
            throw new ShanksException(e);
        } catch (IllegalAccessException e) {
            throw new ShanksException(e);
        } catch (InvocationTargetException e) {
            throw new ShanksException(e);
        }

        Device gateway = (Device) scenario.getNetworkElement(gatewayDeviceID);
        Link externalLink = (Link) this.getNetworkElement(externalLinkID);
        if (!scenario.getCurrentElements().containsKey(gatewayDeviceID)) {
            throw new NonGatewayDeviceException(scenario, gateway, externalLink);
        } else {
            gateway.connectToLink(externalLink);
            if (!this.getCurrentElements().containsKey(externalLink.getID())) {
                this.addNetworkElement(externalLink);
            }
            if (!this.scenarios.containsKey(scenario)) {
                this.scenarios.put(scenario, new ArrayList<Link>());
            } else {
                List<Link> externalLinks = this.scenarios.get(scenario);
                if (!externalLinks.contains(externalLink)) {
                    externalLinks.add(externalLink);
                } else if (externalLink.getLinkedDevices().contains(gateway)) {
                    throw new AlreadyConnectedScenarioException(scenario,
                            gateway, externalLink);
                }
            }
        }
    }

    /**
     * @return set of the scenarios that are in the complex scenario
     */
    public Set<Scenario> getScenarios() {
        return this.scenarios.keySet();
    }

    /**
     * @param scenarioID
     * @return
     * @throws ShanksException
     */
    public Scenario getScenario(String scenarioID) throws ShanksException {
        for (Scenario s : this.scenarios.keySet()) {
            if (s.getID().equals(scenarioID)) {
                return s;
            }
        }
        throw new ScenarioNotFoundException(scenarioID, this.getID());
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.upm.dit.gsi.shanks.model.scenario.Scenario#generateFailures()
     */
    public void generateNetworkElementsEvents(ShanksSimulation sim)
            throws ShanksException {
        super.generateNetworkElementEvents(sim);
        for (Scenario scenario : this.scenarios.keySet()) {
            scenario.generateNetworkElementEvents(sim);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * es.upm.dit.gsi.shanks.model.scenario.Scenario#checkResolvedFailures()
     */
    public List<Failure> checkResolvedFailures() {
        List<Failure> fullList = new ArrayList<Failure>();

        fullList.addAll(super.checkResolvedFailures());

        for (Scenario scenario : this.scenarios.keySet()) {
            fullList.addAll(scenario.checkResolvedFailures());
        }

        return fullList;
    }

    /**
     * @return All failures classified by scenario of the complex scenario
     */
    public HashMap<Scenario, Set<Failure>> getCurrentFailuresByScenario() {
        HashMap<Scenario, Set<Failure>> maps = new HashMap<Scenario, Set<Failure>>();
        maps.put(this, this.getCurrentFailures());
        for (Scenario s : this.getScenarios()) {
            maps.put(s, s.getCurrentFailures());
        }

        return maps;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.upm.dit.gsi.shanks.model.scenario.Scenario#getCurrentFailures()
     */
    public Set<Failure> getCurrentFailures() {
        Set<Failure> failures = new HashSet<Failure>();
        failures.addAll(this.getFullCurrentFailures().keySet());
        for (Scenario s : this.getScenarios()) {
            failures.addAll(s.getCurrentFailures());
        }
        return failures;
    }

    /**
     * @return All elements classified by scenario of the complex scenario
     */
    public HashMap<Scenario, HashMap<String, NetworkElement>> getCurrentElementsByScenario() {
        HashMap<Scenario, HashMap<String, NetworkElement>> maps = new HashMap<Scenario, HashMap<String, NetworkElement>>();
        maps.put(this, this.getCurrentElements());
        for (Scenario s : this.getScenarios()) {
            maps.put(s, s.getCurrentElements());
        }

        return maps;
    }

    // Idem que con los eventos y los dupes
    public void addPossiblesFailuresComplex() {
        Set<Scenario> scenarios = this.getScenarios();
        for (Scenario s : scenarios) {
            for (Class<? extends Failure> c : s.getPossibleFailures().keySet()) {
                if (!this.getPossibleFailures().containsKey(c)) {
                    this.addPossibleFailure(c, s.getPossibleFailures().get(c));
                } else {
                    List<Set<NetworkElement>> elements = this
                            .getPossibleFailures().get(c);
                    // elements.addAll(s.getPossibleFailures().get(c));
                    for (Set<NetworkElement> sne : s.getPossibleFailures().get(
                            c)) {
                        if (!elements.contains(sne))
                            elements.add(sne);
                    }
                    this.addPossibleFailure(c, elements);
                }
            }
        }
    }

    // TODO Hacerlo como con los fallos
    // J Anadido eventos de escenario, y evitado la adiccion de duplicados
    public void addPossiblesEventsComplex() {
        Set<Scenario> scenarios = this.getScenarios();
        for (Scenario s : scenarios) {
            for (Class<? extends Event> c : s.getPossibleEventsOfNE().keySet()) {
                if (!this.getPossibleEventsOfNE().containsKey(c)) {
                    this.addPossibleEventsOfNE(c, s.getPossibleEventsOfNE()
                            .get(c));
                } else {
                    List<Set<NetworkElement>> elements = this
                            .getPossibleEventsOfNE().get(c);
                    // elements.addAll(s.getPossibleEventsOfNE().get(c));
                    for (Set<NetworkElement> sne : s.getPossibleEventsOfNE()
                            .get(c)) {
                        if (!elements.contains(sne))
                            elements.add(sne);
                    }
                    this.addPossibleEventsOfNE(c, elements);
                }
            }
            for (Class<? extends Event> c : s.getPossibleEventsOfScenario()
                    .keySet()) {
                if (!this.getPossibleEventsOfScenario().containsKey(c)) {
                    this.addPossibleEventsOfScenario(c, s
                            .getPossibleEventsOfScenario().get(c));
                } else {
                    List<Set<Scenario>> elements = this
                            .getPossibleEventsOfScenario().get(c);
                    // elements.addAll(s.getPossibleEventsOfScenario().get(c));
                    for (Set<Scenario> sne : s.getPossibleEventsOfScenario()
                            .get(c)) {
                        if (!elements.contains(sne))
                            elements.add(sne);
                    }
                    this.addPossibleEventsOfScenario(c, elements);
                }
            }
        }

    }

}
