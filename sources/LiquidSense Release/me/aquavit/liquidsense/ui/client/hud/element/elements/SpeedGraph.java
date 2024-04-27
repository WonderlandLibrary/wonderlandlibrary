package me.aquavit.liquidsense.ui.client.hud.element.elements;

import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.client.hud.element.Side;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.IntegerValue;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ElementInfo(name = "SpeedGraph")
public class SpeedGraph extends Element {
    public SpeedGraph(){
        super(75,110,1F,new Side(Side.Horizontal.MIDDLE, Side.Vertical.DOWN));
    }

    private FloatValue yMultiplier = new FloatValue("yMultiplier", 7F, 1F, 20F);
    private IntegerValue height = new IntegerValue("Height", 50, 30, 150);
    private IntegerValue width = new IntegerValue("Width", 150, 100, 300);
    private FloatValue thickness = new FloatValue("Thickness", 2F, 1F, 3F);
    private IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private IntegerValue colorGreenValue = new IntegerValue("G", 111, 0, 255);
    private IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);

    private List<Double> speedList = new ArrayList<Double>();
    private int lastTick = -1;

    @Override
    public Border drawElement() {
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

        return new Border(0F, 0F, (float)width, (float)height.get() + 2);
    }
}
