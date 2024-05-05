package ru.smertnix.celestial.ui.changelog;

import java.awt.Color;

public enum ChangeType {
    ADDED("Added", new Color(0, 216, 1)),
    REMOVED("Removed", new Color(216, 0, 0)),
    IMPROVED("Improved", new Color(0, 216, 216)),
    FIXED("Fixed", new Color(216, 0, 216));
    
	public String name;
	public Color color;
    ChangeType(String name, Color color) {
    	this.name = name;
    	this.color = color;
    }
    
}