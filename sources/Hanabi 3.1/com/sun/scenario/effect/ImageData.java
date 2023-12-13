package com.sun.scenario.effect;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.Renderer;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Iterator;

public class ImageData {
   private static HashSet alldatas;
   private ImageData sharedOwner;
   private FilterContext fctx;
   private int refcount;
   private Filterable image;
   private final Rectangle bounds;
   private BaseTransform transform;
   private Throwable fromwhere;
   private boolean reusable;

   public ImageData(FilterContext var1, Filterable var2, Rectangle var3) {
      this(var1, var2, var3, BaseTransform.IDENTITY_TRANSFORM);
   }

   public ImageData(FilterContext var1, Filterable var2, Rectangle var3, BaseTransform var4) {
      this.fctx = var1;
      this.refcount = 1;
      this.image = var2;
      this.bounds = var3;
      this.transform = var4;
      if (alldatas != null) {
         alldatas.add(this);
         this.fromwhere = new Throwable();
      }

   }

   public ImageData transform(BaseTransform var1) {
      if (var1.isIdentity()) {
         return this;
      } else {
         BaseTransform var2;
         if (this.transform.isIdentity()) {
            var2 = var1;
         } else {
            var2 = var1.copy().deriveWithConcatenation(this.transform);
         }

         return new ImageData(this, var2, this.bounds);
      }
   }

   private ImageData(ImageData var1, BaseTransform var2, Rectangle var3) {
      this(var1.fctx, var1.image, var3, var2);
      this.sharedOwner = var1;
   }

   public void setReusable(boolean var1) {
      if (this.sharedOwner != null) {
         throw new InternalError("cannot make a shared ImageData reusable");
      } else {
         this.reusable = var1;
      }
   }

   public FilterContext getFilterContext() {
      return this.fctx;
   }

   public Filterable getUntransformedImage() {
      return this.image;
   }

   public Rectangle getUntransformedBounds() {
      return this.bounds;
   }

   public BaseTransform getTransform() {
      return this.transform;
   }

   public Filterable getTransformedImage(Rectangle var1) {
      if (this.image != null && this.fctx != null) {
         if (this.transform.isIdentity()) {
            return this.image;
         } else {
            Rectangle var2 = this.getTransformedBounds(var1);
            return Renderer.getRenderer(this.fctx).transform(this.fctx, this.image, this.transform, this.bounds, var2);
         }
      } else {
         return null;
      }
   }

   public void releaseTransformedImage(Filterable var1) {
      if (this.fctx != null && var1 != null && var1 != this.image) {
         Effect.releaseCompatibleImage(this.fctx, var1);
      }

   }

   public Rectangle getTransformedBounds(Rectangle var1) {
      if (this.transform.isIdentity()) {
         return this.bounds;
      } else {
         Rectangle var2 = new Rectangle();
         this.transform.transform(this.bounds, var2);
         if (var1 != null) {
            var2.intersectWith(var1);
         }

         return var2;
      }
   }

   public int getReferenceCount() {
      return this.refcount;
   }

   public boolean addref() {
      if (this.reusable && this.refcount == 0 && this.image != null) {
         this.image.lock();
      }

      ++this.refcount;
      return this.image != null && !this.image.isLost();
   }

   public void unref() {
      if (--this.refcount == 0) {
         if (this.sharedOwner != null) {
            this.sharedOwner.unref();
            this.sharedOwner = null;
         } else if (this.fctx != null && this.image != null) {
            if (this.reusable) {
               this.image.unlock();
               return;
            }

            Effect.releaseCompatibleImage(this.fctx, this.image);
         }

         this.fctx = null;
         this.image = null;
         if (alldatas != null) {
            alldatas.remove(this);
         }
      }

   }

   public boolean validate(FilterContext var1) {
      return this.image != null && Renderer.getRenderer(var1).isImageDataCompatible(this);
   }

   public String toString() {
      return "ImageData{sharedOwner=" + this.sharedOwner + ", fctx=" + this.fctx + ", refcount=" + this.refcount + ", image=" + this.image + ", bounds=" + this.bounds + ", transform=" + this.transform + ", reusable=" + this.reusable + '}';
   }

   static {
      AccessController.doPrivileged(() -> {
         if (System.getProperty("decora.showleaks") != null) {
            alldatas = new HashSet();
            Runtime.getRuntime().addShutdownHook(new Thread() {
               public void run() {
                  Iterator var1 = ImageData.alldatas.iterator();

                  while(var1.hasNext()) {
                     ImageData var2 = (ImageData)var1.next();
                     Rectangle var3 = var2.getUntransformedBounds();
                     System.out.println("id[" + var3.width + "x" + var3.height + ", refcount=" + var2.refcount + "] leaked from:");
                     var2.fromwhere.printStackTrace(System.out);
                  }

               }
            });
         }

         return null;
      });
   }
}
