package com.initial.utils.player;

import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import net.minecraft.enchantment.*;
import java.util.*;
import net.minecraft.nbt.*;

public class TagUtils
{
    static Minecraft mc;
    
    public static void renderNametag(final EntityPlayer player, final double x, final double y2, final double z) {
        String entityName = player.getDisplayName().getFormattedText();
        if (player == TagUtils.mc.thePlayer) {
            return;
        }
        if (player.isDead) {
            return;
        }
        if (player.capabilities.isFlying) {
            entityName = "§a[F] §r" + entityName;
        }
        if (player.capabilities.isCreativeMode) {
            entityName = "\u00148[C] §r" + entityName;
        }
        if (player.getDistanceToEntity(TagUtils.mc.thePlayer) >= 64.0f) {
            entityName = "§r" + entityName;
        }
        final double health;
        final double percentage;
        final String healthColor = ((percentage = 100.0 * ((health = (int)player.getHealth()) / (player.getMaxHealth() / 2.0f))) > 75.0) ? "2" : ((percentage > 50.0) ? "e" : ((percentage > 25.0) ? "6" : "4"));
        final String healthDisplay = String.valueOf(health);
        entityName = String.format("%s §%s %s", entityName, healthColor, healthDisplay);
        final float distance = TagUtils.mc.thePlayer.getDistanceToEntity(player);
        final float var13 = Math.max(distance / 5.0f, 2.0f) * 0.95f;
        final float var14 = 0.016666668f * var13;
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(x + 0.0, y2 + player.height + 0.5, z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        if (TagUtils.mc.gameSettings.thirdPersonView == 2) {
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(RenderManager.playerViewX, -1.0f, 0.0f, 0.0f);
        }
        else {
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        }
        GlStateManager.scale(-var14, -var14, var14);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int var15 = 0;
        if (player.isSneaking()) {
            var15 += 4;
        }
        if ((var15 -= (int)(distance / 5.0f)) < -8) {
            var15 = -8;
        }
        GlStateManager.disableTexture2D();
        final int var16 = TagUtils.mc.fontRendererObj.getStringWidth(entityName) / 2;
        GlStateManager.enableTexture2D();
        TagUtils.mc.fontRendererObj.func_175065_a(entityName, (float)(-var16), (float)var15, -1, true);
        final ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        if (player.getCurrentEquippedItem() != null) {
            items.add(player.getCurrentEquippedItem());
        }
        for (int index = 3; index >= 0; --index) {
            final ItemStack stack = player.inventory.armorInventory[index];
            if (stack != null) {
                items.add(stack);
            }
        }
        final int offset = var16 - (items.size() - 1) * 9 - 9;
        int xPos = 0;
        for (final ItemStack stack2 : items) {
            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            TagUtils.mc.getRenderItem().zLevel = -100.0f;
            TagUtils.mc.getRenderItem().func_175030_a(TagUtils.mc.fontRendererObj, stack2, -var16 + offset + xPos, var15 - 20);
            TagUtils.mc.getRenderItem().zLevel = 0.0f;
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            if (stack2.getItem() == Items.golden_apple && stack2.hasEffect()) {
                TagUtils.mc.fontRendererObj.drawStringWithShadow("god", (-var16 + offset + xPos) * 2, (var15 - 20) * 2, -65536);
            }
            else {
                final NBTTagList enchants = stack2.getEnchantmentTagList();
                if (enchants != null) {
                    int encY = 0;
                    final Enchantment[] important = { Enchantment.protection, Enchantment.unbreaking, Enchantment.sharpness, Enchantment.fireAspect, Enchantment.efficiency, Enchantment.featherFalling, Enchantment.power, Enchantment.flame, Enchantment.punch, Enchantment.fortune, Enchantment.infinity, Enchantment.thorns };
                    if (enchants.tagCount() >= 6) {
                        TagUtils.mc.fontRendererObj.drawStringWithShadow("god", (-var16 + offset + xPos) * 2, (var15 - 20) * 2, -65536);
                    }
                    else {
                        for (int index2 = 0; index2 < enchants.tagCount(); ++index2) {
                            final short id = enchants.getCompoundTagAt(index2).getShort("id");
                            final short level = enchants.getCompoundTagAt(index2).getShort("lvl");
                            final Enchantment enc = Enchantment.getEnchantmentById(id);
                            if (enc != null) {
                                for (final Enchantment importantEnchantment : important) {
                                    if (enc == importantEnchantment) {
                                        String encName = enc.getTranslatedName(level).substring(0, 1).toLowerCase();
                                        encName = ((level > 99) ? (String.valueOf(String.valueOf(encName)) + "99+") : (String.valueOf(String.valueOf(String.valueOf(encName))) + level));
                                        TagUtils.mc.fontRendererObj.drawStringWithShadow(encName, (-var16 + offset + xPos) * 2, (var15 - 20 + encY) * 2, -5592406);
                                        encY += 5;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
            xPos += 18;
        }
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
    
    static {
        TagUtils.mc = Minecraft.getMinecraft();
    }
}
