/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.module.render;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.Event3D;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.MathUtils;
import me.Tengoku.Terror.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import org.lwjgl.opengl.GL11;

public class NameTags
extends Module {
    public double scale = 0.1;
    public boolean armor = true;

    @EventTarget
    private void onRender3DEvent(Event3D event3D) {
        if (Minecraft.theWorld != null) {
            boolean bl = Minecraft.gameSettings.viewBobbing;
            for (Object e : Minecraft.theWorld.loadedEntityList) {
                Entity entity = (Entity)e;
                if (entity == Minecraft.thePlayer || entity.isInvisible() || !(entity instanceof EntityPlayer)) continue;
                double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)NameTags.mc.timer.renderPartialTicks - RenderManager.renderPosX;
                double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)NameTags.mc.timer.renderPartialTicks - RenderManager.renderPosY;
                double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)NameTags.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
                this.renderNameTag((EntityPlayer)entity, d, d2, d3, event3D.getPartialTicks());
            }
            Minecraft.gameSettings.viewBobbing = bl;
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    public NameTags() {
        super("Name Tags", 0, Category.RENDER, "Renders every player's nametag in a better way.");
    }

    private void renderLivingLabel(Entity entity, double d, double d2, double d3) {
        float f = Minecraft.thePlayer.getDistanceToEntity(entity);
        float f2 = 0.02672f;
        float f3 = (float)(f <= 8.0f ? 8.0 * this.scale : (double)f * this.scale);
        f2 *= f3;
        int n = Minecraft.fontRendererObj.getStringWidth(String.valueOf(entity.getDisplayName().getFormattedText()) + " " + MathUtils.roundToPlace(((EntityPlayer)entity).getHealth(), 2)) / 2;
        String string = entity.getDisplayName().getFormattedText();
        String string2 = "\ufffd";
        double d4 = MathUtils.roundToPlace(((EntityPlayer)entity).getHealth(), 2);
        string2 = d4 >= 12.0 ? String.valueOf(string2) + "2" : (d4 >= 4.0 ? String.valueOf(string2) + "6" : String.valueOf(string2) + "4");
        string = String.valueOf(string) + " " + string2 + d4;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)d + 0.0f, (float)d2 + entity.height + 0.5f, (float)d3);
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        RenderManager renderManager = NameTags.mc.renderManager;
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        renderManager = NameTags.mc.renderManager;
        GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-f2, -f2, f2);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        boolean bl = false;
        GlStateManager.disableTexture2D();
        worldRenderer.startDrawingQuads();
        worldRenderer.color(0.0f, 0.0f, 0.0f, 0.5f);
        tessellator.draw();
        GlStateManager.enableTexture2D();
        Minecraft.fontRendererObj.drawString(string, -n, (double)bl, -1);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        Minecraft.fontRendererObj.drawString(string, -n, (double)bl, -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    private double interpolate(double d, double d2, float f) {
        return d + (d2 - d) * (double)f;
    }

    private void renderItemStack(ItemStack itemStack, int n, int n2) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        NameTags.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableAlpha();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        mc.getRenderItem().renderItemAboveHead(itemStack, n, n2);
        mc.getRenderItem().renderItemOverlays(Minecraft.fontRendererObj, itemStack, n, n2);
        NameTags.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        this.renderEnchantmentText(itemStack, n, n2);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }

    private String getDisplayName(EntityLivingBase entityLivingBase) {
        String string = entityLivingBase.getDisplayName().getFormattedText();
        String string2 = "\ufffd";
        double d = MathUtils.roundToPlace(entityLivingBase.getHealth(), 2);
        string2 = d >= 12.0 ? String.valueOf(string2) + "2" : (d >= 4.0 ? String.valueOf(string2) + "6" : String.valueOf(string2) + "4");
        string = String.valueOf(string) + " | " + string2 + d;
        return string;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void renderEnchantmentText(ItemStack itemStack, int n, int n2) {
        int n3 = n2 - 24;
        if (itemStack.getEnchantmentTagList() != null && itemStack.getEnchantmentTagList().tagCount() >= 6) {
            Minecraft.fontRendererObj.drawString("god", n * 2, n3, -43177);
        } else {
            int n4;
            int n5;
            int n6;
            int n7 = -5592406;
            if (itemStack.getItem() instanceof ItemArmor) {
                n6 = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack);
                n5 = EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, itemStack);
                n4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, itemStack);
                int n8 = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, itemStack);
                int n9 = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, itemStack);
                int n10 = EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, itemStack);
                int n11 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack);
                if (n6 > 0) {
                    Minecraft.fontRendererObj.drawString("pr" + n6, n * 2, n3, n7);
                    n3 += 8;
                }
                if (n11 > 0) {
                    Minecraft.fontRendererObj.drawString("un" + n11, n * 2, n3, n7);
                    n3 += 8;
                }
                if (n5 > 0) {
                    Minecraft.fontRendererObj.drawString("pp" + n5, n * 2, n3, n7);
                    n3 += 8;
                }
                if (n4 > 0) {
                    Minecraft.fontRendererObj.drawString("bp" + n4, n * 2, n3, n7);
                    n3 += 8;
                }
                if (n8 > 0) {
                    Minecraft.fontRendererObj.drawString("fp" + n8, n * 2, n3, n7);
                    n3 += 8;
                }
                if (n9 > 0) {
                    Minecraft.fontRendererObj.drawString("tho" + n9, n * 2, n3, n7);
                    n3 += 8;
                }
                if (n10 > 0) {
                    Minecraft.fontRendererObj.drawString("ff" + n10, n * 2, n3, n7);
                    n3 += 8;
                }
            }
            if (itemStack.getItem() instanceof ItemBow) {
                n6 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack);
                n5 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStack);
                n4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStack);
                if (n6 > 0) {
                    Minecraft.fontRendererObj.drawString("po" + n6, n * 2, n3, n7);
                    n3 += 8;
                }
                if (n5 > 0) {
                    Minecraft.fontRendererObj.drawString("pu" + n5, n * 2, n3, n7);
                    n3 += 8;
                }
                if (n4 > 0) {
                    Minecraft.fontRendererObj.drawString("fl" + n4, n * 2, n3, n7);
                    n3 += 8;
                }
            }
            if (itemStack.getItem() instanceof ItemPickaxe) {
                n6 = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
                n5 = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemStack);
                if (n6 > 0) {
                    Minecraft.fontRendererObj.drawString("ef" + n6, n * 2, n3, n7);
                    n3 += 8;
                }
                if (n5 > 0) {
                    Minecraft.fontRendererObj.drawString("fo" + n5, n * 2, n3, n7);
                    n3 += 8;
                }
            }
            if (itemStack.getItem() instanceof ItemAxe) {
                n6 = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack);
                n5 = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack);
                n4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
                if (n6 > 0) {
                    Minecraft.fontRendererObj.drawString("sh" + n6, n * 2, n3, n7);
                    n3 += 8;
                }
                if (n5 > 0) {
                    Minecraft.fontRendererObj.drawString("fa" + n5, n * 2, n3, n7);
                    n3 += 8;
                }
                if (n4 > 0) {
                    Minecraft.fontRendererObj.drawString("ef" + n4, n * 2, n3, n7);
                    n3 += 8;
                }
            }
            if (itemStack.getItem() instanceof ItemSword) {
                n6 = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack);
                n5 = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, itemStack);
                n4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack);
                if (n6 > 0) {
                    Minecraft.fontRendererObj.drawString("sh" + n6, n * 2, n3, n7);
                    n3 += 8;
                }
                if (n5 > 0) {
                    Minecraft.fontRendererObj.drawString("kn" + n5, n * 2, n3, n7);
                    n3 += 8;
                }
                if (n4 > 0) {
                    Minecraft.fontRendererObj.drawString("fa" + n4, n * 2, n3, n7);
                    n3 += 8;
                }
            }
            if (itemStack.getItem() == Items.golden_apple && itemStack.hasEffect()) {
                Minecraft.fontRendererObj.drawStringWithShadow("god", n * 2, n3, -3977919);
            }
        }
    }

    private int getDisplayColour(EntityPlayer entityPlayer) {
        int n = -5592406;
        if (entityPlayer.isInvisible()) {
            n = -1113785;
        } else if (entityPlayer.isSneaking()) {
            n = -5592406;
        }
        return n;
    }

    private void renderNameTag(EntityPlayer entityPlayer, double d, double d2, double d3, float f) {
        ItemStack itemStack;
        ItemStack itemStack2;
        double d4 = d2 + (entityPlayer.isSneaking() ? 0.5 : 0.7);
        Entity entity = mc.getRenderViewEntity();
        double d5 = entity.posX;
        double d6 = entity.posY;
        double d7 = entity.posZ;
        entity.posX = this.interpolate(entity.prevPosX, entity.posX, f);
        entity.posY = this.interpolate(entity.prevPosY, entity.posY, f);
        entity.posZ = this.interpolate(entity.prevPosZ, entity.posZ, f);
        mc.getRenderManager();
        mc.getRenderManager();
        mc.getRenderManager();
        double d8 = entity.getDistance(d + RenderManager.viewerPosX, d2 + RenderManager.viewerPosY, d3 + RenderManager.viewerPosZ);
        Minecraft.getMinecraft();
        int n = Minecraft.fontRendererObj.getStringWidth(this.getDisplayName(entityPlayer)) / 2;
        double d9 = 0.0018 + (double)0.003f * d8;
        if (d8 <= 8.0) {
            d9 = 0.02;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)d, (float)d4 + 1.4f, (float)d3);
        mc.getRenderManager();
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        float f2 = Minecraft.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f;
        mc.getRenderManager();
        GlStateManager.rotate(RenderManager.playerViewX, f2, 0.0f, 0.0f);
        GlStateManager.scale(-d9, -d9, d9);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        float f3 = -n - 2;
        Minecraft.getMinecraft();
        RenderUtils.drawBorderedRectReliant(f3, -(Minecraft.fontRendererObj.FONT_HEIGHT + 1), (float)n + 2.0f, 1.5f, 1.6f, 0x77000000, 0x55000000);
        GlStateManager.enableAlpha();
        String string = this.getDisplayName(entityPlayer);
        float f4 = -n;
        Minecraft.getMinecraft();
        Minecraft.fontRendererObj.drawStringWithShadow(string, f4, -(Minecraft.fontRendererObj.FONT_HEIGHT - 1), this.getDisplayColour(entityPlayer));
        GlStateManager.pushMatrix();
        int n2 = 0;
        int n3 = 3;
        while (n3 >= 0) {
            itemStack2 = entityPlayer.inventory.armorInventory[n3];
            if (itemStack2 != null) {
                n2 -= 8;
            }
            --n3;
        }
        if (entityPlayer.getCurrentEquippedItem() != null) {
            n2 -= 8;
            itemStack = entityPlayer.getCurrentEquippedItem().copy();
            if (itemStack.hasEffect() && (itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemArmor)) {
                itemStack.stackSize = 1;
            }
            this.renderItemStack(itemStack, n2, -26);
            n2 += 16;
        }
        n3 = 3;
        while (n3 >= 0) {
            itemStack2 = entityPlayer.inventory.armorInventory[n3];
            if (itemStack2 != null) {
                itemStack = itemStack2.copy();
                if (itemStack.hasEffect() && (itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemArmor)) {
                    itemStack.stackSize = 1;
                }
                this.renderItemStack(itemStack, n2, -26);
                n2 += 16;
            }
            --n3;
        }
        GlStateManager.popMatrix();
        entity.posX = d5;
        entity.posY = d6;
        entity.posZ = d7;
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }
}

