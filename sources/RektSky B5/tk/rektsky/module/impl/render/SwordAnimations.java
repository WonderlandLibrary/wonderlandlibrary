/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.render;

import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.ListSetting;

public class SwordAnimations
extends Module {
    public static ListSetting mode = new ListSetting("Mode", new String[]{"Vanilla", "RektSky", "1.7", "animashion", "Swoop", "AllahSwing", "Swosh", "Fan"}, "Vanilla");

    public SwordAnimations() {
        super("SwordAnimations", "Sword blocking animations", Category.RENDER);
    }
}

