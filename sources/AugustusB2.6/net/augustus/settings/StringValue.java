// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.settings;

import net.augustus.savings.parts.SettingPart;
import net.augustus.modules.Module;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class StringValue extends Setting
{
    @Expose
    @SerializedName("SelectedOption")
    private String string;
    private String[] options;
    
    public StringValue(final int id, final String name, final Module parent, final String value, final String[] options) {
        super(id, name, parent);
        this.string = value;
        this.options = options;
        StringValue.sm.newSetting(this);
    }
    
    public String[] getStringList() {
        return this.options;
    }
    
    public void setStringList(final String[] options) {
        this.options = options;
    }
    
    public String getSelected() {
        return this.string;
    }
    
    public void setString(final String string) {
        this.string = string;
    }
    
    @Override
    public void readSetting(final SettingPart setting) {
        super.readSetting(setting);
        this.setString(setting.getString());
    }
    
    @Override
    public void readConfigSetting(final SettingPart setting) {
        this.setString(setting.getString());
    }
}
