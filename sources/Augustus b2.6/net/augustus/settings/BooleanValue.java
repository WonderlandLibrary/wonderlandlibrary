// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.settings;

import net.augustus.savings.parts.SettingPart;
import net.augustus.modules.Module;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class BooleanValue extends Setting
{
    @Expose
    @SerializedName("Boolean")
    private boolean aBoolean;
    
    public BooleanValue(final int id, final String name, final Module parent, final boolean aBoolean) {
        super(id, name, parent);
        this.aBoolean = aBoolean;
        BooleanValue.sm.newSetting(this);
    }
    
    public boolean getBoolean() {
        return this.aBoolean;
    }
    
    public void setBoolean(final boolean aBoolean) {
        this.aBoolean = aBoolean;
    }
    
    @Override
    public void readSetting(final SettingPart setting) {
        super.readSetting(setting);
        this.setBoolean(setting.isaBoolean());
    }
    
    @Override
    public void readConfigSetting(final SettingPart setting) {
        super.readConfigSetting(setting);
        this.setBoolean(setting.isaBoolean());
    }
}
