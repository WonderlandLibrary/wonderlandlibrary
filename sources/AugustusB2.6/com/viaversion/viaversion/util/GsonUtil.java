// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.util;

import com.viaversion.viaversion.libs.gson.GsonBuilder;
import com.viaversion.viaversion.libs.gson.Gson;

public final class GsonUtil
{
    private static final Gson GSON;
    
    public static Gson getGson() {
        return GsonUtil.GSON;
    }
    
    static {
        GSON = new GsonBuilder().create();
    }
}
