package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.util.render.RenderUtil;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.base.font.CustomFont;
import de.tired.base.font.FontManager;
import de.tired.util.render.ColorUtil;
import de.tired.base.dragging.DragHandler;
import de.tired.base.dragging.Draggable;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.Render2DEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.base.module.ModuleManager;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ModuleAnnotation(name = "SpeedGraph", category = ModuleCategory.RENDER)
public class SpeedGraph extends Module {

    double xAxis = 500;
    double yAxis = 200;
    public static final List<Double> speedData = new ArrayList<>();

    public Draggable drag = DragHandler.setupDrag(this, "SpeedGraph", (float) yAxis, (float) yAxis, false);

    public ModeSetting design = new ModeSetting("Design", this, new String[]{"Tired", "Prestige"});

    public ModeSetting calculationMode = new ModeSetting("CalculationMode", this, new String[]{"BPS", "DeltaXZ"});

    @EventTarget
    public void onRender(Render2DEvent event) {

        SpeedGraph.getInstance().drawRectangleFromSpeedGraph();

        SpeedGraph.getInstance().drawSpeedGraph();

    }


    final double width = 100;
    final double height = 40;
    double add = 0;


    public void drawSpeedGraph() {
        final double width = design.getValue().equalsIgnoreCase("Tired") ? 100 : 150;
        final double interpret = width / speedData.size();

        drag.setObjectWidth((float) width);


        xAxis = drag.getXPosition();
        yAxis = drag.getYPosition() + 23;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(4.5F);
        GL11.glBegin(GL11.GL_LINE_STRIP);

        if (!design.getValue().equalsIgnoreCase("Prestige")) {
            for (int i = 0; i < speedData.size(); ++i) {
                final Color color = Color.BLACK;
                RenderUtil.instance.color(color);
                double speedList = SpeedGraph.speedData.get(i);

                if (speedList >= height)
                    speedList = height;

                GL11.glVertex2d(xAxis + ((float) i * interpret), yAxis - speedList);
            }
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);


        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(design.getValue().equalsIgnoreCase("Tired") ? 2.5F : 1.5F);
        GL11.glBegin(GL11.GL_LINE_STRIP);

        for (int i = 0; i < speedData.size(); ++i) {
            final Color color = ColorUtil.interpolateColorsBackAndForth(6, i * 12, new Color(84, 51, 158).brighter(), new Color(104, 127, 203), true);
            RenderUtil.instance.color(design.getValue().equalsIgnoreCase("Tired") ? color : Color.WHITE);
            double speedList = SpeedGraph.speedData.get(i);

            if (speedList >= height)
                speedList = height;

            GL11.glVertex2d(xAxis + ((float) i * interpret), yAxis - speedList);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);


        double speedList = Collections.max(SpeedGraph.speedData);
        if (speedList > height)
            speedList = height;


        String speed = "" + Math.round((Math.hypot(MC.thePlayer.motionX, MC.thePlayer.motionZ) * 80) * 10.0) / 10.0;

        FontManager.futuraSmall.drawString(speed, calculateMiddle(speed, FontManager.futuraSmall, (float) xAxis + (float) width - 20, 20), ((int) yAxis - (int) speedList) - 15f, -1);

    }

    public int calculateMiddle(String text, CustomFont fontRenderer, double x, double width) {
        return (int) ((float) (x + width) - (fontRenderer.getStringWidth(text) / 2f) - (float) width / 2);
    }

    public void drawRectangleFromSpeedGraph() {
        double speed = Math.round((Math.hypot(MC.thePlayer.motionX, MC.thePlayer.motionZ) * 80) * 10.0) / 10.0;
        if (SpeedGraph.speedData.size() < 10 && speed > 0) return;

        final double height = 40;

        final double width = design.getValue().equalsIgnoreCase("Tired") ? 100 : 150;


        double add = 0;

        final double xAxis = drag.getXPosition();
        yAxis = drag.getYPosition() + 23;

        for (int i = 0; i < speedData.size(); ++i) {
            double speedList = SpeedGraph.speedData.get(i);
            if (speedList >= height)
                speedList = height;
            add = i;
        }

        final double interpret = width / speedData.size();

        double speedList = Collections.max(SpeedGraph.speedData);
        if (speedList > height)
            speedList = height;


        drag.setObjectHeight((float) Math.min(50, speedList));

        if (drag.getObjectHeight() < 10)
            drag.setObjectHeight(50);

        int x = (calculateMiddle("6", FontManager.iconFont, (float) xAxis + (float) width - 20, 20));

        if (!design.getValue().equalsIgnoreCase("Prestige")) {
            RenderUtil.instance.doRenderShadow(Color.BLACK, x, ((int) yAxis - (int) speedList - 7.8f), 10, 20, 22);
        }
        if (!design.getValue().equalsIgnoreCase("Prestige")) {
            FontManager.iconFont.drawString("6", calculateMiddle("6", FontManager.iconFont, (float) xAxis + (float) width - 20, 20), ((int) yAxis - (int) speedList) - 7.8f, new Color(37, 37, 37, 255).getRGB());
        }
        if (!design.getValue().equalsIgnoreCase("Prestige")) {
            RenderUtil.instance.doRenderShadow(Color.BLACK, (float) xAxis + (float) width - 20, (int) ((int) yAxis - (int) speedList) - 20, 20, 9.7f, 22);
            RenderUtil.instance.doRenderShadow(Color.BLACK, (float) xAxis, (float) yAxis - (float) speedList, (float) add * (float) interpret, (float) speedList, 22);
        }
        ShaderManager.shaderBy(RoundedRectShader.class).drawRound((float) xAxis + (float) width - 20, (int) ((int) yAxis - (int) speedList) - 20, 20, 9.7f, 3, new Color(26, 26, 26, 255));

        ShaderManager.shaderBy(RoundedRectShader.class).drawRound((float) xAxis, (float) yAxis - (float) speedList, (float) add * (float) interpret, (float) speedList, design.getValue().equalsIgnoreCase("Tired") ? 2 : 0, design.getValue().equalsIgnoreCase("Tired") ?  new Color(26, 26, 26, 255) : new Color(20, 20, 20, 170));
    }

    @Override
    public void onState() {

    }

    public static SpeedGraph getInstance() {
        return ModuleManager.getInstance(SpeedGraph.class);
    }

    @Override
    public void onUndo() {

    }
}
