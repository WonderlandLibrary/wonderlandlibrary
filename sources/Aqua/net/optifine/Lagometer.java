package net.optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.profiler.Profiler;
import net.minecraft.src.Config;
import net.optifine.Lagometer;
import net.optifine.util.MemoryMonitor;
import org.lwjgl.opengl.GL11;

/*
 * Exception performing whole class analysis ignored.
 */
public class Lagometer {
    private static Minecraft mc;
    private static GameSettings gameSettings;
    private static Profiler profiler;
    public static boolean active;
    public static TimerNano timerTick;
    public static TimerNano timerScheduledExecutables;
    public static TimerNano timerChunkUpload;
    public static TimerNano timerChunkUpdate;
    public static TimerNano timerVisibility;
    public static TimerNano timerTerrain;
    public static TimerNano timerServer;
    private static long[] timesFrame;
    private static long[] timesTick;
    private static long[] timesScheduledExecutables;
    private static long[] timesChunkUpload;
    private static long[] timesChunkUpdate;
    private static long[] timesVisibility;
    private static long[] timesTerrain;
    private static long[] timesServer;
    private static boolean[] gcs;
    private static int numRecordedFrameTimes;
    private static long prevFrameTimeNano;
    private static long renderTimeNano;

    public static void updateLagometer() {
        if (mc == null) {
            mc = Minecraft.getMinecraft();
            gameSettings = Lagometer.mc.gameSettings;
            profiler = Lagometer.mc.mcProfiler;
        }
        if (Lagometer.gameSettings.showDebugInfo && (Lagometer.gameSettings.ofLagometer || Lagometer.gameSettings.showLagometer)) {
            active = true;
            long timeNowNano = System.nanoTime();
            if (prevFrameTimeNano == -1L) {
                prevFrameTimeNano = timeNowNano;
            } else {
                int j = numRecordedFrameTimes & timesFrame.length - 1;
                ++numRecordedFrameTimes;
                boolean flag = MemoryMonitor.isGcEvent();
                Lagometer.timesFrame[j] = timeNowNano - prevFrameTimeNano - renderTimeNano;
                Lagometer.timesTick[j] = Lagometer.timerTick.timeNano;
                Lagometer.timesScheduledExecutables[j] = Lagometer.timerScheduledExecutables.timeNano;
                Lagometer.timesChunkUpload[j] = Lagometer.timerChunkUpload.timeNano;
                Lagometer.timesChunkUpdate[j] = Lagometer.timerChunkUpdate.timeNano;
                Lagometer.timesVisibility[j] = Lagometer.timerVisibility.timeNano;
                Lagometer.timesTerrain[j] = Lagometer.timerTerrain.timeNano;
                Lagometer.timesServer[j] = Lagometer.timerServer.timeNano;
                Lagometer.gcs[j] = flag;
                TimerNano.access$000((TimerNano)timerTick);
                TimerNano.access$000((TimerNano)timerScheduledExecutables);
                TimerNano.access$000((TimerNano)timerVisibility);
                TimerNano.access$000((TimerNano)timerChunkUpdate);
                TimerNano.access$000((TimerNano)timerChunkUpload);
                TimerNano.access$000((TimerNano)timerTerrain);
                TimerNano.access$000((TimerNano)timerServer);
                prevFrameTimeNano = System.nanoTime();
            }
        } else {
            active = false;
            prevFrameTimeNano = -1L;
        }
    }

    public static void showLagometer(ScaledResolution scaledResolution) {
        if (gameSettings != null && (Lagometer.gameSettings.ofLagometer || Lagometer.gameSettings.showLagometer)) {
            long i = System.nanoTime();
            GlStateManager.clear((int)256);
            GlStateManager.matrixMode((int)5889);
            GlStateManager.pushMatrix();
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho((double)0.0, (double)Lagometer.mc.displayWidth, (double)Lagometer.mc.displayHeight, (double)0.0, (double)1000.0, (double)3000.0);
            GlStateManager.matrixMode((int)5888);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate((float)0.0f, (float)0.0f, (float)-2000.0f);
            GL11.glLineWidth((float)1.0f);
            GlStateManager.disableTexture2D();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
            for (int j = 0; j < timesFrame.length; ++j) {
                int k = (j - numRecordedFrameTimes & timesFrame.length - 1) * 100 / timesFrame.length;
                k += 155;
                float f = Lagometer.mc.displayHeight;
                long l = 0L;
                if (gcs[j]) {
                    Lagometer.renderTime(j, timesFrame[j], k, k / 2, 0, f, worldrenderer);
                    continue;
                }
                Lagometer.renderTime(j, timesFrame[j], k, k, k, f, worldrenderer);
                f -= (float)Lagometer.renderTime(j, timesServer[j], k / 2, k / 2, k / 2, f, worldrenderer);
                f -= (float)Lagometer.renderTime(j, timesTerrain[j], 0, k, 0, f, worldrenderer);
                f -= (float)Lagometer.renderTime(j, timesVisibility[j], k, k, 0, f, worldrenderer);
                f -= (float)Lagometer.renderTime(j, timesChunkUpdate[j], k, 0, 0, f, worldrenderer);
                f -= (float)Lagometer.renderTime(j, timesChunkUpload[j], k, 0, k, f, worldrenderer);
                f -= (float)Lagometer.renderTime(j, timesScheduledExecutables[j], 0, 0, k, f, worldrenderer);
                float f2 = f - (float)Lagometer.renderTime(j, timesTick[j], 0, k, k, f, worldrenderer);
            }
            Lagometer.renderTimeDivider(0, timesFrame.length, 33333333L, 196, 196, 196, Lagometer.mc.displayHeight, worldrenderer);
            Lagometer.renderTimeDivider(0, timesFrame.length, 16666666L, 196, 196, 196, Lagometer.mc.displayHeight, worldrenderer);
            tessellator.draw();
            GlStateManager.enableTexture2D();
            int j2 = Lagometer.mc.displayHeight - 80;
            int k2 = Lagometer.mc.displayHeight - 160;
            Lagometer.mc.fontRendererObj.drawString("30", 2, k2 + 1, -8947849);
            Lagometer.mc.fontRendererObj.drawString("30", 1, k2, -3881788);
            Lagometer.mc.fontRendererObj.drawString("60", 2, j2 + 1, -8947849);
            Lagometer.mc.fontRendererObj.drawString("60", 1, j2, -3881788);
            GlStateManager.matrixMode((int)5889);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode((int)5888);
            GlStateManager.popMatrix();
            GlStateManager.enableTexture2D();
            float f1 = 1.0f - (float)((double)(System.currentTimeMillis() - MemoryMonitor.getStartTimeMs()) / 1000.0);
            f1 = Config.limit((float)f1, (float)0.0f, (float)1.0f);
            int l2 = (int)(170.0f + f1 * 85.0f);
            int i1 = (int)(100.0f + f1 * 55.0f);
            int j1 = (int)(10.0f + f1 * 10.0f);
            int k1 = l2 << 16 | i1 << 8 | j1;
            int l1 = 512 / scaledResolution.getScaleFactor() + 2;
            int i2 = Lagometer.mc.displayHeight / scaledResolution.getScaleFactor() - 8;
            GuiIngame guiingame = Lagometer.mc.ingameGUI;
            GuiIngame.drawRect((int)(l1 - 1), (int)(i2 - 1), (int)(l1 + 50), (int)(i2 + 10), (int)-1605349296);
            Lagometer.mc.fontRendererObj.drawString(" " + MemoryMonitor.getAllocationRateMb() + " MB/s", l1, i2, k1);
            renderTimeNano = System.nanoTime() - i;
        }
    }

    private static long renderTime(int frameNum, long time, int r, int g, int b, float baseHeight, WorldRenderer tessellator) {
        long i = time / 200000L;
        if (i < 3L) {
            return 0L;
        }
        tessellator.pos((double)((float)frameNum + 0.5f), (double)(baseHeight - (float)i + 0.5f), 0.0).color(r, g, b, 255).endVertex();
        tessellator.pos((double)((float)frameNum + 0.5f), (double)(baseHeight + 0.5f), 0.0).color(r, g, b, 255).endVertex();
        return i;
    }

    private static long renderTimeDivider(int frameStart, int frameEnd, long time, int r, int g, int b, float baseHeight, WorldRenderer tessellator) {
        long i = time / 200000L;
        if (i < 3L) {
            return 0L;
        }
        tessellator.pos((double)((float)frameStart + 0.5f), (double)(baseHeight - (float)i + 0.5f), 0.0).color(r, g, b, 255).endVertex();
        tessellator.pos((double)((float)frameEnd + 0.5f), (double)(baseHeight - (float)i + 0.5f), 0.0).color(r, g, b, 255).endVertex();
        return i;
    }

    public static boolean isActive() {
        return active;
    }

    static {
        active = false;
        timerTick = new TimerNano();
        timerScheduledExecutables = new TimerNano();
        timerChunkUpload = new TimerNano();
        timerChunkUpdate = new TimerNano();
        timerVisibility = new TimerNano();
        timerTerrain = new TimerNano();
        timerServer = new TimerNano();
        timesFrame = new long[512];
        timesTick = new long[512];
        timesScheduledExecutables = new long[512];
        timesChunkUpload = new long[512];
        timesChunkUpdate = new long[512];
        timesVisibility = new long[512];
        timesTerrain = new long[512];
        timesServer = new long[512];
        gcs = new boolean[512];
        numRecordedFrameTimes = 0;
        prevFrameTimeNano = -1L;
        renderTimeNano = 0L;
    }
}
