// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import java.util.Iterator;
import com.elementars.eclient.Xulu;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class SettingLookup
{
    public static Value getSettingFromMod(final Module module, final String name) {
        for (final Value setting : Xulu.VALUE_MANAGER.getValues()) {
            if (Xulu.VALUE_MANAGER.getSettingsByMod(module).contains(setting) && setting.getName().equals(name)) {
                return setting;
            }
        }
        System.err.println("[Xulu] Error Setting NOT found: '" + name + "'!");
        return null;
    }
}
