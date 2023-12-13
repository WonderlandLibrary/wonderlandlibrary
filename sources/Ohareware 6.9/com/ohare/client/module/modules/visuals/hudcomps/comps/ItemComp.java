package com.ohare.client.module.modules.visuals.hudcomps.comps;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.ohare.client.module.modules.visuals.hudcomps.HudComp;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.util.StringUtils;


/**
 * made by oHare for oHareWare
 *
 * @since 5/31/2019
 **/
public class ItemComp extends HudComp {
    private ScaledResolution sr = RenderUtil.getResolution();

    public ItemComp() {
        super("ItemComp", 2, 103, 130, 50);
    }

    @Override
    public void onRender(ScaledResolution scaledResolution) {
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getHeldItem() != null) {
            setWidth(46 + Fonts.clickfont.getStringWidth("Name: " + StringUtils.stripControlCodes(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem().getItemStackDisplayName(Minecraft.getMinecraft().thePlayer.getHeldItem()))));
            RenderUtil.drawBorderedRect(x, y, width, height, 0.5, new Color(0, 0, 0, 244).getRGB(), new Color(0, 0, 0, 155).getRGB());
            GL11.glPushMatrix();
            GlStateManager.scale(2,2,2);
            RenderItem ir = new RenderItem(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().modelManager);
            RenderHelper.enableGUIStandardItemLighting();
            ir.renderItemIntoGUI(Minecraft.getMinecraft().thePlayer.getHeldItem(), (int) (x + 7) / 2, (int) (y + 4)/ 2);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.scale(1,1,1);
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.clear(256);
            GL11.glPopMatrix();
            if (Minecraft.getMinecraft().thePlayer.getHeldItem().getMaxDamage() > 0) {
                int damage = Minecraft.getMinecraft().thePlayer.getHeldItem().getMaxDamage() - Minecraft.getMinecraft().thePlayer.getHeldItem().getItemDamage();
                Fonts.clickfont.drawStringWithShadow("Dura: " + String.valueOf(damage), (float)x + 7, (float)y + (float)height - 11, -1);
            } else if (Minecraft.getMinecraft().thePlayer.getHeldItem().isStackable()) {
                Fonts.clickfont.drawStringWithShadow("Stack Size: " + Minecraft.getMinecraft().thePlayer.getHeldItem().stackSize, (float)x + 7, (float)y + (float)height - 11, -1);
            }
            Fonts.clickfont.drawStringWithShadow("Name: " + StringUtils.stripControlCodes(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem().getItemStackDisplayName(Minecraft.getMinecraft().thePlayer.getHeldItem())),(float)x + 40,(float)y + 12,-1);
            Fonts.clickfont.drawStringWithShadow("ID: " + Item.getIdFromItem(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem()),(float)x + 40,(float)y + 24,-1);

        }
    }

    @Override
    public void onResize(ScaledResolution scaledResolution) {
        if (sr.getScaledHeight() != scaledResolution.getScaledHeight()) {
            sr = scaledResolution;
        }
    }

    @Override
    public void onFullScreen(float w, float h) {
        if (sr.getScaledHeight() != new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight()) {
            sr = new ScaledResolution(Minecraft.getMinecraft());
        }
    }
}