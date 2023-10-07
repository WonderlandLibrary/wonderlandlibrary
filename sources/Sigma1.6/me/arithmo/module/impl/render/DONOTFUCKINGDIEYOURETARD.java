/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.render;

import java.awt.Color;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventRenderGui;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public class DONOTFUCKINGDIEYOURETARD
extends Module {
    public DONOTFUCKINGDIEYOURETARD(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventRenderGui.class})
    public void onEvent(Event event) {
        if (event instanceof EventRenderGui) {
            EventRenderGui er = (EventRenderGui)event;
            int width = er.getResolution().getScaledWidth() / 2;
            int height = er.getResolution().getScaledHeight() / 2;
            String XD = "" + (int)DONOTFUCKINGDIEYOURETARD.mc.thePlayer.getHealth();
            int XDDD = DONOTFUCKINGDIEYOURETARD.mc.fontRendererObj.getStringWidth(XD);
            float health = DONOTFUCKINGDIEYOURETARD.mc.thePlayer.getHealth();
            if (health > 20.0f) {
                health = 20.0f;
            }
            int red = (int)Math.abs(health * 5.0f * 0.01f * 0.0f + (1.0f - health * 5.0f * 0.01f) * 255.0f);
            int green = (int)Math.abs(health * 5.0f * 0.01f * 255.0f + (1.0f - health * 5.0f * 0.01f) * 0.0f);
            Color customColor = new Color(red, green, 0).brighter();
            DONOTFUCKINGDIEYOURETARD.mc.fontRendererObj.drawStringWithShadow(XD, (- XDDD) / 2 + width, height - 17, customColor.getRGB());
        }
    }
}

