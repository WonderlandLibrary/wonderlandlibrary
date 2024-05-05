package club.cloverhook.ui.main.tab;

import java.util.ArrayList;

import club.cloverhook.ui.main.Interface;
import club.cloverhook.ui.main.components.Component;

/**
 * @author antja03
 */
public class Tab {

    private Interface theInterface;

    public final ArrayList<Component> components = new ArrayList<>();

    public Tab(Interface theInterface) {
        this.theInterface = theInterface;
    }

    public void setup() { }

    public void onTick() { }

    public ArrayList<Component> getActiveComponents() {
        return components;
    }

    public void fixPositions() { }

    public Interface getInterface() {
        return theInterface;
    }
}
