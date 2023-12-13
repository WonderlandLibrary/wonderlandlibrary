package net.minecraft.client.renderer;

import net.minecraft.util.EnumFacing;

public enum EnumFaceDirection {
   SOUTH(new EnumFaceDirection.VertexInformation[]{new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX, EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX, EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX, EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX, EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX, (EnumFaceDirection.VertexInformation)null)}),
   NORTH(new EnumFaceDirection.VertexInformation[]{new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX, EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.NORTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX, EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.NORTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX, EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.NORTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX, EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.NORTH_INDEX, (EnumFaceDirection.VertexInformation)null)}),
   EAST(new EnumFaceDirection.VertexInformation[]{new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX, EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX, EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX, EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.NORTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX, EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.NORTH_INDEX, (EnumFaceDirection.VertexInformation)null)});

   private final EnumFaceDirection.VertexInformation[] vertexInfos;
   UP(new EnumFaceDirection.VertexInformation[]{new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX, EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.NORTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX, EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX, EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX, EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.NORTH_INDEX, (EnumFaceDirection.VertexInformation)null)}),
   DOWN(new EnumFaceDirection.VertexInformation[]{new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX, EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX, EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.NORTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX, EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.NORTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX, EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX, (EnumFaceDirection.VertexInformation)null)});

   private static final EnumFaceDirection[] ENUM$VALUES = new EnumFaceDirection[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
   private static final EnumFaceDirection[] facings = new EnumFaceDirection[6];
   WEST(new EnumFaceDirection.VertexInformation[]{new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX, EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.NORTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX, EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.NORTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX, EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX, (EnumFaceDirection.VertexInformation)null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX, EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX, (EnumFaceDirection.VertexInformation)null)});

   static {
      facings[EnumFaceDirection.Constants.DOWN_INDEX] = DOWN;
      facings[EnumFaceDirection.Constants.UP_INDEX] = UP;
      facings[EnumFaceDirection.Constants.NORTH_INDEX] = NORTH;
      facings[EnumFaceDirection.Constants.SOUTH_INDEX] = SOUTH;
      facings[EnumFaceDirection.Constants.WEST_INDEX] = WEST;
      facings[EnumFaceDirection.Constants.EAST_INDEX] = EAST;
   }

   public EnumFaceDirection.VertexInformation func_179025_a(int var1) {
      return this.vertexInfos[var1];
   }

   private EnumFaceDirection(EnumFaceDirection.VertexInformation[] var3) {
      this.vertexInfos = var3;
   }

   public static EnumFaceDirection getFacing(EnumFacing var0) {
      return facings[var0.getIndex()];
   }

   public static final class Constants {
      public static final int DOWN_INDEX;
      public static final int WEST_INDEX;
      public static final int NORTH_INDEX;
      public static final int EAST_INDEX;
      public static final int SOUTH_INDEX;
      public static final int UP_INDEX;

      static {
         SOUTH_INDEX = EnumFacing.SOUTH.getIndex();
         UP_INDEX = EnumFacing.UP.getIndex();
         EAST_INDEX = EnumFacing.EAST.getIndex();
         NORTH_INDEX = EnumFacing.NORTH.getIndex();
         DOWN_INDEX = EnumFacing.DOWN.getIndex();
         WEST_INDEX = EnumFacing.WEST.getIndex();
      }
   }

   public static class VertexInformation {
      public final int field_179183_c;
      public final int field_179182_b;
      public final int field_179184_a;

      private VertexInformation(int var1, int var2, int var3) {
         this.field_179184_a = var1;
         this.field_179182_b = var2;
         this.field_179183_c = var3;
      }

      VertexInformation(int var1, int var2, int var3, EnumFaceDirection.VertexInformation var4) {
         this(var1, var2, var3);
      }
   }
}
