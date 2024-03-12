// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.GLUtil;
import org.lwjgl.opengl.GL11;
import java.util.stream.Collectors;
import dev.tenacity.event.impl.player.MotionEvent;
import java.util.Iterator;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.event.impl.render.Render2DEvent;
import java.awt.Color;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.ParentAttribute;
import java.util.ArrayList;
import dev.tenacity.Tenacity;
import java.util.LinkedHashMap;
import dev.tenacity.module.Category;
import java.util.List;
import dev.tenacity.utils.render.ShaderUtil;
import dev.tenacity.utils.objects.GradientColorWheel;
import dev.tenacity.utils.objects.Dragging;
import dev.tenacity.module.settings.impl.BooleanSetting;
import java.util.Map;
import dev.tenacity.module.Module;

public class Statistics extends Module
{
    public static int gamesPlayed;
    public static int killCount;
    public static int deathCount;
    public static long startTime;
    public static long endTime;
    public static final String[] KILL_TRIGGERS;
    private final Map<String, Double> statistics;
    private final BooleanSetting motionGraph;
    private final BooleanSetting seprateMotionGraph;
    private final Dragging dragging;
    private final Dragging motionDragging;
    private final GradientColorWheel colorWheel;
    private float width;
    private float height;
    private final ShaderUtil circleShader;
    private final List<Float> speeds;
    
    public Statistics() {
        super("Statistics", "Statistics", Category.RENDER, "Displays statistics about your session");
        this.statistics = new LinkedHashMap<String, Double>();
        this.motionGraph = new BooleanSetting("Show Speed Graph", true);
        this.seprateMotionGraph = new BooleanSetting("Separate Graph", true);
        this.dragging = Tenacity.INSTANCE.createDrag(this, "sessionstats", 5.0f, 150.0f);
        this.motionDragging = Tenacity.INSTANCE.createDrag(this, "motionGraph", 5.0f, 200.0f);
        this.colorWheel = new GradientColorWheel();
        this.circleShader = new ShaderUtil("Tenacity/Shaders/circle-arc.frag");
        this.speeds = new ArrayList<Float>();
        this.seprateMotionGraph.addParent(this.motionGraph, ParentAttribute.BOOLEAN_CONDITION);
        this.addSettings(this.colorWheel.createModeSetting("Color Mode", new String[0]), this.colorWheel.getColorSetting(), this.motionGraph, this.seprateMotionGraph);
    }
    
    @Override
    public void onShaderEvent(final ShaderEvent e) {
        final float x = this.dragging.getX();
        final float y = this.dragging.getY();
        final boolean seperated = this.motionGraph.isEnabled() && this.seprateMotionGraph.isEnabled();
        if (e.getBloomOptions().getSetting("Statistics").isEnabled()) {
            RoundedUtil.drawGradientRound(x, y, this.width, this.height, 6.0f, this.colorWheel.getColor1(), this.colorWheel.getColor4(), this.colorWheel.getColor2(), this.colorWheel.getColor3());
            if (seperated) {
                RoundedUtil.drawGradientRound(this.motionDragging.getX(), this.motionDragging.getY(), this.motionDragging.getWidth(), this.motionDragging.getHeight(), 6.0f, this.colorWheel.getColor1(), this.colorWheel.getColor4(), this.colorWheel.getColor2(), this.colorWheel.getColor3());
            }
        }
        else {
            RoundedUtil.drawRound(x, y, this.width, this.height, 6.0f, Color.BLACK);
            if (seperated) {
                RoundedUtil.drawRound(this.motionDragging.getX(), this.motionDragging.getY(), this.motionDragging.getWidth(), this.motionDragging.getHeight(), 6.0f, Color.BLACK);
            }
        }
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent e) {
        final float x = this.dragging.getX();
        final float y = this.dragging.getY();
        final boolean moreHeight = this.motionGraph.isEnabled() && !this.seprateMotionGraph.isEnabled();
        final boolean seperated = this.motionGraph.isEnabled() && this.seprateMotionGraph.isEnabled();
        this.motionDragging.setWidth(seperated ? this.width : 0.0f);
        this.motionDragging.setHeight(seperated ? 75.0f : 0.0f);
        this.width = 145.0f;
        final float orginalHeight = (float)(this.statistics.size() * (Statistics.tenacityBoldFont18.getHeight() + 6) + 26);
        this.height = orginalHeight + (moreHeight ? 75 : 0);
        this.dragging.setHeight(this.height);
        this.dragging.setWidth(this.width);
        this.colorWheel.setColorsForMode("Dark", ColorUtil.brighter(new Color(30, 30, 30), 0.65f));
        this.colorWheel.setColors();
        final float alpha = this.colorWheel.getColorMode().is("Dark") ? 1.0f : 0.85f;
        RoundedUtil.drawGradientRound(x, y, this.width, this.height, 6.0f, ColorUtil.applyOpacity(this.colorWheel.getColor1(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor4(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor2(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor3(), alpha));
        Statistics.tenacityBoldFont22.drawString("Statistics", x + 5.0f, y + 2.0f, -1);
        final float underlineWidth = Statistics.tenacityBoldFont22.getStringWidth("Statistics");
        RoundedUtil.drawRound(x + 5.0f, y + 2.0f + Statistics.tenacityBoldFont22.getHeight() + 1.0f, underlineWidth - 0.5f, 1.0f, 0.5f, Color.white);
        this.statistics.put("Games Played", (double)Statistics.gamesPlayed);
        this.statistics.put("K/D", (Statistics.deathCount == 0) ? Statistics.killCount : MathUtils.round(Statistics.killCount / (double)Statistics.deathCount, 2));
        this.statistics.put("Kills", (double)Statistics.killCount);
        int count = 0;
        for (final Map.Entry<String, Double> entry : this.statistics.entrySet()) {
            final String key = entry.getKey();
            final Double value = entry.getValue();
            final int offset = count * (Statistics.tenacityBoldFont18.getHeight() + 7);
            Statistics.tenacityBoldFont18.drawString(key + ": ", x + 5.0f, y + offset + 21.0f, -1);
            Statistics.tenacityFont18.drawString(key.equals("K/D") ? String.valueOf((double)value) : String.valueOf(value.intValue()), x + 5.0f + Statistics.tenacityBoldFont18.getStringWidth(key + ": "), y + offset + 21.0f, -1);
            ++count;
        }
        final float radius = 40.0f;
        final float playtimeX = x + this.width - (Statistics.tenacityBoldFont20.getStringWidth("Play Time") + 6.0f);
        Statistics.tenacityBoldFont20.drawString("Play Time", x + this.width - (Statistics.tenacityBoldFont20.getStringWidth("Play Time") + 5.0f), y + 4.0f, -1);
        final float playUnderlineWidth = Statistics.tenacityBoldFont20.getStringWidth("Play Time");
        RoundedUtil.drawRound(x + this.width - (Statistics.tenacityBoldFont20.getStringWidth("Play Time") + 5.0f), y + 4.0f + Statistics.tenacityBoldFont22.getHeight(), playUnderlineWidth - 0.5f, 1.0f, 0.5f, Color.white);
        final int[] playTime = getPlayTime();
        final float circleY = y + 4.0f + Statistics.tenacityBoldFont22.getHeight() + 2.0f;
        this.drawCircle(playtimeX - 1.5f, circleY, radius, -2.0f, 1, ColorUtil.applyOpacity(Color.BLACK, 0.5f), 1.0f);
        final int[] playTimeActual = getPlayTime();
        final boolean change = playTime[0] % 2 == 0;
        final float percentage = (playTime[1] + playTime[2] / 60.0f) / 60.0f;
        this.drawCircle(playtimeX - 1.5f, circleY, radius, 1.0f - percentage, change ? 1 : -1, Color.WHITE, 0.05f);
        this.drawAnimatedPlaytime(playtimeX, circleY + ((radius + 10.0f) / 2.0f - Statistics.tenacityFont16.getHeight() / 2.0f), radius + 10.0f, playTimeActual);
        if (this.motionGraph.isEnabled()) {
            if (seperated) {
                RoundedUtil.drawGradientRound(this.motionDragging.getX(), this.motionDragging.getY(), this.motionDragging.getWidth(), this.motionDragging.getHeight(), 6.0f, ColorUtil.applyOpacity(this.colorWheel.getColor1(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor4(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor2(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor3(), alpha));
                this.drawMotionGraph(this.motionDragging.getX(), this.motionDragging.getY(), this.motionDragging.getWidth(), this.motionDragging.getHeight());
            }
            else {
                this.drawMotionGraph(x, y + this.height - 75.0f, this.width, 75.0f);
            }
        }
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (event.isPre()) {
            if (this.speeds.size() - 1 >= 100) {
                this.speeds.remove(0);
            }
            this.speeds.add(this.getPlayerSpeed());
        }
    }
    
    private void drawMotionGraph(final float x, final float y, final float width, final float height) {
        final float textX = x + 5.0f;
        Statistics.tenacityBoldFont20.drawString("Speed", textX, y + 3.0f, -1);
        final float underlineWidth = Statistics.tenacityBoldFont20.getStringWidth("Speed");
        double average = this.speeds.stream().collect(Collectors.averagingDouble(value -> value * 50.0));
        average = Math.round(average * 100.0) / 100.0;
        final String text = "Average: " + average + " BPS";
        Statistics.tenacityFont18.drawString(text, x + width - (Statistics.tenacityFont18.getStringWidth(text) + 5.0f), y + 3.5f, -1);
        final float lineHeight = height - (Statistics.tenacityBoldFont20.getHeight() + 16);
        final float lineWidth = width - 10.0f;
        final float lineX = x + 5.0f;
        final float lineY = y + height - 5.0f;
        final float distance = (float)(8 + Statistics.tenacityBoldFont20.getHeight());
        RoundedUtil.drawRound(lineX - 3.0f, y + distance, lineWidth + 6.0f, height - (distance + 2.0f), 5.0f, ColorUtil.applyOpacity(Color.BLACK, 0.25f));
        GL11.glPushMatrix();
        GLUtil.setup2DRendering();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(1.5f);
        GL11.glBegin(1);
        int count = 0;
        if (this.speeds.size() > 3) {
            for (final float speed : this.speeds) {
                if (count >= this.speeds.size() - 1) {
                    continue;
                }
                RenderUtil.color(-1);
                final float speedY = speed * lineHeight;
                final float nextSpeedY = this.speeds.get(count + 1) * lineHeight;
                final float length = lineWidth / (this.speeds.size() - 1);
                GL11.glVertex2f(lineX + count * length, lineY - Math.min(speedY, lineHeight));
                GL11.glVertex2f(lineX + (count + 1) * length, lineY - Math.min(nextSpeedY, lineHeight));
                ++count;
            }
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GLUtil.end2DRendering();
        GL11.glPopMatrix();
    }
    
    private void drawAnimatedPlaytime(final float circleX, final float y, final float circleWidth, final int[] playTime) {
        final String seconds = ((playTime[2] < 10) ? "0" : "") + playTime[2];
        final String minutes = ((playTime[1] < 10) ? "0" : "") + playTime[1];
        final StringBuilder sb = new StringBuilder(seconds);
        if (playTime[1] > 0 || playTime[0] > 0) {
            sb.insert(0, minutes + ":");
        }
        if (playTime[0] > 0) {
            sb.insert(0, playTime[0] + ":");
        }
        Statistics.tenacityFont16.drawCenteredString(sb.toString(), circleX - 1.5f + circleWidth / 2.0f, y, -1);
    }
    
    private void drawCircle(final float x, final float y, final float radius, final float progress, final int change, final Color color, final float smoothness) {
        GLUtil.startBlend();
        final float borderThickness = 1.0f;
        this.circleShader.init();
        this.circleShader.setUniformf("radialSmoothness", smoothness);
        this.circleShader.setUniformf("radius", radius);
        this.circleShader.setUniformf("borderThickness", borderThickness);
        this.circleShader.setUniformf("progress", progress);
        this.circleShader.setUniformi("change", change);
        this.circleShader.setUniformf("color", color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        final float wh = radius + 10.0f;
        final ScaledResolution sr = new ScaledResolution(Statistics.mc);
        this.circleShader.setUniformf("pos", (x + (wh / 2.0f - (radius + borderThickness) / 2.0f)) * sr.getScaleFactor(), Minecraft.getMinecraft().displayHeight - (radius + borderThickness) * sr.getScaleFactor() - (y + (wh / 2.0f - (radius + borderThickness) / 2.0f)) * sr.getScaleFactor());
        ShaderUtil.drawQuads(x, y, wh, wh);
        this.circleShader.unload();
        GLUtil.endBlend();
    }
    
    public static int[] getPlayTime() {
        final long diff = getTimeDiff();
        long diffSeconds = 0L;
        long diffMinutes = 0L;
        long diffHours = 0L;
        if (diff > 0L) {
            diffSeconds = diff / 1000L % 60L;
            diffMinutes = diff / 60000L % 60L;
            diffHours = diff / 3600000L % 24L;
        }
        return new int[] { (int)diffHours, (int)diffMinutes, (int)diffSeconds };
    }
    
    public static long getTimeDiff() {
        return ((Statistics.endTime == -1L) ? System.currentTimeMillis() : Statistics.endTime) - Statistics.startTime;
    }
    
    public static void reset() {
        Statistics.startTime = System.currentTimeMillis();
        Statistics.endTime = -1L;
        Statistics.gamesPlayed = 0;
        Statistics.killCount = 0;
    }
    
    private float getPlayerSpeed() {
        final double bps = Math.hypot(Statistics.mc.thePlayer.posX - Statistics.mc.thePlayer.prevPosX, Statistics.mc.thePlayer.posZ - Statistics.mc.thePlayer.prevPosZ) * Statistics.mc.timer.timerSpeed * 20.0;
        return (float)bps / 50.0f;
    }
    
    @Override
    public void onEnable() {
        this.speeds.clear();
        super.onEnable();
    }
    
    static {
        Statistics.startTime = System.currentTimeMillis();
        Statistics.endTime = -1L;
        KILL_TRIGGERS = new String[] { "by *", "para *", "fue destrozado a manos de *" };
    }
}
