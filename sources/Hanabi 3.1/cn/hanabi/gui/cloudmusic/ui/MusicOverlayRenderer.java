package cn.hanabi.gui.cloudmusic.ui;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Hanabi;
import cn.hanabi.gui.cloudmusic.MusicManager;
import cn.hanabi.gui.font.noway.ttfr.HFontRenderer;
import cn.hanabi.modules.modules.render.HUD;
import cn.hanabi.utils.ClientUtil;
import cn.hanabi.utils.Colors;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@ObfuscationClass
public enum MusicOverlayRenderer {
   INSTANCE;

   public String downloadProgress = "0";
   public long readedSecs = 0L;
   public long totalSecs = 0L;
   public float animation = 0.0F;
   public TimeHelper timer = new TimeHelper();
   public boolean firstTime = true;
   public Hanabi hanaInstance;

   private MusicOverlayRenderer() {
      this.hanaInstance = Hanabi.INSTANCE;
   }

   public void renderOverlay() {
      int addonX = ((Double)HUD.musicPosX.getValueState()).intValue();
      int addonY = ((Double)HUD.musicPosY.getValueState()).intValue();
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      if (MusicManager.INSTANCE.getCurrentTrack() != null && MusicManager.INSTANCE.getMediaPlayer() != null) {
         this.readedSecs = (long)((int)MusicManager.INSTANCE.getMediaPlayer().getCurrentTime().toSeconds());
         this.totalSecs = (long)((int)MusicManager.INSTANCE.getMediaPlayer().getStopTime().toSeconds());
      }

      if (MusicManager.INSTANCE.getCurrentTrack() != null && MusicManager.INSTANCE.getMediaPlayer() != null) {
         this.hanaInstance.fontManager.wqy18.drawString(MusicManager.INSTANCE.getCurrentTrack().name + " - " + MusicManager.INSTANCE.getCurrentTrack().artists, 36.0F + (float)addonX, (float)(10 + addonY), Colors.WHITE.c);
         this.hanaInstance.fontManager.wqy18.drawString(this.formatSeconds((int)this.readedSecs) + "/" + this.formatSeconds((int)this.totalSecs), 36.0F + (float)addonX, (float)(20 + addonY), -1);
         if (MusicManager.INSTANCE.circleLocations.containsKey(MusicManager.INSTANCE.getCurrentTrack().id)) {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            ResourceLocation icon = (ResourceLocation)MusicManager.INSTANCE.circleLocations.get(MusicManager.INSTANCE.getCurrentTrack().id);
            RenderUtil.drawImage(icon, 4 + addonX, 6 + addonY, 28, 28);
            GL11.glPopMatrix();
         } else {
            MusicManager.INSTANCE.getCircle(MusicManager.INSTANCE.getCurrentTrack());
         }

         try {
            float currentProgress = (float)(MusicManager.INSTANCE.getMediaPlayer().getCurrentTime().toSeconds() / Math.max(1.0, MusicManager.INSTANCE.getMediaPlayer().getStopTime().toSeconds())) * 100.0F;
            RenderUtil.drawArc((float)(18 + addonX), (float)(19 + addonY), 14.0, Colors.WHITE.c, 0, 360.0, 4);
            RenderUtil.drawArc((float)(18 + addonX), (float)(19 + addonY), 14.0, Colors.BLUE.c, 180, (double)(180.0F + currentProgress * 3.6F), 4);
         } catch (Exception var10) {
         }
      }

      HFontRenderer wqy;
      if (MusicManager.INSTANCE.lyric) {
         wqy = Hanabi.INSTANCE.fontManager.wqy18;
         int addonYlyr = ((Double)HUD.musicPosYlyr.getValueState()).intValue();
         int col = MusicManager.INSTANCE.tlrc.isEmpty() ? Colors.GREY.c : -16732281;
         GlStateManager.disableBlend();
         wqy.drawCenteredString(MusicManager.INSTANCE.lrcCur.contains("_EMPTY_") ? "等待中......." : MusicManager.INSTANCE.lrcCur, (float)sr.getScaledWidth() / 2.0F - 0.5F, (float)(sr.getScaledHeight() - 140 - 80 + addonYlyr), -16732281);
         wqy.drawCenteredString(MusicManager.INSTANCE.tlrcCur.contains("_EMPTY_") ? "Waiting......." : MusicManager.INSTANCE.tlrcCur, (float)sr.getScaledWidth() / 2.0F, (float)(sr.getScaledHeight() - 125) + 0.5F - 80.0F + (float)addonYlyr, col);
         GlStateManager.enableBlend();
      }

      if (MusicManager.showMsg) {
         if (this.firstTime) {
            this.timer.reset();
            this.firstTime = false;
         }

         wqy = Hanabi.INSTANCE.fontManager.wqy18;
         HFontRenderer sans = Hanabi.INSTANCE.fontManager.usans25;
         float width1 = (float)wqy.getStringWidth(MusicManager.INSTANCE.getCurrentTrack().name);
         float width2 = (float)sans.getStringWidth("Now playing");
         float allWidth = Math.max(Math.max(width1, width2), 150.0F);
         RenderUtil.drawRect((float)sr.getScaledWidth() - this.animation, 5.0F, (float)sr.getScaledWidth(), 40.0F, ClientUtil.reAlpha(Colors.BLACK.c, 0.7F));
         if (MusicManager.INSTANCE.circleLocations.containsKey(MusicManager.INSTANCE.getCurrentTrack().id)) {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            ResourceLocation icon = (ResourceLocation)MusicManager.INSTANCE.circleLocations.get(MusicManager.INSTANCE.getCurrentTrack().id);
            RenderUtil.drawImage(icon, (float)sr.getScaledWidth() - this.animation + 5.0F, 8.0F, 28.0F, 28.0F);
            GL11.glPopMatrix();
         } else {
            MusicManager.INSTANCE.getCircle(MusicManager.INSTANCE.getCurrentTrack());
         }

         RenderUtil.drawArc((float)sr.getScaledWidth() - this.animation - 31.0F + 50.0F, 22.0F, 14.0, Colors.WHITE.c, 0, 360.0, 2);
         sans.drawString("Now playing", (float)sr.getScaledWidth() - this.animation - 12.0F + 50.0F, 8.0F, Colors.WHITE.c);
         wqy.drawString(MusicManager.INSTANCE.getCurrentTrack().name, (float)sr.getScaledWidth() - this.animation - 12.0F + 50.0F, 26.0F, Colors.WHITE.c);
         if (this.timer.isDelayComplete(5000L)) {
            this.animation = (float)RenderUtil.getAnimationStateSmooth(0.0, (double)this.animation, (double)(10.0F / (float)Minecraft.getDebugFPS()));
            if (this.animation <= 0.0F) {
               MusicManager.showMsg = false;
               this.firstTime = true;
            }
         } else {
            this.animation = (float)RenderUtil.getAnimationStateSmooth((double)allWidth, (double)this.animation, (double)(10.0F / (float)Minecraft.getDebugFPS()));
         }
      }

      GlStateManager.resetColor();
   }

   public String formatSeconds(int seconds) {
      String rstl = "";
      int mins = seconds / 60;
      if (mins < 10) {
         rstl = rstl + "0";
      }

      rstl = rstl + mins + ":";
      seconds %= 60;
      if (seconds < 10) {
         rstl = rstl + "0";
      }

      rstl = rstl + seconds;
      return rstl;
   }
}
