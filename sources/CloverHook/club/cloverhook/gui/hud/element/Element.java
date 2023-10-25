package club.cloverhook.gui.hud.element;


import java.util.ArrayList;

import club.cloverhook.utils.property.abs.Property;
import club.cloverhook.utils.property.impl.BooleanProperty;

/**
 * @author antja03
 */
public abstract class Element {

    public String identifier;
    public Quadrant quadrant;
    public double positionX, positionY;
    public double editX, editY, width, height;
    public ArrayList<Property> values;

    public Element(String identifier, Quadrant quadrant, double positionX, double positionY) {
        this.identifier = identifier;
        this.quadrant = quadrant;
        this.positionX = positionX;
        this.positionY = positionY;
        this.values = new ArrayList<>();
        this.values.add(new BooleanProperty("Hidden", "Hides this element", null, false));
    }

    public abstract void drawElement(boolean editor);

    public void onKeyPress(int keyCode) { }

    public ArrayList<Property> getValues() {
        return values;
    }
}
