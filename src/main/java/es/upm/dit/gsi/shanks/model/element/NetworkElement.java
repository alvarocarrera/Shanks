/** es.upm.dit.gsi.shanks
 11/01/2012

 */
package es.upm.dit.gsi.shanks.model.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import es.upm.dit.gsi.shanks.model.element.exception.UnsupportedNetworkElementStatusException;


/**
 * @author a.carrera
 *
 */
public abstract class NetworkElement {

    Logger logger = Logger.getLogger(NetworkElement.class.getName());
    
    private String id;
    private List<String> possibleStates;
    private String currentStatus;
    private HashMap<String, Object> properties;
    
    /**
     * @param id
     * @throws UnsupportedNetworkElementStatusException 
     */
    public NetworkElement(String id, String initialStatus) throws UnsupportedNetworkElementStatusException {
        this.id = id;
        this.properties = new HashMap<String, Object>();
        this.possibleStates = new ArrayList<String>();
        
        this.setPossibleStates();
        this.setCurrentStatus(initialStatus);
    }


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
     * @param desiredStatus the currentStatus to set
     * @return true if the status was set correctly and false if the status is not a possible status of the network element
     * @throws UnsupportedNetworkElementStatusException 
     */
    public boolean setCurrentStatus(String desiredStatus) throws UnsupportedNetworkElementStatusException {
        if (this.isPossibleStatus(desiredStatus)) {
            this.currentStatus = desiredStatus;
            logger.fine("Network Element Status changed -> ElementID: " + this.getID() + " Current Status: " + desiredStatus);
            return true;
        } else {
            logger.warning("Impossible to set status: " + desiredStatus + ". This network element " + this.getID() + "does not support this status.");
            throw new UnsupportedNetworkElementStatusException();
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
     * @return the properties
     */
    public HashMap<String, Object> getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(HashMap<String, Object> properties) {
        this.properties = properties;
    }
    
    /**
     * @param propertyName
     * @param propertyValue
     */
    public void addProperty(String propertyName, Object propertyValue) {
        this.properties.put(propertyName, propertyValue);
    }
    
    /**
     * @param propertyName
     * @return property value
     */
    public Object getProperty(String propertyName) {
        return this.properties.get(propertyName);
    }


    /**
     * This method only returns a copy of the list, if you modify the copy, the internal list of PossibleStates will be not modified.
     * To interact with the real list, use "addPossibleStatus" and "removePossibleStatus" methods
     * 
     * @return a copy of possibleStates list
     */
    public List<String> getPossibleStates() {
        List<String> copy = new ArrayList<String>();
        for (String possibleStatus : possibleStates) {
            copy.add(possibleStatus);
        }
        return copy;
    }


    /**
     *
     */
    abstract public void setPossibleStates();
    
    /**
     * @param possibleStatus
     */
    public void addPossibleStatus(String possibleStatus) {
        this.possibleStates.add(possibleStatus);
    }
    
    /**
     * @param possibleStatus
     */
    public void removePossibleStatus(String possibleStatus) {
        this.possibleStates.remove(possibleStatus);
    }

}
