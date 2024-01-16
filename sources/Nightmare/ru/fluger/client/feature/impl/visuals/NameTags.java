// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import java.util.HashMap;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import java.util.Iterator;
import java.util.Objects;
import java.util.ArrayList;
import ru.fluger.client.helpers.render.RenderHelper;
import java.awt.Color;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.helpers.palette.PaletteHelper;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.helpers.math.MathematicHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.feature.impl.misc.NameProtect;
import ru.fluger.client.Fluger;
import ru.fluger.client.feature.impl.combat.AntiBot;
import ru.fluger.client.event.events.impl.render.EventRender2D;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.render.EventRender3D;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import java.util.Map;
import ru.fluger.client.feature.Feature;

public class NameTags extends Feature
{
    public static Map<vp, double[]> entityPositions;
    public BooleanSetting armor;
    public BooleanSetting potion;
    public BooleanSetting glowPotion;
    public BooleanSetting backGround;
    public ListSetting backGroundMode;
    public BooleanSetting offHand;
    public NumberSetting opacity;
    public NumberSetting size;
    
    public NameTags() {
        super("NameTags", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0438\u0433\u0440\u043e\u043a\u043e\u0432, \u043d\u0438\u043a, \u0431\u0440\u043e\u043d\u044e \u0438 \u0438\u0445 \u0437\u0434\u043e\u0440\u043e\u0432\u044c\u0435 \u0441\u043a\u0432\u043e\u0437\u044c \u0441\u0442\u0435\u043d\u044b", Type.Visuals);
        this.armor = new BooleanSetting("Show Armor", true, () -> true);
        this.potion = new BooleanSetting("Show Potions", true, () -> true);
        this.glowPotion = new BooleanSetting("Glow Potions", true, () -> true);
        this.backGround = new BooleanSetting("NameTags Background", true, () -> true);
        this.backGroundMode = new ListSetting("Background Mode", "Default", new String[] { "Default", "Shader" });
        this.offHand = new BooleanSetting("OffHand Render", true, () -> true);
        this.opacity = new NumberSetting("Background Opacity", 150.0f, 0.0f, 255.0f, 5.0f, () -> this.backGround.getCurrentValue());
        this.size = new NumberSetting("NameTags Size", 1.0f, 0.5f, 2.0f, 0.1f, () -> true);
        this.addSettings(this.size, this.backGround, this.backGroundMode, this.opacity, this.offHand, this.armor, this.potion, this.glowPotion);
    }
    
    public static a getHealthColor(final float health) {
        if (health <= 4.0f) {
            return a.m;
        }
        if (health <= 8.0f) {
            return a.g;
        }
        if (health <= 12.0f) {
            return a.o;
        }
        if (health <= 16.0f) {
            return a.c;
        }
        return a.k;
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D event) {
        this.updatePositions();
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D event) {
        final bit sr = new bit(NameTags.mc);
        bus.G();
        for (final vp entity : NameTags.entityPositions.keySet()) {
            if (entity != null && entity != NameTags.mc.h) {
                if (AntiBot.isBotPlayer.contains(entity)) {
                    continue;
                }
                bus.G();
                if (entity instanceof aed) {
                    final double[] array = NameTags.entityPositions.get(entity);
                    if (array[3] < 0.0 || array[3] >= 1.0) {
                        bus.H();
                        continue;
                    }
                    final double scaleFactor = sr.e();
                    bus.b(array[0] / scaleFactor, array[1] / scaleFactor, 0.0);
                    this.scale();
                    final String string = (Fluger.instance.featureManager.getFeatureByClass(NameProtect.class).getState() && NameProtect.otherName.getCurrentValue()) ? "Protected" : (Fluger.instance.friendManager.isFriend(entity.h_()) ? ((Fluger.instance.featureManager.getFeatureByClass(NameProtect.class).getState() && NameProtect.friends.getCurrentValue()) ? "Protected" : (ChatFormatting.GREEN + "[F] " + ChatFormatting.RESET + entity.i_().c())) : entity.i_().c());
                    final String stringHP = MathematicHelper.round(entity.cd(), 1) + " ";
                    final String string2 = "" + stringHP;
                    final float width = (float)(ClientHelper.getFontRender().getStringWidth(string2 + " " + string) + 5);
                    bus.b(0.0, -10.0, 0.0);
                    if (this.backGround.getCurrentValue()) {
                        if (this.backGroundMode.currentMode.equals("Default")) {
                            RectHelper.drawRect(-width / 2.0f, -10.0, width / 2.0f, 2.0, PaletteHelper.getColor(0, (int)this.opacity.getCurrentValue()));
                        }
                        else if (this.backGroundMode.currentMode.equals("Shader")) {
                            RenderHelper.renderBlurredShadow(new Color(0, 0, 0, (int)this.opacity.getCurrentValue()), -width / 2.0f - 2.0f, -10.0, width + 3.0f, 11.0, 20);
                        }
                    }
                    ClientHelper.getFontRender().drawStringWithShadow(string + " " + getHealthColor(entity.cd()) + string2, -width / 2.0f + 2.0f, -7.5, -1);
                    final aip heldItemStack = entity.b(ub.b);
                    final ArrayList<aip> list = new ArrayList<aip>();
                    for (int i = 0; i < 5; ++i) {
                        final aip getEquipmentInSlot = ((aed)entity).getEquipmentInSlot(i);
                        list.add(getEquipmentInSlot);
                    }
                    int n10 = -(list.size() * 9) - 8;
                    if (this.armor.getCurrentValue()) {
                        bus.G();
                        bus.c(0.0f, -2.0f, 0.0f);
                        if (this.offHand.getCurrentValue()) {
                            NameTags.mc.ad().renderItemIntoGUI(heldItemStack, (float)(n10 + 86), -28.0f);
                            NameTags.mc.ad().a(NameTags.mc.k, heldItemStack, n10 + 86, -28);
                        }
                        for (final aip itemStack : list) {
                            bhz.c();
                            NameTags.mc.ad().renderItemIntoGUI(itemStack, (float)(n10 + 6), -28.0f);
                            NameTags.mc.ad().renderItemOverlays(ClientHelper.getFontRender(), itemStack, n10 + 5, -28);
                            n10 += 3;
                            bhz.a();
                            int n11 = 7;
                            final int getEnchantmentLevel = alm.a(Objects.requireNonNull(alk.c(16)), itemStack);
                            final int getEnchantmentLevel2 = alm.a(Objects.requireNonNull(alk.c(20)), itemStack);
                            final int getEnchantmentLevel3 = alm.a(Objects.requireNonNull(alk.c(19)), itemStack);
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
                            }
                            else if (itemStack.c() instanceof agv) {
                                final int getEnchantmentLevel4 = alm.a(Objects.requireNonNull(alk.c(0)), itemStack);
                                final int getEnchantmentLevel5 = alm.a(Objects.requireNonNull(alk.c(7)), itemStack);
                                final int getEnchantmentLevel6 = alm.a(Objects.requireNonNull(alk.c(34)), itemStack);
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
                            }
                            else if (itemStack.c() instanceof ahg) {
                                final int getEnchantmentLevel7 = alm.a(Objects.requireNonNull(alk.c(48)), itemStack);
                                final int getEnchantmentLevel8 = alm.a(Objects.requireNonNull(alk.c(49)), itemStack);
                                final int getEnchantmentLevel9 = alm.a(Objects.requireNonNull(alk.c(50)), itemStack);
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
                            n10 += (int)13.5;
                        }
                        bus.H();
                    }
                    if (this.potion.getCurrentValue()) {
                        this.drawPotionEffect((aed)entity);
                    }
                }
                bus.H();
            }
        }
        bus.H();
        bus.m();
    }
    
    private void drawPotionEffect(final aed entity) {
        final int tagWidth = 0;
        int stringY = -25;
        if (entity.cg() > 0 || (!entity.co().b() && !entity.cp().b())) {
            stringY -= 37;
        }
        for (final va potionEffect : entity.ca()) {
            final uz potion = potionEffect.a();
            final boolean potRanOut = potionEffect.b() != 0.0;
            String power = "";
            if (entity.a(potion)) {
                if (!potRanOut) {
                    continue;
                }
                if (potionEffect.c() == 1) {
                    power = "II";
                }
                else if (potionEffect.c() == 2) {
                    power = "III";
                }
                else if (potionEffect.c() == 3) {
                    power = "IV";
                }
                else if (potionEffect.c() == 4) {
                    power = "V";
                }
                else if (potionEffect.c() == 5) {
                    power = "VI";
                }
                bus.G();
                bus.c(1.0f, 1.0f, 1.0f, 1.0f);
                final float xValue = tagWidth - ClientHelper.getFontRender().getStringWidth(cey.a(potion.a(), new Object[0]) + " " + power + a.h + " " + uz.getDurationString(potionEffect)) / 2.0f;
                if (!this.glowPotion.getCurrentValue()) {
                    ClientHelper.getFontRender().drawStringWithShadow(cey.a(potion.a(), new Object[0]) + " " + power + a.h + " " + uz.getDurationString(potionEffect), xValue, stringY, -1);
                }
                else {
                    ClientHelper.getFontRender().drawBlurredStringWithShadow(cey.a(potion.a(), new Object[0]) + " " + power + a.h + " " + uz.getDurationString(potionEffect), xValue, stringY, 15, new Color(0, 0, 0, 100), -1);
                }
                stringY -= 10;
                bus.H();
            }
        }
    }
    
    private void drawEnchantTag(final String text, final int n, int n2) {
        bus.G();
        bus.j();
        n2 -= 7;
        ClientHelper.getFontRender().drawStringWithShadow(text, n + 6, -35 - n2, -1);
        bus.k();
        bus.H();
    }
    
    private String getColor(final int n) {
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
        NameTags.entityPositions.clear();
        final float pTicks = NameTags.mc.Y.renderPartialTicks;
        for (final vg o : NameTags.mc.f.e) {
            if (o != null && o != NameTags.mc.h) {
                if (!(o instanceof aed)) {
                    continue;
                }
                final double x = o.M + (o.p - o.M) * pTicks - NameTags.mc.ac().h;
                double y = o.N + (o.q - o.N) * pTicks - NameTags.mc.ac().i;
                final double z = o.O + (o.r - o.O) * pTicks - NameTags.mc.ac().j;
                if (Objects.requireNonNull(this.convertTo2D(x, y += o.H + 0.2, z))[2] < 0.0) {
                    continue;
                }
                if (Objects.requireNonNull(this.convertTo2D(x, y, z))[2] >= 2.0) {
                    continue;
                }
                NameTags.entityPositions.put((aed)o, new double[] { Objects.requireNonNull(this.convertTo2D(x, y, z))[0], Objects.requireNonNull(this.convertTo2D(x, y, z))[1], Math.abs(Objects.requireNonNull(this.convertTo2D(x, y + 1.0, z))[1] - Objects.requireNonNull(this.convertTo2D(x, y, z))[1]), Objects.requireNonNull(this.convertTo2D(x, y, z))[2] });
            }
        }
    }
    
    private double[] convertTo2D(final double x, final double y, final double z) {
        final FloatBuffer screenCords = BufferUtils.createFloatBuffer(3);
        final IntBuffer viewport = BufferUtils.createIntBuffer(16);
        final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        final FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        final boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCords);
        if (result) {
            return new double[] { screenCords.get(0), Display.getHeight() - screenCords.get(1), screenCords.get(2) };
        }
        return null;
    }
    
    private void scale() {
        final float n = NameTags.mc.t.aB ? 2.0f : this.size.getCurrentValue();
        bus.b(n, n, n);
    }
    
    static {
        NameTags.entityPositions = new HashMap<vp, double[]>();
    }
}
