package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;

public class CompiledChunk {
   private SetVisibility setVisibility = new SetVisibility();
   private boolean empty = true;
   private final boolean[] layersStarted = new boolean[EnumWorldBlockLayer.values().length];
   private final List tileEntities = Lists.newArrayList();
   private final boolean[] layersUsed = new boolean[EnumWorldBlockLayer.values().length];
   private WorldRenderer.State state;
   public static final CompiledChunk DUMMY = new CompiledChunk() {
      public void setLayerStarted(EnumWorldBlockLayer var1) {
         throw new UnsupportedOperationException();
      }

      public boolean isVisible(EnumFacing var1, EnumFacing var2) {
         return false;
      }

      protected void setLayerUsed(EnumWorldBlockLayer var1) {
         throw new UnsupportedOperationException();
      }
   };

   protected void setLayerUsed(EnumWorldBlockLayer var1) {
      this.empty = false;
      this.layersUsed[var1.ordinal()] = true;
   }

   public void setState(WorldRenderer.State var1) {
      this.state = var1;
   }

   public void addTileEntity(TileEntity var1) {
      this.tileEntities.add(var1);
   }

   public void setLayerStarted(EnumWorldBlockLayer var1) {
      this.layersStarted[var1.ordinal()] = true;
   }

   public WorldRenderer.State getState() {
      return this.state;
   }

   public List getTileEntities() {
      return this.tileEntities;
   }

   public boolean isLayerStarted(EnumWorldBlockLayer var1) {
      return this.layersStarted[var1.ordinal()];
   }

   public void setVisibility(SetVisibility var1) {
      this.setVisibility = var1;
   }

   public boolean isLayerEmpty(EnumWorldBlockLayer var1) {
      return !this.layersUsed[var1.ordinal()];
   }

   public boolean isEmpty() {
      return this.empty;
   }

   public boolean isVisible(EnumFacing var1, EnumFacing var2) {
      return this.setVisibility.isVisible(var1, var2);
   }
}
