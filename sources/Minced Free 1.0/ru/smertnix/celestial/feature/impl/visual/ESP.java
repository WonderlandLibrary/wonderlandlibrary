package ru.smertnix.celestial.feature.impl.visual;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.event.events.impl.render.EventRender2D;
import ru.smertnix.celestial.event.events.impl.render.EventRender3D;
import ru.smertnix.celestial.event.events.impl.render.EventRenderPlayerName;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.feature.impl.combat.AttackAura;
import ru.smertnix.celestial.feature.impl.player.NameProtect;
import ru.smertnix.celestial.friend.FriendManager;
import ru.smertnix.celestial.ui.font.MCFontRenderer;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.MultipleBoolSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.math.MathematicHelper;
import ru.smertnix.celestial.utils.math.RotationHelper;
import ru.smertnix.celestial.utils.other.NameUtils;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.ColorUtils;
import ru.smertnix.celestial.utils.render.RenderUtils;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ESP
        extends Feature {
    public static Map<EntityLivingBase, double[]> entityPositions = new HashMap<EntityLivingBase, double[]>();
    private final int black = Color.BLACK.getRGB();
    public static ColorSetting colorEsp;
    public static BooleanSetting box = new BooleanSetting("Box", true, () -> true);
    public BooleanSetting hp = new BooleanSetting("Health & Armor", true, () -> true);
    public BooleanSetting hpDetails = new BooleanSetting("Details", true, () -> hp.getBoolValue());
    public static ListSetting healcolorMode = new ListSetting("Mode", "Static", () -> true, "Static", "Client");
    public static BooleanSetting nametag;
    public static Entity clipEntity;
	  public static MultipleBoolSetting sel = new MultipleBoolSetting("Target selection",
	    		new BooleanSetting("Players", true),
	    		new BooleanSetting("NPC / Bots"),
	    		new BooleanSetting("Mobs"),
	    		new BooleanSetting("Items"));


    public ESP() {
        super("ESP", "Показывает Entity через стены", FeatureCategory.Render);
        colorEsp = new ColorSetting("Color", new Color(0xFFFFFF).getRGB(), () -> healcolorMode.currentMode.equals("Static"));
        nametag = new BooleanSetting("Player Info", true, () -> true);
        addSettings(healcolorMode, sel, colorEsp,nametag,box, hp, hpDetails);
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
    public void onRender3D1(EventRender3D event) {
        updatePositions();
    }

    @EventTarget
    public void onRender21D(EventRender2D event) {
    }

    private void drawPotionEffect(EntityPlayer entity) {
        int tagWidth = 0;
        int stringY = -25;
        if (entity.getTotalArmorValue() > 0 || !entity.getHeldItemMainhand().func_190926_b() && !entity.getHeldItemOffhand().func_190926_b()) {
            stringY -= 37;
        }
        for (PotionEffect potionEffect : entity.getActivePotionEffects()) {
            Potion potion = potionEffect.getPotion();
            boolean potRanOut = (double) potionEffect.getDuration() != 0.0;
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
            float xValue = (float) tagWidth - ((float) mc.rubik_18.getStringWidth(I18n.format(potion.getName()) + " " + power + TextFormatting.GRAY + " " + Potion.getDurationString(potionEffect)) / 2.0f);
          
            mc.mntsb_18.drawStringWithShadow(I18n.format(potion.getName()) + " " + power + TextFormatting.GRAY + " " + Potion.getDurationString(potionEffect), xValue, stringY, -1);
         
                stringY -= 10;
            GlStateManager.popMatrix();
        }
    }

    private void drawEnchantTag(String text, int n, int n2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        n2 -= 7;
        mc.mntsb_18.drawStringWithShadow(text, n + 6, -35 - n2, -1);
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
        float pTicks = mc.timer.renderPartialTicks;
        for (Entity o : mc.world.loadedEntityList) {
            if (o == mc.player || !(o instanceof EntityPlayer)) continue;
            double x = o.lastTickPosX + (o.posX - o.lastTickPosX) * (double) pTicks - mc.getRenderManager().viewerPosX;
            double y = o.lastTickPosY + (o.posY - o.lastTickPosY) * (double) pTicks - mc.getRenderManager().viewerPosY;
            double z = o.lastTickPosZ + (o.posZ - o.lastTickPosZ) * (double) pTicks - mc.getRenderManager().viewerPosZ;
            if (!(Objects.requireNonNull(convertTo2D(x, y += (double) o.height + 0.2, z))[2] >= 0.0) || !(Objects.requireNonNull(convertTo2D(x, y, z))[2] < 2.0))
                continue;
            entityPositions.put((EntityPlayer) o, new double[]{Objects.requireNonNull(convertTo2D(x, y, z))[0], Objects.requireNonNull(convertTo2D(x, y, z))[1], Math.abs(Objects.requireNonNull(convertTo2D(x, y + 1.0, z))[1] - Objects.requireNonNull(convertTo2D(x, y, z))[1]), Objects.requireNonNull(convertTo2D(x, y, z))[2]});
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
        boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCords);
        if (result) {
            return new double[]{screenCords.get(0), (float) Display.getHeight() - screenCords.get(1), screenCords.get(2)};
        }
        return null;
    }

    private void scale() {
        float n = mc.gameSettings.smoothCamera ? 2.0f : 2;
        GlStateManager.scale(n, n, n);
    }

    private void drawScaledString(String text, double x, double y, double scale, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, x);
        GlStateManager.scale(scale, scale, scale);
        MCFontRenderer.drawStringWithOutline(mc.fontRendererObj, text, 0.0f, 0.0f, color);
        GlStateManager.popMatrix();
    }


    @EventTarget
    public void onRender3D(EventRender3D event3D) {
        if (!isEnabled()) {
            return;
        }

        Color color = ClientHelper.getESPColor();

            GlStateManager.pushMatrix();

            for (Entity entity : mc.world.loadedEntityList) {
                if (!(entity instanceof EntityPlayer) || entity == mc.player && mc.gameSettings.thirdPersonView == 0) {
                    continue;
                }

            }

            GlStateManager.popMatrix();
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
    	 ScaledResolution sr = new ScaledResolution(mc);
    	    int scaleFactor = event.getResolution().getScaleFactor();  
         if (nametag.getBoolValue()) {
         GlStateManager.pushMatrix();
         for (EntityLivingBase entity : entityPositions.keySet()) {
             if (entity == null || entity == mc.player)
                 continue;
             GlStateManager.pushMatrix();
             if (entity instanceof EntityPlayer) {
                 double[] array = entityPositions.get(entity);
                 if (array[3] < 0.0 || array[3] >= 1.0) {
                     GlStateManager.popMatrix();
                     continue;
                 }
                 GlStateManager.translate(array[0] / scaleFactor, array[1] / scaleFactor + (double) 0, 0.0);
                 String stringName = entity.getDisplayName().getUnformattedText().contains("\uff27\uff25\uff26\uff25\uff33\uff34") ? "������" : entity.getDisplayName().getUnformattedText();
              
                 if ((NameProtect.streamer.getBoolValue() || (Celestial.instance.friendManager.isFriend(entity.getName()) && NameProtect.friends2.getBoolValue())) && Celestial.instance.featureManager.getFeature(NameProtect.class).isEnabled()) {
                 	stringName = NameUtils.getName();
                 }
                 
                 String stringHP = MathematicHelper.round(entity.getHealth(), 1) + " ";
                 String stringHP2 = "" + stringHP;
                 float width = (float) (mc.mntsb_18.getStringWidth(stringName) + 5);
                 GlStateManager.translate(0.0, -10.0, 0.0);
                 
                 if (entity.getEntityId() != -10) {
                	 RenderUtils.drawRect(-width / 2.0f, -10.0, width / 2.0f, 9.0, ColorUtils.getColor(0, (int) 100));
                     int color;
                     RenderUtils.drawRect(-width / 2.0f + 2, 1, width / 2.0f - 2, 7.0, RenderUtils.injectAlpha(new Color(0,0,0), (int) 100).getRGB());
                     color = RenderUtils.injectAlpha(ClientHelper.getClientColor(), (int) 255).getRGB();
                     if (Celestial.instance.friendManager.isFriend(entity.getName())) {
                          color = Color.GREEN.getRGB();
                     }
                     RenderUtils.drawRect((-width / 2.0f + 2), 1, (width / 2.0f - 2 - (-width / 2.0f + 2)) * entity.getHealth() / entity.getMaxHealth() + (-width / 2.0f + 2), 7.0, color);
                     
                     mc.mntsb_13.drawStringWithShadow(MathematicHelper.round(entity.getHealth(), 1)+ "", -width / 2.0f + 2.5f, 2.5f, -1);
                     
                     mc.mntsb_13.drawStringWithShadow((int) entity.getMaxHealth() + "", width / 2.0f + 2.0f - 5 - mc.mntsb_13.getStringWidth((int) entity.getHealth() + ""), 2.5f, -1);
                     
                     mc.mntsb_18.drawStringWithShadow(stringName + " " + getHealthColor(entity.getHealth()), -width / 2.0f + 2.0f, -7.5, -1);
                     ItemStack heldItemStack = entity.getHeldItem(EnumHand.OFF_HAND);
                     ArrayList<ItemStack> list = new ArrayList<>();
                     for (int i = 0; i < 5; ++i) {
                         ItemStack getEquipmentInSlot = ((EntityPlayer) entity).getEquipmentInSlot(i);
                         list.add(getEquipmentInSlot);
                     }
                     int n10 = -(list.size() * 9) - 8;
                     if (hp.getBoolValue()) {
                         GlStateManager.pushMatrix();
                         GlStateManager.translate(0.0f, -2.0f, 0.0f);
                         mc.getRenderItem().renderItemIntoGUI(heldItemStack, n10 + 86, -28);
                         mc.getRenderItem().renderItemOverlays(mc.rubik_18, heldItemStack, n10 + 86, -28);

                         for (ItemStack itemStack : list) {
                             RenderHelper.enableGUIStandardItemLighting();
                             mc.getRenderItem().renderItemIntoGUI(itemStack, n10 + 6, -28);
                             mc.getRenderItem().renderItemOverlays(mc.rubik_18, itemStack, n10 + 5, -28);
                             n10 += 3;
                             RenderHelper.disableStandardItemLighting();
                             int n11 = 7;
                             int getEnchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(16)), itemStack);
                             int getEnchantmentLevel2 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(20)), itemStack);
                             int getEnchantmentLevel3 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(19)), itemStack);
                            
                             if (hpDetails.getBoolValue()) {
                             	if (getEnchantmentLevel > 0) {
                                     drawEnchantTag("S" + getColor(getEnchantmentLevel) + getEnchantmentLevel, n10, n11);
                                     n11 += 8;
                                 }
                                 if (getEnchantmentLevel2 > 0) {
                                     drawEnchantTag("F" + getColor(getEnchantmentLevel2) + getEnchantmentLevel2, n10, n11);
                                     n11 += 8;
                                 }
                                 if (getEnchantmentLevel3 > 0) {
                                     drawEnchantTag("Kb" + getColor(getEnchantmentLevel3) + getEnchantmentLevel3, n10, n11);
                                 } else if (itemStack.getItem() instanceof ItemArmor) {
                                     int getEnchantmentLevel4 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(0)), itemStack);
                                     int getEnchantmentLevel5 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(7)), itemStack);
                                     int getEnchantmentLevel6 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(34)), itemStack);
                                     if (getEnchantmentLevel4 > 0) {
                                         drawEnchantTag("P" + getColor(getEnchantmentLevel4) + getEnchantmentLevel4, n10, n11);
                                         n11 += 8;
                                     }
                                     if (getEnchantmentLevel5 > 0) {
                                         drawEnchantTag("Th" + getColor(getEnchantmentLevel5) + getEnchantmentLevel5, n10, n11);
                                         n11 += 8;
                                     }
                                     if (getEnchantmentLevel6 > 0) {
                                         drawEnchantTag("U" + getColor(getEnchantmentLevel6) + getEnchantmentLevel6, n10, n11);
                                     }
                                 } else if (itemStack.getItem() instanceof ItemBow) {
                                     int getEnchantmentLevel7 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(48)), itemStack);
                                     int getEnchantmentLevel8 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(49)), itemStack);
                                     int getEnchantmentLevel9 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(50)), itemStack);
                                     if (getEnchantmentLevel7 > 0) {
                                         drawEnchantTag("P" + getColor(getEnchantmentLevel7) + getEnchantmentLevel7, n10, n11);
                                         n11 += 8;
                                     }
                                     if (getEnchantmentLevel8 > 0) {
                                         drawEnchantTag("P" + getColor(getEnchantmentLevel8) + getEnchantmentLevel8, n10, n11);
                                         n11 += 8;
                                     }
                                     if (getEnchantmentLevel9 > 0) {
                                         drawEnchantTag("F" + getColor(getEnchantmentLevel9) + getEnchantmentLevel9, n10, n11);
                                     }
                                 }
                             }
                             n10 = (int) ((double) n10 + 13.5);
                         }
                         GlStateManager.popMatrix();
                     }
                     if (hp.getBoolValue()) {
                         drawPotionEffect((EntityPlayer) entity);
                     }
                 }
             }
             GlStateManager.popMatrix();
         }
         GlStateManager.popMatrix();
         GlStateManager.enableBlend();
         }
         
         
         
         
         
         
         
         
         
         
         
         
         RenderManager renderMng = mc.getRenderManager();
         float partialTicks = mc.timer.renderPartialTicks;
         int black = this.black;
         EntityRenderer entityRenderer = mc.entityRenderer;
         for (Entity entity : mc.world.loadedEntityList) {
        	 if (entity == null) {
                 continue;
             }
             int color = ClientHelper.getESPColor().getRGB();

             if (Celestial.instance.friendManager.isFriend(entity.getName())) {
            //     color = Color.GREEN.getRGB();
             }

             if (!isValid(entity) || !RenderUtils.isInViewFrustum(entity)) {
                 continue;
             }

             double x = RenderUtils.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
             double y = RenderUtils.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
             double z = RenderUtils.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
             double width = (double) entity.width / 1.5;
             double height = (double) entity.height + (entity.isSneaking() || entity == mc.player && mc.player.isSneaking() ? -0.3 : 0.2);
             AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
             Vector3d[] vectors = new Vector3d[]{new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ)};
             mc.entityRenderer.setupCameraTransform(partialTicks, 0);
             Vector4d position = null;

             for (Vector3d vector : vectors) {
                 vector = project2D(scaleFactor, vector.x - mc.getRenderManager().renderPosX, vector.y - mc.getRenderManager().renderPosY, vector.z - mc.getRenderManager().renderPosZ);

                 if (vector == null || !(vector.z > 0.0) || !(vector.z < 2.0)) {
                     continue;
                 }

                 if (position == null) {
                     position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                 }

                 position.x = Math.min(vector.x, position.x);
                 position.y = Math.min(vector.y, position.y);
                 position.z = Math.max(vector.x, position.z);
                 position.w = Math.max(vector.y, position.w);
             }

             if (position == null) {
                 continue;
             }

             mc.entityRenderer.setupOverlayRendering();
             double posX = position.x;
             double posY = position.y;
             double endPosX = position.z;
             double endPosY = position.w;
             double finalValue = 0.5;
             if (entity.getEntityId() != -20) {
            	 if (box.getBoolValue()) {
                     RenderUtils.drawRect(posX - 1.0, posY, posX + finalValue, endPosY + finalValue, black);
                     RenderUtils.drawGradientRect(posX - finalValue, posY, posX + finalValue - finalValue, endPosY, -1, color);
                     
                     RenderUtils.drawRect(posX - 1.0, posY - finalValue, endPosX + finalValue, posY + finalValue + finalValue, black);
                     RenderUtils.drawRect(posX - finalValue, posY, endPosX, posY + finalValue, -1);
                     
                     RenderUtils.drawRect(endPosX - finalValue - finalValue, posY, endPosX + finalValue, endPosY + finalValue, black);
                     RenderUtils.drawGradientRect(endPosX - finalValue, posY, endPosX, endPosY, -1, color);
                     
                     RenderUtils.drawRect(posX - 1.0, endPosY - finalValue - finalValue, endPosX + finalValue, endPosY + finalValue, black);
                     RenderUtils.drawRect(posX, endPosY - finalValue, endPosX, endPosY, color);
                 }
             }

             EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
             float targetHP = entityLivingBase.getHealth();
             targetHP = MathHelper.clamp(targetHP, 0.0f, 24.0f);
             float maxHealth = entityLivingBase.getMaxHealth();
             double hpPercentage = targetHP / maxHealth;
             double hpHeight2 = (endPosY - posY) * hpPercentage;
             int colorHeal = healcolorMode.currentMode.equals("Static") ? colorEsp.getColorValue() : ClientHelper.getClientColor().getRGB();

             if (hpDetails.getBoolValue()) {
                 String healthDisplay = (int) ((EntityLivingBase) entity).getHealth() + "HP";
                 drawScaledString(healthDisplay, posX - 6.0 - (double) ((float) mc.fontRendererObj.getStringWidth(healthDisplay) * 0.5f), endPosY - hpHeight2, 0.5, colorHeal);
             }

             if (hp.getBoolValue()) {
                 RenderUtils.drawRect(posX - 4.0, posY - 0.5, posX - 1.5, endPosY + 0.5, new Color(0, 0, 0, 125).getRGB());
                 RenderUtils.drawRect(posX - 3.5, endPosY, posX - 2.0, endPosY - hpHeight2, colorHeal);
             }
         }
         

    }

    private boolean isValid(Entity entity) {
        if (entity == null) {
            return false;
        }

        if (mc.gameSettings.thirdPersonView == 0 && entity == mc.player) {
            return false;
        }

        if (entity.isDead) {
            return false;
        }

        if (entity instanceof EntityAnimal && !sel.getSetting("Mobs").getBoolValue()) {
            return false;
        }

        if (entity instanceof EntityPlayer && !sel.getSetting("Players").getBoolValue()) {
            return false;
        }

        if (entity instanceof EntityArmorStand) {
            return false;
        }
        if (entity.getEntityId() == -10) {
            return false;
        }
        if (!sel.getSetting("NPC / Bots").getBoolValue() && !entity.getUniqueID().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + entity.getName()).getBytes(StandardCharsets.UTF_8))) && (entity instanceof EntityOtherPlayerMP)) {
        	return false;
        }

        if (entity instanceof IAnimals && !sel.getSetting("Mobs").getBoolValue()) {
            return false;
        }

        if (entity instanceof EntityVillager && !sel.getSetting("Mobs").getBoolValue()) {
            return false;
        }

        return true;
    }

    private Vector3d project2D(int scaleFactor, double x, double y, double z) {
        float xPos = (float) x;
        float yPos = (float) y;
        float zPos = (float) z;
        IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
        FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

        if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector)) {
            return new Vector3d(vector.get(0) / (float) scaleFactor, ((float) Display.getHeight() - vector.get(1)) / (float) scaleFactor, vector.get(2));
        }

        return null;
    }
}
