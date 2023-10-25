/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.render.EventRender2D;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.misc.StreamerMode;
import org.celestial.client.feature.impl.visuals.CustomModel;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class NameTags
extends Feature {
    public static Map<EntityLivingBase, double[]> entityPositions = new HashMap<EntityLivingBase, double[]>();
    public ListSetting fontMode;
    public BooleanSetting armor;
    public BooleanSetting potion;
    public BooleanSetting backGround;
    public ListSetting backGroundMode = new ListSetting("Background Mode", "Default", "Default", "Shader");
    public BooleanSetting offHand;
    public BooleanSetting noNPC;
    public NumberSetting opacity;
    public NumberSetting size;
    public ListSetting hpMode;

    public NameTags() {
        super("NameTags", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0438\u0433\u0440\u043e\u043a\u043e\u0432, \u043d\u0438\u043a, \u0431\u0440\u043e\u043d\u044e \u0438 \u0438\u0445 \u0437\u0434\u043e\u0440\u043e\u0432\u044c\u0435 \u0441\u043a\u0432\u043e\u0437\u044c \u0441\u0442\u0435\u043d\u044b", Type.Visuals);
        this.fontMode = new ListSetting("Font Mode", "Minecraft", () -> true, "Client", "Minecraft");
        this.hpMode = new ListSetting("Health Mode", "HP", () -> true, "HP", "Percentage");
        this.size = new NumberSetting("NameTags Size", 1.0f, 0.5f, 2.0f, 0.1f, () -> true);
        this.backGround = new BooleanSetting("NameTags Background", true, () -> true);
        this.opacity = new NumberSetting("Background Opacity", 150.0f, 0.0f, 255.0f, 5.0f, () -> this.backGround.getCurrentValue());
        this.offHand = new BooleanSetting("OffHand Render", true, () -> true);
        this.armor = new BooleanSetting("Show Armor", true, () -> true);
        this.potion = new BooleanSetting("Show Potions", true, () -> true);
        this.noNPC = new BooleanSetting("No NPC", false, () -> true);
        this.addSettings(this.fontMode, this.hpMode, this.noNPC, this.size, this.backGround, this.backGroundMode, this.opacity, this.offHand, this.armor, this.potion);
    }

    public static TextFormatting getHealthColor(float health) {
        if (health <= 4.0f) {
            return TextFormatting.RED;
        }
        if (health <= 8.0f) {
            return TextFormatting.GOLD;
        }
        if (health <= 12.0f) {
            return TextFormatting.YELLOW;
        }
        if (health <= 16.0f) {
            return TextFormatting.DARK_GREEN;
        }
        return TextFormatting.GREEN;
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        this.updatePositions();
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        ScaledResolution sr = new ScaledResolution(mc);
        GlStateManager.pushMatrix();
        for (EntityLivingBase entity : entityPositions.keySet()) {
            if (entity == null || entity == NameTags.mc.player || entity.getName().startsWith("npc") && this.noNPC.getCurrentValue()) continue;
            GlStateManager.pushMatrix();
            if (entity instanceof EntityPlayer) {
                double[] array = entityPositions.get(entity);
                if (array[3] < 0.0 || array[3] >= 1.0) {
                    GlStateManager.popMatrix();
                    continue;
                }
                double scaleFactor = sr.getScaleFactor();
                GlStateManager.translate(array[0] / scaleFactor, array[1] / scaleFactor + (double)(Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && (CustomModel.modelMode.currentMode.equals("Crab") || CustomModel.modelMode.currentMode.equals("Chni") || CustomModel.modelMode.currentMode.equals("Red Panda")) && !CustomModel.onlyMe.getCurrentValue() ? 20 : 0), 0.0);
                this.scale();
                String string = Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getCurrentValue() ? "Protected" : (Celestial.instance.friendManager.isFriend(entity.getName()) ? (Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.friendNames.getCurrentValue() ? "Protected" : (Object)((Object)ChatFormatting.GREEN) + "[F] " + (Object)((Object)ChatFormatting.RESET) + entity.getDisplayName().getUnformattedText()) : entity.getDisplayName().getUnformattedText());
                String stringHP = this.hpMode.currentMode.equals("Percentage") ? MathematicHelper.round(entity.getHealth() / entity.getMaxHealth() * 100.0f, 1) + "% " : MathematicHelper.round(entity.getHealth(), 1) + " ";
                String string2 = "" + stringHP;
                float width = this.fontMode.currentMode.equals("Minecraft") ? (float)(NameTags.mc.fontRendererObj.getStringWidth(string2 + " " + string) + 4) : (float)(ClientHelper.getFontRender().getStringWidth(string2 + " " + string) + 5);
                GlStateManager.translate(0.0, -10.0, 0.0);
                if (this.backGround.getCurrentValue()) {
                    if (this.backGroundMode.currentMode.equals("Default")) {
                        RectHelper.drawRect(-width / 2.0f, -10.0, width / 2.0f, 2.0, PaletteHelper.getColor(0, (int)this.opacity.getCurrentValue()));
                    } else if (this.backGroundMode.currentMode.equals("Shader")) {
                        RenderHelper.renderBlurredShadow(new Color(0, 0, 0, (int)this.opacity.getCurrentValue()), (double)(-width / 2.0f - 2.0f), -10.0, (double)(width + 3.0f), 11.0, 20);
                    }
                }
                if (this.fontMode.currentMode.equals("Minecraft")) {
                    NameTags.mc.fontRendererObj.drawStringWithShadow(string + " " + (Object)((Object)NameTags.getHealthColor(entity.getHealth())) + string2, -width / 2.0f + 2.0f, -7.5f, -1);
                } else {
                    ClientHelper.getFontRender().drawStringWithShadow(string + " " + (Object)((Object)NameTags.getHealthColor(entity.getHealth())) + string2, -width / 2.0f + 2.0f, -7.5, -1);
                }
                ItemStack heldItemStack = entity.getHeldItem(EnumHand.OFF_HAND);
                ArrayList<ItemStack> list = new ArrayList<ItemStack>();
                for (int i = 0; i < 5; ++i) {
                    ItemStack getEquipmentInSlot = ((EntityPlayer)entity).getEquipmentInSlot(i);
                    list.add(getEquipmentInSlot);
                }
                int n10 = -(list.size() * 9) - 8;
                if (this.armor.getCurrentValue()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0.0f, -2.0f, 0.0f);
                    if (this.offHand.getCurrentValue()) {
                        if (this.fontMode.currentMode.equals("Minecraft")) {
                            mc.getRenderItem().renderItemIntoGUI(heldItemStack, n10 + 86, -28.0f);
                            mc.getRenderItem().renderItemOverlays(NameTags.mc.fontRendererObj, heldItemStack, n10 + 86, -28);
                        } else {
                            mc.getRenderItem().renderItemIntoGUI(heldItemStack, n10 + 86, -28.0f);
                            mc.getRenderItem().renderItemOverlays(ClientHelper.getFontRender(), heldItemStack, n10 + 86, -28);
                        }
                    }
                    for (ItemStack itemStack : list) {
                        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
                        mc.getRenderItem().renderItemIntoGUI(itemStack, n10 + 6, -28.0f);
                        if (this.fontMode.currentMode.equals("Minecraft")) {
                            mc.getRenderItem().renderItemOverlays(NameTags.mc.fontRendererObj, itemStack, n10 + 5, -28);
                        } else {
                            mc.getRenderItem().renderItemOverlays(ClientHelper.getFontRender(), itemStack, n10 + 5, -28);
                        }
                        n10 += 3;
                        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                        int n11 = 7;
                        int getEnchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(16)), itemStack);
                        int getEnchantmentLevel2 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(20)), itemStack);
                        int getEnchantmentLevel3 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(19)), itemStack);
                        if (getEnchantmentLevel > 0) {
                            this.drawEnchantTag("S" + this.getColor(getEnchantmentLevel) + getEnchantmentLevel, n10, n11);
                            n11 += 8;
                        }
                        if (getEnchantmentLevel2 > 0) {
                            this.drawEnchantTag("F" + this.getColor(getEnchantmentLevel2) + getEnchantmentLevel2, n10, n11);
                            n11 += 8;
                        }
                        if (getEnchantmentLevel3 > 0) {
                            this.drawEnchantTag("Kb" + this.getColor(getEnchantmentLevel3) + getEnchantmentLevel3, n10, n11);
                        } else if (itemStack.getItem() instanceof ItemArmor) {
                            int getEnchantmentLevel4 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(0)), itemStack);
                            int getEnchantmentLevel5 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(7)), itemStack);
                            int getEnchantmentLevel6 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(34)), itemStack);
                            if (getEnchantmentLevel4 > 0) {
                                this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel4) + getEnchantmentLevel4, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel5 > 0) {
                                this.drawEnchantTag("Th" + this.getColor(getEnchantmentLevel5) + getEnchantmentLevel5, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel6 > 0) {
                                this.drawEnchantTag("U" + this.getColor(getEnchantmentLevel6) + getEnchantmentLevel6, n10, n11);
                            }
                        } else if (itemStack.getItem() instanceof ItemBow) {
                            int getEnchantmentLevel7 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(48)), itemStack);
                            int getEnchantmentLevel8 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(49)), itemStack);
                            int getEnchantmentLevel9 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(50)), itemStack);
                            if (getEnchantmentLevel7 > 0) {
                                this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel7) + getEnchantmentLevel7, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel8 > 0) {
                                this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel8) + getEnchantmentLevel8, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel9 > 0) {
                                this.drawEnchantTag("F" + this.getColor(getEnchantmentLevel9) + getEnchantmentLevel9, n10, n11);
                            }
                        }
                        n10 = (int)((double)n10 + 13.5);
                    }
                    GlStateManager.popMatrix();
                }
                if (this.potion.getCurrentValue()) {
                    this.drawPotionEffect((EntityPlayer)entity);
                }
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
        GlStateManager.enableBlend();
    }

    private void drawPotionEffect(EntityPlayer entity) {
        boolean tagwidth = false;
        int stringY = -25;
        if (entity.getTotalArmorValue() > 0 || !entity.getHeldItemMainhand().isEmpty() && !entity.getHeldItemOffhand().isEmpty()) {
            stringY -= 37;
        }
        for (PotionEffect potionEffect : entity.getActivePotionEffects()) {
            Potion potion = potionEffect.getPotion();
            boolean potRanOut = (double)potionEffect.getDuration() != 0.0;
            String power = "";
            if (!entity.isPotionActive(potion) || !potRanOut) continue;
            if (potionEffect.getAmplifier() == 1) {
                power = "II";
            } else if (potionEffect.getAmplifier() == 2) {
                power = "III";
            } else if (potionEffect.getAmplifier() == 3) {
                power = "IV";
            }
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            float xValue = (float)tagwidth - (this.fontMode.currentMode.equals("Minecraft") ? (float)NameTags.mc.fontRendererObj.getStringWidth(I18n.format(potion.getName(), new Object[0]) + " " + power + (Object)((Object)TextFormatting.GRAY) + " " + Potion.getDurationString(potionEffect)) / 2.0f : (float)ClientHelper.getFontRender().getStringWidth(I18n.format(potion.getName(), new Object[0]) + " " + power + (Object)((Object)TextFormatting.GRAY) + " " + Potion.getDurationString(potionEffect)) / 2.0f);
            if (this.fontMode.currentMode.equals("Minecraft")) {
                NameTags.mc.fontRendererObj.drawStringWithShadow(I18n.format(potion.getName(), new Object[0]) + " " + power + (Object)((Object)TextFormatting.GRAY) + " " + Potion.getDurationString(potionEffect), xValue, stringY, -1);
            } else if (this.fontMode.currentMode.equals("Client")) {
                ClientHelper.getFontRender().drawStringWithShadow(I18n.format(potion.getName(), new Object[0]) + " " + power + (Object)((Object)TextFormatting.GRAY) + " " + Potion.getDurationString(potionEffect), xValue, stringY, -1);
            }
            stringY -= 10;
            GlStateManager.popMatrix();
        }
    }

    private void drawEnchantTag(String text, int n, int n2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        n2 -= 7;
        if (this.fontMode.currentMode.equals("Minecraft")) {
            NameTags.mc.fontRendererObj.drawStringWithShadow(text, n + 6, -35 - n2, -1);
        } else {
            ClientHelper.getFontRender().drawStringWithShadow(text, n + 6, -35 - n2, -1);
        }
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
        for (Entity o : NameTags.mc.world.loadedEntityList) {
            if (o == null || o == NameTags.mc.player || !(o instanceof EntityPlayer)) continue;
            double x = o.lastTickPosX + (o.posX - o.lastTickPosX) * (double)pTicks - NameTags.mc.getRenderManager().viewerPosX;
            double y = o.lastTickPosY + (o.posY - o.lastTickPosY) * (double)pTicks - NameTags.mc.getRenderManager().viewerPosY;
            double z = o.lastTickPosZ + (o.posZ - o.lastTickPosZ) * (double)pTicks - NameTags.mc.getRenderManager().viewerPosZ;
            if (!(Objects.requireNonNull(this.convertTo2D(x, y += (double)o.height + 0.2, z))[2] >= 0.0) || !(Objects.requireNonNull(this.convertTo2D(x, y, z))[2] < 2.0)) continue;
            entityPositions.put((EntityPlayer)o, new double[]{Objects.requireNonNull(this.convertTo2D(x, y, z))[0], Objects.requireNonNull(this.convertTo2D(x, y, z))[1], Math.abs(Objects.requireNonNull(this.convertTo2D(x, y + 1.0, z))[1] - Objects.requireNonNull(this.convertTo2D(x, y, z))[1]), Objects.requireNonNull(this.convertTo2D(x, y, z))[2]});
        }
    }

    private double[] convertTo2D(double x, double y, double z) {
        FloatBuffer screenCords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCords);
        if (result) {
            return new double[]{screenCords.get(0), (float)Display.getHeight() - screenCords.get(1), screenCords.get(2)};
        }
        return null;
    }

    private void scale() {
        float n = NameTags.mc.gameSettings.smoothCamera ? 2.0f : this.size.getCurrentValue();
        GlStateManager.scale(n, n, n);
    }
}

