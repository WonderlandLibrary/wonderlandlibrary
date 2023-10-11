package com.initial.modules;

import java.awt.*;

public enum Category
{
    COMBAT("Combat", new Color(232, 76, 60)), 
    MOVEMENT("Movement", new Color(46, 204, 113)), 
    PLAYER("Player", new Color(141, 68, 173)), 
    EXPLOIT("Exploit", new Color(51, 152, 217)), 
    OTHER("Other", new Color(243, 155, 18)), 
    VISUAL("Visual", new Color(55, 0, 206)), 
    SCRIPT("Script", new Color(8322938));
    
    public boolean expanded;
    public Color pastelColor;
    public String name;
    public int posX;
    public int posY;
    
    private Category(final String name, final Color pastelColor) {
        this.expanded = false;
        this.name = name;
        this.pastelColor = pastelColor;
    }
}
