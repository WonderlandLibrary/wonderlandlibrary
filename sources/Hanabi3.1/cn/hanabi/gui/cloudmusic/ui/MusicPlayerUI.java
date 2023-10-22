package cn.hanabi.gui.cloudmusic.ui;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.cloudmusic.MusicManager;
import cn.hanabi.gui.cloudmusic.api.CloudMusicAPI;
import cn.hanabi.gui.cloudmusic.impl.Track;
import cn.hanabi.utils.PaletteUtil;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.RenderUtil;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.scene.media.MediaPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class MusicPlayerUI extends GuiScreen {
   public float x = 10.0F;
   public float y = 10.0F;
   public float x2 = 0.0F;
   public float y2 = 0.0F;
   public boolean drag = false;
   public CopyOnWriteArrayList slots = new CopyOnWriteArrayList();
   public float width = 150.0F;
   public float height = 250.0F;
   public boolean extended = false;
   public float sidebarAnimation = 0.0F;
   public float scrollY = 0.0F;
   public float scrollAni = 0.0F;
   public float minY = -100.0F;
   public CustomTextField textField = new CustomTextField("");

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.sidebarAnimation = RenderUtil.smoothAnimation(this.sidebarAnimation, this.extended ? this.width + 5.0F : 0.1F, 50.0F, 0.4F);
      float newX;
      if (Math.ceil((double)this.sidebarAnimation) > 1.0) {
         newX = this.x + this.sidebarAnimation;
         float newWidth = this.x + this.width + this.sidebarAnimation;
         RenderUtil.drawRoundedRect(newX, this.y, newWidth, this.y + this.height, 2.0F, -13684426);
         this.textField.draw(newX + 6.0F, this.y + 2.0F);
         RenderUtil.drawRoundedRect(newWidth - 26.0F, this.y + 5.0F, newWidth - 7.0F, this.y + 17.0F, 2.0F, !RenderUtil.isHovering(mouseX, mouseY, newWidth - 26.0F, this.y + 5.0F, newWidth - 7.0F, this.y + 17.0F) && MusicManager.INSTANCE.analyzeThread == null ? -13355204 : (new Color(80, 80, 80)).getRGB());
         Hanabi.INSTANCE.fontManager.wqy13.drawString("导入", newWidth - 23.0F, this.y + 6.0F, Color.GRAY.getRGB());
         if (this.textField.textString.isEmpty()) {
            Hanabi.INSTANCE.fontManager.wqy13.drawString("输入歌单ID", newX + 8.0F, this.y + 6.0F, Color.GRAY.getRGB());
         }

         if (RenderUtil.isHovering(mouseX, mouseY, newX + 5.0F, this.y + 20.0F, newWidth - 5.0F, this.y + this.height - 4.0F)) {
            int wheel = Mouse.getDWheel() / 2;
            this.scrollY += (float)wheel;
            if (this.scrollY <= this.minY) {
               this.scrollY = this.minY;
            }

            if (this.scrollY >= 0.0F) {
               this.scrollY = 0.0F;
            }

            this.minY = this.height - 24.0F;
         } else {
            Mouse.getDWheel();
         }

         this.scrollAni = RenderUtil.getAnimationState(this.scrollAni, this.scrollY, Math.max(10.0F, Math.abs(this.scrollAni - this.scrollY) * 50.0F) * 0.3F);
         float startY = this.y + 21.0F + this.scrollAni;
         float yShouldbe = 0.0F;
         GL11.glEnable(3089);
         RenderUtil.doGlScissor((int)newX + 6, (int)this.y + 21, 137, 224);

         for(Iterator var8 = this.slots.iterator(); var8.hasNext(); yShouldbe += 22.0F) {
            TrackSlot s = (TrackSlot)var8.next();
            if (startY > this.y && startY < this.y + this.height - 4.0F) {
               s.render(newX + 6.0F, startY, mouseX, mouseY);
            }

            startY += 22.0F;
         }

         GL11.glDisable(3089);
         if (RenderUtil.isHovering(mouseX, mouseY, newX + 5.0F, this.y + 20.0F, newWidth - 5.0F, this.y + this.height - 4.0F)) {
            this.minY -= yShouldbe;
         }

         if (this.slots.size() > 10) {
            float viewable = 224.0F;
            float progress = MathHelper.clamp_float(-this.scrollAni / -this.minY, 0.0F, 1.0F);
            float ratio = viewable / yShouldbe * viewable;
            float barHeight = Math.max(ratio, 20.0F);
            float position = progress * (viewable - barHeight);
            RenderUtil.drawRect(newWidth - 5.0F, this.y + 21.0F, newWidth - 2.0F, this.y + 245.0F, (new Color(100, 100, 100)).getRGB());
            RenderUtil.drawRect(newWidth - 5.0F, this.y + 21.0F + position, newWidth - 2.0F, this.y + 21.0F + position + barHeight, (new Color(73, 73, 73)).getRGB());
         }
      } else {
         Mouse.getDWheel();
      }

      RenderUtil.drawRoundedRect(this.x, this.y, this.x + this.width, this.y + this.height, 2.0F, -13684426);
      RenderUtil.drawRoundedRect(this.x, this.y + this.height - 60.0F, this.x + this.width, this.y + this.height, 2.0F, -13355204);
      RenderUtil.drawRect(this.x, this.y + this.height - 60.0F, this.x + this.width, this.y + this.height - 58.0F, -13355204);
      Hanabi.INSTANCE.fontManager.wqy16.drawString("网易云音乐", this.x + this.width / 2.0F - (float)Hanabi.INSTANCE.fontManager.wqy16.getStringWidth("网易云音乐") / 2.0F - 2.0F, this.y + 5.0F, -1);
      newX = 0.0F;
      if (MusicManager.INSTANCE.getMediaPlayer() != null) {
         newX = (float)MusicManager.INSTANCE.getMediaPlayer().getCurrentTime().toSeconds() / (float)MusicManager.INSTANCE.getMediaPlayer().getStopTime().toSeconds() * 100.0F;
      }

      RenderUtil.drawRoundedRect(this.x + 10.0F, this.y + this.height - 50.0F, this.x + this.width - 10.0F, this.y + this.height - 46.0F, 1.4F, Color.GRAY.getRGB());
      if (MusicManager.INSTANCE.loadingThread != null) {
         RenderUtil.drawRoundedRect(this.x + 10.0F, this.y + this.height - 50.0F, this.x + 10.0F + 1.3F * MusicManager.INSTANCE.downloadProgress, this.y + this.height - 46.0F, 1.4F, Color.WHITE.getRGB());
         RenderUtil.circle(this.x + 10.0F + 1.3F * MusicManager.INSTANCE.downloadProgress, this.y + this.height - 48.0F, 3.0F, (new Color(255, 255, 255)).getRGB());
         RenderUtil.circle(this.x + 10.0F + 1.3F * MusicManager.INSTANCE.downloadProgress, this.y + this.height - 48.0F, 2.0F, PaletteUtil.fade(new Color(255, 50, 50, 255)).getRGB());
      } else {
         RenderUtil.drawRoundedRect(this.x + 10.0F, this.y + this.height - 50.0F, this.x + 10.0F + 1.3F * newX, this.y + this.height - 46.0F, 1.4F, Color.WHITE.getRGB());
         RenderUtil.circle(this.x + 10.0F + 1.3F * newX, this.y + this.height - 48.0F, 3.0F, (new Color(255, 255, 255)).getRGB());
         RenderUtil.circle(this.x + 10.0F + 1.3F * newX, this.y + this.height - 48.0F, 2.0F, (new Color(50, 176, 255, 255)).getRGB());
      }

      RenderUtil.circle(this.x + this.width / 2.0F, this.y + this.height - 24.0F, 12.0F, -12565429);
      if (this.extended) {
         Hanabi.INSTANCE.fontManager.micon15.drawString("C", this.x + this.width - 15.0F, this.y + 5.5F, Color.WHITE.getRGB());
      } else {
         Hanabi.INSTANCE.fontManager.micon15.drawString("Q", this.x + this.width - 15.0F, this.y + 5.5F, Color.WHITE.getRGB());
      }

      Hanabi.INSTANCE.fontManager.micon15.drawString("R", this.x + 5.0F, this.y + 5.5F, Color.WHITE.getRGB());
      String songName = MusicManager.INSTANCE.currentTrack == null ? "当前未在播放" : MusicManager.INSTANCE.currentTrack.name;
      String songArtist = MusicManager.INSTANCE.currentTrack == null ? "N/A" : MusicManager.INSTANCE.currentTrack.artists;
      GL11.glEnable(3089);
      RenderUtil.doGlScissor((int)this.x, (int)this.y + (int)(this.height / 2.0F - 95.0F), (int)this.width, 25);
      Hanabi.INSTANCE.fontManager.wqy16.drawString(songName, this.x + this.width / 2.0F - (float)(Hanabi.INSTANCE.fontManager.wqy16.getStringWidth(songName) / 2) - 1.5F, this.y + (this.height / 2.0F - 95.0F), -1);
      Hanabi.INSTANCE.fontManager.wqy13.drawString(songArtist, this.x + this.width / 2.0F - (float)(Hanabi.INSTANCE.fontManager.wqy13.getStringWidth(songArtist) / 2) - 1.5F, this.y + (this.height / 2.0F - 82.0F), -1);
      GL11.glDisable(3089);
      if (MusicManager.INSTANCE.getMediaPlayer() != null) {
         if (MusicManager.INSTANCE.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
            Hanabi.INSTANCE.fontManager.micon30.drawString("K", this.x + this.width / 2.0F - (float)(Hanabi.INSTANCE.fontManager.micon30.getStringWidth("K") / 2) - 2.0F, this.y + this.height - 32.0F, Color.WHITE.getRGB());
         } else {
            Hanabi.INSTANCE.fontManager.micon30.drawString("J", this.x + this.width / 2.0F - (float)(Hanabi.INSTANCE.fontManager.micon30.getStringWidth("J") / 2), this.y + this.height - 32.0F, Color.WHITE.getRGB());
         }
      } else {
         Hanabi.INSTANCE.fontManager.micon30.drawString("J", this.x + this.width / 2.0F - (float)(Hanabi.INSTANCE.fontManager.micon30.getStringWidth("J") / 2), this.y + this.height - 32.0F, Color.WHITE.getRGB());
      }

      Hanabi.INSTANCE.fontManager.micon30.drawString("L", this.x + this.width / 2.0F - (float)(Hanabi.INSTANCE.fontManager.micon30.getStringWidth("L") / 2) - 30.0F, this.y + this.height - 32.0F, Color.WHITE.getRGB());
      Hanabi.INSTANCE.fontManager.micon30.drawString("M", this.x + this.width / 2.0F - (float)(Hanabi.INSTANCE.fontManager.micon30.getStringWidth("M") / 2) + 27.5F, this.y + this.height - 32.0F, Color.WHITE.getRGB());
      if (MusicManager.INSTANCE.repeat) {
         Hanabi.INSTANCE.fontManager.micon15.drawString("O", this.x + this.width - 20.0F, this.y + this.height - 28.5F, Color.WHITE.getRGB());
      } else {
         Hanabi.INSTANCE.fontManager.micon15.drawString("N", this.x + this.width - 20.0F, this.y + this.height - 28.5F, Color.WHITE.getRGB());
      }

      if (MusicManager.INSTANCE.lyric) {
         Hanabi.INSTANCE.fontManager.micon15.drawString("P", this.x + 10.0F, this.y + this.height - 28.5F, -1);
      } else {
         Hanabi.INSTANCE.fontManager.micon15.drawString("P", this.x + 10.0F, this.y + this.height - 28.5F, -9736591);
      }

      if (MusicManager.INSTANCE.currentTrack != null && MusicManager.INSTANCE.getArt(MusicManager.INSTANCE.currentTrack.id) != null) {
         GL11.glPushMatrix();
         RenderUtil.drawImage(MusicManager.INSTANCE.getArt(MusicManager.INSTANCE.currentTrack.id), this.x + this.width / 2.0F - 50.0F, this.y + (this.height / 2.0F - 10.0F) - 50.0F, 100.0F, 100.0F, 1.0F);
         GL11.glPopMatrix();
      }

      this.dragWindow(mouseX, mouseY);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (RenderUtil.isHovering(mouseX, mouseY, this.x + this.width - 15.0F, this.y + 5.0F, this.x + this.width - 5.0F, this.y + 15.0F) && mouseButton == 0) {
         this.extended = !this.extended;
      }

      if (mouseButton == 0) {
         if (RenderUtil.isHovering(mouseX, mouseY, this.x + this.width / 2.0F - 12.0F, this.y + this.height - 36.0F, this.x + this.width / 2.0F + 12.0F, this.y + this.height - 12.0F) && !MusicManager.INSTANCE.playlist.isEmpty()) {
            if (MusicManager.INSTANCE.currentTrack == null) {
               MusicManager.INSTANCE.play((Track)MusicManager.INSTANCE.playlist.get(0));
            } else if (MusicManager.INSTANCE.getMediaPlayer() != null) {
               if (MusicManager.INSTANCE.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
                  MusicManager.INSTANCE.getMediaPlayer().pause();
               } else {
                  MusicManager.INSTANCE.getMediaPlayer().play();
               }
            }
         }

         if (RenderUtil.isHovering(mouseX, mouseY, this.x + 39.0F, this.y + this.height - 32.0F, this.x + 55.0F, this.y + this.height - 16.0F)) {
            MusicManager.INSTANCE.prev();
         }

         if (RenderUtil.isHovering(mouseX, mouseY, this.x + 96.0F, this.y + this.height - 32.0F, this.x + 112.0F, this.y + this.height - 16.0F)) {
            MusicManager.INSTANCE.next();
         }

         if (RenderUtil.isHovering(mouseX, mouseY, this.x + 10.0F, this.y + this.height - 29.0F, this.x + 20.0F, this.y + this.height - 19.0F)) {
            MusicManager.INSTANCE.lyric = !MusicManager.INSTANCE.lyric;
         }

         if (RenderUtil.isHovering(mouseX, mouseY, this.x + this.width - 20.0F, this.y + this.height - 29.0F, this.x + this.width - 10.0F, this.y + this.height - 19.0F)) {
            MusicManager.INSTANCE.repeat = !MusicManager.INSTANCE.repeat;
         }

         if (RenderUtil.isHovering(mouseX, mouseY, this.x + 5.0F, this.y + 5.0F, this.x + 15.0F, this.y + 15.0F)) {
            this.mc.displayGuiScreen(new QRLoginScreen(this));
         }
      }

      if (this.extended && Math.ceil((double)this.sidebarAnimation) >= (double)(this.width + 5.0F)) {
         float newX = this.x + this.sidebarAnimation;
         float newWidth = this.x + this.width + this.sidebarAnimation;
         if (mouseButton == 0 && RenderUtil.isHovering(mouseX, mouseY, newWidth - 26.0F, this.y + 5.0F, newWidth - 7.0F, this.y + 17.0F) && !this.textField.textString.isEmpty() && MusicManager.INSTANCE.analyzeThread == null) {
            MusicManager.INSTANCE.analyzeThread = new Thread(() -> {
               try {
                  this.slots.clear();
                  MusicManager.INSTANCE.playlist = (ArrayList)CloudMusicAPI.INSTANCE.getPlaylistDetail(this.textField.textString)[1];
                  Iterator var1 = MusicManager.INSTANCE.playlist.iterator();

                  while(var1.hasNext()) {
                     Track t = (Track)var1.next();
                     this.slots.add(new TrackSlot(t));
                  }
               } catch (Exception var3) {
                  PlayerUtil.tellPlayer("解析歌单时发生错误!");
                  var3.printStackTrace();
               }

               MusicManager.INSTANCE.analyzeThread = null;
            });
            MusicManager.INSTANCE.analyzeThread.start();
         }

         if (RenderUtil.isHovering(mouseX, mouseY, newX + 5.0F, this.y + 20.0F, newWidth - 5.0F, this.y + this.height - 4.0F)) {
            float startY = this.y + 21.0F + this.scrollAni;

            for(Iterator var7 = this.slots.iterator(); var7.hasNext(); startY += 22.0F) {
               TrackSlot s = (TrackSlot)var7.next();
               if (startY > this.y && startY < this.y + this.height - 4.0F) {
                  s.click(mouseX, mouseY, mouseButton);
               }
            }
         }

         this.textField.mouseClicked(mouseX, mouseY, mouseButton);
      }

      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (this.extended) {
         this.textField.keyPressed(keyCode);
         this.textField.charTyped(typedChar);
      }

      super.keyTyped(typedChar, keyCode);
   }

   public void dragWindow(int mouseX, int mouseY) {
      if (!RenderUtil.isHovering(mouseX, mouseY, this.x + this.width - 15.0F, this.y + 5.0F, this.x + this.width - 5.0F, this.y + 15.0F)) {
         if (!Mouse.isButtonDown(0) && this.drag) {
            this.drag = false;
         }

         if (this.drag) {
            this.x = (float)mouseX - this.x2;
            this.y = (float)mouseY - this.y2;
         } else if (RenderUtil.isHovering(mouseX, mouseY, this.x, this.y, this.x + this.width, this.y + 20.0F) && Mouse.isButtonDown(0)) {
            this.drag = true;
            this.x2 = (float)mouseX - this.x;
            this.y2 = (float)mouseY - this.y;
         }

      }
   }

   public void updateScreen() {
      super.updateScreen();
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      super.onGuiClosed();
   }

   public boolean doesGuiPauseGame() {
      return false;
   }
}
