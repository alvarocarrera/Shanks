/**
 * 
 */
package es.upm.dit.gsi.shanks.model.failure.ftth;

import es.upm.dit.gsi.shanks.model.failure.ConfigurationFailure;
import es.upm.dit.gsi.shanks.model.scenario.ScenarioDefinitions;

/**
 * @author a.carrera
 * 
 */
public class ONTFailure extends ConfigurationFailure {
    // LOOK una clase como esta es la que deben implementar para cada fallo
    /**
	 * 
	 */
    private static final long serialVersionUID = 7899469300086259485L;

    /**
     * @param type
     */
    public ONTFailure() {
        super(ScenarioDefinitions.FAILURRE_CONFIGURATION_ONT);
    }

}
