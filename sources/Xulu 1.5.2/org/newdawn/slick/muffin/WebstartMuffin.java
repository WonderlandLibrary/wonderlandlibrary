// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.muffin;

import java.util.Iterator;
import java.util.Set;
import java.io.IOException;
import org.newdawn.slick.util.Log;
import java.util.HashMap;

public class WebstartMuffin implements Muffin
{
    @Override
    public void saveFile(final HashMap scoreMap, final String fileName) throws IOException {
        try {
            final Set keys = scoreMap.keySet();
            for (final String key : keys) {
                if (fileName.endsWith("Number")) {
                    scoreMap.get(key);
                }
                else {
                    final String s = (String)scoreMap.get(key);
                }
            }
        }
        catch (Exception e) {
            Log.error(e);
            throw new IOException("Failed to store map of state data");
        }
    }
    
    @Override
    public HashMap loadFile(final String fileName) throws IOException {
        final HashMap hashMap = new HashMap();
        try {
            if (fileName.endsWith("Number")) {}
        }
        catch (Exception e) {
            Log.error(e);
            throw new IOException("Failed to load state from webstart muffin");
        }
        return hashMap;
    }
}
