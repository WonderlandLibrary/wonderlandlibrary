// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.macro;

import java.util.ArrayList;

public class MacroManager
{
    private ArrayList<Macro> macros;
    
    public MacroManager() {
        this.macros = new ArrayList<Macro>();
    }
    
    public void addMacro(final String msg, final int key) {
        this.macros.add(new Macro(msg, key));
    }
    
    public void delMacro(final int key) {
        this.macros.stream().filter(macro -> macro.getKey() == key).forEach(macro -> this.macros.remove(macro));
    }
    
    public ArrayList<Macro> getMacros() {
        return this.macros;
    }
    
    public void runMacros(final int key) {
        this.macros.stream().filter(macro -> macro.getKey() == key).forEach(Macro::runMacro);
    }
}
