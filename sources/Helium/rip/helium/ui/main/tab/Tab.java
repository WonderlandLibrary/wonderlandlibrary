package rip.helium.ui.main.tab;

import rip.helium.ui.main.Interface;
import rip.helium.ui.main.components.Component;

import java.util.ArrayList;

/**
 * @author antja03
 */
public class Tab {

    public final ArrayList<Component> components = new ArrayList<>();
    private final Interface theInterface;

    public Tab(Interface theInterface) {
        this.theInterface = theInterface;
    }

    public void setup() {
    }

    public void onTick() {
    }

    public ArrayList<Component> getActiveComponents() {
        return components;
    }

    public void fixPositions() {
    }

    public Interface getInterface() {
        return theInterface;
    }
}
