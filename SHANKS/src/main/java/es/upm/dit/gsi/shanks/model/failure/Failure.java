package es.upm.dit.gsi.shanks.model.failure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import sim.portrayal.DrawInfo2D;
import sim.portrayal.SimplePortrayal2D;

/**
 * DeviceErrors class
 * 
 * Make the possible erros of the devices
 * 
 * @author Daniel Lara
 * @author a.carrera
 * @version 0.1.1
 * 
 */

public abstract class Failure extends SimplePortrayal2D {

    /** DeviceErrors parametres */
    private static final long serialVersionUID = -5684572432145540188L;
    public String type;
    public boolean status;

    public static List<Failure> deverrors = new ArrayList<Failure>();

    /**
     * Constructor of the class
     * 
     * @param name
     *            The type of the error
     */
    public Failure(String type) {
        this.type = type;
        this.status = false;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Used to activate a failure
     * 
     */
    public void activateFailure() {
        this.status = true;
    }

    /**
     * Used to deactivate a failure
     * 
     */
    public void deactivateFailure() {
        this.status = false;
    }

    public boolean getStatus() {
        return status;
    }

    @Override
    public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {

        final double width = 20;
        final double height = 20;

        // Draw the devices
        final int x = (int) (info.draw.x - width / 2.0);
        final int y = (int) (info.draw.y - height / 2.0);

        // Draw the devices ID ID
        graphics.setColor(Color.black);
        graphics.drawString("Problem generated ----> " + this.getType(), x - 3,
                y);
    }

    // TODO Aqu� antes se generaban todos los errores, ahora se deben generar en
    // otra clase

}
