// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.config;

import com.google.gson.JsonObject;

public interface ConfigUpdater
{
    JsonObject save();
    
    void load(final JsonObject p0);
}
