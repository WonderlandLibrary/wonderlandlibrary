/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.arithmo.module.impl.render;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventRenderEntity;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Chams
extends Module {
    public Chams(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventRenderEntity.class})
    public void onEvent(Event event) {
        if (event instanceof EventRenderEntity) {
            EventRenderEntity er = (EventRenderEntity)event;
            if (er.isPre()) {
                if (er.getEntity() instanceof EntityPlayer && er.getEntity() != Chams.mc.thePlayer) {
                    GL11.glPushAttrib((int)287181);
                    GlStateManager.clear(256);
                }
            } else {
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
                GL11.glPopAttrib();
            }
        }
    }
}

