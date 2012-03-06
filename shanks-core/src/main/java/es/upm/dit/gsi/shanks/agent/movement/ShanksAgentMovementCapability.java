/**
 * 
 */
package es.upm.dit.gsi.shanks.agent.movement;

import sim.field.continuous.Continuous2D;
import sim.field.continuous.Continuous3D;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal3d.continuous.ContinuousPortrayal3D;
import sim.util.Double2D;
import sim.util.Double3D;
import es.upm.dit.gsi.shanks.ShanksSimulation;
import es.upm.dit.gsi.shanks.model.scenario.portrayal.Scenario3DPortrayal;
import es.upm.dit.gsi.shanks.model.scenario.portrayal.ScenarioPortrayal;

/**
 * @author a.carrera
 * 
 */
public class ShanksAgentMovementCapability {

    /**
     * Update the location of the agent in the simulation
     * 
     * @param simulation
     * @param agent
     * @param location
     */
    public static void updateLocation(ShanksSimulation simulation,
            MobileShanksAgent agent, Double3D location) {
        agent.getCurrentLocation().setLocation3D(location);
        ContinuousPortrayal3D devicesPortrayal;
        try {
            devicesPortrayal = (ContinuousPortrayal3D) simulation
                    .getScenarioPortrayal().getPortrayals()
                    .get(Scenario3DPortrayal.MAIN_DISPLAY_ID)
                    .get(ScenarioPortrayal.DEVICES_PORTRAYAL);
            Continuous3D devicesField = (Continuous3D) devicesPortrayal
                    .getField();
            devicesField.setObjectLocation(agent, location);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the location of the agent in the simulation
     * 
     * @param simulation
     * @param agent
     * @param location
     */
    public static void updateLocation(ShanksSimulation simulation,
            MobileShanksAgent agent, Double2D location) {
        agent.getCurrentLocation().setLocation2D(location);
        ContinuousPortrayal2D devicesPortrayal;
        try {
            devicesPortrayal = (ContinuousPortrayal2D) simulation
                    .getScenarioPortrayal().getPortrayals()
                    .get(Scenario3DPortrayal.MAIN_DISPLAY_ID)
                    .get(ScenarioPortrayal.DEVICES_PORTRAYAL);
            Continuous2D devicesField = (Continuous2D) devicesPortrayal
                    .getField();
            devicesField.setObjectLocation(agent, location);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Move the agent to the target location with the specific speed. Call this method always you want to move.
     * This method only moves the agent a fragment equals to the velocity.
     * 
     * @param simulation
     * @param agent
     * @param currentLocation
     * @param targetLocation
     * @param speed
     */
    public static void goTo(ShanksSimulation simulation,
            MobileShanksAgent agent, Double2D currentLocation,
            Double2D targetLocation, double speed) {
        if (!targetLocation.equals(currentLocation) && agent.isAllowedToMove()) {
            Double2D direction = targetLocation.subtract(currentLocation);
            direction = direction.normalize();
            Double2D movement = direction.multiply(speed);
            ShanksAgentMovementCapability.updateLocation(simulation, agent,
                    currentLocation.add(movement));
        }
    }

    /**
     * Move the agent to the target location with the specific speed. Call this method always you want to move.
     * This method only moves the agent a fragment equals to the velocity.
     * 
     * @param simulation
     * @param agent
     * @param currentLocation
     * @param targetLocation
     * @param speed
     */
    public static void goTo(ShanksSimulation simulation,
            MobileShanksAgent agent, Double3D currentLocation,
            Double3D targetLocation, double speed) {
        if (!targetLocation.equals(currentLocation) && agent.isAllowedToMove()) {
            Double3D direction = new Double3D(targetLocation.x
                    - currentLocation.x, targetLocation.y - currentLocation.y,
                    targetLocation.z - currentLocation.z);
            Double3D normalized = ShanksAgentMovementCapability
                    .normalizeDouble3D(direction);
            Double3D movement = new Double3D(normalized.x * speed, normalized.y
                    * speed, normalized.z * speed);
            ShanksAgentMovementCapability.updateLocation(simulation, agent,
                    new Double3D(currentLocation.x + movement.x,
                            currentLocation.y + movement.y, currentLocation.z
                                    + movement.z));
        }
    }

    /**
     * 
     * @param vector
     * @return the normalized vector
     */
    private static Double3D normalizeDouble3D(Double3D vector) {
        double invertedlen = 1.0D / Math.sqrt(vector.x * vector.x + vector.y
                * vector.y + vector.z * vector.z);
        if ((invertedlen == (1.0D / 0.0D)) || (invertedlen == (-1.0D / 0.0D))
                || (invertedlen == 0.0D) || (invertedlen != invertedlen))
            throw new ArithmeticException("Vector length is "
                    + Math.sqrt(vector.x * vector.x + vector.y * vector.y
                            + vector.z * vector.z) + ", cannot normalize");
        return new Double3D(vector.x * invertedlen, vector.y * invertedlen,
                vector.z * invertedlen);
    }

}
