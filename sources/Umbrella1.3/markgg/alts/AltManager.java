/*
 * Decompiled with CFR 0.150.
 */
package markgg.alts;

import java.util.ArrayList;
import markgg.alts.Alt;

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

