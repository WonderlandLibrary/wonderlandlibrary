package net.minecraft.client.renderer.vertex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VertexFormatElement {
   private final VertexFormatElement.EnumUsage usage;
   private int index;
   private static final Logger LOGGER = LogManager.getLogger();
   private int elementCount;
   private final VertexFormatElement.EnumType type;

   public final int getElementCount() {
      return this.elementCount;
   }

   public final int getSize() {
      return this.type.getSize() * this.elementCount;
   }

   public String toString() {
      return this.elementCount + "," + this.usage.getDisplayName() + "," + this.type.getDisplayName();
   }

   public final VertexFormatElement.EnumUsage getUsage() {
      return this.usage;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         VertexFormatElement var2 = (VertexFormatElement)var1;
         return this.elementCount != var2.elementCount ? false : (this.index != var2.index ? false : (this.type != var2.type ? false : this.usage == var2.usage));
      } else {
         return false;
      }
   }

   public VertexFormatElement(int var1, VertexFormatElement.EnumType var2, VertexFormatElement.EnumUsage var3, int var4) {
      if (var3 != false) {
         LOGGER.warn("Multiple vertex elements of the same type other than UVs are not supported. Forcing type to UV.");
         this.usage = VertexFormatElement.EnumUsage.UV;
      } else {
         this.usage = var3;
      }

      this.type = var2;
      this.index = var1;
      this.elementCount = var4;
   }

   public final int getIndex() {
      return this.index;
   }

   public int hashCode() {
      int var1 = this.type.hashCode();
      var1 = 31 * var1 + this.usage.hashCode();
      var1 = 31 * var1 + this.index;
      var1 = 31 * var1 + this.elementCount;
      return var1;
   }

   public final boolean isPositionElement() {
      return this.usage == VertexFormatElement.EnumUsage.POSITION;
   }

   public final VertexFormatElement.EnumType getType() {
      return this.type;
   }

   public static enum EnumType {
      FLOAT(4, "Float", 5126),
      INT(4, "Int", 5124),
      UINT(4, "Unsigned Int", 5125);

      private final String displayName;
      USHORT(2, "Unsigned Short", 5123),
      SHORT(2, "Short", 5122),
      BYTE(1, "Byte", 5120);

      private final int glConstant;
      private static final VertexFormatElement.EnumType[] ENUM$VALUES = new VertexFormatElement.EnumType[]{FLOAT, UBYTE, BYTE, USHORT, SHORT, UINT, INT};
      private final int size;
      UBYTE(1, "Unsigned Byte", 5121);

      public int getSize() {
         return this.size;
      }

      private EnumType(int var3, String var4, int var5) {
         this.size = var3;
         this.displayName = var4;
         this.glConstant = var5;
      }

      public String getDisplayName() {
         return this.displayName;
      }

      public int getGlConstant() {
         return this.glConstant;
      }
   }

   public static enum EnumUsage {
      BLEND_WEIGHT("Blend Weight"),
      POSITION("Position"),
      UV("UV"),
      MATRIX("Bone Matrix"),
      PADDING("Padding"),
      COLOR("Vertex Color");

      private static final VertexFormatElement.EnumUsage[] ENUM$VALUES = new VertexFormatElement.EnumUsage[]{POSITION, NORMAL, COLOR, UV, MATRIX, BLEND_WEIGHT, PADDING};
      NORMAL("Normal");

      private final String displayName;

      private EnumUsage(String var3) {
         this.displayName = var3;
      }

      public String getDisplayName() {
         return this.displayName;
      }
   }
}
