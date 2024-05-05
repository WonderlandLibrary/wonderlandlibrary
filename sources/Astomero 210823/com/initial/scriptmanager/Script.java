package com.initial.scriptmanager;

import javax.script.*;
import com.initial.modules.*;
import com.initial.*;
import java.io.*;
import com.initial.events.*;
import com.initial.settings.*;
import com.initial.settings.impl.*;
import java.util.*;

public class Script
{
    public Invocable invocable;
    public ScriptEngineManager engineManager;
    public ScriptEngine scriptEngine;
    public String scriptName;
    public File scriptFile;
    public Module scriptModule;
    
    public Script(final Module m) {
        this.scriptModule = m;
        this.scriptName = m.getName();
        this.scriptFile = new File(Astomero.instance.scriptFolder, m.getName() + ".js");
        this.engineManager = new ScriptEngineManager();
        this.scriptEngine = this.engineManager.getEngineByName("JavaScript");
        this.invocable = (Invocable)this.scriptEngine;
    }
    
    public void callEvent(final String funcName) {
        try {
            this.scriptEngine.eval(new FileReader(this.scriptFile));
            this.invocable.invokeFunction(funcName, new Object[0]);
        }
        catch (Exception ex) {}
    }
    
    public void callEvent(final String funcName, final Event e) {
        try {
            this.scriptEngine.eval(new FileReader(this.scriptFile));
            this.invocable.invokeFunction(funcName, e);
        }
        catch (Exception ex) {}
    }
    
    public void putVars() {
        this.scriptEngine.put("scriptManager", Astomero.instance.scriptManager);
        this.scriptEngine.put("module", this.scriptModule);
        this.scriptEngine.put("script", this);
    }
    
    public void declareSetting(final String mode, final Object... args) {
        switch (mode) {
            case "double": {
                this.scriptModule.addSettings(new DoubleSet((String)args[0], (double)args[1], (double)args[2], (double)args[3], (double)args[4]));
                break;
            }
            case "boolean": {
                this.scriptModule.addSettings(new BooleanSet((String)args[0], (boolean)args[1]));
                break;
            }
            case "mode": {
                final List<String> strs = new ArrayList<String>();
                for (int a = 2; a < args.length; ++a) {
                    strs.add((String)args[a]);
                }
                final String[] strings = strs.toArray(new String[0]);
                this.scriptModule.addSettings(new ModeSet((String)args[0], (String)args[1], strings));
                break;
            }
        }
    }
}
