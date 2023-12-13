package com.ohare.client.module.modules.visuals;

import com.ohare.client.Client;
import com.ohare.client.event.events.render.Render2DEvent;
import com.ohare.client.module.Module;
import com.ohare.client.module.modules.combat.AntiBot;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.value.impl.BooleanValue;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * made by oHare for oHareWare
 *
 * @since 5/29/2019
 **/
public class ESP extends Module {
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue animals = new BooleanValue("Animals", true);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    private BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private BooleanValue passives = new BooleanValue("Passives", true);
    private BooleanValue box = new BooleanValue("Box", true);
    private BooleanValue health = new BooleanValue("Health", true);
    private BooleanValue armor = new BooleanValue("Armor", true);
    private BooleanValue items = new BooleanValue("Items", false);

    public ESP() {
        super("ESP", Category.VISUALS, new Color(255, 120, 120, 255).getRGB());
        setDescription("ESP");
        addValues(players, animals, mobs, invisibles, passives, box, health, armor, items);
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        mc.theWorld.getLoadedEntityList().forEach(entity -> {
            if (entity instanceof EntityItem && items.isEnabled()) {
                EntityItem ent = (EntityItem) entity;
                if (RenderUtil.isInViewFrustrum(ent)) {
                    double x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, event.getPT());
                    double y = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, event.getPT());
                    double z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, event.getPT());
                    double width = entity.width / 1.25;
                    double height = entity.height + (entity.isSneaking() ? -0.3 : 0.2);
                    AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                    List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                    mc.entityRenderer.setupCameraTransform(event.getPT(), 0);
                    Vector4d position = null;
                    for (Vector3d vector : vectors) {
                        vector = RenderUtil.project(vector.x - mc.getRenderManager().viewerPosX, vector.y - mc.getRenderManager().viewerPosY, vector.z - mc.getRenderManager().viewerPosZ);
                        if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                            if (position == null) {
                                position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                            }
                            position.x = Math.min(vector.x, position.x);
                            position.y = Math.min(vector.y, position.y);
                            position.z = Math.max(vector.x, position.z);
                            position.w = Math.max(vector.y, position.w);
                        }
                    }
                    mc.entityRenderer.setupOverlayRendering();
                    if (position != null) {
                        GL11.glPushMatrix();
                        RenderUtil.drawBorderedRect(position.x - 1.5, position.y - 0.5, position.z - position.x + 4.0, position.w - position.y + 1.0, 1.5f, -16777216, 0);
                        RenderUtil.drawBorderedRect(position.x - 1, position.y, position.z - position.x + 3, position.w - position.y, 0.5f, -1, 0);
                        if (ent.getEntityItem().getMaxDamage() > 0) {
                            double offset = position.w - position.y;
                            double percentoffset = offset / ent.getEntityItem().getMaxDamage();
                            double finalnumber = percentoffset * (ent.getEntityItem().getMaxDamage() - ent.getEntityItem().getItemDamage());
                            RenderUtil.drawBorderedRect(position.x - 3.5, position.y - 0.5, 1.5, position.w - position.y + 1, 0.5, 0xff000000, 0x60000000);
                            RenderUtil.drawRect(position.x - 3, position.y + offset, 0.5, -finalnumber, 0xff3E83E3);
                        }
                        GL11.glScalef(0.5f,0.5f,0.5f);
                        final String nametext = StringUtils.stripControlCodes(ent.getEntityItem().getItem().getItemStackDisplayName(ent.getEntityItem())) + (ent.getEntityItem().getMaxDamage() > 0 ? "§9 : " + (ent.getEntityItem().getMaxDamage() - ent.getEntityItem().getItemDamage()) : "");
                        RenderUtil.drawCustomString(nametext, (float)((position.x + ((position.z - position.x) / 2)) - (mc.fontRendererObj.getStringWidth(nametext) / 4)) * 2, (float)(position.y - mc.fontRendererObj.FONT_HEIGHT + 2) * 2, -1);
                        GL11.glScalef(1.0f,1.0f,1.0f);
                        GL11.glPopMatrix();
                    }
                }
            }
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) entity;
                if (isValid(ent) && RenderUtil.isInViewFrustrum(ent)) {
                    double x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, event.getPT());
                    double y = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, event.getPT());
                    double z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, event.getPT());
                    double width = entity.width / 1.5;
                    double height = entity.height + (entity.isSneaking() ? -0.3 : 0.2);
                    AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                    List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                    mc.entityRenderer.setupCameraTransform(event.getPT(), 0);
                    Vector4d position = null;
                    for (Vector3d vector : vectors) {
                        vector = RenderUtil.project(vector.x - mc.getRenderManager().viewerPosX, vector.y - mc.getRenderManager().viewerPosY, vector.z - mc.getRenderManager().viewerPosZ);
                        if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                            if (position == null) {
                                position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                            }
                            position.x = Math.min(vector.x, position.x);
                            position.y = Math.min(vector.y, position.y);
                            position.z = Math.max(vector.x, position.z);
                            position.w = Math.max(vector.y, position.w);
                        }
                    }
                    mc.entityRenderer.setupOverlayRendering();
                    if (position != null) {
                        GL11.glPushMatrix();
                        RenderUtil.drawBorderedRect(position.x - 1.5, position.y - 0.5, position.z - position.x + 4.0, position.w - position.y + 1.0, 1.5f, -16777216, 0);
                        RenderUtil.drawBorderedRect(position.x - 1, position.y, position.z - position.x + 3, position.w - position.y, 0.5f, Client.INSTANCE.getFriendManager().isFriend((entity).getName()) ? 0xFF7FCDFF : ((entity).getName().equalsIgnoreCase(mc.thePlayer.getName()) ? 0xFF99ff99 : new Color(0xFFA59C).getRGB()), 0);
                        if (ent.getHealth() > 0.0f && health.isEnabled()) {
                            double offset = position.w - position.y;
                            double percentoffset = offset / ent.getMaxHealth();
                            double finalnumber = percentoffset * ent.getHealth();
                            RenderUtil.drawBorderedRect(position.x - 3.5, position.y - 0.5, 1.5, position.w - position.y + 1, 0.5, 0xff000000, 0x60000000);
                            RenderUtil.drawRect(position.x - 3, position.y + offset, 0.5, -finalnumber, getHealthColor(ent));
                        }
                        if (ent instanceof EntityPlayer) {
                            double armorstrength = 0;
                            EntityPlayer player = (EntityPlayer) entity;
                            for (int index = 3; index >= 0; index--) {
                                ItemStack stack = player.inventory.armorInventory[index];
                                if (stack != null) {
                                    armorstrength += getArmorStrength(stack);
                                }
                            }
                            if (armorstrength > 0.0f && armor.isEnabled()) {
                                double percent = (armorstrength > 40 ? 40 : armorstrength) / 40;
                                double offset = position.w - position.y;
                                RenderUtil.drawBorderedRect(position.z + 3, position.y - 0.5, 1.5, position.w - position.y + 1, 0.5, 0xff000000, 0x60000000);
                                RenderUtil.drawRect(position.z + 3.5, position.y + offset, 0.5, -(position.w - position.y) * percent, 0xff4798D1);
                            }
                        }
                        GL11.glPopMatrix();
                    }
                }
            }
        });
    }

    private boolean isValid(EntityLivingBase entity) {
        return !AntiBot.getBots().contains(entity) && entity != mc.thePlayer && isValidType(entity) && entity.getEntityId() != -1488 && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players.isEnabled() && entity instanceof EntityPlayer) || (mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime) || (passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals.isEnabled() && entity instanceof EntityAnimal));
    }

    private double getArmorStrength(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) return -1;
        float damageReduction = ((ItemArmor) itemStack.getItem()).damageReduceAmount;
        Map enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            int level = (int) enchantments.get(Enchantment.protection.effectId);
            damageReduction += Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }

    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 1.0F) | 0xFF000000;
    }
}