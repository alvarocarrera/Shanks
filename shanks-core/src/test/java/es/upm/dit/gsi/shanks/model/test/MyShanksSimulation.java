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
package es.upm.dit.gsi.shanks.model.test;

import jason.JasonException;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import sim.engine.Schedule;
import sim.engine.Steppable;
import es.upm.dit.gsi.shanks.ShanksSimulation;
import es.upm.dit.gsi.shanks.agent.exception.DuplicatedActionIDException;
import es.upm.dit.gsi.shanks.agent.exception.DuplicatedAgentIDException;
import es.upm.dit.gsi.shanks.agent.test.MyFixAgent;
import es.upm.dit.gsi.shanks.agent.test.MyJasonShanksAgent;
import es.upm.dit.gsi.shanks.agent.test.MySimpleShanksAgent;
import es.upm.dit.gsi.shanks.exception.ShanksException;
import es.upm.dit.gsi.shanks.model.element.exception.TooManyConnectionException;
import es.upm.dit.gsi.shanks.model.element.exception.UnsupportedNetworkElementFieldException;
import es.upm.dit.gsi.shanks.model.scenario.Scenario;
import es.upm.dit.gsi.shanks.model.scenario.exception.DuplicatedIDException;
import es.upm.dit.gsi.shanks.model.scenario.exception.ScenarioNotFoundException;
import es.upm.dit.gsi.shanks.model.scenario.exception.UnsupportedScenarioStatusException;
import es.upm.dit.gsi.shanks.model.scenario.portrayal.exception.DuplicatedPortrayalIDException;
import es.upm.dit.gsi.shanks.model.test.steppable.FailureLog;
import es.upm.dit.gsi.shanks.model.test.steppable.FailuresChartPainter;
import es.upm.dit.gsi.shanks.model.test.steppable.FailuresGUI;

/**
 * @author a.carrera
 * 
 */
public class MyShanksSimulation extends ShanksSimulation {

    private static final long serialVersionUID = 1778288778609950190L;
    private Properties configuration;

    public static final String CONFIGURATION = "Configuration";
    
    public int counter = 0;

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
     * @throws UnsupportedNetworkElementFieldException
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
            throws ShanksException {
        super(seed, scenarioClass, scenarioID, initialState, properties);
        this.configuration = configPropertiesMyShanksSimulation;
        this.counter++;
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
            Steppable steppable = new FailureLog();
            schedule.scheduleRepeating(Schedule.EPOCH, 3, steppable, 500);
            break;
        case 2:
            Steppable failuresgui = new FailuresGUI();
            schedule.scheduleRepeating(Schedule.EPOCH, 4, failuresgui, 1);
            break;
        case 3:
            Steppable chart = new FailuresChartPainter();
            schedule.scheduleRepeating(Schedule.EPOCH, 3, chart, 50);
            Steppable failures = new FailuresGUI();
            schedule.scheduleRepeating(Schedule.EPOCH, 4, failures, 1);
            break;
        default:
            logger.info("No configuration for MyShanksSimulation. Configuration 0 loaded -> default");
        }

    }

    @Override
    public void registerShanksAgents() throws ShanksException {
        MyJasonShanksAgent agent = new MyJasonShanksAgent("resolverAgent1",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent1.asl", this.getLogger());
        this.registerShanksAgent(agent);
        MyJasonShanksAgent agent2 = new MyJasonShanksAgent("resolverAgent2",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent2.asl", this.getLogger());
        this.registerShanksAgent(agent2);
        MyJasonShanksAgent agent3 = new MyJasonShanksAgent("resolverAgent3",
                "src/test/java/es/upm/dit/gsi/shanks/agent/test/MyShanksAgent3.asl", this.getLogger());
        this.registerShanksAgent(agent3);
        MySimpleShanksAgent agent4 = new MySimpleShanksAgent("simpleAgent1", 5, 10, this.getLogger());
        this.registerShanksAgent(agent4);
        MySimpleShanksAgent agent5 = new MySimpleShanksAgent("toBeRemovedAgent", 5, 10, this.getLogger());
        this.registerShanksAgent(agent5);
        MyFixAgent agent6 = new MyFixAgent("FixAgent", this.getLogger());
        this.registerShanksAgent(agent6);
    }

    
    public int getCounter(){
        return this.counter;
    }
    
    public void increaseCounter(){
        this.counter++;
    }
}