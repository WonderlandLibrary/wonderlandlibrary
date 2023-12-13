// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.settings;

import net.augustus.savings.parts.SettingPart;
import java.util.Arrays;
import net.augustus.modules.Module;
import com.google.gson.annotations.SerializedName;

public class BooleansSetting extends Setting
{
    @SerializedName("Settings")
    private Setting[] settings;
    
    public BooleansSetting(final int id, final String name, final Module parent, final Setting[] settings) {
        super(id, name, parent);
        this.settings = settings;
        Arrays.stream(settings).forEach(setting -> setting.setVisible(false));
        BooleansSetting.sm.newSetting(this);
    }
    
    public Setting[] getSettingList() {
        return this.settings;
    }
    
    public void setSettingList(final Setting[] settings) {
        this.settings = settings;
    }
    
    @Override
    public void readSetting(final SettingPart setting) {
        super.readSetting(setting);
    }
}
