/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.arithmo.module.impl.hud;

import java.util.ArrayList;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventRenderGui;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import org.lwjgl.opengl.GL11;

public class ArmorStatus
extends Module {
    public ArmorStatus(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventRenderGui.class})
    public void onEvent(Event e) {
        EventRenderGui event = (EventRenderGui)e;
        boolean currentItem = true;
        GL11.glPushMatrix();
        ArrayList<ItemStack> stuff = new ArrayList<ItemStack>();
        boolean onwater = ArmorStatus.mc.thePlayer.isEntityAlive() && ArmorStatus.mc.thePlayer.isInsideOfMaterial(Material.water);
        int split = -3;
        for (int index = 3; index >= 0; --index) {
            ItemStack armer = ArmorStatus.mc.thePlayer.inventory.armorInventory[index];
            if (armer == null) continue;
            stuff.add(armer);
        }
        if (ArmorStatus.mc.thePlayer.getCurrentEquippedItem() != null) {
            stuff.add(ArmorStatus.mc.thePlayer.getCurrentEquippedItem());
        }
        for (ItemStack errything : stuff) {
            if (ArmorStatus.mc.theWorld != null) {
                RenderHelper.enableGUIStandardItemLighting();
                split += 16;
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(256);
            GlStateManager.enableBlend();
            ArmorStatus.mc.getRenderItem().zLevel = -150.0f;
            mc.getRenderItem().func_180450_b(errything, split + event.getResolution().getScaledWidth() / 2 - 4, event.getResolution().getScaledHeight() - (onwater ? 65 : 55));
            mc.getRenderItem().renderItemOverlays(ArmorStatus.mc.fontRendererObj, errything, split + event.getResolution().getScaledWidth() / 2 - 4, event.getResolution().getScaledHeight() - (onwater ? 65 : 55));
            ArmorStatus.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.disableBlend();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
            errything.getEnchantmentTagList();
        }
        GL11.glPopMatrix();
    }
}

