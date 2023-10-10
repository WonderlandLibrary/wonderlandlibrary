package net.minecraft.realms;

import net.minecraft.client.renderer.Tessellator;

public class Tezzelator {
   public static Tessellator t = Tessellator.getInstance();
   public static final Tezzelator instance = new Tezzelator();

   public void end() {
      t.draw();
   }

   public void tex2(short var1, short var2) {
      t.getWorldRenderer().lightmap(var1, var2);
   }

   public void color(float var1, float var2, float var3, float var4) {
      t.getWorldRenderer().color(var1, var2, var3, var4);
   }

   public void normal(float var1, float var2, float var3) {
      t.getWorldRenderer().normal(var1, var2, var3);
   }

   public RealmsBufferBuilder color(int var1, int var2, int var3, int var4) {
      return new RealmsBufferBuilder(t.getWorldRenderer().color(var1, var2, var3, var4));
   }

   public void endVertex() {
      t.getWorldRenderer().endVertex();
   }

   public void begin(int var1, RealmsVertexFormat var2) {
      t.getWorldRenderer().begin(var1, var2.getVertexFormat());
   }

   public void offset(double var1, double var3, double var5) {
      t.getWorldRenderer().setTranslation(var1, var3, var5);
   }

   public Tezzelator tex(double var1, double var3) {
      t.getWorldRenderer().tex(var1, var3);
      return this;
   }

   public Tezzelator vertex(double var1, double var3, double var5) {
      t.getWorldRenderer().pos(var1, var3, var5);
      return this;
   }
}
