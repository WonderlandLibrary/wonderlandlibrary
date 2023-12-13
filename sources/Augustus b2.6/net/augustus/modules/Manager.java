// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules;

import java.util.ArrayList;
import java.util.List;

public class Manager
{
    private List<Module> modules;
    private ArrayList<Module> activeModules;
    
    public Manager() {
        this.modules = new ArrayList<Module>();
        this.activeModules = new ArrayList<Module>();
    }
    
    public List<Module> getModules() {
        return this.modules;
    }
    
    public void setModules(final List<Module> modules) {
        this.modules = modules;
    }
    
    public ArrayList<Module> getActiveModules() {
        return this.activeModules;
    }
    
    public void setActiveModules(final ArrayList<Module> activeModules) {
        this.activeModules = activeModules;
    }
}
