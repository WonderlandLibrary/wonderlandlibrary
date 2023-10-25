package fun.expensive.client.feature.impl.hud;

import fun.rich.client.Rich;
import fun.rich.client.draggable.component.impl.DraggableTargetHUD;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.render.EventRender2D;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.feature.impl.combat.KillAura;
import fun.rich.client.feature.impl.misc.NameProtect;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ColorSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.utils.math.AnimationHelper;
import fun.rich.client.utils.math.MathematicHelper;
import fun.rich.client.utils.math.TimerHelper;
import fun.rich.client.utils.other.Particles;
import fun.rich.client.utils.render.ClientHelper;
import fun.rich.client.utils.render.ColorUtils;
import fun.rich.client.utils.render.Colors;
import fun.rich.client.utils.render.RenderUtils;
import fun.rich.client.utils.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class TargetHUD extends Feature {
    private double scale = 0;
    private static EntityLivingBase curTarget = null;

    public static TimerHelper thudTimer = new TimerHelper();
    private float healthBarWidth;
    private ArrayList<Particles> particles = new ArrayList<>();
    public ListSetting targetHudMode = new ListSetting("TargetHUD Mode", "Style", () -> true, "Style", "New");
    public static ListSetting thudColorMode = new ListSetting("TargetHUD Color", "Astolfo", () -> true, "Astolfo", "Rainbow", "Client", "Custom");

    public BooleanSetting particles2 = new BooleanSetting("Particles", thudColorMode.currentMode.equals("Custom"), () -> targetHudMode.currentMode.equals("Style") && thudColorMode.currentMode.equals("Custom"));
    public static ColorSetting targetHudColor = new ColorSetting("THUD Color", Color.PINK.getRGB(), () -> thudColorMode.currentMode.equals("Custom"));
    public BooleanSetting shadowThud = new BooleanSetting("Shadow", true, () -> true);
    public BooleanSetting blurThud = new BooleanSetting("Blur", true, () -> true);

    public TargetHUD() {
        super("TargetHUD", FeatureCategory.Hud);
        addSettings(targetHudMode, thudColorMode, particles2, targetHudColor, shadowThud, blurThud);
    }

    @EventTarget
    public void onRender2D(EventRender2D e) {
        if (targetHudMode.currentMode.equals("Style")) {
            DraggableTargetHUD dth = (DraggableTargetHUD) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableTargetHUD.class);
            float x = dth.getX();
            float y = dth.getY();
            dth.setWidth(130);
            dth.setHeight(42 - 5);
            if (KillAura.target != null) {
                curTarget = KillAura.target;
                scale = AnimationHelper.animation((float) scale, (float) 1, (float) (6 * Rich.deltaTime()));
            } else {
                scale = AnimationHelper.animation((float) scale, (float) 0, (float) (6 * Rich.deltaTime()));
            }
            EntityLivingBase target = KillAura.target;
            if (curTarget != null) {
                if (curTarget instanceof EntityPlayer) {
                    try {

                        GlStateManager.pushMatrix();
                        GlStateManager.resetColor();
                        GL11.glTranslated(x + 36, y + 26, 0);
                        GL11.glScaled(scale, scale, 0);
                        GL11.glTranslated(-(x + 36), -(y + 26), 0);
                        if (blurThud.getBoolValue()) {
                            RenderUtils.drawBlur(8, () -> {
                                RenderUtils.drawSmoothRect(x, y, x + dth.getWidth(), y + dth.getHeight(), new Color(0, 0, 0, 100).getRGB());
                            });
                        }
                        if (shadowThud.getBoolValue()) {
                            RenderUtils.drawShadow(8, 1, () -> {
                                RenderUtils.drawSmoothRect(x, y, x + dth.getWidth(), y + dth.getHeight(), new Color(0, 0, 0, 100).getRGB());
                            });
                        }
                        RenderUtils.drawSmoothRect(x, y, x + dth.getWidth(), y + dth.getHeight(), new Color(0, 0, 0, 100).getRGB());
                        mc.rubik_18.drawString(Rich.instance.featureManager.getFeature(NameProtect.class).isEnabled() && NameProtect.otherName.getBoolValue() ? "Protected" : curTarget.getName(), x + 35, y + 5, -1);

                        mc.rubik_17.drawString((int) curTarget.getHealth() + " HP - " + (int) mc.player.getDistanceToEntity(curTarget) + "m", x + 35, y + 5 + 10, -1);

                        double healthWid = (curTarget.getHealth() / curTarget.getMaxHealth() * 90);
                        healthWid = MathHelper.clamp(healthWid, 0.0D, 90.0D);
                        healthBarWidth = AnimationHelper.animation((float) healthBarWidth, (float) healthWid, (float) (10 * Rich.deltaTime()));


                        RenderUtils.drawRect2(x + 36, y + 25, 90, 5, new Colors(Color.decode("#191f13")).setAlpha(180).getColor().getRGB());

                        RenderUtils.drawGradientRected(x + 36, y + 25, (float) healthBarWidth, 5, ClientHelper.getTargetHudColor().darker().getRGB(), ClientHelper.getTargetHudColor().brighter().brighter().getRGB());
                        RenderUtils.drawBlurredShadow(x + 36, y + 25, (float) healthBarWidth + 2, 5, 8, ClientHelper.getTargetHudColor().darker());

                        if (particles2.getBoolValue() && thudColorMode.currentMode.equals("Custom")) {
                            for (final Particles p : particles) {
                                if (p.opacity > 4) p.render2D();
                            }

                            if (thudTimer.hasReached(15)) {
                                for (final Particles p : particles) {
                                    p.updatePosition();

                                    if (p.opacity < 1) particles.remove(p);
                                }
                                thudTimer.reset();
                            }

                            if (curTarget.hurtTime == 8) {
                                for (int i = 0; i < 1; i++) {
                                    final Particles p = new Particles();
                                    p.init((x + 15), y + 15, ((Math.random() - 0.5) * 2) * 1.9, ((Math.random() - 0.5) * 2) * 1.4, (float) Math.random() * 1, ClientHelper.getTargetHudColor());
                                    particles.add(p);
                                }
                            }
                        }
                        for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                            try {
                                if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == curTarget) {
                                    mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                    final int scaleOffset = (int) (curTarget.hurtTime * 0.55f);

                                    float hurtPercent = getHurtPercent(curTarget);
                                    GL11.glPushMatrix();
                                    GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                                    Gui.drawScaledCustomSizeModalRect((int) x + 3, y + 3, 8.0f, 8.0f, 8, 8, 30, 30, 64.0f, 64.0f);
                                    GL11.glPopMatrix();
                                    GlStateManager.bindTexture(0);
                                }
                            } catch (Exception exception) {
                            }
                        }
                    } catch (Exception exception) {
                    } finally {
                        GlStateManager.popMatrix();
                    }
                }
            }
        } else if (targetHudMode.currentMode.equals("New")) {
            DraggableTargetHUD dth = (DraggableTargetHUD) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableTargetHUD.class);
            float x = dth.getX();
            float y = dth.getY();
            dth.setWidth(153);
            dth.setHeight(42);
            if (KillAura.target != null) {
                curTarget = KillAura.target;
                scale = AnimationHelper.animation((float) scale, (float) 1, (float) (6 * Rich.deltaTime()));
            } else {
                scale = AnimationHelper.animation((float) scale, (float) 0, (float) (6 * Rich.deltaTime()));
            }
            EntityLivingBase target = KillAura.target;
            if (curTarget != null) {
                if (curTarget instanceof EntityPlayer) {
                    try {

                        GlStateManager.pushMatrix();
                        GlStateManager.resetColor();
                        GL11.glTranslated(x + 50, y + 31, 0);
                        GL11.glScaled(scale, scale, 0);
                        GL11.glTranslated(-(x + 50), -(y + 31), 0);
                        if (blurThud.getBoolValue()) {
                            RenderUtils.drawBlur(7, () -> {
                                RenderUtils.drawSmoothRect(x, y, x + dth.getWidth(), y + dth.getHeight(),new Color(17, 17, 17, 200).getRGB());
                            });
                        }
                        if (shadowThud.getBoolValue()) {
                            RenderUtils.drawShadow(5, 1, () -> {
                                RenderUtils.drawSmoothRect(x, y, x + dth.getWidth(), y + dth.getHeight(),new Color(17, 17, 17, 200).getRGB());
                            });
                        }
                        RenderUtils.drawSmoothRect(x, y, x + dth.getWidth(), y + dth.getHeight(),new Color(17, 17, 17, 200).getRGB());


                        double healthWid = (curTarget.getHealth() / curTarget.getMaxHealth() * 110);
                        healthWid = MathHelper.clamp(healthWid, 0.0D, 110.0D);
                        healthBarWidth = AnimationHelper.animation((float) healthBarWidth, (float) healthWid, (float) (10 * Rich.deltaTime()));
                        String health = "" + MathematicHelper.round(curTarget.getHealth(), 1);
                        String distance = "" + MathematicHelper.round(mc.player.getDistanceToEntity(curTarget), 1);

                        mc.rubik_15.drawString("Name: " + curTarget.getName(), x + 42, y + 6, -1);
                        mc.rubik_15.drawString("Distance: " + distance, x + 42, y + 15, -1);

                        mc.rubik_14.drawString(curTarget.getHealth() >= 3 ? health : "", x + 24 + healthBarWidth, y + 26.5F, new Color(200, 200, 200).getRGB());

                        RenderUtils.drawSmoothRect(x + 38, y + 33, (float) x + 38 + healthBarWidth, y + 33 + 5, ClientHelper.getTargetHudColor().darker().getRGB());
                        RenderUtils.drawBlurredShadow(x + 38, y + 33, (float) healthBarWidth, 5, 12, RenderUtils.injectAlpha(ClientHelper.getTargetHudColor(), 100));
                        mc.getRenderItem().renderItemOverlays(mc.rubik_13, curTarget.getHeldItem(EnumHand.OFF_HAND), (int) x + 132, (int) y + 7);
                        mc.getRenderItem().renderItemIntoGUI(curTarget.getHeldItem(EnumHand.OFF_HAND), (int) x + 135, (int) y + 1);

                        for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                            try {
                                if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == curTarget) {
                                    mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                    final int scaleOffset = (int) (curTarget.hurtTime * 0.55f);

                                    float hurtPercent = getHurtPercent(curTarget);
                                    GL11.glPushMatrix();
                                    GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                                    Gui.drawScaledCustomSizeModalRect((int) x + 3, y + 4, 8.0f, 8.0f, 8, 8, 32, 35, 64.0f, 64.0f);
                                    GL11.glPopMatrix();
                                    GlStateManager.bindTexture(0);
                                }
                            } catch (Exception exception) {
                            }
                        }
                    } catch (Exception exception) {
                    } finally {
                        GlStateManager.popMatrix();
                    }
                }
            }
        }
    }

    public static float getRenderHurtTime(EntityLivingBase hurt) {
        return (float) hurt.hurtTime - (hurt.hurtTime != 0 ? mc.timer.renderPartialTicks : 0.0f);
    }

    public static float getHurtPercent(EntityLivingBase hurt) {
        return getRenderHurtTime(hurt) / (float) 10;
    }

    @Override
    public void onEnable() {
        if (this.mc.gameSettings.ofFastRender) {
            this.mc.gameSettings.ofFastRender = false;
        }
        super.onEnable();
    }
}
