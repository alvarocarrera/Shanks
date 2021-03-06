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
/**
 * 
 */
package es.upm.dit.gsi.shanks.model.event.failure.portrayal;

import java.awt.Color;
import java.awt.Font;

import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

import sim.portrayal3d.SimplePortrayal3D;
import es.upm.dit.gsi.shanks.model.event.failure.Failure;

/**
 * @author a.carrera
 *
 */
@SuppressWarnings("restriction")
public class Failure3DPortrayal extends SimplePortrayal3D {
    //TODO i think this class is non-useful. Need to be eliminated. 

    public TransformGroup getModel(Object object, TransformGroup model) {

        Failure failure = (Failure) object;
//        model = new TransformGroup();

//        Color3f colour;
//        Sphere s = new Sphere(diameter);
//        Cone s = new Cone(diameter, diameter);
//        ColorCube s = new ColorCube(diameter);
        
//        colour = new Color3f(Color.black);

        int FONT_SIZE = 50;
        Font labelFont = new Font("SansSerif", Font.BOLD, FONT_SIZE);
        double labelScale = 1.0;
        double SCALING_MODIFIER = 1.0 / 5.0; 

//        model.removeAllChildren();
//        Appearance appearance = new Appearance();
//        appearance.setColoringAttributes(new ColoringAttributes(colour,
//                ColoringAttributes.SHADE_GOURAUD));
//        Material m = new Material();
//        m.setAmbientColor(colour);
//        m.setEmissiveColor(0f, 0f, 0f);
//        m.setDiffuseColor(colour);
//        m.setSpecularColor(1f, 1f, 1f);
//        m.setShininess(128f);
//        appearance.setMaterial(m);
//        s.setAppearance(appearance);
//        model.addChild(s);
//        model.setName(failure.getID());
//        clearPickableFlags(model);
//        model.setPickable(true);
        
        


        // draw the device labels if the user wants
//        Transform3D offset = new Transform3D();
//        offset.setTranslation(new Vector3d(5+diameter/2,5+diameter/2,5+diameter/2));
//        Transform3D trans = offset;
        String str = failure.getID();
        com.sun.j3d.utils.geometry.Text2D text = new com.sun.j3d.utils.geometry.Text2D(
                str, new Color3f(Color.black), labelFont.getFamily(),
                labelFont.getSize(), labelFont.getStyle());
        text.setRectangleScaleFactor((float) (labelScale * SCALING_MODIFIER));

        // text = new Shape3D(new Text3D(labelFont3D, ""));

        OrientedShape3D o3d = new OrientedShape3D(text.getGeometry(),
                text.getAppearance(), OrientedShape3D.ROTATE_ABOUT_POINT,
                new Point3f(0, 0, 0));
        o3d.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE); // may need to change
                                                           // the appearance
                                                           // (see below)
        o3d.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE); // may need to change
                                                         // the geometry (see
                                                         // below)
        o3d.clearCapabilityIsFrequent(Shape3D.ALLOW_APPEARANCE_WRITE);
        o3d.clearCapabilityIsFrequent(Shape3D.ALLOW_GEOMETRY_WRITE);

        // make the offset TransformGroup
        model = new TransformGroup();
        model.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        model.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        model.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        model.clearCapabilityIsFrequent(TransformGroup.ALLOW_CHILDREN_READ);
//        model.setTransform(trans);
        model.setUserData(str);

        // the label shouldn't be pickable -- we'll turn this off in the
        // TransformGroup
        clearPickableFlags(model);
        model.addChild(o3d); // Add label to the offset TransformGroup
//        model.addChild(model);

        return model;
    }

    private static final long serialVersionUID = -3804876915020673179L;
}
