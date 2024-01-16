// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.helpers.render.RenderHelper;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.helpers.palette.PaletteHelper;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.Fluger;
import ru.fluger.client.feature.impl.combat.KillAura;
import ru.fluger.client.event.events.impl.render.EventRender3D;
import ru.fluger.client.settings.Setting;
import java.awt.Color;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class TargetESP extends Feature
{
    double height;
    boolean animat;
    private double circleAnim;
    private double circleValue;
    private boolean canDown;
    public NumberSetting circlesize;
    public NumberSetting circleSpeed;
    public BooleanSetting depthTest;
    public ColorSetting targetEspColor;
    public ListSetting jelloMode;
    
    public TargetESP() {
        super("TargetESP", "\u0420\u0438\u0441\u0443\u0435\u0442 \u043a\u0440\u0430\u0441\u0438\u0432\u044b\u0439 \u043a\u0440\u0443\u0433 \u043d\u0430 \u044d\u043d\u0442\u0438\u0442\u0438", Type.Visuals);
        this.jelloMode = new ListSetting("Jello Color", "Client", () -> true, new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
        this.circlesize = new NumberSetting("Circle Size", "\u0420\u0430\u0437\u043c\u0435\u0440 \u043a\u0440\u0443\u0433\u0430", 0.1f, 0.1f, 0.5f, 0.1f, () -> true);
        this.depthTest = new BooleanSetting("DepthTest", "\u0413\u043b\u0443\u0431\u0438\u043d\u0430(test)", false, () -> true);
        this.targetEspColor = new ColorSetting("TargetESP Color", Color.PINK.getRGB(), () -> true);
        this.circleSpeed = new NumberSetting("Circle Speed", "\u0420\u0435\u0433\u0443\u043b\u0438\u0440\u0443\u0435\u0442 \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c Jello \u043a\u0440\u0443\u0433\u0430", 0.01f, 0.001f, 0.05f, 0.001f, () -> true);
        this.addSettings(this.jelloMode, this.circleSpeed, this.circlesize, this.targetEspColor, this.depthTest);
    }
    
    @EventTarget
    public void onRender(final EventRender3D event3D) {
        if (!KillAura.target.F) {
            if (KillAura.mc.h.g(KillAura.target) <= KillAura.range.getCurrentValue() + KillAura.preAimRange.getCurrentValue()) {
                if (KillAura.target != null && Fluger.instance.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.mc.h.g(KillAura.target) <= KillAura.range.getCurrentValue() + KillAura.preAimRange.getCurrentValue()) {
                    final int oneColor = this.targetEspColor.getColor();
                    int color = 0;
                    final String currentMode = this.jelloMode.currentMode;
                    switch (currentMode) {
                        case "Client": {
                            color = ClientHelper.getClientColor().getRGB();
                            break;
                        }
                        case "Custom": {
                            color = oneColor;
                            break;
                        }
                        case "Astolfo": {
                            color = PaletteHelper.astolfo(false, 1).getRGB();
                            break;
                        }
                        case "Rainbow": {
                            color = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                            break;
                        }
                    }
                    final double x = KillAura.target.M + (KillAura.target.p - KillAura.target.M) * KillAura.mc.Y.renderPartialTicks - KillAura.mc.ac().o;
                    final double y = KillAura.target.N + (KillAura.target.q - KillAura.target.N) * KillAura.mc.Y.renderPartialTicks - KillAura.mc.ac().p;
                    final double z = KillAura.target.O + (KillAura.target.r - KillAura.target.O) * KillAura.mc.Y.renderPartialTicks - KillAura.mc.ac().q;
                    this.circleValue += this.circleSpeed.getCurrentValue() * (bib.frameTime * 0.1);
                    final float targetHeight = (float)(0.5 * (1.0 + Math.sin(6.283185307179586 * (this.circleValue * 0.30000001192092896))));
                    final float size = KillAura.target.G + this.circlesize.getCurrentValue();
                    final float endYValue = (float)((float)(KillAura.target.H * 1.0 + 0.2) * (double)targetHeight);
                    if (targetHeight > 0.99) {
                        this.canDown = true;
                    }
                    else if (targetHeight < 0.01) {
                        this.canDown = false;
                    }
                    bus.m();
                    GL11.glBlendFunc(770, 771);
                    GL11.glEnable(2848);
                    if (this.depthTest.getCurrentValue()) {
                        bus.j();
                    }
                    bus.z();
                    bus.d();
                    GL11.glLineWidth(2.0f);
                    GL11.glShadeModel(7425);
                    GL11.glDisable(2884);
                    GL11.glBegin(5);
                    final float alpha = (this.canDown ? (255.0f * targetHeight) : (255.0f * (1.0f - targetHeight))) / 255.0f;
                    final float red = (color >> 16 & 0xFF) / 255.0f;
                    final float green = (color >> 8 & 0xFF) / 255.0f;
                    final float blue = (color & 0xFF) / 255.0f;
                    for (int i = 0; i < 2166; ++i) {
                        RenderHelper.color(red, green, blue, alpha);
                        final double iSin = Math.sin(Math.toRadians(i)) * size;
                        final double iCos = Math.cos(Math.toRadians(i)) * size;
                        GL11.glVertex3d(x + iCos, y + endYValue, z - iSin);
                        RenderHelper.color(red, green, blue, 0.0f);
                        GL11.glVertex3d(x + iCos, y + endYValue + (this.canDown ? (-0.5f * (1.0f - targetHeight)) : (0.5f * targetHeight)), z - iSin);
                    }
                    GL11.glEnd();
                    GL11.glBegin(2);
                    RenderHelper.color(color);
                    for (int i = 0; i < 361; ++i) {
                        final double iSin = Math.sin(Math.toRadians(i)) * size;
                        final double iCos = Math.cos(Math.toRadians(i)) * size;
                        GL11.glVertex3d(x + iCos, y + endYValue, z - iSin);
                    }
                    GL11.glEnd();
                    bus.e();
                    GL11.glShadeModel(7424);
                    GL11.glDisable(2848);
                    GL11.glEnable(2884);
                    bus.y();
                    if (this.depthTest.getCurrentValue()) {
                        bus.k();
                    }
                    bus.l();
                    bus.I();
                }
            }
            else {
                this.circleAnim = 0.0;
            }
        }
    }
}
