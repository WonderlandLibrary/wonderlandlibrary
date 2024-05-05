package com.initial.scriptmanager;

import java.io.*;
import com.initial.*;
import com.initial.modules.*;
import java.util.*;

public class ScriptManager
{
    public ArrayList<Script> scripts;
    
    public ScriptManager() {
        this.scripts = new ArrayList<Script>();
    }
    
    public void onStart() {
        for (final File f : Objects.requireNonNull(Astomero.instance.scriptFolder.listFiles())) {
            final String unExtendedName = f.getName().substring(0, f.getName().length() - 3);
            System.out.println(unExtendedName);
            final Module mod = new Module(unExtendedName, Category.SCRIPT);
            Astomero.instance.scriptManager.scripts.add(new Script(mod));
            Astomero.instance.moduleManager.getModules().add(mod);
        }
        this.loadScripts();
    }
    
    public void loadScripts() {
        for (final Script c : Astomero.instance.scriptManager.scripts) {
            c.putVars();
            c.callEvent("onLoad");
        }
    }
    
    public Script getScriptByName(final String str) {
        for (final Script s : this.scripts) {
            if (s.scriptName.equals(str)) {
                return s;
            }
        }
        return null;
    }
}
