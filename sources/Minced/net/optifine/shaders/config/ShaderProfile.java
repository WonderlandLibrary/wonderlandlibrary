// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.config;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;

public class ShaderProfile
{
    private String name;
    private Map<String, String> mapOptionValues;
    private Set<String> disabledPrograms;
    
    public ShaderProfile(final String name) {
        this.name = null;
        this.mapOptionValues = new LinkedHashMap<String, String>();
        this.disabledPrograms = new LinkedHashSet<String>();
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void addOptionValue(final String option, final String value) {
        this.mapOptionValues.put(option, value);
    }
    
    public void addOptionValues(final ShaderProfile prof) {
        if (prof != null) {
            this.mapOptionValues.putAll(prof.mapOptionValues);
        }
    }
    
    public void applyOptionValues(final ShaderOption[] options) {
        for (int i = 0; i < options.length; ++i) {
            final ShaderOption shaderoption = options[i];
            final String s = shaderoption.getName();
            final String s2 = this.mapOptionValues.get(s);
            if (s2 != null) {
                shaderoption.setValue(s2);
            }
        }
    }
    
    public String[] getOptions() {
        final Set<String> set = this.mapOptionValues.keySet();
        final String[] astring = set.toArray(new String[set.size()]);
        return astring;
    }
    
    public String getValue(final String key) {
        return this.mapOptionValues.get(key);
    }
    
    public void addDisabledProgram(final String program) {
        this.disabledPrograms.add(program);
    }
    
    public void removeDisabledProgram(final String program) {
        this.disabledPrograms.remove(program);
    }
    
    public Collection<String> getDisabledPrograms() {
        return new LinkedHashSet<String>(this.disabledPrograms);
    }
    
    public void addDisabledPrograms(final Collection<String> programs) {
        this.disabledPrograms.addAll(programs);
    }
    
    public boolean isProgramDisabled(final String program) {
        return this.disabledPrograms.contains(program);
    }
}
