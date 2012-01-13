package es.upm.dit.gsi.shanks.model.element.link;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.upm.dit.gsi.shanks.model.element.NetworkElement;
import es.upm.dit.gsi.shanks.model.element.device.Device;

/**
 * Link class
 * 
 * This class represents the links than connect the different devices
 * 
 * @author Daniel Lara
 * @author a.carrera
 * @version 0.2
 * 
 */

public class Link extends NetworkElement {
    
    private Logger logger = Logger.getLogger(Link.class.getName());
    
    private List<Device> linkedDevices;
    private int deviceCapacity;

    /**
     * @param id
     * @param capacity
     */
    public Link(String id, int capacity) {
        super(id);
        this.deviceCapacity = capacity;
        this.linkedDevices = new ArrayList<Device>();
    }

    /**
     * @return
     */
    public List<Device> getLinkedDevices() {
        return linkedDevices;
    }

    /**
     * @param device
     * @return
     */
    public boolean connectDevice(Device device) {
        if (this.linkedDevices.size()<deviceCapacity) {
            this.linkedDevices.add(device);
            logger.fine("Link " + this.getID() + " has Device " + device.getID() + " in its linked device list.");
            return true;
        } else {
            logger.fine("Link " + this.getID() + " is full of its capacity. Device " + device.getID() + " was not included in its linked device list.");
            return false;
        }
    }
    
    /**
     * @param device
     */
    public void disconnectDevice(Device device) {
        this.linkedDevices.remove(device);
    }
    
}
