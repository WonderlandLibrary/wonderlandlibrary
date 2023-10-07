// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.render;

import java.util.HashMap;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.BufferUtils;
import exhibition.util.RotationUtils;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.Minecraft;
import exhibition.event.RegisterEvent;
import java.util.List;
import exhibition.util.render.TTFFontRenderer;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import exhibition.event.EventSystem;
import exhibition.event.impl.EventNametagRender;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.opengl.GL11;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemArmor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.awt.Color;
import exhibition.util.RenderingUtil;
import exhibition.util.render.Colors;
import exhibition.Client;
import exhibition.management.friend.FriendManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import exhibition.event.impl.EventRenderGui;
import exhibition.event.impl.EventRender3D;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import net.minecraft.entity.EntityLivingBase;
import java.util.Map;
import exhibition.module.Module;

public class Nametags extends Module
{
    public static Map<EntityLivingBase, double[]> entityPositions;
    public static String ARMOR;
    public static String HEALTH;
    private final String INVISIBLES = "INVISIBLES";
    
    public Nametags(final ModuleData data) {
        super(data);
        ((HashMap<String, Setting<Boolean>>)this.settings).put(Nametags.ARMOR, new Setting<Boolean>(Nametags.ARMOR, true, "Show armor when not hovering."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put(Nametags.HEALTH, new Setting<Boolean>(Nametags.HEALTH, false, "Show health when not hovering."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("INVISIBLES", new Setting<Boolean>("INVISIBLES", false, "Show invisibles."));
    }
    
    @RegisterEvent(events = { EventRender3D.class, EventRenderGui.class, EventNametagRender.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventRender3D) {
            try {
                this.updatePositions();
            }
            catch (Exception ex) {}
        }
        if (event instanceof EventRenderGui) {
            final EventRenderGui er = (EventRenderGui)event;
            GlStateManager.pushMatrix();
            final ScaledResolution scaledRes = new ScaledResolution(Nametags.mc, Nametags.mc.displayWidth, Nametags.mc.displayHeight);
            for (final Entity ent : Nametags.entityPositions.keySet()) {
                if ((ent != Nametags.mc.thePlayer && ((HashMap<K, Setting<Boolean>>)this.settings).get("INVISIBLES").getValue()) || !ent.isInvisible()) {
                    GlStateManager.pushMatrix();
                    if (ent instanceof EntityPlayer) {
                        String str = ent.getDisplayName().getFormattedText();
                        str = str.replace(ent.getDisplayName().getFormattedText(), FriendManager.isFriend(ent.getName()) ? ("§b" + FriendManager.getAlias(ent.getName())) : ("§f" + ent.getDisplayName().getFormattedText()));
                        final double[] renderPositions = Nametags.entityPositions.get(ent);
                        if (renderPositions[3] < 0.0 || renderPositions[3] >= 1.0) {
                            GlStateManager.popMatrix();
                            continue;
                        }
                        final TTFFontRenderer font = Client.subHeader;
                        GlStateManager.translate(renderPositions[0] / scaledRes.getScaleFactor(), renderPositions[1] / scaledRes.getScaleFactor(), 0.0);
                        this.scale();
                        final String healthInfo = (int)((EntityLivingBase)ent).getHealth() + "";
                        GlStateManager.translate(0.0, -2.5, 0.0);
                        final float strWidth = font.getWidth(str);
                        final float strWidth2 = font.getWidth(healthInfo);
                        RenderingUtil.rectangle(-strWidth / 2.0f - 1.0f, -10.0, strWidth / 2.0f + 1.0f, 0.0, Colors.getColor(0, 130));
                        final int x3 = (int)(renderPositions[0] + -strWidth / 2.0f - 3.0) / 2 - 26;
                        final int x4 = (int)(renderPositions[0] + strWidth / 2.0f + 3.0) / 2 + 20;
                        final int y1 = (int)(renderPositions[1] - 30.0) / 2;
                        final int y2 = (int)(renderPositions[1] + 11.0) / 2;
                        final int mouseY = er.getResolution().getScaledHeight() / 2;
                        final int mouseX = er.getResolution().getScaledWidth() / 2;
                        font.drawStringWithShadow(str, -strWidth / 2.0f, -7.0f, Colors.getColor(255, 50, 50));
                        final boolean healthOption = !((HashMap<K, Setting<Boolean>>)this.settings).get(Nametags.HEALTH).getValue();
                        final boolean armor = !((HashMap<K, Setting<Boolean>>)this.settings).get(Nametags.ARMOR).getValue();
                        final boolean hovered = x3 < mouseX && mouseX < x4 && y1 < mouseY && mouseY < y2;
                        if (!healthOption || hovered) {
                            final float health = ((EntityPlayer)ent).getHealth();
                            final float[] fractions = { 0.0f, 0.5f, 1.0f };
                            final Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
                            final float progress = health * 5.0f * 0.01f;
                            final Color customColor = ESP2D.blendColors(fractions, colors, progress).brighter();
                            try {
                                RenderingUtil.rectangle(strWidth / 2.0f + 2.0f, -10.0, strWidth / 2.0f + 1.0f + strWidth2, 0.0, Colors.getColor(0, 130));
                                font.drawStringWithShadow(healthInfo, strWidth / 2.0f + 2.0f, -7.0f, customColor.getRGB());
                            }
                            catch (Exception ex2) {}
                        }
                        if (hovered || !armor) {
                            final List<ItemStack> itemsToRender = new ArrayList<ItemStack>();
                            for (int i = 0; i < 5; ++i) {
                                final ItemStack stack = ((EntityPlayer)ent).getEquipmentInSlot(i);
                                if (stack != null) {
                                    itemsToRender.add(stack);
                                }
                            }
                            int x5 = -(itemsToRender.size() * 9);
                            for (final ItemStack stack2 : itemsToRender) {
                                RenderHelper.enableGUIStandardItemLighting();
                                Nametags.mc.getRenderItem().remderItemIntoGUI(stack2, x5, -27);
                                Nametags.mc.getRenderItem().renderItemOverlays(Nametags.mc.fontRendererObj, stack2, x5, -27);
                                x5 += 3;
                                RenderHelper.disableStandardItemLighting();
                                if (stack2 != null) {
                                    int y3 = 21;
                                    final int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack2);
                                    final int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack2);
                                    final int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack2);
                                    if (sLevel > 0) {
                                        drawEnchantTag("Sh" + this.getColor(sLevel) + sLevel, x5, y3);
                                        y3 -= 9;
                                    }
                                    if (fLevel > 0) {
                                        drawEnchantTag("Fir" + this.getColor(fLevel) + fLevel, x5, y3);
                                        y3 -= 9;
                                    }
                                    if (kLevel > 0) {
                                        drawEnchantTag("Kb" + this.getColor(kLevel) + kLevel, x5, y3);
                                    }
                                    else if (stack2.getItem() instanceof ItemArmor) {
                                        final int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack2);
                                        final int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack2);
                                        final int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack2);
                                        if (pLevel > 0) {
                                            drawEnchantTag("P" + this.getColor(pLevel) + pLevel, x5, y3);
                                            y3 -= 9;
                                        }
                                        if (tLevel > 0) {
                                            drawEnchantTag("Th" + this.getColor(tLevel) + tLevel, x5, y3);
                                            y3 -= 9;
                                        }
                                        if (uLevel > 0) {
                                            drawEnchantTag("Unb" + this.getColor(uLevel) + uLevel, x5, y3);
                                        }
                                    }
                                    else if (stack2.getItem() instanceof ItemBow) {
                                        final int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack2);
                                        final int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack2);
                                        final int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack2);
                                        if (powLevel > 0) {
                                            drawEnchantTag("Pow" + this.getColor(powLevel) + powLevel, x5, y3);
                                            y3 -= 9;
                                        }
                                        if (punLevel > 0) {
                                            drawEnchantTag("Pun" + this.getColor(punLevel) + punLevel, x5, y3);
                                            y3 -= 9;
                                        }
                                        if (fireLevel > 0) {
                                            drawEnchantTag("Fir" + this.getColor(fireLevel) + fireLevel, x5, y3);
                                        }
                                    }
                                    else if (stack2.getRarity() == EnumRarity.EPIC) {
                                        drawEnchantTag("§6§lGod", x5, y3);
                                    }
                                    final int var7 = (int)Math.round(255.0 - stack2.getItemDamage() * 255.0 / stack2.getMaxDamage());
                                    final int var8 = 255 - var7 << 16 | var7 << 8;
                                    final Color customColor2 = new Color(var8).brighter();
                                    final int x6 = (int)(x5 * 1.75);
                                    if (stack2.getMaxDamage() - stack2.getItemDamage() > 0) {
                                        GlStateManager.pushMatrix();
                                        GlStateManager.disableDepth();
                                        GL11.glScalef(0.57f, 0.57f, 0.57f);
                                        font.drawStringWithShadow("" + (stack2.getMaxDamage() - stack2.getItemDamage()), x6, -54.0f, customColor2.getRGB());
                                        GlStateManager.enableDepth();
                                        GlStateManager.popMatrix();
                                    }
                                    y3 = -53;
                                    for (final Object o : ((EntityPlayer)ent).getActivePotionEffects()) {
                                        final PotionEffect pot = (PotionEffect)o;
                                        final String potName = StringUtils.capitalize(pot.getEffectName().substring(pot.getEffectName().lastIndexOf(".") + 1));
                                        final int XD = pot.getDuration() / 20;
                                        final SimpleDateFormat df = new SimpleDateFormat("m:ss");
                                        final String time = df.format(XD * 1000);
                                        font.drawStringWithShadow((XD > 0) ? (potName + " " + time) : "", -30.0f, y3, -1);
                                        y3 -= 8;
                                    }
                                    x5 += 12;
                                }
                            }
                        }
                    }
                    GlStateManager.popMatrix();
                }
            }
            GlStateManager.popMatrix();
        }
        final Event enr = EventSystem.getInstance(EventNametagRender.class);
        enr.setCancelled(true);
    }
    
    private String getColor(final int level) {
        if (level != 1) {
            if (level == 2) {
                return "§a";
            }
            if (level == 3) {
                return "§3";
            }
            if (level == 4) {
                return "§4";
            }
            if (level >= 5) {
                return "§6";
            }
        }
        return "§f";
    }
    
    private static void drawEnchantTag(final String text, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        x *= (int)1.75;
        y -= 4;
        GL11.glScalef(0.57f, 0.57f, 0.57f);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, -30 - y, Colors.getColor(255));
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
    
    private void scale() {
        float scale = 1.0f;
        scale *= ((Nametags.mc.currentScreen == null && GameSettings.isKeyDown(Nametags.mc.gameSettings.ofKeyBindZoom)) ? 2 : 1);
        GlStateManager.scale(scale, scale, scale);
    }
    
    private void updatePositions() {
        Nametags.entityPositions.clear();
        final float pTicks = Nametags.mc.timer.renderPartialTicks;
        for (final Object o : Nametags.mc.theWorld.loadedEntityList) {
            final Entity ent = (Entity)o;
            if (ent != Nametags.mc.thePlayer && ent instanceof EntityPlayer && (((HashMap<K, Setting<Boolean>>)this.settings).get("INVISIBLES").getValue() || !ent.isInvisible())) {
                final double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - Nametags.mc.getRenderManager().viewerPosX;
                double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - Nametags.mc.getRenderManager().viewerPosY;
                final double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - Nametags.mc.getRenderManager().viewerPosZ;
                y += ent.height + 0.2;
                if (this.convertTo2D(x, y, z)[2] < 0.0 || this.convertTo2D(x, y, z)[2] >= 1.0) {
                    continue;
                }
                Nametags.entityPositions.put((EntityPlayer)ent, new double[] { this.convertTo2D(x, y, z)[0], this.convertTo2D(x, y, z)[1], Math.abs(this.convertTo2D(x, y + 1.0, z, ent)[1] - this.convertTo2D(x, y, z, ent)[1]), this.convertTo2D(x, y, z)[2] });
            }
        }
    }
    
    private double[] convertTo2D(final double x, final double y, final double z, final Entity ent) {
        final float pTicks = Nametags.mc.timer.renderPartialTicks;
        final float prevYaw = Nametags.mc.thePlayer.rotationYaw;
        final float prevPrevYaw = Nametags.mc.thePlayer.prevRotationYaw;
        final float[] rotations = RotationUtils.getRotationFromPosition(ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks, ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks, ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - 1.6);
        final Entity renderViewEntity = Nametags.mc.getRenderViewEntity();
        final Entity renderViewEntity2 = Nametags.mc.getRenderViewEntity();
        final float n = rotations[0];
        renderViewEntity2.prevRotationYaw = n;
        renderViewEntity.rotationYaw = n;
        Nametags.mc.entityRenderer.setupCameraTransform(pTicks, 0);
        final double[] convertedPoints = this.convertTo2D(x, y, z);
        Nametags.mc.getRenderViewEntity().rotationYaw = prevYaw;
        Nametags.mc.getRenderViewEntity().prevRotationYaw = prevPrevYaw;
        Nametags.mc.entityRenderer.setupCameraTransform(pTicks, 0);
        return convertedPoints;
    }
    
    private double[] convertTo2D(final double x, final double y, final double z) {
        final FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        final IntBuffer viewport = BufferUtils.createIntBuffer(16);
        final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        final FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        final boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
        if (result) {
            return new double[] { screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2) };
        }
        return null;
    }
    
    static {
        Nametags.entityPositions = new HashMap<EntityLivingBase, double[]>();
        Nametags.ARMOR = "ARMOR";
        Nametags.HEALTH = "HEALTH";
    }
}
