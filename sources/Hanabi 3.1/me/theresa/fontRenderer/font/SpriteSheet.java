package me.theresa.fontRenderer.font;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import me.theresa.fontRenderer.font.opengl.Texture;

public class SpriteSheet extends Image {
   private final int tw;
   private final int th;
   private int margin;
   private Image[][] subImages;
   private int spacing;
   private final Image target;

   public SpriteSheet(URL ref, int tw, int th) throws SlickException, IOException {
      this(new Image(ref.openStream(), ref.toString(), false), tw, th);
   }

   public SpriteSheet(Image image, int tw, int th) {
      super(image);
      this.margin = 0;
      this.target = image;
      this.tw = tw;
      this.th = th;
      this.initImpl();
   }

   public SpriteSheet(Image image, int tw, int th, int spacing, int margin) {
      super(image);
      this.margin = 0;
      this.target = image;
      this.tw = tw;
      this.th = th;
      this.spacing = spacing;
      this.margin = margin;
      this.initImpl();
   }

   public SpriteSheet(Image image, int tw, int th, int spacing) {
      this(image, tw, th, spacing, 0);
   }

   public SpriteSheet(String ref, int tw, int th, int spacing) throws SlickException {
      this(ref, tw, th, (Color)null, spacing);
   }

   public SpriteSheet(String ref, int tw, int th) throws SlickException {
      this(ref, tw, th, (Color)null);
   }

   public SpriteSheet(String ref, int tw, int th, Color col) throws SlickException {
      this(ref, tw, th, col, 0);
   }

   public SpriteSheet(String ref, int tw, int th, Color col, int spacing) throws SlickException {
      super(ref, false, 2, col);
      this.margin = 0;
      this.target = this;
      this.tw = tw;
      this.th = th;
      this.spacing = spacing;
   }

   public SpriteSheet(String name, InputStream ref, int tw, int th) throws SlickException {
      super(ref, name, false);
      this.margin = 0;
      this.target = this;
      this.tw = tw;
      this.th = th;
   }

   protected void initImpl() {
      if (this.subImages == null) {
         int tilesAcross = (this.getWidth() - this.margin * 2 - this.tw) / (this.tw + this.spacing) + 1;
         int tilesDown = (this.getHeight() - this.margin * 2 - this.th) / (this.th + this.spacing) + 1;
         if ((this.getHeight() - this.th) % (this.th + this.spacing) != 0) {
            ++tilesDown;
         }

         this.subImages = new Image[tilesAcross][tilesDown];

         for(int x = 0; x < tilesAcross; ++x) {
            for(int y = 0; y < tilesDown; ++y) {
               this.subImages[x][y] = this.getSprite(x, y);
            }
         }

      }
   }

   public Image getSubImage(int x, int y) {
      this.init();
      if (x >= 0 && x < this.subImages.length) {
         if (y >= 0 && y < this.subImages[0].length) {
            return this.subImages[x][y];
         } else {
            throw new RuntimeException("SubImage out of sheet bounds: " + x + "," + y);
         }
      } else {
         throw new RuntimeException("SubImage out of sheet bounds: " + x + "," + y);
      }
   }

   public Image getSprite(int x, int y) {
      this.target.init();
      this.initImpl();
      if (x >= 0 && x < this.subImages.length) {
         if (y >= 0 && y < this.subImages[0].length) {
            return this.target.getSubImage(x * (this.tw + this.spacing) + this.margin, y * (this.th + this.spacing) + this.margin, this.tw, this.th);
         } else {
            throw new RuntimeException("SubImage out of sheet bounds: " + x + "," + y);
         }
      } else {
         throw new RuntimeException("SubImage out of sheet bounds: " + x + "," + y);
      }
   }

   public int getHorizontalCount() {
      this.target.init();
      this.initImpl();
      return this.subImages.length;
   }

   public int getVerticalCount() {
      this.target.init();
      this.initImpl();
      return this.subImages[0].length;
   }

   public void renderInUse(int x, int y, int sx, int sy) {
      this.subImages[sx][sy].drawEmbedded((float)x, (float)y, (float)this.tw, (float)this.th);
   }

   public void endUse() {
      if (this.target == this) {
         super.endUse();
      } else {
         this.target.endUse();
      }
   }

   public void startUse() {
      if (this.target == this) {
         super.startUse();
      } else {
         this.target.startUse();
      }
   }

   public void setTexture(Texture texture) {
      if (this.target == this) {
         super.setTexture(texture);
      } else {
         this.target.setTexture(texture);
      }
   }
}
