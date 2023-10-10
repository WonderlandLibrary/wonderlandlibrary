package net.minecraft.realms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class RealmsVertexFormat {
   private VertexFormat v;

   public int getOffset(int var1) {
      return this.v.func_181720_d(var1);
   }

   public List getElements() {
      ArrayList var1 = new ArrayList();
      Iterator var3 = this.v.getElements().iterator();

      while(var3.hasNext()) {
         VertexFormatElement var2 = (VertexFormatElement)var3.next();
         var1.add(new RealmsVertexFormatElement(var2));
      }

      return var1;
   }

   public int getColorOffset() {
      return this.v.getColorOffset();
   }

   public int getNormalOffset() {
      return this.v.getNormalOffset();
   }

   public boolean hasColor() {
      return this.v.hasColor();
   }

   public int getIntegerSize() {
      return this.v.func_181719_f();
   }

   public RealmsVertexFormatElement getElement(int var1) {
      return new RealmsVertexFormatElement(this.v.getElement(var1));
   }

   public int hashCode() {
      return this.v.hashCode();
   }

   public boolean hasUv(int var1) {
      return this.v.hasUvOffset(var1);
   }

   public boolean equals(Object var1) {
      return this.v.equals(var1);
   }

   public RealmsVertexFormat addElement(RealmsVertexFormatElement var1) {
      return this.from(this.v.func_181721_a(var1.getVertexFormatElement()));
   }

   public int getElementCount() {
      return this.v.getElementCount();
   }

   public RealmsVertexFormat(VertexFormat var1) {
      this.v = var1;
   }

   public RealmsVertexFormat from(VertexFormat var1) {
      this.v = var1;
      return this;
   }

   public int getVertexSize() {
      return this.v.getNextOffset();
   }

   public int getUvOffset(int var1) {
      return this.v.getUvOffsetById(var1);
   }

   public boolean hasNormal() {
      return this.v.hasNormal();
   }

   public String toString() {
      return this.v.toString();
   }

   public void clear() {
      this.v.clear();
   }

   public VertexFormat getVertexFormat() {
      return this.v;
   }
}
