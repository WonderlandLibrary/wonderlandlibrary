// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.cmd.macro;

import java.util.ArrayList;
import java.util.List;

public class MacroManager
{
    public List<Macro> macros;
    
    public MacroManager() {
        this.macros = new ArrayList<Macro>();
    }
    
    public List<Macro> getMacros() {
        return this.macros;
    }
    
    public void addMacro(final Macro macro) {
        this.macros.add(macro);
    }
    
    public void deleteMacroByKey(final int key) {
        this.macros.removeIf(macro -> macro.getKey() == key);
    }
}
