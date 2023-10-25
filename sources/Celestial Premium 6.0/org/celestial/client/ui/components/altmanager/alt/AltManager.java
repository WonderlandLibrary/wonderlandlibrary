/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.altmanager.alt;

import java.util.ArrayList;
import org.celestial.client.ui.components.altmanager.alt.Alt;

public class AltManager {
    public static Alt lastAlt;
    public static ArrayList registry;

    public ArrayList getRegistry() {
        return registry;
    }

    public void setLastAlt(Alt alt) {
        lastAlt = alt;
    }

    static {
        registry = new ArrayList();
    }
}

