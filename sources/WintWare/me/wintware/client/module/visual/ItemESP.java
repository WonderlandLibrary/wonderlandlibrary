/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.visual;

import java.awt.Color;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventRender3D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;

public class ItemESP
extends Module {
    private final Setting red;
    private final Setting green;
    private final Setting blue;
    private final Setting range = new Setting("Render Distance", this, 20.0, 5.0, 50.0, false);

    public ItemESP() {
        super("ItemESP", Category.Visuals);
        Main.instance.setmgr.rSetting(this.range);
        this.red = new Setting("Red", this, 0.0, 0.0, 255.0, false);
        Main.instance.setmgr.rSetting(this.red);
        this.green = new Setting("Green", this, 200.0, 0.0, 255.0, false);
        Main.instance.setmgr.rSetting(this.green);
        this.blue = new Setting("Blue", this, 0.0, 0.0, 255.0, false);
        Main.instance.setmgr.rSetting(this.blue);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        for (Entity entities : ItemESP.mc.world.loadedEntityList) {
            if (!(entities instanceof EntityItem)) continue;
            if (!(Minecraft.player.getDistanceToEntity(entities) <= this.range.getValFloat())) continue;
            RenderUtil.drawEntityESP(entities, new Color(this.red.getValInt(), this.green.getValInt(), this.blue.getValInt()));
        }
    }
}

