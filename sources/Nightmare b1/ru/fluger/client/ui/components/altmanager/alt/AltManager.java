// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.components.altmanager.alt;

import java.util.ArrayList;

public class AltManager
{
    public static Alt lastAlt;
    public static ArrayList registry;
    
    public ArrayList getRegistry() {
        return AltManager.registry;
    }
    
    public void setLastAlt(final Alt alt) {
        AltManager.lastAlt = alt;
    }
    
    static {
        AltManager.registry = new ArrayList();
    }
}
