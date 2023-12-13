package org.alphacentauri.management.sexyness.background;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.alphacentauri.management.data.Rect;
import org.alphacentauri.management.sexyness.background.Star;

public class BackgroundRenderer {
   private GuiScreen screen;
   private Rect area;
   public int starAmount;
   private final int bgUP;
   private final int bgDOWN;
   private final int starR;
   private final int starG;
   private final int starB;
   private boolean isToScreen;
   private List stars;

   public BackgroundRenderer(GuiScreen screen, Rect area, int starAmount, int bgUP, int bgDOWN, int starR, int starG, int starB) {
      this.isToScreen = false;
      this.stars = new ArrayList();
      this.screen = screen;
      this.area = area;
      this.starAmount = starAmount;
      this.bgUP = bgUP;
      this.bgDOWN = bgDOWN;
      this.starR = starR;
      this.starG = starG;
      this.starB = starB;
   }

   public BackgroundRenderer(GuiScreen screen, Rect area, int starAmount) {
      this(screen, area, starAmount, -16728065, -16744792, 255, 255, 255);
   }

   public BackgroundRenderer(GuiScreen screen, Rect area) {
      this(screen, area, 100);
   }

   public BackgroundRenderer(GuiScreen screen) {
      this(screen, (Rect)null);
      this.isToScreen = true;
   }

   public void render() {
      GlStateManager.disableLighting();
      GlStateManager.disableFog();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      if(this.area == null) {
         this.area = new Rect(0, 0, this.screen.width, this.screen.height);
      } else if(this.isToScreen && (this.area.getWidth() != this.screen.width || this.area.getHeight() != this.screen.height)) {
         this.area = new Rect(0, 0, this.screen.width, this.screen.height);
      }

      this.checkStars();
      this.screen.drawGradientRect(this.area.getStartX(), this.area.getStartY(), this.area.getEndX(), this.area.getEndY(), this.bgUP, this.bgDOWN);

      for(Star star : this.stars) {
         star.render();
      }

   }

   public void checkStars() {
      ArrayList<Integer> toRemove = new ArrayList();

      for(int i = 0; i < this.stars.size(); ++i) {
         if(((Star)this.stars.get(i)).isOutOfBounds()) {
            toRemove.add(Integer.valueOf(i));
         }
      }

      toRemove.sort(Collections.reverseOrder());

      for(Integer integer : toRemove) {
         this.stars.remove(integer.intValue());
      }

      int starsToCreate = this.starAmount - this.stars.size();

      for(int i = 0; i < starsToCreate; ++i) {
         this.stars.add(new Star(this.screen, this.area, this.starR, this.starG, this.starB));
      }

   }
}
