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
        this.fillIntialProperties();
        this.setCurrentStatus(initialStatus);
    }


    /**
     * @return the id
     */
    public String getID() {
        return id;
    }


    /**
     * @param id
     */
    public void setID(String id) {
        this.id = id;
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
            this.checkProperties();
            logger.fine("Network Element Status changed -> ElementID: " + this.getID() + " Current Status: " + desiredStatus);
            return true;
        } else {
            logger.warning("Impossible to set status: " + desiredStatus + ". This network element " + this.getID() + "does not support this status.");
            throw new UnsupportedNetworkElementStatusException();
        }
    }
    
    /**
     * @param desiredStatus the currentStatus to set
     * @return true if the status was set correctly and false if the status is not a possible status of the network element
     * @throws UnsupportedNetworkElementStatusException 
     */
    public boolean updateStatusTo(String desiredStatus) throws UnsupportedNetworkElementStatusException {
        if (this.isPossibleStatus(desiredStatus)) {
            this.currentStatus = desiredStatus;
            logger.fine("Network Element Status updated -> ElementID: " + this.getID() + " Current Status: " + desiredStatus);
            return true;
        } else {
            logger.warning("Impossible to update status: " + desiredStatus + ". This network element " + this.getID() + "does not support this status.");
            throw new UnsupportedNetworkElementStatusException();
        }
    }


    /**
     * Set the initial properties of the network element
     */
    abstract public void fillIntialProperties();

    /**
     * This method check properties and change them depending on the current status
     * @throws UnsupportedNetworkElementStatusException 
     */
    abstract public void checkProperties() throws UnsupportedNetworkElementStatusException;


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
     * If the property exists, this method changes it. If not, this method creates it.
     * 
     * @param propertyName
     * @param propertyValue
     * @throws UnsupportedNetworkElementStatusException 
     */
    public void changeProperty(String propertyName, Object propertyValue) throws UnsupportedNetworkElementStatusException {
        this.properties.put(propertyName, propertyValue);
        this.checkStatus();
    }
    
    /**
     * This method check status and change them depending on the current properties
     * This method must use updateStatusTo(String desiredStatus) method to change element status.
     * WARNING: Do not use setCurrentStatus(String desiredStatus) to avoid an infinitive loop
     * @throws UnsupportedNetworkElementStatusException 
     */
    abstract public void checkStatus() throws UnsupportedNetworkElementStatusException;


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
