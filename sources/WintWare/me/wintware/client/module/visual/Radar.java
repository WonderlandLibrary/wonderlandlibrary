/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.visual;

import java.awt.Color;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventRender2D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.other.TimerHelper;
import me.wintware.client.utils.visual.ColorUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class Radar
extends Module {
    private final TimerHelper timer = new TimerHelper();
    private boolean dragging;
    public int scale;
    float hue;
    private final Setting size1;
    private final Setting posx = new Setting("PosX", this, 860.0, 0.0, 900.0, true);
    private final Setting posy;

    public Radar() {
        super("Radar", Category.Visuals);
        Main.instance.setmgr.rSetting(this.posx);
        this.posy = new Setting("PosY", this, 15.0, 0.0, 350.0, true);
        Main.instance.setmgr.rSetting(this.posy);
        this.size1 = new Setting("Size", this, 100.0, 30.0, 300.0, true);
        Main.instance.setmgr.rSetting(this.size1);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        Color cl = new Color(-1);
        double psx = this.posx.getValDouble();
        double psy = this.posy.getValDouble();
        if (Radar.mc.gameSettings.showDebugInfo) {
            psy = 260.0;
        }
        ScaledResolution sr = new ScaledResolution(mc);
        this.scale = 2;
        int size = this.size1.getValInt();
        float xOffset = (float)((double)(sr.getScaledWidth() - size) - psx);
        float yOffset = (float)psy;
        float playerOffsetX = (float)Minecraft.player.posX;
        float playerOffsetZ = (float)Minecraft.player.posZ;
        RenderUtil.rectangleBordered((double)xOffset + 2.5, (double)yOffset + 2.5, (double)(xOffset + (float)size) - 2.5, (double)(yOffset + (float)size) - 2.5, 0.5, ColorUtils.getColor(11), ColorUtils.getColor(88));
        RenderUtil.rectangleBordered(xOffset + 3.0f, yOffset + 3.0f, xOffset + (float)size - 3.0f, yOffset + (float)size - 3.0f, 0.2, ColorUtils.getColor(11), ColorUtils.getColor(88));
        RenderUtil.drawRect((double)xOffset + ((double)((float)size / 2.0f) - 0.5), (double)yOffset + 3.5, (double)xOffset + ((double)((float)size / 2.0f) + 0.2), (double)(yOffset + (float)size) - 3.5, ColorUtils.getColor(255, 100));
        RenderUtil.drawRect((double)xOffset + 3.5, (double)yOffset + ((double)((float)size / 2.0f) - 0.2), (double)(xOffset + (float)size) - 3.5, (double)yOffset + ((double)((float)size / 2.0f) + 0.5), ColorUtils.getColor(255, 100));
        for (Entity obj : Radar.mc.world.getLoadedEntityList()) {
            if (!(obj instanceof EntityPlayer)) continue;
            EntityPlayer ent = (EntityPlayer)obj;
            if (ent == Minecraft.player || ent.isInvisible()) continue;
            float pTicks = Radar.mc.timer.renderPartialTicks;
            float posX = (float)((ent.posX + (ent.posX - ent.lastTickPosX) * (double)pTicks - (double)playerOffsetX) * (double)this.scale);
            float posZ = (float)((ent.posZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - (double)playerOffsetZ) * (double)this.scale);
            int color = Minecraft.player.canEntityBeSeen(ent) ? new Color(255, 0, 0).getRGB() : new Color(255, 255, 0).getRGB();
            float cos = (float)Math.cos((double)Minecraft.player.rotationYaw * (Math.PI / 180));
            float sin = (float)Math.sin((double)Minecraft.player.rotationYaw * (Math.PI / 180));
            float rotY = -(posZ * cos - posX * sin);
            float rotX = -(posX * cos + posZ * sin);
            if (rotY > (float)size / 2.0f - 5.0f) {
                rotY = (float)size / 2.0f - 5.0f;
            } else if (rotY < -((float)size / 2.0f - 5.0f)) {
                rotY = -((float)size / 2.0f - 5.0f);
            }
            if (rotX > (float)size / 2.0f - 5.0f) {
                rotX = (float)size / 2.0f - 5.0f;
            } else if (rotX < -((float)size / 2.0f - 5.0f)) {
                rotX = -((float)size / 2.0f - 5.0f);
            }
            RenderUtil.rectangleBordered((double)(xOffset + (float)(size / 2) + rotX) - 1.5, (double)(yOffset + (float)(size / 2) + rotY) - 1.5, (double)(xOffset + (float)(size / 2) + rotX) + 1.5, (double)(yOffset + (float)(size / 2) + rotY) + 1.5, 0.5, color, Main.getClientColor().getRGB());
        }
    }
}

