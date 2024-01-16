// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.settings.impl;

import java.util.function.Supplier;
import ru.fluger.client.settings.Setting;

public class BooleanSetting extends Setting
{
    private boolean state;
    private aow block;
    private String desc;
    
    public BooleanSetting(final String name, final aow block, final boolean state, final Supplier<Boolean> visible) {
        this.name = name;
        this.block = block;
        this.state = state;
        this.setVisible(visible);
    }
    
    public BooleanSetting(final String name, final String desc, final boolean state, final Supplier<Boolean> visible) {
        this.name = name;
        this.desc = desc;
        this.state = state;
        this.setVisible(visible);
    }
    
    public BooleanSetting(final String name, final boolean state) {
        this.name = name;
        this.state = state;
        this.setVisible(() -> true);
    }
    
    public BooleanSetting(final String name, final boolean state, final Supplier<Boolean> visible) {
        this.name = name;
        this.state = state;
        this.setVisible(visible);
    }
    
    public aow getBlock() {
        return this.block;
    }
    
    public void setBlock(final aow block) {
        this.block = block;
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public void setDesc(final String desc) {
        this.desc = desc;
    }
    
    public boolean getCurrentValue() {
        return this.state;
    }
    
    public void setValue(final boolean state) {
        this.state = state;
    }
}
