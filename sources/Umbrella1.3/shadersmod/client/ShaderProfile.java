/*
 * Decompiled with CFR 0.150.
 */
package shadersmod.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import shadersmod.client.ShaderOption;

public class ShaderProfile {
    private String name = null;
    private Map<String, String> mapOptionValues = new HashMap<String, String>();
    private Set<String> disabledPrograms = new HashSet<String>();

    public ShaderProfile(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addOptionValue(String option, String value) {
        this.mapOptionValues.put(option, value);
    }

    public void addOptionValues(ShaderProfile prof) {
        if (prof != null) {
            this.mapOptionValues.putAll(prof.mapOptionValues);
        }
    }

    public void applyOptionValues(ShaderOption[] options) {
        for (int i = 0; i < options.length; ++i) {
            ShaderOption so = options[i];
            String key = so.getName();
            String val = this.mapOptionValues.get(key);
            if (val == null) continue;
            so.setValue(val);
        }
    }

    public String[] getOptions() {
        Set<String> keys = this.mapOptionValues.keySet();
        String[] opts = keys.toArray(new String[keys.size()]);
        return opts;
    }

    public String getValue(String key) {
        return this.mapOptionValues.get(key);
    }

    public void addDisabledProgram(String program) {
        this.disabledPrograms.add(program);
    }

    public Collection<String> getDisabledPrograms() {
        return new HashSet<String>(this.disabledPrograms);
    }

    public void addDisabledPrograms(Collection<String> programs) {
        this.disabledPrograms.addAll(programs);
    }

    public boolean isProgramDisabled(String program) {
        return this.disabledPrograms.contains(program);
    }
}

