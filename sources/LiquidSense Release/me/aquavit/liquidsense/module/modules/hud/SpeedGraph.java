package me.aquavit.liquidsense.module.modules.hud;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "SpeedGraph", description = "Drawing 2D SpeedGraph", category = ModuleCategory.HUD, array = false)
public class SpeedGraph extends Module {

    private final ListValue horizontal = new ListValue("Horizontal", new String[] {"Left", "Middle", "Right"}, "Middle") {
        @Override
        protected void onChanged(String oldValue, String newValue) {
            switch (newValue.toLowerCase()) {
                case "left":
                    setRenderx(x);
                    break;
                case "middle":
                    setRenderx((float) new ScaledResolution(mc).getScaledWidth() / 2 - x);
                    break;
                case "right":
                    setRenderx(new ScaledResolution(mc).getScaledWidth() - x);
                    break;
            }
        }
    };
    private final ListValue vertical = new ListValue("Vertical", new String[] {"Up", "Middle", "Down"}, "Down") {
        @Override
        protected void onChanged(String oldValue, String newValue) {
            switch (newValue.toLowerCase()) {
                case "up":
                    setRendery(y);
                    break;
                case "middle":
                    setRendery((float) new ScaledResolution(mc).getScaledHeight() / 2 - y);
                    break;
                case "down":
                    setRendery(new ScaledResolution(mc).getScaledHeight() - y);
                    break;
            }
        }
    };
    int x,y;

    private FloatValue yMultiplier = new FloatValue("yMultiplier", 7F, 1F, 20F);
    private IntegerValue height = new IntegerValue("Height", 50, 30, 150);
    private IntegerValue width = new IntegerValue("Width", 150, 100, 300);
    private FloatValue thickness = new FloatValue("Thickness", 2F, 1F, 3F);
    private IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private IntegerValue colorGreenValue = new IntegerValue("G", 111, 0, 255);
    private IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);

    private List<Double> speedList = new ArrayList<Double>();
    private int lastTick = -1;

    public SpeedGraph(double x, double y){
        this.setDefaultx(x);
        this.setDefaulty(y);
    }

    public SpeedGraph(){
        this(76,115);
    }

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        isLeft = horizontal.get().equalsIgnoreCase("Left");
        isUp = vertical.get().equalsIgnoreCase("Up");
        x = (int) getHUDX(horizontal.get().equalsIgnoreCase("Left"),
                horizontal.get().equalsIgnoreCase("Middle"),
                horizontal.get().equalsIgnoreCase("Right"));
        y = (int) getHUDY(vertical.get().equalsIgnoreCase("Up"),
                vertical.get().equalsIgnoreCase("Middle"),
                vertical.get().equalsIgnoreCase("Down"));

        int width = this.width.get();
        if (mc.thePlayer != null && lastTick != mc.thePlayer.ticksExisted) {
            lastTick = mc.thePlayer.ticksExisted;
            double z2 = mc.thePlayer.posZ;
            double z1 = mc.thePlayer.prevPosZ;
            double x2 = mc.thePlayer.posX;
            double x1 = mc.thePlayer.prevPosX;
            double speed = Math.sqrt((z2 - z1) * (z2 - z1) + (x2 - x1) * (x2 - x1));
            if (speed < 0)
                speed = -speed;

            speedList.add(speed);
            while (speedList.size() > width) {
                speedList.remove(0);
            }
        }

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0.0);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(thickness.get());
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        GL11.glBegin(GL11.GL_LINES);
        int size = this.speedList.size();
        int start = size > width ? size - width : 0;

        for (int i = start; i < size - 1; ++i) {
            double y = speedList.get(i) * 10 * (double)yMultiplier.get();
            double y1 = speedList.get(i + 1) * 10 * (double)yMultiplier.get();

            RenderUtils.glColor(new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get(), 255));
            GL11.glVertex2d(i - start, height.get() + 1 - Math.min(y,((double)height.get())));
            GL11.glVertex2d(i + 1.0 - start, height.get() + 1 - Math.min(y1,((double)height.get())));
        }

        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.resetColor();
        GL11.glPopMatrix();

        drawBorder((float) x, (float) y,
                (float)width, (float)height.get() + 2);
    }
}
