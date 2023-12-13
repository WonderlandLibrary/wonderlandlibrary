// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.data;

import java.util.Iterator;
import java.util.HashMap;

public class SettingsMap extends HashMap<String, Setting>
{
    public void update(final HashMap<String, Setting> newMap) {
        for (final String key : newMap.keySet()) {
            if (this.containsKey(key)) {
                ((HashMap<K, Setting>)this).get(key).update(newMap.get(key));
            }
        }
    }
}
