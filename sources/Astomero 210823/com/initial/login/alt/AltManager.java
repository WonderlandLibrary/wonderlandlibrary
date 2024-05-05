package com.initial.login.alt;

import java.util.*;

public class AltManager
{
    public static Alt lastAlt;
    public static ArrayList<Alt> registry;
    
    public ArrayList<Alt> getRegistry() {
        return AltManager.registry;
    }
    
    public void setLastAlt(final Alt alt2) {
        AltManager.lastAlt = alt2;
    }
    
    static {
        AltManager.registry = new ArrayList<Alt>();
    }
}
