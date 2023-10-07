package com.ohare.client.module.modules.visuals;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.ohare.client.Client;
import com.ohare.client.event.events.render.Render2DEvent;
import com.ohare.client.event.events.render.RenderNametagEvent;
import com.ohare.client.module.Module;
import com.ohare.client.module.modules.combat.AntiBot;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.value.impl.BooleanValue;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * made by oHare for oHareWare
 *
 * @since 7/3/2019
 **/
public class Nametags extends Module {
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue animals = new BooleanValue("Animals", true);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    private BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private BooleanValue passives = new BooleanValue("Passives", true);
    private BooleanValue health = new BooleanValue("Health", true);
    private BooleanValue armor = new BooleanValue("Armor", true);
    private BooleanValue potions = new BooleanValue("Potions", true);

    public Nametags() {
        super("Nametags", Category.VISUALS, new Color(0xFF90B1).getRGB());
        addValues(players, animals, mobs, invisibles, passives, health, armor, potions);
    }

    @Subscribe
    public void onNameTagRender(RenderNametagEvent event) {
        event.setCanceled(true);
    }

    @Subscribe
    public void onWorldToScreen(Render2DEvent event) {
        mc.theWorld.loadedEntityList.forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) entity;
                if (isValid(ent) && RenderUtil.isInViewFrustrum(ent)) {
                    double posX = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, event.getPT());
                    double posY = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, event.getPT());
                    double posZ = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, event.getPT());
                    double width = entity.width / 1.5;
                    double height = entity.height + (entity.isSneaking() ? -0.3 : 0.2);
                    AxisAlignedBB aabb = new AxisAlignedBB(posX - width, posY, posZ - width, posX + width, posY + height, posZ + width);
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
                        if (ent instanceof EntityPlayer) {
                            if (armor.isEnabled())
                                drawArmor((EntityPlayer) ent, (int) (position.x + ((position.z - position.x) / 2)), (int) position.y - 4 - mc.fontRendererObj.FONT_HEIGHT * 2);
                            GlStateManager.scale(.5f, .5f, .5f);
                            if (potions.isEnabled())
                                drawPotions((EntityPlayer) ent, (int) (position.x + ((position.z - position.x) / 2)) * 2, ((int) position.y - 4 - mc.fontRendererObj.FONT_HEIGHT) * 2);
                            float x = (float) position.x * 2;
                            float x2 = (float) position.z * 2;
                            float y = (float) position.y * 2;
                            final String nametext = "(" + Math.round(mc.thePlayer.getDistance(ent.posX, ent.posY, ent.posZ)) + "m) " + (((Client.INSTANCE.getFriendManager().isFriend(ent.getName()) && Client.INSTANCE.getFriendManager().getFriend(ent.getName()).getAlias() != null) ? Client.INSTANCE.getFriendManager().getFriend(ent.getName()).getAlias() : StringUtils.stripControlCodes(ent.getName()))) + (health.isEnabled() ? getNameHealthColor(ent) + " : " + (int) ent.getHealth() : "");
                            RenderUtil.drawRect2((x + (x2 - x) / 2) - (mc.fontRendererObj.getStringWidth(nametext) >> 1) - 2, y - mc.fontRendererObj.FONT_HEIGHT - 4, (x + (x2 - x) / 2) + (mc.fontRendererObj.getStringWidth(nametext) >> 1) + 2, y - 2, new Color(0, 0, 0, 120).getRGB());

                            RenderUtil.drawCustomString(nametext, (x + ((x2 - x) / 2)) - (mc.fontRendererObj.getStringWidth(nametext) / 2), y - mc.fontRendererObj.FONT_HEIGHT - 2, getNameColor(ent));
                        }
                        GL11.glPopMatrix();
                    }
                }
            }
        });
    }

    private void drawPotions(EntityPlayer player, float x, float y) {
        final ItemStack[] items = player.getInventory();
        boolean noArmor = player.getHeldItem() == null && items[0] == null && items[1] == null && items[2] == null && items[3] == null;
        float tagwidth = x;
        float stringY = y - 5;
        float stringYY = y + 4;
        if (!noArmor) {
            stringY -= 33;
            stringYY -= 32;
        }
        for (Object o : player.getActivePotionEffects()) {
            PotionEffect effect = (PotionEffect) o;
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            boolean potRanOut = effect.getDuration() != 0.0;
            String effectpower = "";
            if (potion != null && player.isPotionActive(potion) && potRanOut) {
                if (effect.getAmplifier() == 1) {
                    effectpower = "II";
                } else if (effect.getAmplifier() == 2) {
                    effectpower = "III";
                } else if (effect.getAmplifier() == 3) {
                    effectpower = "IV";
                }
                player.getActivePotionEffects().size();
                RenderUtil.drawCustomString(I18n.format(potion.getName(), new Object[0]) + " " + ChatFormatting.GRAY + effectpower, tagwidth - mc.fontRendererObj.getStringWidth(I18n.format(potion.getName(), new Object[0]) + " " + effectpower) / 2, stringY, potion.getLiquidColor());
                RenderUtil.drawCustomString(Potion.getDurationString(effect), tagwidth - mc.fontRendererObj.getStringWidth(Potion.getDurationString(effect)) / 2, stringYY + 2, getPotionColor(effect));
                stringYY -= 20;
                stringY -= 20;
            }
        }
    }


    private void drawArmor(EntityPlayer player, int x, int y) {
        if (player.inventory.armorInventory.length > 0) {
            List<ItemStack> items = new ArrayList<>();
            if (player.getHeldItem() != null) {
                items.add(player.getHeldItem());
            }
            for (int index = 3; index >= 0; index--) {
                ItemStack stack = player.inventory.armorInventory[index];
                if (stack != null) {
                    items.add(stack);
                }
            }
            int armorX = x - ((items.size() * 18) / 2);
            for (ItemStack stack : items) {
                GlStateManager.pushMatrix();
                GlStateManager.enableLighting();
                mc.getRenderItem().renderItemIntoGUI(stack, armorX, y);
                mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, armorX, y, "");
                GlStateManager.disableLighting();
                GlStateManager.popMatrix();
                GlStateManager.disableDepth();
                NBTTagList enchants = stack.getEnchantmentTagList();
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.5, 0.5, 0.5);
                if (stack.isStackable()) {
                    RenderUtil.drawCustomString(String.valueOf(stack.stackSize), armorX * 2 + 4, (y + 8) * 2, 0xDDD1E6);
                }
                if (stack.getItem() == Items.golden_apple && stack.getMetadata() == 1) {
                    RenderUtil.drawCustomString("op", armorX * 2, y * 2, 0xFFFF0000);
                }
                Enchantment[] important = new Enchantment[]{Enchantment.protection, Enchantment.unbreaking, Enchantment.sharpness, Enchantment.fireAspect, Enchantment.efficiency, Enchantment.featherFalling, Enchantment.power, Enchantment.flame, Enchantment.punch, Enchantment.fortune, Enchantment.infinity, Enchantment.thorns};
                if (enchants != null) {
                    int ency = y + 8;
                    for (int index = 0; index < enchants.tagCount(); ++index) {
                        short id = enchants.getCompoundTagAt(index).getShort("id");
                        short level = enchants.getCompoundTagAt(index).getShort("lvl");
                        Enchantment enc = Enchantment.getEnchantmentById(id);
                        for (Enchantment importantEnchantment : important) {
                            if (enc == importantEnchantment) {
                                String encName = enc.getTranslatedName(level).substring(0, 1).toLowerCase();
                                if (level > 99) encName = encName + "99+";
                                else encName = encName + level;
                                RenderUtil.drawCustomString(encName, armorX * 2 + 4, ency * 2, 0xDDD1E6);
                                ency -= 5;
                                break;
                            }
                        }
                    }
                }
                GlStateManager.enableDepth();
                GlStateManager.popMatrix();
                armorX += 18;
            }
        }
    }


    private boolean isValid(EntityLivingBase entity) {
        return !AntiBot.getBots().contains(entity) && mc.thePlayer != entity && entity.getEntityId() != -1488 && isValidType(entity) && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players.isEnabled() && entity instanceof EntityPlayer) || (mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime) || (passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals.isEnabled() && entity instanceof EntityAnimal));
    }


    private int getNameColor(EntityLivingBase ent) {
        if (Client.INSTANCE.getFriendManager().isFriend(ent.getName())) return new Color(122, 190, 255).getRGB();
        else if (ent.getName().equals(mc.thePlayer.getName())) return new Color(0xFF99ff99).getRGB();
        return new Color(0xFFA59C).getRGB();
    }

    private ChatFormatting getNameHealthColor(EntityLivingBase player) {
        final double health = Math.ceil(player.getHealth());
        final double maxHealth = player.getMaxHealth();
        final double percentage = 100 * (health / maxHealth);
        if (percentage > 85) return ChatFormatting.DARK_GREEN;
        else if (percentage > 75) return ChatFormatting.GREEN;
        else if (percentage > 50) return ChatFormatting.YELLOW;
        else if (percentage > 25) return ChatFormatting.RED;
        else if (percentage > 0) return ChatFormatting.DARK_RED;
        return ChatFormatting.BLACK;
    }

    private int getPotionColor(PotionEffect ptn) {
        float f = ptn.getDuration();
        float f1 = 1000;
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 1.0F) | 0xFF000000;
    }

}
