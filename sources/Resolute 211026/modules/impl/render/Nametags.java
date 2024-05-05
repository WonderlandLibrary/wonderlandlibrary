// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import net.minecraft.init.Items;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemBow;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import java.text.DecimalFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTool;
import java.awt.Color;
import vip.Resolute.util.render.RenderUtils;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import vip.Resolute.events.impl.EventRenderNametag;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import vip.Resolute.events.impl.EventRender3D;
import vip.Resolute.events.Event;
import vip.Resolute.modules.Module;

public class Nametags extends Module
{
    public static boolean enabled;
    
    public Nametags() {
        super("Nametags", 0, "Renders custom nametags", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        Nametags.enabled = true;
    }
    
    @Override
    public void onDisable() {
        Nametags.enabled = false;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventRender3D) {
            final EventRender3D er = (EventRender3D)e;
            for (final Object o : Nametags.mc.theWorld.playerEntities) {
                final EntityPlayer player = (EntityPlayer)o;
                if (!player.isInvisible() && !(player instanceof EntityPlayerSP)) {
                    final double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * er.getPartialTicks() - RenderManager.renderPosX;
                    final double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * er.getPartialTicks() - RenderManager.renderPosY;
                    final double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * er.getPartialTicks() - RenderManager.renderPosZ;
                    this.renderNametag(player, x, y, z);
                }
            }
        }
        if (e instanceof EventRenderNametag) {
            e.setCancelled(true);
        }
    }
    
    public void renderNametag(final EntityPlayer player, final double x, final double y, final double z) {
        final double tempY = y + (player.isSneaking() ? 0.5 : 0.7);
        final double size = this.getSize(player) * -0.015;
        GlStateManager.pushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        Nametags.mc.entityRenderer.setupCameraTransform(Nametags.mc.timer.renderPartialTicks, 0);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate((float)x, (float)tempY + 1.6f, (float)z);
        GL11.glNormal3f(0.0f, 2.0f, 0.0f);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        final float var10001 = (Nametags.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f;
        GlStateManager.rotate(RenderManager.playerViewX, var10001, 0.0f, 0.0f);
        GL11.glScaled(size, size, size);
        GlStateManager.disableLighting();
        final int width = Nametags.mc.fontRendererObj.getStringWidth(player.getName() + " " + this.getHealth(player)) / 2;
        GlStateManager.enableTexture2D();
        RenderUtils.rectangle(-width - 2, -(Nametags.mc.fontRendererObj.FONT_HEIGHT - 6), width + 2, Nametags.mc.fontRendererObj.FONT_HEIGHT + 1, -1728052224);
        GlStateManager.enableTexture2D();
        final int color = -1;
        final String str = player.getName();
        Nametags.mc.fontRendererObj.drawStringWithShadow(str, (float)(-Nametags.mc.fontRendererObj.getStringWidth(player.getName() + " " + this.getHealth(player)) / 2), 0.0f, color);
        final float health = player.getHealth();
        final float[] fractions = { 0.0f, 0.5f, 1.0f };
        final Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
        final float progress = health / player.getMaxHealth();
        final Color customColor = (health >= 0.0f) ? RenderUtils.blendColors(fractions, colors, progress).brighter() : Color.RED;
        Nametags.mc.fontRendererObj.drawStringWithShadow((int)health + "", (float)((Nametags.mc.fontRendererObj.getStringWidth(player.getName() + " " + this.getHealth(player)) - Nametags.mc.fontRendererObj.getStringWidth(this.getHealth(player)) * 2) / 2), 0.0f, customColor.getRGB());
        GlStateManager.disableBlend();
        this.renderArmor(player);
        GlStateManager.enableBlend();
        GL11.glColor3d(1.0, 1.0, 1.0);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glPopMatrix();
    }
    
    public void renderArmor(final EntityPlayer player) {
        int xOffset = 0;
        for (final ItemStack armourStack : player.inventory.armorInventory) {
            if (armourStack != null) {
                xOffset -= 8;
            }
        }
        if (player.getHeldItem() != null) {
            xOffset -= 8;
            final ItemStack stock = player.getHeldItem().copy();
            if (stock.hasEffect() && (stock.getItem() instanceof ItemTool || stock.getItem() instanceof ItemArmor)) {
                stock.stackSize = 1;
            }
            this.renderItemStack(stock, xOffset, -20);
            xOffset += 16;
        }
        final ItemStack[] renderStack = player.inventory.armorInventory;
        for (int index = 3; index >= 0; --index) {
            final ItemStack armourStack2 = renderStack[index];
            if (armourStack2 != null) {
                this.renderItemStack(armourStack2, xOffset, -20);
                xOffset += 16;
            }
        }
    }
    
    private int getHealthColorHEX(final EntityPlayer e) {
        final int health = Math.round(20.0f * (e.getHealth() / e.getMaxHealth()));
        int color = 0;
        switch (health) {
            case -1:
            case 0:
            case 1: {
                color = 16190746;
                break;
            }
            case 2:
            case 3: {
                color = 16711680;
                break;
            }
            case 4:
            case 5: {
                color = 15031100;
                break;
            }
            case 6:
            case 7: {
                color = 16286040;
                break;
            }
            case 8:
            case 9: {
                color = 16285719;
                break;
            }
            case 10:
            case 11: {
                color = 15313687;
                break;
            }
            case 12:
            case 13: {
                color = 16633879;
                break;
            }
            case 14:
            case 15: {
                color = 12844472;
                break;
            }
            case 16:
            case 17: {
                color = 10026904;
                break;
            }
            case 18:
            case 19: {
                color = 9108247;
                break;
            }
            default: {
                color = -11746281;
                break;
            }
        }
        return color;
    }
    
    private String getHealth(final EntityPlayer e) {
        String hp = "";
        final DecimalFormat numberFormat = new DecimalFormat("#.0");
        final double abs = 2.0f * (e.getAbsorptionAmount() / 4.0f);
        double health = (10.0 + abs) * (e.getHealth() / e.getMaxHealth());
        health = Double.valueOf(numberFormat.format(health));
        if (Math.floor(health) == health) {
            hp = String.valueOf((int)health);
        }
        else {
            hp = String.valueOf(health);
        }
        return hp;
    }
    
    private float getSize(final EntityPlayer player) {
        final Entity ent = Nametags.mc.thePlayer;
        final double dist = ent.getDistanceToEntity(player) / 5.0f;
        final float size = (dist <= 2.0) ? 1.3f : ((float)dist);
        return size;
    }
    
    private void renderItemStack(final ItemStack stack, final int x, final int y) {
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        Nametags.mc.getRenderItem().zLevel = -150.0f;
        Nametags.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        Nametags.mc.getRenderItem().renderItemOverlays(Nametags.mc.fontRendererObj, stack, x, y);
        Nametags.mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.disableBlend();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        this.renderEnchantText(stack, x, y);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }
    
    private void renderEnchantText(final ItemStack stack, final int x, final int y) {
        int enchantmentY = y - 24;
        if (stack.getEnchantmentTagList() != null && stack.getEnchantmentTagList().tagCount() >= 6) {
            Nametags.mc.fontRendererObj.drawStringWithShadow("god", (float)(x * 2), (float)enchantmentY, 16711680);
        }
        else {
            if (stack.getItem() instanceof ItemArmor) {
                final int unbreakingLevel3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
                final int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack);
                final int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack);
                final int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
                final int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
                final int unbreakingLevel4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                if (unbreakingLevel3 > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("pr" + unbreakingLevel3, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
                if (efficiencyLevel > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("pp" + efficiencyLevel, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
                if (fortuneLevel > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("bp" + fortuneLevel, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
                if (silkTouch > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("fp" + silkTouch, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
                if (thornsLevel > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("t" + thornsLevel, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
                if (unbreakingLevel4 > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("u" + unbreakingLevel4, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
            }
            if (stack.getItem() instanceof ItemBow) {
                final int unbreakingLevel3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
                final int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
                final int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
                final int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                if (unbreakingLevel3 > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("po" + unbreakingLevel3, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
                if (efficiencyLevel > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("pu" + efficiencyLevel, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
                if (fortuneLevel > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("f" + fortuneLevel, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
                if (silkTouch > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("u" + silkTouch, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
            }
            if (stack.getItem() instanceof ItemSword) {
                final int unbreakingLevel3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
                final int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
                final int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
                final int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                if (unbreakingLevel3 > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("sh" + unbreakingLevel3, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
                if (efficiencyLevel > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("kn" + efficiencyLevel, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
                if (fortuneLevel > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("f" + fortuneLevel, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
                if (silkTouch > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("ub" + silkTouch, (float)(x * 2), (float)enchantmentY, -1052689);
                }
            }
            if (stack.getItem() instanceof ItemTool) {
                final int unbreakingLevel3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                final int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
                final int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
                final int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack);
                if (efficiencyLevel > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("eff" + efficiencyLevel, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
                if (fortuneLevel > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("fo" + fortuneLevel, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
                if (silkTouch > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("st" + silkTouch, (float)(x * 2), (float)enchantmentY, -1052689);
                    enchantmentY += 8;
                }
                if (unbreakingLevel3 > 0) {
                    Nametags.mc.fontRendererObj.drawStringWithShadow("ub" + unbreakingLevel3, (float)(x * 2), (float)enchantmentY, -1052689);
                }
            }
            if (stack.getItem() == Items.golden_apple && stack.hasEffect()) {
                Nametags.mc.fontRendererObj.drawStringWithShadow("god", (float)(x * 2), (float)enchantmentY, -1052689);
            }
        }
    }
    
    static {
        Nametags.enabled = false;
    }
}
