package cn.hanabi.gui.cloudmusic.ui;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Hanabi;
import cn.hanabi.gui.cloudmusic.MusicManager;
import cn.hanabi.gui.cloudmusic.impl.Track;
import cn.hanabi.utils.RenderUtil;
import java.awt.Color;

@ObfuscationClass
public class TrackSlot {
   public Track track;
   public float x;
   public float y;

   public TrackSlot(Track t) {
      this.track = t;
   }

   public void render(float a, float b, int mouseX, int mouseY) {
      this.x = a;
      this.y = b;
      RenderUtil.drawRoundedRect(this.x, this.y, this.x + 137.0F, this.y + 20.0F, 2.0F, -13355204);
      Hanabi.INSTANCE.fontManager.wqy16.drawString(this.track.name, this.x + 2.0F, this.y + 1.0F, Color.WHITE.getRGB());
      Hanabi.INSTANCE.fontManager.wqy13.drawString(this.track.artists, this.x + 2.0F, this.y + 10.0F, Color.WHITE.getRGB());
      RenderUtil.drawRoundedRect(this.x + 122.0F, this.y, this.x + 137.0F, this.y + 20.0F, 2.0F, -13355204);
      Hanabi.INSTANCE.fontManager.micon15.drawString("J", this.x + 125.5F, this.y + 5.5F, Color.WHITE.getRGB());
   }

   public void click(int mouseX, int mouseY, int mouseButton) {
      if (RenderUtil.isHovering(mouseX, mouseY, this.x + 125.0F, this.y + 5.0F, this.x + 135.0F, this.y + 15.0F) && mouseButton == 0) {
         MusicManager.INSTANCE.play(this.track);
      }

   }
}
