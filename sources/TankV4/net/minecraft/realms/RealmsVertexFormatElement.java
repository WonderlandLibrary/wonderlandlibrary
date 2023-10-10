package net.minecraft.realms;

import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class RealmsVertexFormatElement {
   private VertexFormatElement v;

   public RealmsVertexFormatElement(VertexFormatElement var1) {
      this.v = var1;
   }

   public String toString() {
      return this.v.toString();
   }

   public int getCount() {
      return this.v.getElementCount();
   }

   public boolean equals(Object var1) {
      return this.v.equals(var1);
   }

   public boolean isPosition() {
      return this.v.isPositionElement();
   }

   public int getByteSize() {
      return this.v.getSize();
   }

   public int hashCode() {
      return this.v.hashCode();
   }

   public int getIndex() {
      return this.v.getIndex();
   }

   public VertexFormatElement getVertexFormatElement() {
      return this.v;
   }
}
