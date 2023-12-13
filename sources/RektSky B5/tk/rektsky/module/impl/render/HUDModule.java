/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.render;

import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.ListSetting;

public class HUDModule
extends Module {
    public ListSetting mode = new ListSetting("HUD Mode", new String[]{"RektSky", "Legacy", "WeebClient", "Jello", "Fireentr"}, "RektSky");
    public ListSetting arrayListColor = new ListSetting("ArrayList color", new String[]{"Rainbow", "Astolfo"}, "RektSky");
    public BooleanSetting ShowBPS = new BooleanSetting("Show B/S", true);

    public HUDModule() {
        super("HUD", "", Category.RENDER);
        this.rawSetToggled(true);
    }

    @Override
    public void onDisable() {
        this.rawSetToggled(true);
    }
}

