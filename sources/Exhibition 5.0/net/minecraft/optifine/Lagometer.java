// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.profiler.Profiler;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.Minecraft;

public class Lagometer
{
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
    
    public static boolean updateMemoryAllocation() {
        final long timeNowMs = System.currentTimeMillis();
        final long memNow = getMemoryUsed();
        boolean gc = false;
        if (memNow < Lagometer.memLast) {
            final double memDiffMb = Lagometer.memDiff / 1000000.0;
            final double timeDiffSec = Lagometer.memTimeDiffMs / 1000.0;
            final int mbSec = (int)(memDiffMb / timeDiffSec);
            if (mbSec > 0) {
                Lagometer.memMbSec = mbSec;
            }
            Lagometer.memTimeStartMs = timeNowMs;
            Lagometer.memStart = memNow;
            Lagometer.memTimeDiffMs = 0L;
            Lagometer.memDiff = 0L;
            gc = true;
        }
        else {
            Lagometer.memTimeDiffMs = timeNowMs - Lagometer.memTimeStartMs;
            Lagometer.memDiff = memNow - Lagometer.memStart;
        }
        Lagometer.memTimeLast = timeNowMs;
        Lagometer.memLast = memNow;
        return gc;
    }
    
    private static long getMemoryUsed() {
        final Runtime r = Runtime.getRuntime();
        return r.totalMemory() - r.freeMemory();
    }
    
    public static void updateLagometer() {
        if (Lagometer.mc == null) {
            Lagometer.mc = Minecraft.getMinecraft();
            Lagometer.gameSettings = Lagometer.mc.gameSettings;
            Lagometer.profiler = Lagometer.mc.mcProfiler;
        }
        if (Lagometer.gameSettings.showDebugInfo && Lagometer.gameSettings.ofLagometer) {
            Lagometer.active = true;
            final long timeNowNano = System.nanoTime();
            if (Lagometer.prevFrameTimeNano == -1L) {
                Lagometer.prevFrameTimeNano = timeNowNano;
            }
            else {
                final int frameIndex = Lagometer.numRecordedFrameTimes & Lagometer.timesFrame.length - 1;
                ++Lagometer.numRecordedFrameTimes;
                final boolean gc = updateMemoryAllocation();
                Lagometer.timesFrame[frameIndex] = timeNowNano - Lagometer.prevFrameTimeNano - Lagometer.renderTimeNano;
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
                Lagometer.prevFrameTimeNano = System.nanoTime();
            }
        }
        else {
            Lagometer.active = false;
            Lagometer.prevFrameTimeNano = -1L;
        }
    }
    
    public static void showLagometer(final ScaledResolution scaledResolution) {
        if (Lagometer.gameSettings != null && Lagometer.gameSettings.ofLagometer) {
            final long timeRenderStartNano = System.nanoTime();
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
            GL11.glLineWidth(1.0f);
            GlStateManager.disableTextures();
            final Tessellator tess = Tessellator.getInstance();
            final WorldRenderer tessellator = tess.getWorldRenderer();
            tessellator.startDrawing(1);
            for (int lumMem = 0; lumMem < Lagometer.timesFrame.length; ++lumMem) {
                int memColR = (lumMem - Lagometer.numRecordedFrameTimes & Lagometer.timesFrame.length - 1) * 100 / Lagometer.timesFrame.length;
                memColR += 155;
                float memColG = Lagometer.mc.displayHeight;
                final long memColB = 0L;
                if (Lagometer.gcs[lumMem]) {
                    renderTime(lumMem, Lagometer.timesFrame[lumMem], memColR, memColR / 2, 0, memColG, tessellator);
                }
                else {
                    renderTime(lumMem, Lagometer.timesFrame[lumMem], memColR, memColR, memColR, memColG, tessellator);
                    memColG -= renderTime(lumMem, Lagometer.timesServer[lumMem], memColR / 2, memColR / 2, memColR / 2, memColG, tessellator);
                    memColG -= renderTime(lumMem, Lagometer.timesTerrain[lumMem], 0, memColR, 0, memColG, tessellator);
                    memColG -= renderTime(lumMem, Lagometer.timesVisibility[lumMem], memColR, memColR, 0, memColG, tessellator);
                    memColG -= renderTime(lumMem, Lagometer.timesChunkUpdate[lumMem], memColR, 0, 0, memColG, tessellator);
                    memColG -= renderTime(lumMem, Lagometer.timesChunkUpload[lumMem], memColR, 0, memColR, memColG, tessellator);
                    memColG -= renderTime(lumMem, Lagometer.timesScheduledExecutables[lumMem], 0, 0, memColR, memColG, tessellator);
                    final float n = memColG - renderTime(lumMem, Lagometer.timesTick[lumMem], 0, memColR, memColR, memColG, tessellator);
                }
            }
            tess.draw();
            GlStateManager.matrixMode(5889);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.enableTextures();
            float var12 = 1.0f - (float)((System.currentTimeMillis() - Lagometer.memTimeStartMs) / 1000.0);
            var12 = Config.limit(var12, 0.0f, 1.0f);
            int memColR = (int)(170.0f + var12 * 85.0f);
            final int var13 = (int)(100.0f + var12 * 55.0f);
            final int var14 = (int)(10.0f + var12 * 10.0f);
            final int colMem = memColR << 16 | var13 << 8 | var14;
            final int posX = 512 / scaledResolution.getScaleFactor() + 2;
            final int posY = Lagometer.mc.displayHeight / scaledResolution.getScaleFactor() - 8;
            final GuiIngame var15 = Lagometer.mc.ingameGUI;
            Gui.drawRect(posX - 1, posY - 1, posX + 50, posY + 10, -1605349296);
            Lagometer.mc.fontRendererObj.drawString(" " + Lagometer.memMbSec + " MB/s", posX, posY, colMem);
            Lagometer.renderTimeNano = System.nanoTime() - timeRenderStartNano;
        }
    }
    
    private static long renderTime(final int frameNum, final long time, final int r, final int g, final int b, final float baseHeight, final WorldRenderer tessellator) {
        final long heightTime = time / 200000L;
        if (heightTime < 3L) {
            return 0L;
        }
        tessellator.func_178961_b(r, g, b, 255);
        tessellator.addVertex(frameNum + 0.5f, baseHeight - heightTime + 0.5f, 0.0);
        tessellator.addVertex(frameNum + 0.5f, baseHeight + 0.5f, 0.0);
        return heightTime;
    }
    
    public static boolean isActive() {
        return Lagometer.active;
    }
    
    static {
        Lagometer.active = false;
        Lagometer.timerTick = new TimerNano();
        Lagometer.timerScheduledExecutables = new TimerNano();
        Lagometer.timerChunkUpload = new TimerNano();
        Lagometer.timerChunkUpdate = new TimerNano();
        Lagometer.timerVisibility = new TimerNano();
        Lagometer.timerTerrain = new TimerNano();
        Lagometer.timerServer = new TimerNano();
        Lagometer.timesFrame = new long[512];
        Lagometer.timesTick = new long[512];
        Lagometer.timesScheduledExecutables = new long[512];
        Lagometer.timesChunkUpload = new long[512];
        Lagometer.timesChunkUpdate = new long[512];
        Lagometer.timesVisibility = new long[512];
        Lagometer.timesTerrain = new long[512];
        Lagometer.timesServer = new long[512];
        Lagometer.gcs = new boolean[512];
        Lagometer.numRecordedFrameTimes = 0;
        Lagometer.prevFrameTimeNano = -1L;
        Lagometer.renderTimeNano = 0L;
        Lagometer.memTimeStartMs = System.currentTimeMillis();
        Lagometer.memStart = getMemoryUsed();
        Lagometer.memTimeLast = Lagometer.memTimeStartMs;
        Lagometer.memLast = Lagometer.memStart;
        Lagometer.memTimeDiffMs = 1L;
        Lagometer.memDiff = 0L;
        Lagometer.memMbSec = 0;
    }
    
    public static class TimerNano
    {
        public long timeStartNano;
        public long timeNano;
        
        public TimerNano() {
            this.timeStartNano = 0L;
            this.timeNano = 0L;
        }
        
        public void start() {
            if (Lagometer.active && this.timeStartNano == 0L) {
                this.timeStartNano = System.nanoTime();
            }
        }
        
        public void end() {
            if (Lagometer.active && this.timeStartNano != 0L) {
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
