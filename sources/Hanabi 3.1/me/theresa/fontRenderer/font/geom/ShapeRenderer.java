package me.theresa.fontRenderer.font.geom;

import me.theresa.fontRenderer.font.Image;
import me.theresa.fontRenderer.font.impl.ShapeFill;
import me.theresa.fontRenderer.font.opengl.Texture;
import me.theresa.fontRenderer.font.opengl.TextureImpl;
import me.theresa.fontRenderer.font.opengl.renderer.LineStripRenderer;
import me.theresa.fontRenderer.font.opengl.renderer.Renderer;
import me.theresa.fontRenderer.font.opengl.renderer.SGL;

public final class ShapeRenderer {
   private static final SGL GL = Renderer.get();
   private static final LineStripRenderer LSR = Renderer.getLineStripRenderer();

   public static void draw(Shape shape) {
      Texture t = TextureImpl.getLastBind();
      TextureImpl.bindNone();
      float[] points = shape.getPoints();
      LSR.start();

      for(int i = 0; i < points.length; i += 2) {
         LSR.vertex(points[i], points[i + 1]);
      }

      if (shape.closed()) {
         LSR.vertex(points[0], points[1]);
      }

      LSR.end();
      if (t == null) {
         TextureImpl.bindNone();
      } else {
         t.bind();
      }

   }

   public static void draw(Shape shape, ShapeFill fill) {
      float[] points = shape.getPoints();
      Texture t = TextureImpl.getLastBind();
      TextureImpl.bindNone();
      float[] center = shape.getCenter();
      GL.glBegin(3);

      for(int i = 0; i < points.length; i += 2) {
         fill.colorAt(shape, points[i], points[i + 1]).bind();
         Vector2f offset = fill.getOffsetAt(shape, points[i], points[i + 1]);
         GL.glVertex2f(points[i] + offset.x, points[i + 1] + offset.y);
      }

      if (shape.closed()) {
         fill.colorAt(shape, points[0], points[1]).bind();
         Vector2f offset = fill.getOffsetAt(shape, points[0], points[1]);
         GL.glVertex2f(points[0] + offset.x, points[1] + offset.y);
      }

      GL.glEnd();
      if (t == null) {
         TextureImpl.bindNone();
      } else {
         t.bind();
      }

   }

   public static boolean validFill(Shape shape) {
      if (shape.getTriangles() == null) {
         return true;
      } else {
         return shape.getTriangles().getTriangleCount() == 0;
      }
   }

   public static void fill(Shape shape) {
      if (!validFill(shape)) {
         Texture t = TextureImpl.getLastBind();
         TextureImpl.bindNone();
         fill(shape, (shape1, x, y) -> {
            return null;
         });
         if (t == null) {
            TextureImpl.bindNone();
         } else {
            t.bind();
         }

      }
   }

   private static void fill(Shape shape, PointCallback callback) {
      Triangulator tris = shape.getTriangles();
      GL.glBegin(4);

      for(int i = 0; i < tris.getTriangleCount(); ++i) {
         for(int p = 0; p < 3; ++p) {
            float[] pt = tris.getTrianglePoint(i, p);
            float[] np = callback.preRenderPoint(shape, pt[0], pt[1]);
            if (np == null) {
               GL.glVertex2f(pt[0], pt[1]);
            } else {
               GL.glVertex2f(np[0], np[1]);
            }
         }
      }

      GL.glEnd();
   }

   public static void texture(Shape shape, Image image) {
      texture(shape, image, 0.01F, 0.01F);
   }

   public static void textureFit(Shape shape, Image image) {
      textureFit(shape, image, 1.0F, 1.0F);
   }

   public static void texture(Shape shape, Image image, float scaleX, float scaleY) {
      if (!validFill(shape)) {
         Texture t = TextureImpl.getLastBind();
         image.getTexture().bind();
         fill(shape, (shape1, x, y) -> {
            float tx = x * scaleX;
            float ty = y * scaleY;
            tx = image.getTextureOffsetX() + image.getTextureWidth() * tx;
            ty = image.getTextureOffsetY() + image.getTextureHeight() * ty;
            GL.glTexCoord2f(tx, ty);
            return null;
         });
         float[] points = shape.getPoints();
         if (t == null) {
            TextureImpl.bindNone();
         } else {
            t.bind();
         }

      }
   }

   public static void textureFit(Shape shape, Image image, float scaleX, float scaleY) {
      if (!validFill(shape)) {
         float[] points = shape.getPoints();
         Texture t = TextureImpl.getLastBind();
         image.getTexture().bind();
         float minX = shape.getX();
         float minY = shape.getY();
         float maxX = shape.getMaxX() - minX;
         float maxY = shape.getMaxY() - minY;
         fill(shape, (shape1, x, y) -> {
            x -= shape1.getMinX();
            y -= shape1.getMinY();
            x /= shape1.getMaxX() - shape1.getMinX();
            y /= shape1.getMaxY() - shape1.getMinY();
            float tx = x * scaleX;
            float ty = y * scaleY;
            tx = image.getTextureOffsetX() + image.getTextureWidth() * tx;
            ty = image.getTextureOffsetY() + image.getTextureHeight() * ty;
            GL.glTexCoord2f(tx, ty);
            return null;
         });
         if (t == null) {
            TextureImpl.bindNone();
         } else {
            t.bind();
         }

      }
   }

   public static void fill(Shape shape, ShapeFill fill) {
      if (!validFill(shape)) {
         Texture t = TextureImpl.getLastBind();
         TextureImpl.bindNone();
         float[] center = shape.getCenter();
         fill(shape, (shape1, x, y) -> {
            fill.colorAt(shape1, x, y).bind();
            Vector2f offset = fill.getOffsetAt(shape1, x, y);
            return new float[]{offset.x + x, offset.y + y};
         });
         if (t == null) {
            TextureImpl.bindNone();
         } else {
            t.bind();
         }

      }
   }

   public static void texture(Shape shape, Image image, float scaleX, float scaleY, ShapeFill fill) {
      if (!validFill(shape)) {
         Texture t = TextureImpl.getLastBind();
         image.getTexture().bind();
         float[] center = shape.getCenter();
         fill(shape, (shape1, x, y) -> {
            fill.colorAt(shape1, x - center[0], y - center[1]).bind();
            Vector2f offset = fill.getOffsetAt(shape1, x, y);
            x += offset.x;
            y += offset.y;
            float tx = x * scaleX;
            float ty = y * scaleY;
            tx = image.getTextureOffsetX() + image.getTextureWidth() * tx;
            ty = image.getTextureOffsetY() + image.getTextureHeight() * ty;
            GL.glTexCoord2f(tx, ty);
            return new float[]{offset.x + x, offset.y + y};
         });
         if (t == null) {
            TextureImpl.bindNone();
         } else {
            t.bind();
         }

      }
   }

   public static void texture(Shape shape, Image image, TexCoordGenerator gen) {
      Texture t = TextureImpl.getLastBind();
      image.getTexture().bind();
      float[] center = shape.getCenter();
      fill(shape, (shape1, x, y) -> {
         Vector2f tex = gen.getCoordFor(x, y);
         GL.glTexCoord2f(tex.x, tex.y);
         return new float[]{x, y};
      });
      if (t == null) {
         TextureImpl.bindNone();
      } else {
         t.bind();
      }

   }

   private interface PointCallback {
      float[] preRenderPoint(Shape var1, float var2, float var3);
   }
}
