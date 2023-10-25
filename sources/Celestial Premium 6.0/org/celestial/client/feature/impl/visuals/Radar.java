/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.render.EventRender2D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.settings.impl.NumberSetting;

public class Radar
extends Feature {
    private final NumberSetting size;
    private final NumberSetting posx = new NumberSetting("PosX", 860.0f, 0.0f, 900.0f, 1.0f, () -> true);
    private final NumberSetting posy = new NumberSetting("PosY", 15.0f, 0.0f, 350.0f, 1.0f, () -> true);
    public int scale;

    public Radar() {
        super("Radar", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0440\u0430\u0434\u0430\u0440 \u0438 \u0438\u0433\u0440\u043e\u043a\u043e\u0432 \u043d\u0430 \u043d\u0435\u043c", Type.Visuals);
        this.size = new NumberSetting("Size", 100.0f, 30.0f, 300.0f, 1.0f, () -> true);
        this.addSettings(this.posx, this.posy, this.size);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        double psx = this.posx.getCurrentValue();
        double psy = this.posy.getCurrentValue();
        ScaledResolution sr = new ScaledResolution(mc);
        this.scale = 2;
        int sizeRect = (int)this.size.getCurrentValue();
        float xOffset = (float)((double)(sr.getScaledWidth() - sizeRect) - psx);
        float yOffset = (float)psy;
        double playerPosX = Radar.mc.player.posX;
        double playerPosZ = Radar.mc.player.posZ;
        RectHelper.drawBorderedRect((double)xOffset + 2.5, (double)yOffset + 2.5, (double)(xOffset + (float)sizeRect) - 2.5, (double)(yOffset + (float)sizeRect) - 2.5, 0.5, PaletteHelper.getColor(2), PaletteHelper.getColor(11));
        RectHelper.drawBorderedRect(xOffset + 3.0f, yOffset + 3.0f, xOffset + (float)sizeRect - 3.0f, yOffset + (float)sizeRect - 3.0f, 0.2, PaletteHelper.getColor(2), PaletteHelper.getColor(11));
        RectHelper.drawRect((double)xOffset + ((double)((float)sizeRect / 2.0f) - 0.5), (double)yOffset + 3.5, (double)xOffset + ((double)((float)sizeRect / 2.0f) + 0.2), (double)(yOffset + (float)sizeRect) - 3.5, PaletteHelper.getColor(155, 100));
        RectHelper.drawRect((double)xOffset + 3.5, (double)yOffset + ((double)((float)sizeRect / 2.0f) - 0.2), (double)(xOffset + (float)sizeRect) - 3.5, (double)yOffset + ((double)((float)sizeRect / 2.0f) + 0.5), PaletteHelper.getColor(155, 100));
        RectHelper.drawRectBetter(xOffset + 3.5f, yOffset + 3.5f, sizeRect - 7, 1.0, ClientHelper.getClientColor().getRGB());
        for (Entity entity : Radar.mc.world.loadedEntityList) {
            EntityPlayer entityPlayer;
            if (!(entity instanceof EntityPlayer) || (entityPlayer = (EntityPlayer)entity) == Radar.mc.player || entityPlayer.isInvisible()) continue;
            float partialTicks = Radar.mc.timer.renderPartialTicks;
            float posX = (float)(entityPlayer.posX + (entityPlayer.posX - entityPlayer.lastTickPosX) * (double)partialTicks - playerPosX) * (float)this.scale;
            float posZ = (float)(entityPlayer.posZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * (double)partialTicks - playerPosZ) * (float)this.scale;
            int color = Radar.mc.player.canEntityBeSeen(entityPlayer) ? new Color(255, 0, 0).getRGB() : new Color(255, 255, 0).getRGB();
            float cos = (float)Math.cos((double)Radar.mc.player.rotationYaw * (Math.PI / 180));
            float sin = (float)Math.sin((double)Radar.mc.player.rotationYaw * (Math.PI / 180));
            float rotY = -(posZ * cos - posX * sin);
            float rotX = -(posX * cos + posZ * sin);
            if (rotY > (float)sizeRect / 2.0f - 5.0f) {
                rotY = (float)sizeRect / 2.0f - 5.0f;
            } else if (rotY < -((float)sizeRect / 2.0f - 5.0f)) {
                rotY = -((float)sizeRect / 2.0f - 5.0f);
            }
            if (rotX > (float)sizeRect / 2.0f - 5.0f) {
                rotX = (float)sizeRect / 2.0f - 5.0f;
            } else if (rotX < -((float)sizeRect / 2.0f - 5.0f)) {
                rotX = -((float)sizeRect / 2.0f - 5.0f);
            }
            RectHelper.drawBorderedRect((double)(xOffset + (float)sizeRect / 2.0f + rotX) - 1.5, (double)(yOffset + (float)sizeRect / 2.0f + rotY) - 1.5, (double)(xOffset + (float)sizeRect / 2.0f + rotX) + 1.5, (double)(yOffset + (float)sizeRect / 2.0f + rotY) + 1.5, 0.5, color, ClientHelper.getClientColor().getRGB());
        }
    }
}

