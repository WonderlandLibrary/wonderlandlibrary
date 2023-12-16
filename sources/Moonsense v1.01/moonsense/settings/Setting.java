// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.settings;

import java.util.Iterator;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.security.InvalidParameterException;
import moonsense.utils.KeyBinding;
import moonsense.utils.ColorObject;
import java.util.function.Consumer;
import java.util.List;
import moonsense.features.SCModule;

public class Setting
{
    private final SCModule module;
    private final String description;
    private final String key;
    private final List<Object> value;
    private Type type;
    private Consumer<Setting> consumer;
    private Object defaultValue;
    private boolean disabled;
    private boolean hidden;
    private boolean compound;
    
    public Setting(final SCModule module, final String description) {
        this(module, description, description.replace(" ", "").toUpperCase());
        if (module != null) {
            this.module.settings.add(this);
        }
    }
    
    public Setting onValueChanged(final Consumer<Setting> consumer) {
        this.consumer = consumer;
        return this;
    }
    
    public Setting setDefault(final int color, final int chromaSpeed) {
        this.type = Type.COLOR;
        this.replaceIndex(0, this.defaultValue = new ColorObject(color, chromaSpeed != 0, chromaSpeed));
        if (this.module != null) {
            this.module.settings.add(this);
        }
        return this;
    }
    
    public Setting setDefault(final CompoundSettingGroup settings) {
        this.type = Type.COMPOUND;
        this.replaceIndex(0, this.defaultValue = settings);
        if (this.module != null) {
            this.module.settings.add(this);
        }
        return this;
    }
    
    public Setting setDefault(final KeyBinding keyBinding) {
        this.type = Type.KEYBIND;
        this.replaceIndex(0, this.defaultValue = keyBinding);
        if (this.module != null) {
            this.module.settings.add(this);
        }
        return this;
    }
    
    public Setting setDefault(final String s) {
        this.type = Type.STRING;
        this.replaceIndex(0, this.defaultValue = s);
        if (this.module != null) {
            this.module.settings.add(this);
        }
        return this;
    }
    
    public Setting setDefault(final Number n) {
        this.replaceIndex(0, this.defaultValue = n);
        if (this.module != null) {
            this.module.settings.add(this);
        }
        return this;
    }
    
    public Setting setDefault(final boolean b) {
        this.type = Type.CHECKBOX;
        this.replaceIndex(0, this.defaultValue = b);
        if (this.module != null) {
            this.module.settings.add(this);
        }
        return this;
    }
    
    public Setting compound(final boolean compound) {
        this.compound = compound;
        if (this.module == null) {
            return this;
        }
        if (compound) {
            this.module.settings.remove(this);
        }
        else if (!this.module.settings.contains(this)) {
            this.module.settings.add(this);
        }
        return this;
    }
    
    public boolean isCompound() {
        return this.compound;
    }
    
    public SCModule getModule() {
        return this.module;
    }
    
    public Setting setValue(final Object value) {
        final Object prevValue = this.getObject();
        this.replaceIndex(0, value);
        try {
            if (this.consumer != null && prevValue != value) {
                this.consumer.accept(this);
            }
        }
        catch (Exception ex) {}
        return this;
    }
    
    public Object getObject() {
        return this.value.get(0);
    }
    
    public int getInt() {
        return this.value.get(0);
    }
    
    public float getFloat() {
        return this.value.get(0);
    }
    
    public boolean getBoolean() {
        return this.value.get(0);
    }
    
    public String getString() {
        return this.value.get(0);
    }
    
    public boolean hasValue() {
        return this.value.size() > 0;
    }
    
    public Setting setRange(final int min, final int max, final int step) {
        this.type = Type.INT_SLIDER;
        this.value.add(min);
        this.value.add(max);
        this.value.add(step);
        return this;
    }
    
    public Setting setRange(final float min, final float max, final float step) {
        this.type = Type.FLOAT_SLIDER;
        this.value.add(min);
        this.value.add(max);
        this.value.add(0.01f);
        return this;
    }
    
    public Setting setRange(final String... display) {
        if (display.length == 0) {
            throw new InvalidParameterException("Supplied parameter has an insufficient length!");
        }
        this.type = Type.MODE;
        Collections.addAll(this.value, display);
        return this;
    }
    
    public float getRange(final int i) {
        return Float.parseFloat(String.valueOf(this.value.get(i + 1)));
    }
    
    public ColorObject getColorObject() {
        return this.value.get(0);
    }
    
    public int getColor() {
        final ColorObject color = this.getColorObject();
        if (color.isChroma() && color.getChromaSpeed() != 0) {
            return color.getChromaColor();
        }
        return color.getColor();
    }
    
    public void replaceIndex(final int index, final Object value) {
        if (this.value.size() > 0) {
            this.value.remove(index);
        }
        this.value.add(index, value);
    }
    
    public Setting(final SCModule module, final String description, final String key) {
        this.disabled = false;
        this.hidden = false;
        this.compound = false;
        this.value = (List<Object>)Lists.newArrayList();
        this.module = module;
        this.description = description;
        this.key = key;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    public Setting hide() {
        this.hidden = true;
        return this;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public void disable() {
        this.disabled = true;
    }
    
    public void enable() {
        this.disabled = false;
    }
    
    public boolean enabled() {
        return !this.disabled;
    }
    
    public List<Object> getValue() {
        return this.value;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public Object getDefaultValue() {
        return this.defaultValue;
    }
    
    public enum Type
    {
        COLOR("COLOR", 0), 
        INT_SLIDER("INT_SLIDER", 1), 
        FLOAT_SLIDER("FLOAT_SLIDER", 2), 
        CHECKBOX("CHECKBOX", 3), 
        KEYBIND("KEYBIND", 4), 
        MODE("MODE", 5), 
        STRING("STRING", 6), 
        COMPOUND("COMPOUND", 7), 
        DEFAULT("DEFAULT", 8);
        
        private Type(final String name, final int ordinal) {
        }
    }
    
    public static class CompoundSettingGroup
    {
        private final String name;
        private final ArrayList<Setting> settings;
        
        public CompoundSettingGroup(final String groupName, final Setting... settings) {
            this.name = groupName;
            this.settings = (ArrayList<Setting>)Lists.newArrayList();
            for (final Setting s : settings) {
                this.settings.add(s);
            }
        }
        
        public String getName() {
            return this.name;
        }
        
        public ArrayList<Setting> getSettings() {
            return this.settings;
        }
        
        public Setting getSettingByName(final String name) {
            for (final Setting s : this.settings) {
                if (s.getDescription().equals(name)) {
                    return s;
                }
            }
            return null;
        }
        
        public Setting getSettingByKey(final String key) {
            for (final Setting s : this.settings) {
                if (s.getKey().equals(this.name)) {
                    return s;
                }
            }
            return null;
        }
    }
}
