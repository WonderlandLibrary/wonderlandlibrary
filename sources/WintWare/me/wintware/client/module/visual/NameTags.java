/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package me.wintware.client.module.visual;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventRender2D;
import me.wintware.client.event.impl.EventRender3D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.combat.RotationUtil;
import me.wintware.client.utils.visual.ColorUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class NameTags
extends Module {
    public Setting armor;
    public Setting invis;
    public static Map<EntityLivingBase, double[]> entityPositions = new HashMap<EntityLivingBase, double[]>();

    public NameTags() {
        super("NameTags", Category.Visuals);
        ArrayList options = new ArrayList();
        Main.instance.setmgr.rSetting(new Setting("NametagsSize", this, 35.0, 1.0, 100.0, true));
        Main.instance.setmgr.rSetting(new Setting("ItemsDur", this, true));
        Main.instance.setmgr.rSetting(new Setting("NameBorder", this, true));
        this.invis = new Setting("Invisibles", this, true);
        Main.instance.setmgr.rSetting(this.invis);
        this.armor = new Setting("Armor", this, true);
        Main.instance.setmgr.rSetting(this.armor);
    }

    @EventTarget
    public void on3D(EventRender3D event) {
        try {
            this.updatePositions();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @EventTarget
    public void on2D(EventRender2D event) {
        GlStateManager.pushMatrix();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        for (Entity entity : entityPositions.keySet()) {
            if ((entity == Minecraft.player || !this.invis.getValue()) && entity.isInvisible()) continue;
            GlStateManager.pushMatrix();
            if (entity instanceof EntityPlayer) {
                double[] array = entityPositions.get(entity);
                if (array[3] < 0.0 || array[3] >= 1.0) {
                    GlStateManager.popMatrix();
                    continue;
                }
                GlStateManager.translate(array[0] / (double)ScaledResolution.getScaleFactor(), array[1] / (double)ScaledResolution.getScaleFactor(), 0.0);
                this.scale();
                float t = Main.instance.setmgr.getSettingByName("NametagsSize").getValFloat() / 50.0f;
                GlStateManager.translate(0.0, -2.5, 0.0);
                String string = "Health: " + Math.round(((EntityLivingBase)entity).getHealth() * 10.0f) / 20;
                String prefix = Main.instance.friendManager.getFriends().contains(entity.getName()) ? " \u00a7a[\u00a7f\u00a7lFRIEND\u00a7a]" : " ";
                String name = entity.getDisplayName().getFormattedText();
                String string4 = name + prefix;
                float n = NameTags.mc.fontRenderer.getStringWidth(string4.replaceAll("\u0412.", ""));
                float n2 = NameTags.mc.fontRenderer.getStringWidth(string);
                float n3 = (n > n2 ? n : n2) + 10.0f;
                if (Main.instance.setmgr.getSettingByName("NameBorder").getValue()) {
                    RenderUtil.drawNewRect(-n3 * t, -25.0, n3 * t, 0.0, ColorUtils.getColor(0, 130));
                }
                int n4 = (int)(array[0] + (double)(-n3 * t) - 3.0) / 2 - 26;
                int n5 = (int)(array[0] + (double)(n3 * t) + 3.0) / 2 + 20;
                int n6 = (int)(array[1] - 30.0) / 2;
                int n7 = (int)(array[1] + 11.0) / 2;
                int n8 = scaledResolution.getScaledHeight() / 2;
                int n9 = scaledResolution.getScaledWidth() / 2;
                NameTags.mc.fontRenderer.drawStringWithShadow(string4, -n3 * t + 7.0f, -22.0f, new Color(255, 255, 255, 255).getRGB());
                NameTags.mc.fontRenderer.drawString(string, -n3 * t + 6.0f, -10.0f, new Color(255, 255, 255, 255).getRGB());
                EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
                if (Main.instance.setmgr.getSettingByName("NameBorder").getValue()) {
                    RenderUtil.drawNewRect(-n3 * t, -1.0, n3 * t, 0.0, ColorUtils.getColor(0, 100));
                    RenderUtil.drawNewRect(-n3 * t, -1.0, n3 * t - n3 * t * (1.0f - (float)Math.ceil(entityLivingBase.getHealth() + entityLivingBase.getAbsorptionAmount()) / (entityLivingBase.getMaxHealth() + entityLivingBase.getAbsorptionAmount())) * 2.0f, 0.0, ColorUtils.getTeamColor(entity));
                }
                if (this.armor.getValue()) {
                    ArrayList<ItemStack> list = new ArrayList<ItemStack>();
                    for (int i = 0; i < 5; ++i) {
                        ItemStack getEquipmentInSlot = ((EntityPlayer)entity).getEquipmentInSlot(i);
                        if (getEquipmentInSlot == null) continue;
                        list.add(getEquipmentInSlot);
                    }
                    int n10 = -(list.size() * 9);
                    for (ItemStack itemStack : list) {
                        RenderHelper.enableGUIStandardItemLighting();
                        mc.getRenderItem().renderItemIntoGUI(itemStack, n10 + 6, -42);
                        if (Main.instance.setmgr.getSettingByName("ItemsDur").getValue()) {
                            mc.getRenderItem().renderItemOverlays(NameTags.mc.fontRendererObj, itemStack, n10, -40);
                        }
                        n10 += 3;
                        RenderHelper.disableStandardItemLighting();
                        if (itemStack == null) continue;
                        int n11 = 21;
                        int getEnchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(16), itemStack);
                        int getEnchantmentLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(20), itemStack);
                        int getEnchantmentLevel3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(19), itemStack);
                        if (getEnchantmentLevel > 0) {
                            this.drawEnchantTag("Sh" + this.getColor(getEnchantmentLevel) + getEnchantmentLevel, n10, n11);
                            n11 += 6;
                        }
                        if (getEnchantmentLevel2 > 0) {
                            this.drawEnchantTag("Fir" + this.getColor(getEnchantmentLevel2) + getEnchantmentLevel2, n10, n11);
                            n11 += 6;
                        }
                        if (getEnchantmentLevel3 > 0) {
                            this.drawEnchantTag("Kb" + this.getColor(getEnchantmentLevel3) + getEnchantmentLevel3, n10, n11);
                        } else if (itemStack.getItem() instanceof ItemArmor) {
                            int getEnchantmentLevel4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), itemStack);
                            int getEnchantmentLevel5 = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(7), itemStack);
                            int getEnchantmentLevel6 = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(34), itemStack);
                            if (getEnchantmentLevel4 > 0) {
                                this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel4) + getEnchantmentLevel4, n10, n11);
                                n11 += 6;
                            }
                            if (getEnchantmentLevel5 > 0) {
                                this.drawEnchantTag("Th" + this.getColor(getEnchantmentLevel5) + getEnchantmentLevel5, n10, n11);
                                n11 += 6;
                            }
                            if (getEnchantmentLevel6 > 0) {
                                this.drawEnchantTag("Un" + this.getColor(getEnchantmentLevel6) + getEnchantmentLevel6, n10, n11);
                            }
                        } else if (itemStack.getItem() instanceof ItemBow) {
                            int getEnchantmentLevel7 = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(48), itemStack);
                            int getEnchantmentLevel8 = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(49), itemStack);
                            int getEnchantmentLevel9 = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(50), itemStack);
                            if (getEnchantmentLevel7 > 0) {
                                this.drawEnchantTag("Pw" + this.getColor(getEnchantmentLevel7) + getEnchantmentLevel7, n10, n11);
                                n11 += 6;
                            }
                            if (getEnchantmentLevel8 > 0) {
                                this.drawEnchantTag("Pn" + this.getColor(getEnchantmentLevel8) + getEnchantmentLevel8, n10, n11);
                                n11 += 6;
                            }
                            if (getEnchantmentLevel9 > 0) {
                                this.drawEnchantTag("Fa" + this.getColor(getEnchantmentLevel9) + getEnchantmentLevel9, n10, n11);
                            }
                        } else if (itemStack.getRarity() == EnumRarity.EPIC) {
                            this.drawEnchantTag("\u0412\u00a76\u0412\u00a7lGod", n10 - 2, n11);
                        }
                        int n12 = (int)Math.round(255.0 - (double)itemStack.getItemDamage() * 255.0 / (double)itemStack.getMaxDamage());
                        new Color(255 - n12 << 16 | n12 << 8).brighter();
                        float n13 = (float)((double)n10 * 1.05) - 2.0f;
                        if (itemStack.getMaxDamage() - itemStack.getItemDamage() > 0) {
                            GlStateManager.pushMatrix();
                            GlStateManager.disableDepth();
                            GlStateManager.enableDepth();
                            GlStateManager.popMatrix();
                        }
                        n10 += 12;
                    }
                }
            }
            GlStateManager.popMatrix();
        }
        GL11.glPopMatrix();
        GlStateManager.enableBlend();
    }

    private void drawEnchantTag(String text, int n, int n2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        NameTags.mc.smallfontRenderer.drawStringWithShadow(text, (n *= 1) + 9, -30 - (n2 -= 6), ColorUtils.getColor1(255));
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    private String getColor(int n) {
        if (n != 1) {
            if (n == 2) {
                return "";
            }
            if (n == 3) {
                return "";
            }
            if (n == 4) {
                return "";
            }
            if (n >= 5) {
                return "";
            }
        }
        return "";
    }

    private void updatePositions() {
        entityPositions.clear();
        float pTicks = NameTags.mc.timer.renderPartialTicks;
        for (Object o : NameTags.mc.world.loadedEntityList) {
            Entity ent = (Entity)o;
            if (ent == Minecraft.player || !(ent instanceof EntityPlayer)) continue;
            double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks - NameTags.mc.getRenderManager().viewerPosX;
            double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)pTicks - NameTags.mc.getRenderManager().viewerPosY;
            double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - NameTags.mc.getRenderManager().viewerPosZ;
            if (!(this.convertTo2D(x, y += (double)ent.height + 0.2, z)[2] >= 0.0) || !(this.convertTo2D(x, y, z)[2] < 1.0)) continue;
            entityPositions.put((EntityPlayer)ent, new double[]{this.convertTo2D(x, y, z)[0], this.convertTo2D(x, y, z)[1], Math.abs(this.convertTo2D(x, y + 1.0, z, ent)[1] - this.convertTo2D(x, y, z, ent)[1]), this.convertTo2D(x, y, z)[2]});
        }
    }

    private double[] convertTo2D(double x, double y, double z, Entity ent) {
        float pTicks = NameTags.mc.timer.renderPartialTicks;
        float prevYaw = Minecraft.player.rotationYaw;
        float prevPrevYaw = Minecraft.player.prevRotationYaw;
        float[] rotations = RotationUtil.getRotationFromPosition(ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks, ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks, ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)pTicks - 1.6);
        NameTags.mc.getRenderViewEntity().rotationYaw = NameTags.mc.getRenderViewEntity().prevRotationYaw = rotations[0];
        NameTags.mc.entityRenderer.setupCameraTransform(pTicks, 0);
        double[] convertedPoints = this.convertTo2D(x, y, z);
        NameTags.mc.getRenderViewEntity().rotationYaw = prevYaw;
        NameTags.mc.getRenderViewEntity().prevRotationYaw = prevPrevYaw;
        NameTags.mc.entityRenderer.setupCameraTransform(pTicks, 0);
        return convertedPoints;
    }

    private double[] convertTo2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
        if (result) {
            return new double[]{screenCoords.get(0), (float)Display.getHeight() - screenCoords.get(1), screenCoords.get(2)};
        }
        return null;
    }

    private void scale() {
        float n = 1.0f * (NameTags.mc.gameSettings.smoothCamera ? 2.0f : 1.0f);
        GlStateManager.scale(n, n, n);
    }
}

