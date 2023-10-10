package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.profiler.Profiler;
import org.lwjgl.opengl.GL11;

public class Lagometer {
   private static Minecraft mc;
   private static GameSettings gameSettings;
   private static Profiler profiler;
   public static boolean active = false;
   public static Lagometer.TimerNano timerTick = new Lagometer.TimerNano();
   public static Lagometer.TimerNano timerScheduledExecutables = new Lagometer.TimerNano();
   public static Lagometer.TimerNano timerChunkUpload = new Lagometer.TimerNano();
   public static Lagometer.TimerNano timerChunkUpdate = new Lagometer.TimerNano();
   public static Lagometer.TimerNano timerVisibility = new Lagometer.TimerNano();
   public static Lagometer.TimerNano timerTerrain = new Lagometer.TimerNano();
   public static Lagometer.TimerNano timerServer = new Lagometer.TimerNano();
   private static long[] timesFrame = new long[512];
   private static long[] timesTick = new long[512];
   private static long[] timesScheduledExecutables = new long[512];
   private static long[] timesChunkUpload = new long[512];
   private static long[] timesChunkUpdate = new long[512];
   private static long[] timesVisibility = new long[512];
   private static long[] timesTerrain = new long[512];
   private static long[] timesServer = new long[512];
   private static boolean[] gcs = new boolean[512];
   private static int numRecordedFrameTimes = 0;
   private static long prevFrameTimeNano = -1L;
   private static long renderTimeNano = 0L;
   private static long memTimeStartMs = System.currentTimeMillis();
   private static long memStart = getMemoryUsed();
   private static long memTimeLast;
   private static long memLast;
   private static long memTimeDiffMs;
   private static long memDiff;
   private static int memMbSec;

   static {
      memTimeLast = memTimeStartMs;
      memLast = memStart;
      memTimeDiffMs = 1L;
      memDiff = 0L;
      memMbSec = 0;
   }

   public static boolean updateMemoryAllocation() {
      long var0 = System.currentTimeMillis();
      long var2 = getMemoryUsed();
      boolean var4 = false;
      if (var2 < memLast) {
         double var5 = (double)memDiff / 1000000.0D;
         double var7 = (double)memTimeDiffMs / 1000.0D;
         int var9 = (int)(var5 / var7);
         if (var9 > 0) {
            memMbSec = var9;
         }

         memTimeStartMs = var0;
         memStart = var2;
         memTimeDiffMs = 0L;
         memDiff = 0L;
         var4 = true;
      } else {
         memTimeDiffMs = var0 - memTimeStartMs;
         memDiff = var2 - memStart;
      }

      memTimeLast = var0;
      memLast = var2;
      return var4;
   }

   private static long getMemoryUsed() {
      Runtime var0 = Runtime.getRuntime();
      return var0.totalMemory() - var0.freeMemory();
   }

   public static void updateLagometer() {
      if (mc == null) {
         mc = Minecraft.getMinecraft();
         gameSettings = mc.gameSettings;
         profiler = mc.mcProfiler;
      }

      if (!gameSettings.showDebugInfo || !gameSettings.ofLagometer && !gameSettings.field_181657_aC) {
         active = false;
         prevFrameTimeNano = -1L;
      } else {
         active = true;
         long var0 = System.nanoTime();
         int var2 = numRecordedFrameTimes & timesFrame.length - 1;
         ++numRecordedFrameTimes;
         boolean var3 = updateMemoryAllocation();
         timesFrame[var2] = var0 - prevFrameTimeNano - renderTimeNano;
         timesTick[var2] = timerTick.timeNano;
         timesScheduledExecutables[var2] = timerScheduledExecutables.timeNano;
         timesChunkUpload[var2] = timerChunkUpload.timeNano;
         timesChunkUpdate[var2] = timerChunkUpdate.timeNano;
         timesVisibility[var2] = timerVisibility.timeNano;
         timesTerrain[var2] = timerTerrain.timeNano;
         timesServer[var2] = timerServer.timeNano;
         gcs[var2] = var3;
         Lagometer.TimerNano.access$0(timerTick);
         Lagometer.TimerNano.access$0(timerScheduledExecutables);
         Lagometer.TimerNano.access$0(timerVisibility);
         Lagometer.TimerNano.access$0(timerChunkUpdate);
         Lagometer.TimerNano.access$0(timerChunkUpload);
         Lagometer.TimerNano.access$0(timerTerrain);
         Lagometer.TimerNano.access$0(timerServer);
         prevFrameTimeNano = System.nanoTime();
      }

   }

   public static void showLagometer(ScaledResolution var0) {
      if (gameSettings != null && (gameSettings.ofLagometer || gameSettings.field_181657_aC)) {
         long var1 = System.nanoTime();
         GlStateManager.clear(256);
         GlStateManager.matrixMode(5889);
         GlStateManager.pushMatrix();
         GlStateManager.enableColorMaterial();
         GlStateManager.loadIdentity();
         GlStateManager.ortho(0.0D, (double)mc.displayWidth, (double)mc.displayHeight, 0.0D, 1000.0D, 3000.0D);
         GlStateManager.matrixMode(5888);
         GlStateManager.pushMatrix();
         GlStateManager.loadIdentity();
         GlStateManager.translate(0.0F, 0.0F, -2000.0F);
         GL11.glLineWidth(1.0F);
         GlStateManager.disableTexture2D();
         Tessellator var3 = Tessellator.getInstance();
         WorldRenderer var4 = var3.getWorldRenderer();
         var4.begin(1, DefaultVertexFormats.POSITION_COLOR);

         int var5;
         int var6;
         float var7;
         for(var5 = 0; var5 < timesFrame.length; ++var5) {
            var6 = (var5 - numRecordedFrameTimes & timesFrame.length - 1) * 100 / timesFrame.length;
            var6 += 155;
            var7 = (float)mc.displayHeight;
            long var8 = 0L;
            if (gcs[var5]) {
               renderTime(var5, timesFrame[var5], var6, var6 / 2, 0, var7, var4);
            } else {
               renderTime(var5, timesFrame[var5], var6, var6, var6, var7, var4);
               var7 -= (float)renderTime(var5, timesServer[var5], var6 / 2, var6 / 2, var6 / 2, var7, var4);
               var7 -= (float)renderTime(var5, timesTerrain[var5], 0, var6, 0, var7, var4);
               var7 -= (float)renderTime(var5, timesVisibility[var5], var6, var6, 0, var7, var4);
               var7 -= (float)renderTime(var5, timesChunkUpdate[var5], var6, 0, 0, var7, var4);
               var7 -= (float)renderTime(var5, timesChunkUpload[var5], var6, 0, var6, var7, var4);
               var7 -= (float)renderTime(var5, timesScheduledExecutables[var5], 0, 0, var6, var7, var4);
               float var10000 = var7 - (float)renderTime(var5, timesTick[var5], 0, var6, var6, var7, var4);
            }
         }

         renderTimeDivider(0, timesFrame.length, 33333333L, 196, 196, 196, (float)mc.displayHeight, var4);
         renderTimeDivider(0, timesFrame.length, 16666666L, 196, 196, 196, (float)mc.displayHeight, var4);
         var3.draw();
         GlStateManager.enableTexture2D();
         var5 = mc.displayHeight - 80;
         var6 = mc.displayHeight - 160;
         Minecraft.fontRendererObj.drawString("30", 2.0D, (double)(var6 + 1), -8947849);
         Minecraft.fontRendererObj.drawString("30", 1.0D, (double)var6, -3881788);
         Minecraft.fontRendererObj.drawString("60", 2.0D, (double)(var5 + 1), -8947849);
         Minecraft.fontRendererObj.drawString("60", 1.0D, (double)var5, -3881788);
         GlStateManager.matrixMode(5889);
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5888);
         GlStateManager.popMatrix();
         GlStateManager.enableTexture2D();
         var7 = 1.0F - (float)((double)(System.currentTimeMillis() - memTimeStartMs) / 1000.0D);
         var7 = Config.limit(var7, 0.0F, 1.0F);
         int var15 = (int)(170.0F + var7 * 85.0F);
         int var9 = (int)(100.0F + var7 * 55.0F);
         int var10 = (int)(10.0F + var7 * 10.0F);
         int var11 = var15 << 16 | var9 << 8 | var10;
         int var12 = 512 / var0.getScaleFactor() + 2;
         int var13 = mc.displayHeight / var0.getScaleFactor() - 8;
         GuiIngame var14 = mc.ingameGUI;
         GuiIngame.drawRect((double)(var12 - 1), (double)(var13 - 1), (double)(var12 + 50), (double)(var13 + 10), -1605349296);
         Minecraft.fontRendererObj.drawString(" " + memMbSec + " MB/s", (double)var12, (double)var13, var11);
         renderTimeNano = System.nanoTime() - var1;
      }

   }

   private static long renderTime(int var0, long var1, int var3, int var4, int var5, float var6, WorldRenderer var7) {
      long var8 = var1 / 200000L;
      if (var8 < 3L) {
         return 0L;
      } else {
         var7.pos((double)((float)var0 + 0.5F), (double)(var6 - (float)var8 + 0.5F), 0.0D).color(var3, var4, var5, 255).endVertex();
         var7.pos((double)((float)var0 + 0.5F), (double)(var6 + 0.5F), 0.0D).color(var3, var4, var5, 255).endVertex();
         return var8;
      }
   }

   private static long renderTimeDivider(int var0, int var1, long var2, int var4, int var5, int var6, float var7, WorldRenderer var8) {
      long var9 = var2 / 200000L;
      if (var9 < 3L) {
         return 0L;
      } else {
         var8.pos((double)((float)var0 + 0.5F), (double)(var7 - (float)var9 + 0.5F), 0.0D).color(var4, var5, var6, 255).endVertex();
         var8.pos((double)((float)var1 + 0.5F), (double)(var7 - (float)var9 + 0.5F), 0.0D).color(var4, var5, var6, 255).endVertex();
         return var9;
      }
   }

   public static boolean isActive() {
      return active;
   }

   public static class TimerNano {
      public long timeStartNano = 0L;
      public long timeNano = 0L;

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

      static void access$0(Lagometer.TimerNano var0) {
         var0.reset();
      }
   }
}
