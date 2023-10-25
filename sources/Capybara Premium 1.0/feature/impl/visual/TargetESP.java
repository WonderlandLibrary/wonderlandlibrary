package fun.expensive.client.feature.impl.visual;

import fun.rich.client.Rich;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.render.EventRender3D;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.feature.impl.combat.KillAura;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ColorSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.render.ClientHelper;
import fun.rich.client.utils.render.ColorUtils;
import fun.rich.client.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

public class TargetESP extends Feature {
    double height;
    boolean animat;
    private double circleAnim;
    private double circleValue;
    private boolean canDown;
    public ListSetting bebraPonyxana;
    public NumberSetting circlesize;
    public NumberSetting circleSpeed;

    public NumberSetting points;
    public BooleanSetting depthTest;
    public ColorSetting targetEspColor;
    public ListSetting jelloMode = new ListSetting("Jello Color", "Client", () -> bebraPonyxana.currentMode.equals("Jello"), "Astolfo", "Rainbow", "Client", "Custom");

    public TargetESP() {
        super("TargetESP", "Рисует красивый круг на энтити", FeatureCategory.Visuals);
        bebraPonyxana = new ListSetting("TargetESP Mode", "Jello", () -> true, "Jello", "Astolfo");
        circlesize = new NumberSetting("Circle Size", "Размер круга", 0.1F, 0.1F, 0.5F, 0.1F, () -> bebraPonyxana.currentMode.equalsIgnoreCase("Jello") || bebraPonyxana.currentMode.equalsIgnoreCase("Astolfo"));
        points = new NumberSetting("Points", 30F, 3F, 30F, 1F, () -> bebraPonyxana.currentMode.equalsIgnoreCase("Astolfo"));
        depthTest = new BooleanSetting("DepthTest", "Глубина(test)", false, () -> bebraPonyxana.currentMode.equalsIgnoreCase("Jello"));
        targetEspColor = new ColorSetting("TargetESP Color", Color.PINK.getRGB(), () -> true);
        circleSpeed = new NumberSetting("Circle Speed", "Регулирует скорость Jello круга", 0.01f, 0.001f, 0.05f, 0.001f, () -> bebraPonyxana.currentMode.equals("Jello"));
        addSettings(bebraPonyxana, jelloMode, circleSpeed, circlesize, points, targetEspColor, depthTest);
    }

    @EventTarget
    public void onRender(EventRender3D event3D) {
        double z;
        double y;
        int color;
        int oneColor;
        String mode = bebraPonyxana.getOptions();

        this.setSuffix(mode);
        if (bebraPonyxana.currentMode.equals("Jello") && !KillAura.target.isDead) {
            if (KillAura.mc.player.getDistanceToEntity(KillAura.target) <= KillAura.range.getNumberValue()) {
                if (KillAura.target != null && Rich.instance.featureManager.getFeature(KillAura.class).isEnabled() && KillAura.mc.player.getDistanceToEntity(KillAura.target) <= KillAura.range.getNumberValue()) {
                    double iCos;
                    double iSin;
                    int i;
                    oneColor = targetEspColor.getColorValue();
                    color = 0;
                    switch (this.jelloMode.currentMode) {
                        case "Client": {
                            color = ClientHelper.getClientColor().getRGB();
                            break;
                        }
                        case "Custom": {
                            color = oneColor;
                            break;
                        }
                        case "Astolfo": {
                            color = ColorUtils.astolfo(false, 1).getRGB();
                            break;
                        }
                        case "Rainbow": {
                            color = ColorUtils.rainbow(300, 1.0f, 1.0f).getRGB();
                        }
                    }
                    double x = KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * (double) KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosX;
                    y = KillAura.target.lastTickPosY + (KillAura.target.posY - KillAura.target.lastTickPosY) * (double) KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosY;
                    z = KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * (double) KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosZ;
                    this.circleValue += (double) circleSpeed.getNumberValue() * (Minecraft.frameTime * 0.1);
                    float targetHeight = (float) (0.5 * (1.0 + Math.sin(Math.PI * 2 * (this.circleValue * (double) 0.3f))));
                    float size = KillAura.target.width + circlesize.getNumberValue();
                    float endYValue = (float) ((float) (((Rich.instance.featureManager.getFeature(CustomModel.class).isEnabled() && CustomModel.modelMode.currentMode.equals("Amogus") && KillAura.target instanceof EntityPlayer && !CustomModel.onlyMe.getBoolValue() ? 1.3 : (double) KillAura.target.height)) * 1.0 + 0.2) * (double) targetHeight);
                    if ((double) targetHeight > 0.99) {
                        this.canDown = true;
                    } else if ((double) targetHeight < 0.01) {
                        this.canDown = false;
                    }
                    GlStateManager.enableBlend();
                    GL11.glBlendFunc(770, 771);
                    GL11.glEnable(2848);
                    if (depthTest.getBoolValue()) {
                        GlStateManager.disableDepth();
                    }
                    GlStateManager.disableTexture2D();
                    GlStateManager.disableAlpha();
                    GL11.glLineWidth(2.0f);
                    GL11.glShadeModel(7425);
                    GL11.glDisable(2884);
                    GL11.glBegin(5);
                    float alpha = (this.canDown ? 255.0f * targetHeight : 255.0f * (1.0f - targetHeight)) / 255.0f;
                    float red = (float) (color >> 16 & 0xFF) / 255.0f;
                    float green = (float) (color >> 8 & 0xFF) / 255.0f;
                    float blue = (float) (color & 0xFF) / 255.0f;
                    for (i = 0; i < 2166; ++i) {
                        RenderUtils.color(red, green, blue, alpha);
                        iSin = Math.sin(Math.toRadians(i)) * (double) size;
                        iCos = Math.cos(Math.toRadians(i)) * (double) size;
                        GL11.glVertex3d(x + iCos, y + (double) endYValue, z - iSin);
                        RenderUtils.color(red, green, blue, 0.0);
                        GL11.glVertex3d(x + iCos, y + (double) endYValue + (double) (this.canDown ? -0.5f * (1.0f - targetHeight) : 0.5f * targetHeight), z - iSin);
                    }
                    GL11.glEnd();
                    GL11.glBegin(2);
                    RenderUtils.color(color);
                    for (i = 0; i < 361; ++i) {
                        iSin = Math.sin(Math.toRadians(i)) * (double) size;
                        iCos = Math.cos(Math.toRadians(i)) * (double) size;
                        GL11.glVertex3d(x + iCos, y + (double) endYValue, z - iSin);
                    }
                    GL11.glEnd();
                    GlStateManager.enableAlpha();
                    GL11.glShadeModel(7424);
                    GL11.glDisable(2848);
                    GL11.glEnable(2884);
                    GlStateManager.enableTexture2D();
                    if (depthTest.getBoolValue()) {
                        GlStateManager.enableDepth();
                    }
                    GlStateManager.disableBlend();
                    GlStateManager.resetColor();
                }
            } else {
                this.circleAnim = 0.0;
            }
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            if (KillAura.target != null) {
                if (KillAura.target.getHealth() > 0) {
                    circleAnim += 0.015F * Minecraft.frameTime / 10;
                    RenderUtils.drawCircle3D(KillAura.target, circleAnim + 0.001, event3D.getPartialTicks(), (int) points.getNumberValue(), 4, Color.black.getRGB());
                    RenderUtils.drawCircle3D(KillAura.target, circleAnim - 0.001, event3D.getPartialTicks(), (int) points.getNumberValue(), 4, Color.black.getRGB());
                    RenderUtils.drawCircle3D(KillAura.target, circleAnim, event3D.getPartialTicks(), (int) points.getNumberValue(), 2, targetEspColor.getColorValue());
                    circleAnim = MathHelper.clamp(circleAnim, 0, KillAura.target.width + circlesize.getNumberValue() * 0.5f);
                } else {
                    circleAnim = 0;
                }
            }
        }
    }
}

