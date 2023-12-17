// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.altmanager.alt;

import ru.tuskevich.Minced;
import java.util.ArrayList;

public class AltManager
{
    public static Alt lastAlt;
    public static ArrayList<Alt> registry;
    
    public ArrayList<Alt> getRegistry() {
        return AltManager.registry;
    }
    
    public static void addAccount(final Alt alt) {
        AltManager.registry.add(alt);
        try {
            Minced.getInstance().fileManager.saveFiles();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void removeAccount(final Alt alt) {
        AltManager.registry.remove(alt);
        try {
            Minced.getInstance().fileManager.saveFiles();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void clearAccounts() {
        AltManager.registry.clear();
        try {
            Minced.getInstance().fileManager.saveFiles();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setLastAlt(final Alt alt) {
        AltManager.lastAlt = alt;
    }
    
    static {
        AltManager.registry = new ArrayList<Alt>();
    }
}
