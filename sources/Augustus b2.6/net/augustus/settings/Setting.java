// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.settings;

import net.augustus.savings.parts.SettingPart;
import net.augustus.modules.Module;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import net.augustus.utils.interfaces.SM;

public abstract class Setting implements SM
{
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    @SerializedName("ParentName")
    private String parentName;
    private Module parent;
    private boolean visible;
    @Expose
    private int id;
    
    public Setting(final int id, final String name, final Module parent) {
        this.name = name;
        this.parent = parent;
        this.parentName = parent.getName();
        this.id = id;
        this.visible = true;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public Module getParent() {
        return this.parent;
    }
    
    public void setParent(final Module parent) {
        this.parent = parent;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public String getParentName() {
        return this.parentName;
    }
    
    public void setParentName(final String parentName) {
        this.parentName = parentName;
    }
    
    public void readSetting(final SettingPart setting) {
        this.setId(setting.getId());
        this.setName(setting.getName());
    }
    
    public void readConfigSetting(final SettingPart setting) {
    }
}
