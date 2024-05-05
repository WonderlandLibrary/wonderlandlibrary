package ru.smertnix.celestial.ui.changelog;

import java.awt.Color;

public class ChangeLog {

    protected String changeName;
    protected ChangeType type;

    public ChangeLog(String name) {
        this.changeName = name;
        for (ChangeType c : ChangeType.values()) {
        	 if (name.contains(c.name)) {
        		 type = c;
             }
        }
    }

    public String getLogName() {
        return changeName;
    }

    public ChangeType getType() {
        return type;
    }
    
    public Color getTypeColor() {
        return type.color;
    }
}