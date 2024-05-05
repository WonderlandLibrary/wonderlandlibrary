package com.initial.settings.impl;

import com.initial.settings.*;

public class DoubleSet extends Setting
{
    public double value;
    public double min;
    public double max;
    public double inc;
    public String suffix;
    
    public DoubleSet(final String name, final double value, final double min, final double max, final double inc) {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
        this.inc = inc;
    }
    
    public DoubleSet(final String name, final double value, final double min, final double max, final double inc, final String suffix) {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
        this.inc = inc;
        this.suffix = suffix;
    }
    
    public String getSuffix() {
        return (this.suffix == null) ? "" : this.suffix;
    }
    
    public void setSuffix(final String s) {
        this.suffix = s;
    }
    
    public double getValue() {
        return this.value;
    }
    
    public void setValue(final double value) {
        final double prec = 1.0 / this.inc;
        this.value = Math.round(Math.max(this.min, Math.min(this.max, value)) * prec) / prec;
        this.onChange();
    }
    
    public double getMin() {
        return this.min;
    }
    
    public void setMin(final double min) {
        this.min = min;
    }
    
    public double getMax() {
        return this.max;
    }
    
    public void setMax(final double max) {
        this.max = max;
    }
    
    public double getInc() {
        return this.inc;
    }
    
    public void setInc(final double inc) {
        this.inc = inc;
    }
    
    public void onChange() {
    }
}
