package ru.smertnix.celestial.ui.components;

import java.util.Comparator;

import ru.smertnix.celestial.ui.clickgui.component.Component;
import ru.smertnix.celestial.ui.clickgui.component.impl.ModuleComponent;

public class SorterHelper implements Comparator<Component> {

    @Override
    public int compare(Component component, Component component2) {
        if (component instanceof ModuleComponent && component2 instanceof ModuleComponent) {
            return component.getName().compareTo(component2.getName());
        }
        return 0;
    }
}