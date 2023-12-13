package org.newdawn.slick.svg;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.geom.TexCoordGenerator;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

public class SimpleDiagramRenderer {
   protected static SGL GL = Renderer.get();
   public Diagram diagram;
   public int list = -1;

   public SimpleDiagramRenderer(Diagram var1) {
      this.diagram = var1;
   }

   public void render(Graphics var1) {
      if (this.list == -1) {
         this.list = GL.glGenLists(1);
         GL.glNewList(this.list, 4864);
         render(var1, this.diagram);
         GL.glEndList();
      }

      GL.glCallList(this.list);
      TextureImpl.bindNone();
   }

   public static void render(Graphics var0, Diagram var1) {
      for(int var2 = 0; var2 < var1.getFigureCount(); ++var2) {
         Figure var3 = var1.getFigure(var2);
         if (var3.getData().isFilled()) {
            if (var3.getData().isColor("fill")) {
               var0.setColor(var3.getData().getAsColor("fill"));
               var0.fill(var1.getFigure(var2).getShape());
               var0.setAntiAlias(true);
               var0.draw(var1.getFigure(var2).getShape());
               var0.setAntiAlias(false);
            }

            String var4 = var3.getData().getAsReference("fill");
            if (var1.getPatternDef(var4) != null) {
               System.out.println("PATTERN");
            }

            if (var1.getGradient(var4) != null) {
               Gradient var5 = var1.getGradient(var4);
               Shape var6 = var1.getFigure(var2).getShape();
               Object var7 = null;
               if (var5.isRadial()) {
                  var7 = new RadialGradientFill(var6, var1.getFigure(var2).getTransform(), var5);
               } else {
                  var7 = new LinearGradientFill(var6, var1.getFigure(var2).getTransform(), var5);
               }

               Color.white.bind();
               ShapeRenderer.texture(var6, var5.getImage(), (TexCoordGenerator)var7);
            }
         }

         if (var3.getData().isStroked() && var3.getData().isColor("stroke")) {
            var0.setColor(var3.getData().getAsColor("stroke"));
            var0.setLineWidth(var3.getData().getAsFloat("stroke-width"));
            var0.setAntiAlias(true);
            var0.draw(var1.getFigure(var2).getShape());
            var0.setAntiAlias(false);
            var0.resetLineWidth();
         }
      }

   }
}
