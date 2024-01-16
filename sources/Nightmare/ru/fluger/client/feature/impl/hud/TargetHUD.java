// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.hud;

import ru.fluger.client.event.EventTarget;
import java.util.Iterator;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.helpers.math.MathematicHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.helpers.render.AnimationHelper;
import ru.fluger.client.feature.impl.combat.KillAura;
import ru.fluger.client.ui.components.draggable.component.DraggableComponent;
import ru.fluger.client.Fluger;
import ru.fluger.client.ui.components.draggable.component.impl.DraggableTargetHUD;
import ru.fluger.client.event.events.impl.render.EventRender2D;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.feature.Feature;

public class TargetHUD extends Feature
{
    private double scale;
    private static vp curTarget;
    public static TimerHelper thudTimer;
    private float healthBarWidth;
    public static ListSetting thudColorMode;
    public static ColorSetting targetHudColor;
    
    public TargetHUD() {
        super("TargetHUD", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u043a\u0430\u043a\u0438\u0448 \u043d\u0430 \u044d\u043a\u0440\u0430\u043d\u0435", Type.Hud);
        this.scale = 0.0;
        this.addSettings(TargetHUD.thudColorMode, TargetHUD.targetHudColor);
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D e) {
        final DraggableTargetHUD dth = (DraggableTargetHUD)Fluger.instance.draggableHUD.getDraggableComponentByClass(DraggableTargetHUD.class);
        final float x = (float)dth.getX();
        final float y = (float)dth.getY();
        dth.setWidth(153);
        dth.setHeight(42);
        if (KillAura.target != null) {
            TargetHUD.curTarget = KillAura.target;
            this.scale = AnimationHelper.animation((float)this.scale, 1.0f, (float)(14.0 * Fluger.deltaTime()));
        }
        else {
            this.scale = AnimationHelper.animation((float)this.scale, 0.0f, (float)(14.0 * Fluger.deltaTime()));
        }
        final vp target = KillAura.target;
        if (TargetHUD.curTarget != null && TargetHUD.curTarget instanceof aed) {
            try {
                bus.G();
                bus.I();
                GL11.glTranslated((double)(x + 50.0f), (double)(y + 31.0f), 0.0);
                GL11.glScaled(this.scale, this.scale, 0.0);
                GL11.glTranslated((double)(-(x + 50.0f)), (double)(-(y + 31.0f)), 0.0);
                RenderHelper.renderBlurredShadow(new Color(18, 18, 18, 200), dth.getX(), dth.getY(), 155.0, 43.0, 20);
                RectHelper.drawSmoothRect(x, y, x + dth.getWidth(), y + dth.getHeight(), new Color(17, 17, 17, 200).getRGB());
                double healthWid = TargetHUD.curTarget.cd() / TargetHUD.curTarget.cj() * 110.0f;
                healthWid = rk.a(healthWid, 0.0, 110.0);
                this.healthBarWidth = AnimationHelper.calculateCompensation((float)healthWid, this.healthBarWidth, 5.0f, 5.0);
                final String health = "" + MathematicHelper.round(TargetHUD.curTarget.cd(), 1);
                final String distance = "" + MathematicHelper.round(TargetHUD.mc.h.g(TargetHUD.curTarget), 1);
                TargetHUD.mc.rubik_15.drawString("Name: " + TargetHUD.curTarget.h_(), x + 42.0f, y + 6.0f, -1);
                TargetHUD.mc.rubik_15.drawString("Distance: " + distance, x + 42.0f, y + 15.0f, -1);
                TargetHUD.mc.rubik_14.drawString((TargetHUD.curTarget.cd() >= 3.0f) ? health : "", x + 24.0f + this.healthBarWidth, y + 26.5f, new Color(200, 200, 200).getRGB());
                RectHelper.drawSmoothRect(x + 38.0f, y + 33.0f, x + 38.0f + this.healthBarWidth, y + 33.0f + 5.0f, ClientHelper.getTargetHudColor().darker().getRGB());
                RenderHelper.drawBlurredShadow(x + 38.0f, y + 33.0f, this.healthBarWidth, 5.0f, 12, RenderHelper.injectAlpha(ClientHelper.getTargetHudColor(), 100));
                TargetHUD.mc.ad().renderItemOverlays(TargetHUD.mc.rubik_13, TargetHUD.curTarget.b(ub.b), (int)x + 132, (int)y + 7);
                TargetHUD.mc.ad().a(TargetHUD.curTarget.b(ub.b), (int)x + 135, (int)y + 1);
                for (final bsc targetHead : TargetHUD.mc.h.d.d()) {
                    try {
                        if (TargetHUD.mc.f.b(targetHead.a().getId()) != TargetHUD.curTarget) {
                            continue;
                        }
                        TargetHUD.mc.N().a(targetHead.g());
                        final int scaleOffset = (int)(TargetHUD.curTarget.ay * 0.55f);
                        final float hurtPercent = getHurtPercent(TargetHUD.curTarget);
                        GL11.glPushMatrix();
                        GL11.glColor4f(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                        bir.drawScaledCustomSizeModalRect((float)((int)x + 3), y + 4.0f, 8.0f, 8.0f, 8.0f, 8.0f, 32.0f, 35.0f, 64.0f, 64.0f);
                        GL11.glPopMatrix();
                        bus.i(0);
                    }
                    catch (Exception ex) {}
                }
            }
            catch (Exception ex2) {}
            finally {
                bus.H();
            }
        }
    }
    
    public static float getRenderHurtTime(final vp hurt) {
        return hurt.ay - ((hurt.ay != 0) ? TargetHUD.mc.Y.renderPartialTicks : 0.0f);
    }
    
    public static float getHurtPercent(final vp hurt) {
        return getRenderHurtTime(hurt) / 10.0f;
    }
    
    @Override
    public void onEnable() {
        if (TargetHUD.mc.t.ofFastRender) {
            TargetHUD.mc.t.ofFastRender = false;
        }
        super.onEnable();
    }
    
    static {
        TargetHUD.curTarget = null;
        TargetHUD.thudTimer = new TimerHelper();
        TargetHUD.thudColorMode = new ListSetting("TargetHUD Color", "Astolfo", () -> true, new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
        TargetHUD.targetHudColor = new ColorSetting("THUD Color", Color.PINK.getRGB(), () -> TargetHUD.thudColorMode.currentMode.equals("Custom"));
    }
}
