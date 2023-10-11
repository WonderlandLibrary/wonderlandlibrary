/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.ui.altmanager;

import cow.milkgod.cheese.ui.altmanager.Alt;
import java.util.ArrayList;

public class AltManager {
    public static Alt lastAlt;
    public static ArrayList<Alt> registry;

    static {
        registry = new ArrayList();
    }

    public ArrayList<Alt> getRegistry() {
        return registry;
    }

    public void setLastAlt(Alt alt2) {
        lastAlt = alt2;
    }
}

