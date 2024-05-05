package com.initial.settings.impl;

import com.initial.settings.*;
import java.util.*;

public class ModeSet extends Setting
{
    private String selected;
    public int index;
    public List<String> modes;
    
    public ModeSet(final String name, final String defaultMode, final String... modeOptions) {
        this.modes = new ArrayList<String>();
        this.name = name;
        this.modes = Arrays.asList(modeOptions);
        this.index = this.modes.indexOf(defaultMode);
        this.selected = this.modes.get(this.index);
    }
    
    public String getMode() {
        return this.modes.get(this.index);
    }
    
    public boolean is(final String mode) {
        return this.index == this.modes.indexOf(mode);
    }
    
    public void setSelected(final String selected) {
        this.selected = selected;
        this.index = this.modes.indexOf(selected);
        this.onChange();
    }
    
    public void positiveCycle() {
        if (this.index < this.modes.size() - 1) {
            ++this.index;
        }
        else {
            this.index = 0;
        }
        this.onChange();
    }
    
    public void negativeCycle() {
        if (this.index <= 0) {
            this.index = this.modes.size() - 1;
        }
        else {
            --this.index;
        }
        this.onChange();
    }
    
    public void setIndex(final int index) {
        this.index = index;
        this.onChange();
    }
    
    public void onChange() {
    }
}
