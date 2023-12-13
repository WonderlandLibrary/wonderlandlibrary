// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.settings;

import com.elementars.eclient.Xulu;
import com.elementars.eclient.util.ValueList;
import java.util.Iterator;
import com.elementars.eclient.module.Module;
import java.util.ArrayList;

public class ValueManager
{
    private ArrayList<Value<?>> values;
    
    public ValueManager() {
        this.values = new ArrayList<Value<?>>();
    }
    
    public <T> Value<T> register(final Value<T> in) {
        this.values.add(in);
        return in;
    }
    
    public ArrayList<Value<?>> getValues() {
        return this.values;
    }
    
    public ArrayList<Value<?>> getSettingsByMod(final Module mod) {
        final ArrayList<Value<?>> out = new ArrayList<Value<?>>();
        for (final Value<?> s : this.getValues()) {
            if (s.getParentMod().equals(mod)) {
                out.add(s);
            }
        }
        if (out.isEmpty()) {
            return null;
        }
        return out;
    }
    
    public ArrayList<Value<?>> getValuesByMod(final Module mod) {
        final ValueList out = new ValueList();
        for (final Value<?> s : this.getValues()) {
            if (s.getParentMod().equals(mod)) {
                out.add(s);
            }
        }
        if (out.isEmpty()) {
            return null;
        }
        return out;
    }
    
    public <T> Value<T> getValueByMod(final Module mod, final String name) {
        for (final Value s : this.getValues()) {
            if (s.getParentMod().equals(mod) && s.getName().equalsIgnoreCase(name)) {
                return (Value<T>)s;
            }
        }
        return null;
    }
    
    public <T> Value<T> getValueByName(final String name) {
        for (final Value<?> set : this.getValues()) {
            if (set.getName().equalsIgnoreCase(name)) {
                return (Value<T>)set;
            }
        }
        System.err.println("[Xulu] Error Setting NOT found: '" + name + "'!");
        return null;
    }
    
    public <T> Value<T> getValueT(final String name, final Class<? extends Module> modClazz) {
        for (final Value<?> set : this.getValues()) {
            if (set.getName().equalsIgnoreCase(name) && set.getParentMod().equals(Xulu.MODULE_MANAGER.getModule(modClazz))) {
                return (Value<T>)set;
            }
        }
        System.err.println("[Xulu] Error Setting NOT found: '" + name + "'!");
        return null;
    }
}
