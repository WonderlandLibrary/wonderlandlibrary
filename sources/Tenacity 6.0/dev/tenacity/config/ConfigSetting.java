// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.config;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class ConfigSetting
{
    @Expose
    @SerializedName("name")
    public String name;
    @Expose
    @SerializedName("value")
    public Object value;
    
    public ConfigSetting(final String name, final Object value) {
        this.name = name;
        this.value = value;
    }
}
