package net.optifine.shaders.config;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.optifine.shaders.config.ShaderOption;

public class ShaderProfile {
    private String name = null;
    private Map<String, String> mapOptionValues = new LinkedHashMap();
    private Set<String> disabledPrograms = new LinkedHashSet();

    public ShaderProfile(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addOptionValue(String option, String value) {
        this.mapOptionValues.put((Object)option, (Object)value);
    }

    public void addOptionValues(ShaderProfile prof) {
        if (prof != null) {
            this.mapOptionValues.putAll(prof.mapOptionValues);
        }
    }

    public void applyOptionValues(ShaderOption[] options) {
        for (int i = 0; i < options.length; ++i) {
            ShaderOption shaderoption = options[i];
            String s = shaderoption.getName();
            String s1 = (String)this.mapOptionValues.get((Object)s);
            if (s1 == null) continue;
            shaderoption.setValue(s1);
        }
    }

    public String[] getOptions() {
        Set set = this.mapOptionValues.keySet();
        String[] astring = (String[])set.toArray((Object[])new String[set.size()]);
        return astring;
    }

    public String getValue(String key) {
        return (String)this.mapOptionValues.get((Object)key);
    }

    public void addDisabledProgram(String program) {
        this.disabledPrograms.add((Object)program);
    }

    public void removeDisabledProgram(String program) {
        this.disabledPrograms.remove((Object)program);
    }

    public Collection<String> getDisabledPrograms() {
        return new LinkedHashSet(this.disabledPrograms);
    }

    public void addDisabledPrograms(Collection<String> programs) {
        this.disabledPrograms.addAll(programs);
    }

    public boolean isProgramDisabled(String program) {
        return this.disabledPrograms.contains((Object)program);
    }
}
