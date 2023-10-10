/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.rektsky;

import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.ListSetting;

public class Cape
extends Module {
    public ListSetting mode = new ListSetting("Cape", new String[]{"RektSky", "Legacy", "Astolfo", "Kant", "Cats"}, "RektSky");

    public Cape() {
        super("Cape", "Show RektSky User's Cape", Category.RENDER);
    }
}

