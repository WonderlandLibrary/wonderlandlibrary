// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.render;

import java.util.HashMap;
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
import exhibition.management.friend.FriendManager;
import exhibition.util.RenderingUtil;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import exhibition.event.RegisterEvent;
import java.util.Iterator;
import exhibition.event.impl.EventNametagRender;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import exhibition.event.impl.EventRender3D;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Tags extends Module
{
    private final String INVISIBLES = "INVISIBLES";
    public static String ARMOR;
    public static String SCALE;
    
    public Tags(final ModuleData data) {
        super(data);
        ((HashMap<String, Setting<Boolean>>)this.settings).put(Tags.ARMOR, new Setting<Boolean>(Tags.ARMOR, true, "Show armor."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("INVISIBLES", new Setting<Boolean>("INVISIBLES", false, "Show invisibles."));
        ((HashMap<String, Setting<Double>>)this.settings).put(Tags.SCALE, new Setting<Double>(Tags.SCALE, 1.5, "Scale factor.", 0.1, 0.5, 3.0));
    }
    
    @RegisterEvent(events = { EventRender3D.class, EventNametagRender.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventRender3D) {
            final EventRender3D er = (EventRender3D)event;
            for (final Object o : Tags.mc.theWorld.playerEntities) {
                final EntityPlayer player = (EntityPlayer)o;
                if (!((HashMap<K, Setting<Boolean>>)this.settings).get("INVISIBLES").getValue()) {
                    if (player.isInvisible() || !(player instanceof EntityPlayer) || player instanceof EntityPlayerSP) {
                        continue;
                    }
                }
                final double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * er.renderPartialTicks - RenderManager.renderPosX;
                final double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * er.renderPartialTicks - RenderManager.renderPosY;
                final double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * er.renderPartialTicks - RenderManager.renderPosZ;
                this.renderNametag(player, x, y, z);
            }
        }
        if (event instanceof EventNametagRender) {
            event.setCancelled(true);
        }
    }
    
    public void renderNametag(final EntityPlayer player, final double x, final double y, final double z) {
        final double tempY = y + (player.isSneaking() ? 0.5 : 0.7);
        final double size = this.getSize(player) * -0.02;
        GlStateManager.pushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        Tags.mc.entityRenderer.setupCameraTransform(Tags.mc.timer.renderPartialTicks, 0);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate((float)x, (float)tempY + 1.6f, (float)z);
        GL11.glNormal3f(0.0f, 2.0f, 0.0f);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        final float var10001 = (Tags.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f;
        GlStateManager.rotate(RenderManager.playerViewX, var10001, 0.0f, 0.0f);
        GL11.glScaled(size, size, size);
        GlStateManager.disableLighting();
        final int width = Tags.mc.fontRendererObj.getStringWidth(player.getName() + " " + this.getHealth(player)) / 2;
        GlStateManager.enableTextures();
        RenderingUtil.rectangle(-width - 2, -(Tags.mc.fontRendererObj.FONT_HEIGHT - 6), width + 2, Tags.mc.fontRendererObj.FONT_HEIGHT + 1, -1728052224);
        GlStateManager.enableTextures();
        int color = -1;
        String str = player.getName();
        if (FriendManager.isFriend(str)) {
            color = 6083583;
            str = FriendManager.getAlias(str);
        }
        Tags.mc.fontRendererObj.drawStringWithShadow(str, -Tags.mc.fontRendererObj.getStringWidth(String.valueOf(player.getName()) + " " + this.getHealth(player)) / 2, 0.0f, color);
        final float health = player.getHealth();
        final float[] fractions = { 0.0f, 0.5f, 1.0f };
        final Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
        final float progress = health * 5.0f * 0.01f;
        final Color customColor = ESP2D.blendColors(fractions, colors, progress).brighter();
        Tags.mc.fontRendererObj.drawStringWithShadow((int)health + "", (Tags.mc.fontRendererObj.getStringWidth(String.valueOf(player.getName()) + " " + this.getHealth(player)) - Tags.mc.fontRendererObj.getStringWidth(this.getHealth(player)) * 2) / 2, 0.0f, customColor.getRGB());
        GlStateManager.disableBlend();
        if (((HashMap<K, Setting<Boolean>>)this.settings).get(Tags.ARMOR).getValue()) {
            this.renderArmor(player);
        }
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
                final ItemStack renderStack2 = armourStack2;
                this.renderItemStack(renderStack2, xOffset, -20);
                xOffset += 16;
            }
        }
    }
    
    private int getHealthColorHEX(final EntityPlayer e) {
        final int health = Math.round(20.0f * (e.getHealth() / e.getMaxHealth()));
        int color = 0;
        switch (health) {
            case 18:
            case 19: {
                color = 9108247;
                break;
            }
            case 16:
            case 17: {
                color = 10026904;
                break;
            }
            case 14:
            case 15: {
                color = 12844472;
                break;
            }
            case 12:
            case 13: {
                color = 16633879;
                break;
            }
            case 10:
            case 11: {
                color = 15313687;
                break;
            }
            case 8:
            case 9: {
                color = 16285719;
                break;
            }
            case 6:
            case 7: {
                color = 16286040;
                break;
            }
            case 4:
            case 5: {
                color = 15031100;
                break;
            }
            case 2:
            case 3: {
                color = 16711680;
                break;
            }
            case -1:
            case 0:
            case 1: {
                color = 16190746;
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
        final Entity ent = Tags.mc.thePlayer;
        final double dist = ent.getDistanceToEntity(player) / 5.0f;
        final float size = (dist <= 2.0) ? 1.3f : ((float)dist);
        return size;
    }
    
    private void renderItemStack(final ItemStack stack, final int x, final int y) {
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        Tags.mc.getRenderItem().zLevel = -150.0f;
        Tags.mc.getRenderItem().func_180450_b(stack, x, y);
        Tags.mc.getRenderItem().renderItemOverlays(Tags.mc.fontRendererObj, stack, x, y);
        Tags.mc.getRenderItem().zLevel = 0.0f;
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
            Tags.mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, 16711680);
            return;
        }
        if (stack.getItem() instanceof ItemArmor) {
            final int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
            final int projectileProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180308_g.effectId, stack);
            final int blastProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack);
            final int fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
            final int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
            final int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (protectionLevel > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("pr" + protectionLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (projectileProtectionLevel > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("pp" + projectileProtectionLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (blastProtectionLevel > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("bp" + blastProtectionLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (fireProtectionLevel > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("fp" + fireProtectionLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (thornsLevel > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("t" + thornsLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (unbreakingLevel > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("u" + unbreakingLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
        }
        if (stack.getItem() instanceof ItemBow) {
            final int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            final int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
            final int flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
            final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (powerLevel > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("po" + powerLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (punchLevel > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("pu" + punchLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (flameLevel > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("f" + flameLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (unbreakingLevel2 > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("u" + unbreakingLevel2, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
        }
        if (stack.getItem() instanceof ItemSword) {
            final int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
            final int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
            final int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
            final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (sharpnessLevel > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("sh" + sharpnessLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (knockbackLevel > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("kn" + knockbackLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (fireAspectLevel > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("f" + fireAspectLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (unbreakingLevel2 > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("ub" + unbreakingLevel2, x * 2, enchantmentY, -1052689);
            }
        }
        if (stack.getItem() instanceof ItemTool) {
            final int unbreakingLevel3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            final int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
            final int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
            final int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack);
            if (efficiencyLevel > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("eff" + efficiencyLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (fortuneLevel > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("fo" + fortuneLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (silkTouch > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("st" + silkTouch, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (unbreakingLevel3 > 0) {
                Tags.mc.fontRendererObj.drawStringWithShadow("ub" + unbreakingLevel3, x * 2, enchantmentY, -1052689);
            }
        }
        if (stack.getItem() == Items.golden_apple && stack.hasEffect()) {
            Tags.mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, -1052689);
        }
    }
    
    static {
        Tags.ARMOR = "ARMOR";
        Tags.SCALE = "SCALE";
    }
}
