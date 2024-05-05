package rip.helium.gui.hud.element;


import rip.helium.utils.property.abs.Property;
import rip.helium.utils.property.impl.BooleanProperty;

import java.util.ArrayList;

/**
 * @author antja03
 */
public abstract class Element {

    public static double positionY;
    public String identifier;
    public Quadrant quadrant;
    public double positionX;
    public double editX, editY, width, height;
    public ArrayList<Property> values;

    public Element(String identifier, Quadrant quadrant, double positionX, double positionY) {
        this.identifier = identifier;
        this.quadrant = quadrant;
        this.positionX = positionX;
        Element.positionY = positionY;
        this.values = new ArrayList<>();
        this.values.add(new BooleanProperty("Hidden", "Hides this element", null, false));
    }

    public abstract void drawElement(boolean editor);

    public void onKeyPress(int keyCode) {
    }

    public ArrayList<Property> getValues() {
        return values;
    }
}
