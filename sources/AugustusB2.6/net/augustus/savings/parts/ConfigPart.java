// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.savings.parts;

import net.augustus.modules.Module;
import java.util.ArrayList;
import com.google.gson.annotations.Expose;

public class ConfigPart
{
    @Expose
    private String name;
    @Expose
    private String date;
    @Expose
    private String time;
    @Expose
    private ArrayList<Module> modules;
    @Expose
    private ArrayList<SettingPart> settings;
    
    public ConfigPart(final String name, final String date, final String time, final ArrayList<Module> moduleParts, final ArrayList<SettingPart> settings) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.modules = moduleParts;
        this.settings = settings;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDate() {
        return this.date;
    }
    
    public void setDate(final String date) {
        this.date = date;
    }
    
    public String getTime() {
        return this.time;
    }
    
    public void setTime(final String time) {
        this.time = time;
    }
    
    public ArrayList<Module> getModules() {
        return this.modules;
    }
    
    public void setModules(final ArrayList<Module> modules) {
        this.modules = modules;
    }
    
    public ArrayList<SettingPart> getSettingParts() {
        return this.settings;
    }
    
    public void setSettingParts(final ArrayList<SettingPart> settingParts) {
        this.settings = settingParts;
    }
}
