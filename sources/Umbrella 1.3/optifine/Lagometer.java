/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.profiler.Profiler;
import optifine.Config;
import org.lwjgl.opengl.GL11;

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
    private static long memTimeStartMs;
    private static long memStart;
    private static long memTimeLast;
    private static long memLast;
    private static long memTimeDiffMs;
    private static long memDiff;
    private static int memMbSec;

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
        memTimeStartMs = System.currentTimeMillis();
        memStart = Lagometer.getMemoryUsed();
        memTimeLast = memTimeStartMs;
        memLast = memStart;
        memTimeDiffMs = 1L;
        memDiff = 0L;
        memMbSec = 0;
    }

    public static boolean updateMemoryAllocation() {
        long timeNowMs = System.currentTimeMillis();
        long memNow = Lagometer.getMemoryUsed();
        boolean gc = false;
        if (memNow < memLast) {
            double memDiffMb = (double)memDiff / 1000000.0;
            double timeDiffSec = (double)memTimeDiffMs / 1000.0;
            int mbSec = (int)(memDiffMb / timeDiffSec);
            if (mbSec > 0) {
                memMbSec = mbSec;
            }
            memTimeStartMs = timeNowMs;
            memStart = memNow;
            memTimeDiffMs = 0L;
            memDiff = 0L;
            gc = true;
        } else {
            memTimeDiffMs = timeNowMs - memTimeStartMs;
            memDiff = memNow - memStart;
        }
        memTimeLast = timeNowMs;
        memLast = memNow;
        return gc;
    }

    private static long getMemoryUsed() {
        Runtime r = Runtime.getRuntime();
        return r.totalMemory() - r.freeMemory();
    }

    public static void updateLagometer() {
        if (mc == null) {
            mc = Minecraft.getMinecraft();
            gameSettings = Lagometer.mc.gameSettings;
            profiler = Lagometer.mc.mcProfiler;
        }
        if (Lagometer.gameSettings.showDebugInfo && Lagometer.gameSettings.ofLagometer) {
            active = true;
            long timeNowNano = System.nanoTime();
            if (prevFrameTimeNano == -1L) {
                prevFrameTimeNano = timeNowNano;
            } else {
                int frameIndex = numRecordedFrameTimes & timesFrame.length - 1;
                ++numRecordedFrameTimes;
                boolean gc = Lagometer.updateMemoryAllocation();
                Lagometer.timesFrame[frameIndex] = timeNowNano - prevFrameTimeNano - renderTimeNano;
                Lagometer.timesTick[frameIndex] = Lagometer.timerTick.timeNano;
                Lagometer.timesScheduledExecutables[frameIndex] = Lagometer.timerScheduledExecutables.timeNano;
                Lagometer.timesChunkUpload[frameIndex] = Lagometer.timerChunkUpload.timeNano;
                Lagometer.timesChunkUpdate[frameIndex] = Lagometer.timerChunkUpdate.timeNano;
                Lagometer.timesVisibility[frameIndex] = Lagometer.timerVisibility.timeNano;
                Lagometer.timesTerrain[frameIndex] = Lagometer.timerTerrain.timeNano;
                Lagometer.timesServer[frameIndex] = Lagometer.timerServer.timeNano;
                Lagometer.gcs[frameIndex] = gc;
                Lagometer.timerTick.reset();
                Lagometer.timerScheduledExecutables.reset();
                Lagometer.timerVisibility.reset();
                Lagometer.timerChunkUpdate.reset();
                Lagometer.timerChunkUpload.reset();
                Lagometer.timerTerrain.reset();
                Lagometer.timerServer.reset();
                prevFrameTimeNano = System.nanoTime();
            }
        } else {
            active = false;
            prevFrameTimeNano = -1L;
        }
    }

    public static void showLagometer(ScaledResolution scaledResolution) {
        if (gameSettings != null && Lagometer.gameSettings.ofLagometer) {
            float lumMem;
            int y30;
            int y60;
            long timeRenderStartNano = System.nanoTime();
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.pushMatrix();
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0, Lagometer.mc.displayWidth, Lagometer.mc.displayHeight, 0.0, 1000.0, 3000.0);
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -2000.0f);
            GL11.glLineWidth((float)1.0f);
            GlStateManager.func_179090_x();
            Tessellator tess = Tessellator.getInstance();
            WorldRenderer tessellator = tess.getWorldRenderer();
            tessellator.startDrawing(1);
            for (y60 = 0; y60 < timesFrame.length; ++y60) {
                y30 = (y60 - numRecordedFrameTimes & timesFrame.length - 1) * 100 / timesFrame.length;
                y30 += 155;
                lumMem = Lagometer.mc.displayHeight;
                long memColR = 0L;
                if (gcs[y60]) {
                    Lagometer.renderTime(y60, timesFrame[y60], y30, y30 / 2, 0, lumMem, tessellator);
                    continue;
                }
                Lagometer.renderTime(y60, timesFrame[y60], y30, y30, y30, lumMem, tessellator);
                lumMem -= (float)Lagometer.renderTime(y60, timesServer[y60], y30 / 2, y30 / 2, y30 / 2, lumMem, tessellator);
                lumMem -= (float)Lagometer.renderTime(y60, timesTerrain[y60], 0, y30, 0, lumMem, tessellator);
                lumMem -= (float)Lagometer.renderTime(y60, timesVisibility[y60], y30, y30, 0, lumMem, tessellator);
                lumMem -= (float)Lagometer.renderTime(y60, timesChunkUpdate[y60], y30, 0, 0, lumMem, tessellator);
                lumMem -= (float)Lagometer.renderTime(y60, timesChunkUpload[y60], y30, 0, y30, lumMem, tessellator);
                lumMem -= (float)Lagometer.renderTime(y60, timesScheduledExecutables[y60], 0, 0, y30, lumMem, tessellator);
                float f = lumMem - (float)Lagometer.renderTime(y60, timesTick[y60], 0, y30, y30, lumMem, tessellator);
            }
            Lagometer.renderTimeDivider(0, timesFrame.length, 33333333L, 196, 196, 196, Lagometer.mc.displayHeight, tessellator);
            Lagometer.renderTimeDivider(0, timesFrame.length, 16666666L, 196, 196, 196, Lagometer.mc.displayHeight, tessellator);
            tess.draw();
            GlStateManager.func_179098_w();
            y60 = Lagometer.mc.displayHeight - 80;
            y30 = Lagometer.mc.displayHeight - 160;
            Lagometer.mc.fontRendererObj.drawString("30", 2.0, y30 + 1, -8947849);
            Lagometer.mc.fontRendererObj.drawString("30", 1.0, y30, -3881788);
            Lagometer.mc.fontRendererObj.drawString("60", 2.0, y60 + 1, -8947849);
            Lagometer.mc.fontRendererObj.drawString("60", 1.0, y60, -3881788);
            GlStateManager.matrixMode(5889);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.func_179098_w();
            lumMem = 1.0f - (float)((double)(System.currentTimeMillis() - memTimeStartMs) / 1000.0);
            lumMem = Config.limit(lumMem, 0.0f, 1.0f);
            int var14 = (int)(170.0f + lumMem * 85.0f);
            int memColG = (int)(100.0f + lumMem * 55.0f);
            int memColB = (int)(10.0f + lumMem * 10.0f);
            int colMem = var14 << 16 | memColG << 8 | memColB;
            int posX = 512 / scaledResolution.getScaleFactor() + 2;
            int posY = Lagometer.mc.displayHeight / scaledResolution.getScaleFactor() - 8;
            GuiIngame var15 = Lagometer.mc.ingameGUI;
            GuiIngame.drawRect(posX - 1, posY - 1, posX + 50, posY + 10, -1605349296);
            Lagometer.mc.fontRendererObj.drawString(" " + memMbSec + " MB/s", posX, posY, colMem);
            renderTimeNano = System.nanoTime() - timeRenderStartNano;
        }
    }

    private static long renderTime(int frameNum, long time, int r, int g, int b, float baseHeight, WorldRenderer tessellator) {
        long heightTime = time / 200000L;
        if (heightTime < 3L) {
            return 0L;
        }
        tessellator.func_178961_b(r, g, b, 255);
        tessellator.addVertex((float)frameNum + 0.5f, baseHeight - (float)heightTime + 0.5f, 0.0);
        tessellator.addVertex((float)frameNum + 0.5f, baseHeight + 0.5f, 0.0);
        return heightTime;
    }

    private static long renderTimeDivider(int frameStart, int frameEnd, long time, int r, int g, int b, float baseHeight, WorldRenderer tessellator) {
        long heightTime = time / 200000L;
        if (heightTime < 3L) {
            return 0L;
        }
        tessellator.func_178961_b(r, g, b, 255);
        tessellator.addVertex((float)frameStart + 0.5f, baseHeight - (float)heightTime + 0.5f, 0.0);
        tessellator.addVertex((float)frameEnd + 0.5f, baseHeight - (float)heightTime + 0.5f, 0.0);
        return heightTime;
    }

    public static boolean isActive() {
        return active;
    }

    public static class TimerNano {
        public long timeStartNano = 0L;
        public long timeNano = 0L;

        public void start() {
            if (active && this.timeStartNano == 0L) {
                this.timeStartNano = System.nanoTime();
            }
        }

        public void end() {
            if (active && this.timeStartNano != 0L) {
                this.timeNano += System.nanoTime() - this.timeStartNano;
                this.timeStartNano = 0L;
            }
        }

        private void reset() {
            this.timeNano = 0L;
            this.timeStartNano = 0L;
        }
    }
}

