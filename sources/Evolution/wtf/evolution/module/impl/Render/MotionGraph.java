package wtf.evolution.module.impl.Render;

import org.lwjgl.opengl.GL11;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;


import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

@ModuleInfo(name = "MotionGraph", type = Category.Render)
public class MotionGraph extends Module {
    public ArrayList<Double> height = new ArrayList<>();
    public double maxHeight;
    public double chlen, chlen2, avg;

    @EventTarget
    public void onUpdate(EventMotion e) {
        chlen = MathHelper.interpolate((Math.hypot(mc.player.motionX, mc.player.motionZ) * 75) * mc.timer.timerSpeed, chlen, 0.3f);

        height.add(chlen);
        if (height.size() >= 120) {
            height.remove(0);
        }

        avg = height.stream().mapToDouble(Double::doubleValue).sum() / height.size();
        maxHeight = Collections.max(height);
        chlen2 = MathHelper.interpolate(maxHeight, chlen2, 0.1);
    }

    @EventTarget
    public void onRender(EventDisplay e) {

        RenderUtil.bloom(() -> {
            RenderUtil.drawRectWH(e.sr.getScaledWidth() / 2f - 120/ 2f, (float) (e.sr.getScaledHeight() - 50 - maxHeight), (float) height.size(), (float) maxHeight, new Color(21, 21, 21, 100).getRGB());
        }, 5, 2, Color.BLACK.getRGB());
        RenderUtil.blur(() -> {
            RenderUtil.drawRectWH(e.sr.getScaledWidth() / 2f - 120/ 2f, (float) (e.sr.getScaledHeight() - 50 - maxHeight), (float) height.size(), (float) maxHeight, new Color(21, 21, 21, 100).getRGB());
        }, 10);
        RenderUtil.drawRectWH(e.sr.getScaledWidth() / 2f - 120/ 2f, (float) (e.sr.getScaledHeight() - 50 - avg), (float) height.size(), (float) 0.5, new Color(255, 255, 255, 255).getRGB());
        RenderUtil.drawRectWH(e.sr.getScaledWidth() / 2f - 120/ 2f, (float) (e.sr.getScaledHeight() - 50 - maxHeight), (float) height.size(), (float) maxHeight, new Color(21, 21, 21, 100).getRGB());
        GL11.glPushMatrix();
        RenderUtil.start();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        {

            for (int i = 0; i < height.size(); i++) {
                GL11.glColor4f(1, 1, 1, 1);
                GL11.glVertex2d(e.sr.getScaledWidth() / 2f - 120 / 2f + 0.5f + i, e.sr.getScaledHeight() - 50 - height.get(i));
            }
        }
        GL11.glEnd();

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        RenderUtil.stop();
        GL11.glPopMatrix();
    }
}
