// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.settings.config;

import com.google.gson.JsonObject;

public interface ConfigUpdater
{
    JsonObject save();
    
    void load(final JsonObject p0);
}
