/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.render;

import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.BooleanSetting;

public class KeyStrokes
extends Module {
    public static boolean isEnabled;
    public BooleanSetting Font = new BooleanSetting("Custom Font", true);
    public static BooleanSetting Rounded;
    public static BooleanSetting Rainbow;

    public KeyStrokes() {
        super("KeyStrokes", "Shows what buttons you're pressing", Category.RENDER);
    }

    @Override
    public void onEnable() {
        isEnabled = true;
    }

    @Override
    public void onDisable() {
        isEnabled = false;
    }

    static {
        Rounded = new BooleanSetting("Rounded Corners", true);
        Rainbow = new BooleanSetting("rainbow", true);
    }
}

