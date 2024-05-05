package com.initial.modules.impl.visual;

import com.initial.modules.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.renderer.entity.*;
import com.initial.utils.player.*;
import java.util.*;
import com.initial.events.*;
import net.minecraft.item.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.*;
import com.initial.events.impl.*;

public class NameTags extends Module
{
    public NameTags() {
        super("NameTags", 0, Category.VISUAL);
    }
    
    @EventTarget
    public void on3D(final EventRender3D e) {
        for (final Object o : this.mc.theWorld.playerEntities) {
            final EntityPlayer player = (EntityPlayer)o;
            if (player.isInvisible()) {
                continue;
            }
            final double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * e.getPartialTicks() - RenderManager.renderPosX;
            final double y2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * e.getPartialTicks() - RenderManager.renderPosY;
            final double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * e.getPartialTicks() - RenderManager.renderPosZ;
            TagUtils.renderNametag(player, x, y2, z);
        }
    }
    
    public static void renderItem(final ItemStack stack, final int x, final int y) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().zLevel = -100.0f;
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
        GlStateManager.enableDepth();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x, y + 8);
        Minecraft.getMinecraft().getRenderItem().zLevel = 0.0f;
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }
    
    @EventTarget
    public void onUpdate(final EventNamePlayer e) {
        e.setCancelled(true);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        this.setDisplayName("Name Tags");
    }
}
