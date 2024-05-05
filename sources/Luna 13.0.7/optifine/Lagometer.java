package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.profiler.Profiler;
import org.lwjgl.opengl.GL11;

public class Lagometer
{
  private static Minecraft mc;
  private static GameSettings gameSettings;
  private static Profiler profiler;
  public static boolean active = false;
  public static TimerNano timerTick = new TimerNano();
  public static TimerNano timerScheduledExecutables = new TimerNano();
  public static TimerNano timerChunkUpload = new TimerNano();
  public static TimerNano timerChunkUpdate = new TimerNano();
  public static TimerNano timerVisibility = new TimerNano();
  public static TimerNano timerTerrain = new TimerNano();
  public static TimerNano timerServer = new TimerNano();
  private static long[] timesFrame = new long['Ȁ'];
  private static long[] timesTick = new long['Ȁ'];
  private static long[] timesScheduledExecutables = new long['Ȁ'];
  private static long[] timesChunkUpload = new long['Ȁ'];
  private static long[] timesChunkUpdate = new long['Ȁ'];
  private static long[] timesVisibility = new long['Ȁ'];
  private static long[] timesTerrain = new long['Ȁ'];
  private static long[] timesServer = new long['Ȁ'];
  private static boolean[] gcs = new boolean['Ȁ'];
  private static int numRecordedFrameTimes = 0;
  private static long prevFrameTimeNano = -1L;
  private static long renderTimeNano = 0L;
  private static long memTimeStartMs = System.currentTimeMillis();
  private static long memStart = getMemoryUsed();
  private static long memTimeLast = memTimeStartMs;
  private static long memLast = memStart;
  private static long memTimeDiffMs = 1L;
  private static long memDiff = 0L;
  private static int memMbSec = 0;
  
  public Lagometer() {}
  
  public static boolean updateMemoryAllocation()
  {
    long timeNowMs = System.currentTimeMillis();
    long memNow = getMemoryUsed();
    boolean gc = false;
    if (memNow < memLast)
    {
      double memDiffMb = memDiff / 1000000.0D;
      double timeDiffSec = memTimeDiffMs / 1000.0D;
      int mbSec = (int)(memDiffMb / timeDiffSec);
      if (mbSec > 0) {
        memMbSec = mbSec;
      }
      memTimeStartMs = timeNowMs;
      memStart = memNow;
      memTimeDiffMs = 0L;
      memDiff = 0L;
      gc = true;
    }
    else
    {
      memTimeDiffMs = timeNowMs - memTimeStartMs;
      memDiff = memNow - memStart;
    }
    memTimeLast = timeNowMs;
    memLast = memNow;
    return gc;
  }
  
  private static long getMemoryUsed()
  {
    Runtime r = Runtime.getRuntime();
    return r.totalMemory() - r.freeMemory();
  }
  
  public static void updateLagometer()
  {
    if (mc == null)
    {
      mc = Minecraft.getMinecraft();
      gameSettings = mc.gameSettings;
      profiler = mc.mcProfiler;
    }
    if ((gameSettings.showDebugInfo) && (gameSettings.ofLagometer))
    {
      active = true;
      long timeNowNano = System.nanoTime();
      if (prevFrameTimeNano == -1L)
      {
        prevFrameTimeNano = timeNowNano;
      }
      else
      {
        int frameIndex = numRecordedFrameTimes & timesFrame.length - 1;
        numRecordedFrameTimes += 1;
        boolean gc = updateMemoryAllocation();
        timesFrame[frameIndex] = (timeNowNano - prevFrameTimeNano - renderTimeNano);
        timesTick[frameIndex] = timerTick.timeNano;
        timesScheduledExecutables[frameIndex] = timerScheduledExecutables.timeNano;
        timesChunkUpload[frameIndex] = timerChunkUpload.timeNano;
        timesChunkUpdate[frameIndex] = timerChunkUpdate.timeNano;
        timesVisibility[frameIndex] = timerVisibility.timeNano;
        timesTerrain[frameIndex] = timerTerrain.timeNano;
        timesServer[frameIndex] = timerServer.timeNano;
        gcs[frameIndex] = gc;
        timerTick.reset();
        timerScheduledExecutables.reset();
        timerVisibility.reset();
        timerChunkUpdate.reset();
        timerChunkUpload.reset();
        timerTerrain.reset();
        timerServer.reset();
        prevFrameTimeNano = System.nanoTime();
      }
    }
    else
    {
      active = false;
      prevFrameTimeNano = -1L;
    }
  }
  
  public static void showLagometer(ScaledResolution scaledResolution)
  {
    if (gameSettings != null) {
      if (gameSettings.ofLagometer)
      {
        long timeRenderStartNano = System.nanoTime();
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.enableColorMaterial();
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, mc.displayWidth, mc.displayHeight, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GL11.glLineWidth(1.0F);
        GlStateManager.func_179090_x();
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer tessellator = tess.getWorldRenderer();
        tessellator.startDrawing(1);
        for (int y60 = 0; y60 < timesFrame.length; y60++)
        {
          int y30 = (y60 - numRecordedFrameTimes & timesFrame.length - 1) * 100 / timesFrame.length;
          y30 += 155;
          float lumMem = mc.displayHeight;
          long memColR = 0L;
          if (gcs[y60] != 0)
          {
            renderTime(y60, timesFrame[y60], y30, y30 / 2, 0, lumMem, tessellator);
          }
          else
          {
            renderTime(y60, timesFrame[y60], y30, y30, y30, lumMem, tessellator);
            lumMem -= (float)renderTime(y60, timesServer[y60], y30 / 2, y30 / 2, y30 / 2, lumMem, tessellator);
            lumMem -= (float)renderTime(y60, timesTerrain[y60], 0, y30, 0, lumMem, tessellator);
            lumMem -= (float)renderTime(y60, timesVisibility[y60], y30, y30, 0, lumMem, tessellator);
            lumMem -= (float)renderTime(y60, timesChunkUpdate[y60], y30, 0, 0, lumMem, tessellator);
            lumMem -= (float)renderTime(y60, timesChunkUpload[y60], y30, 0, y30, lumMem, tessellator);
            lumMem -= (float)renderTime(y60, timesScheduledExecutables[y60], 0, 0, y30, lumMem, tessellator);
            float f1 = lumMem - (float)renderTime(y60, timesTick[y60], 0, y30, y30, lumMem, tessellator);
          }
        }
        renderTimeDivider(0, timesFrame.length, 33333333L, 196, 196, 196, mc.displayHeight, tessellator);
        renderTimeDivider(0, timesFrame.length, 16666666L, 196, 196, 196, mc.displayHeight, tessellator);
        tess.draw();
        GlStateManager.func_179098_w();
        y60 = mc.displayHeight - 80;
        int y30 = mc.displayHeight - 160;
        Minecraft.fontRendererObj.drawString("30", 2, y30 + 1, -8947849);
        Minecraft.fontRendererObj.drawString("30", 1, y30, -3881788);
        Minecraft.fontRendererObj.drawString("60", 2, y60 + 1, -8947849);
        Minecraft.fontRendererObj.drawString("60", 1, y60, -3881788);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.func_179098_w();
        float lumMem = 1.0F - (float)((System.currentTimeMillis() - memTimeStartMs) / 1000.0D);
        lumMem = Config.limit(lumMem, 0.0F, 1.0F);
        int var14 = (int)(170.0F + lumMem * 85.0F);
        int memColG = (int)(100.0F + lumMem * 55.0F);
        int memColB = (int)(10.0F + lumMem * 10.0F);
        int colMem = var14 << 16 | memColG << 8 | memColB;
        int posX = 512 / scaledResolution.getScaleFactor() + 2;
        int posY = mc.displayHeight / scaledResolution.getScaleFactor() - 8;
        GuiIngame var15 = mc.ingameGUI;
        GuiIngame.drawRect(posX - 1, posY - 1, posX + 50, posY + 10, -1605349296);
        Minecraft.fontRendererObj.drawString(" " + memMbSec + " MB/s", posX, posY, colMem);
        renderTimeNano = System.nanoTime() - timeRenderStartNano;
      }
    }
  }
  
  private static long renderTime(int frameNum, long time, int r, int g, int b, float baseHeight, WorldRenderer tessellator)
  {
    long heightTime = time / 200000L;
    if (heightTime < 3L) {
      return 0L;
    }
    tessellator.func_178961_b(r, g, b, 255);
    tessellator.addVertex(frameNum + 0.5F, baseHeight - (float)heightTime + 0.5F, 0.0D);
    tessellator.addVertex(frameNum + 0.5F, baseHeight + 0.5F, 0.0D);
    return heightTime;
  }
  
  private static long renderTimeDivider(int frameStart, int frameEnd, long time, int r, int g, int b, float baseHeight, WorldRenderer tessellator)
  {
    long heightTime = time / 200000L;
    if (heightTime < 3L) {
      return 0L;
    }
    tessellator.func_178961_b(r, g, b, 255);
    tessellator.addVertex(frameStart + 0.5F, baseHeight - (float)heightTime + 0.5F, 0.0D);
    tessellator.addVertex(frameEnd + 0.5F, baseHeight - (float)heightTime + 0.5F, 0.0D);
    return heightTime;
  }
  
  public static boolean isActive()
  {
    return active;
  }
  
  public static class TimerNano
  {
    public long timeStartNano = 0L;
    public long timeNano = 0L;
    
    public TimerNano() {}
    
    public void start()
    {
      if (Lagometer.active) {
        if (this.timeStartNano == 0L) {
          this.timeStartNano = System.nanoTime();
        }
      }
    }
    
    public void end()
    {
      if (Lagometer.active) {
        if (this.timeStartNano != 0L)
        {
          this.timeNano += System.nanoTime() - this.timeStartNano;
          this.timeStartNano = 0L;
        }
      }
    }
    
    private void reset()
    {
      this.timeNano = 0L;
      this.timeStartNano = 0L;
    }
  }
}
