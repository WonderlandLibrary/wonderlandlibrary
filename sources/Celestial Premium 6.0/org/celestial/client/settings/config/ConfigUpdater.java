/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.settings.config;

import com.google.gson.JsonObject;

public interface ConfigUpdater {
    public JsonObject save();

    public void load(JsonObject var1);
}

