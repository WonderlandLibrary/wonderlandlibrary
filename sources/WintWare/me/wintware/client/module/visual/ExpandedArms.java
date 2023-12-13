/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.visual;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdateLiving;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.renderer.ItemRenderer;

public class ExpandedArms
extends Module {
    private final Setting MainHand = new Setting("MainHand", this, 1.0, 0.0, 3.0, false);
    private final Setting OffHand;

    public ExpandedArms() {
        super("ExpandedArms", Category.Visuals);
        Main.instance.setmgr.rSetting(this.MainHand);
        this.OffHand = new Setting("OffHand", this, 1.0, 0.0, 3.0, false);
        Main.instance.setmgr.rSetting(this.OffHand);
    }

    @EventTarget
    public void onLiving(EventUpdateLiving event) {
        ItemRenderer itemRenderer = ExpandedArms.mc.entityRenderer.itemRenderer;
        itemRenderer.equippedProgressOffHand = (float)(0.5 * this.OffHand.getValDouble());
        itemRenderer.equippedProgressMainHand = (float)(0.5 * this.MainHand.getValDouble());
    }
}

