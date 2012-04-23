package es.upm.dit.gsi.shanks.notification.util.test;

import java.util.HashMap;
import java.util.Properties;

import es.upm.dit.gsi.shanks.model.element.exception.TooManyConnectionException;
import es.upm.dit.gsi.shanks.model.element.exception.UnsupportedNetworkElementStatusException;
import es.upm.dit.gsi.shanks.model.failure.Failure;
import es.upm.dit.gsi.shanks.model.scenario.Scenario;
import es.upm.dit.gsi.shanks.model.scenario.exception.DuplicatedIDException;
import es.upm.dit.gsi.shanks.model.scenario.exception.ScenarioNotFoundException;
import es.upm.dit.gsi.shanks.model.scenario.exception.UnsupportedScenarioStatusException;
import es.upm.dit.gsi.shanks.model.scenario.portrayal.Scenario2DPortrayal;
import es.upm.dit.gsi.shanks.model.scenario.portrayal.Scenario3DPortrayal;
import es.upm.dit.gsi.shanks.model.scenario.portrayal.exception.DuplicatedPortrayalIDException;

public class TestScenario extends Scenario{

    public static final String TEST_STATE = "sunny";

    public TestScenario(String id, String initialState, Properties properties)
            throws UnsupportedNetworkElementStatusException,
            TooManyConnectionException, UnsupportedScenarioStatusException,
            DuplicatedIDException {
        super(id, initialState, properties);
    }

    @Override
    public Scenario2DPortrayal createScenario2DPortrayal()
            throws DuplicatedPortrayalIDException, ScenarioNotFoundException {
        return null;
    }

    @Override
    public Scenario3DPortrayal createScenario3DPortrayal()
            throws DuplicatedPortrayalIDException, ScenarioNotFoundException {
        return null;
    }

    @Override
    public void setPossibleStates() {
        this.addPossibleStatus(TestScenario.TEST_STATE);
    }

    @Override
    public void addNetworkElements()
            throws UnsupportedNetworkElementStatusException,
            TooManyConnectionException, DuplicatedIDException {
    }

    @Override
    public void addPossibleFailures() {
    }

    @Override
    public void addPossibleEvents() {
        try {
            
            TestDevice neperd = new TestDevice(TestDefinitions.DEVICE_ID+"NEPerE", null, false);
            this.addPossibleEventsOfNE(TestPeriodicNetworkElementEvent.class, neperd);
            
            TestDevice neprod = new TestDevice(TestDefinitions.DEVICE_ID+"NEProE", null, false);
            this.addPossibleEventsOfNE(TestProbabilisticNetworkElementEvent.class, neprod);
            
            this.addPossibleEventsOfScenario(TestProbabilisticScenarioEvent.class, this);
            this.addPossibleEventsOfScenario(TestPeriodicScenarioEvent.class, this);
            
        } catch (UnsupportedNetworkElementStatusException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public HashMap<Class<? extends Failure>, Double> getPenaltiesInStatus(
            String status) throws UnsupportedScenarioStatusException {
        return null;
    }
}
