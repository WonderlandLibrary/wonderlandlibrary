package net.minecraft.client.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import optifine.Reflector;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GuiOverlayDebug extends Gui {
   private final FontRenderer fontRenderer;
   private static final String __OBFID = "CL_00001956";
   private final Minecraft mc;

   private int func_181552_c(int var1, int var2, int var3, int var4) {
      return var1 < var3 ? this.func_181553_a(-16711936, -256, (float)var1 / (float)var3) : this.func_181553_a(-256, -65536, (float)(var1 - var3) / (float)(var4 - var3));
   }

   private void func_181554_e() {
      GlStateManager.disableDepth();
      FrameTimer var1 = this.mc.func_181539_aj();
      int var2 = var1.func_181749_a();
      int var3 = var1.func_181750_b();
      long[] var4 = var1.func_181746_c();
      new ScaledResolution(this.mc);
      int var6 = var2;
      int var7 = 0;
      drawRect(0.0D, (double)(ScaledResolution.getScaledHeight() - 60), 240.0D, (double)ScaledResolution.getScaledHeight(), -1873784752);

      while(var6 != var3) {
         int var8 = var1.func_181748_a(var4[var6], 30);
         int var9 = this.func_181552_c(MathHelper.clamp_int(var8, 0, 60), 0, 30, 60);
         drawVerticalLine(var7, ScaledResolution.getScaledHeight(), ScaledResolution.getScaledHeight() - var8, var9);
         ++var7;
         var6 = var1.func_181751_b(var6 + 1);
      }

      drawRect(1.0D, (double)(ScaledResolution.getScaledHeight() - 30 + 1), 14.0D, (double)(ScaledResolution.getScaledHeight() - 30 + 10), -1873784752);
      this.fontRenderer.drawString("60", 2.0D, (double)(ScaledResolution.getScaledHeight() - 30 + 2), 14737632);
      drawHorizontalLine(0, 239, ScaledResolution.getScaledHeight() - 30, -1);
      drawRect(1.0D, (double)(ScaledResolution.getScaledHeight() - 60 + 1), 14.0D, (double)(ScaledResolution.getScaledHeight() - 60 + 10), -1873784752);
      this.fontRenderer.drawString("30", 2.0D, (double)(ScaledResolution.getScaledHeight() - 60 + 2), 14737632);
      drawHorizontalLine(0, 239, ScaledResolution.getScaledHeight() - 60, -1);
      drawHorizontalLine(0, 239, ScaledResolution.getScaledHeight() - 1, -1);
      drawVerticalLine(0, ScaledResolution.getScaledHeight() - 60, ScaledResolution.getScaledHeight(), -1);
      drawVerticalLine(239, ScaledResolution.getScaledHeight() - 60, ScaledResolution.getScaledHeight(), -1);
      if (this.mc.gameSettings.limitFramerate <= 120) {
         drawHorizontalLine(0, 239, ScaledResolution.getScaledHeight() - 60 + this.mc.gameSettings.limitFramerate / 2, -16711681);
      }

      GlStateManager.enableDepth();
   }

   protected void renderDebugInfoLeft() {
      List var1 = this.call();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         String var3 = (String)var1.get(var2);
         if (!Strings.isNullOrEmpty(var3)) {
            int var4 = this.fontRenderer.FONT_HEIGHT;
            int var5 = this.fontRenderer.getStringWidth(var3);
            boolean var6 = true;
            int var7 = 2 + var4 * var2;
            drawRect(1.0D, (double)(var7 - 1), (double)(2 + var5 + 1), (double)(var7 + var4 - 1), -1873784752);
            this.fontRenderer.drawString(var3, 2.0D, (double)var7, 14737632);
         }
      }

   }

   protected List getDebugInfoRight() {
      long var1 = Runtime.getRuntime().maxMemory();
      long var3 = Runtime.getRuntime().totalMemory();
      long var5 = Runtime.getRuntime().freeMemory();
      long var7 = var3 - var5;
      ArrayList var9 = Lists.newArrayList((Object[])(String.format("Java: %s %dbit", System.getProperty("java.version"), this.mc.isJava64bit() ? 64 : 32), String.format("Mem: % 2d%% %03d/%03dMB", var7 * 100L / var1, bytesToMb(var7), bytesToMb(var1)), String.format("Allocated: % 2d%% %03dMB", var3 * 100L / var1, bytesToMb(var3)), "", String.format("CPU: %s", OpenGlHelper.func_183029_j()), "", String.format("Display: %dx%d (%s)", Display.getWidth(), Display.getHeight(), GL11.glGetString(7936)), GL11.glGetString(7937), GL11.glGetString(7938)));
      if (Reflector.FMLCommonHandler_getBrandings.exists()) {
         Object var10 = Reflector.call(Reflector.FMLCommonHandler_instance);
         var9.add("");
         var9.addAll((Collection)Reflector.call(var10, Reflector.FMLCommonHandler_getBrandings, false));
      }

      if (this != false) {
         return var9;
      } else {
         if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            BlockPos var15 = this.mc.objectMouseOver.getBlockPos();
            IBlockState var11 = Minecraft.theWorld.getBlockState(var15);
            if (Minecraft.theWorld.getWorldType() != WorldType.DEBUG_WORLD) {
               var11 = var11.getBlock().getActualState(var11, Minecraft.theWorld, var15);
            }

            var9.add("");
            var9.add(String.valueOf(Block.blockRegistry.getNameForObject(var11.getBlock())));

            Entry var12;
            String var13;
            for(UnmodifiableIterator var14 = var11.getProperties().entrySet().iterator(); var14.hasNext(); var9.add(((IProperty)var12.getKey()).getName() + ": " + var13)) {
               var12 = (Entry)var14.next();
               var13 = ((Comparable)var12.getValue()).toString();
               if (var12.getValue() == Boolean.TRUE) {
                  var13 = EnumChatFormatting.GREEN + var13;
               } else if (var12.getValue() == Boolean.FALSE) {
                  var13 = EnumChatFormatting.RED + var13;
               }
            }
         }

         return var9;
      }
   }

   private static long bytesToMb(long var0) {
      return var0 / 1024L / 1024L;
   }

   protected void renderDebugInfoRight(ScaledResolution var1) {
      List var2 = this.getDebugInfoRight();

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         String var4 = (String)var2.get(var3);
         if (!Strings.isNullOrEmpty(var4)) {
            int var5 = this.fontRenderer.FONT_HEIGHT;
            int var6 = this.fontRenderer.getStringWidth(var4);
            int var7 = var1.getScaledWidth() - 2 - var6;
            int var8 = 2 + var5 * var3;
            drawRect((double)(var7 - 1), (double)(var8 - 1), (double)(var7 + var6 + 1), (double)(var8 + var5 - 1), -1873784752);
            this.fontRenderer.drawString(var4, (double)var7, (double)var8, 14737632);
         }
      }

   }

   public void renderDebugInfo(ScaledResolution var1) {
      this.mc.mcProfiler.startSection("debug");
      GlStateManager.pushMatrix();
      this.renderDebugInfoLeft();
      this.renderDebugInfoRight(var1);
      GlStateManager.popMatrix();
      this.mc.mcProfiler.endSection();
   }

   private int func_181553_a(int var1, int var2, float var3) {
      int var4 = var1 >> 24 & 255;
      int var5 = var1 >> 16 & 255;
      int var6 = var1 >> 8 & 255;
      int var7 = var1 & 255;
      int var8 = var2 >> 24 & 255;
      int var9 = var2 >> 16 & 255;
      int var10 = var2 >> 8 & 255;
      int var11 = var2 & 255;
      int var12 = MathHelper.clamp_int((int)((float)var4 + (float)(var8 - var4) * var3), 0, 255);
      int var13 = MathHelper.clamp_int((int)((float)var5 + (float)(var9 - var5) * var3), 0, 255);
      int var14 = MathHelper.clamp_int((int)((float)var6 + (float)(var10 - var6) * var3), 0, 255);
      int var15 = MathHelper.clamp_int((int)((float)var7 + (float)(var11 - var7) * var3), 0, 255);
      return var12 << 24 | var13 << 16 | var14 << 8 | var15;
   }

   protected List call() {
      BlockPos var1 = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
      if (this == false) {
         return Lists.newArrayList((Object[])("Minecraft 1.8.8 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + Minecraft.theWorld.getDebugLoadedEntities(), Minecraft.theWorld.getProviderName(), "", String.format("Chunk-relative: %d %d %d", var1.getX() & 15, var1.getY() & 15, var1.getZ() & 15)));
      } else {
         Entity var2 = this.mc.getRenderViewEntity();
         EnumFacing var3 = var2.getHorizontalFacing();
         String var4 = "Invalid";
         switch(var3) {
         case NORTH:
            var4 = "Towards negative Z";
            break;
         case SOUTH:
            var4 = "Towards positive Z";
            break;
         case WEST:
            var4 = "Towards negative X";
            break;
         case EAST:
            var4 = "Towards positive X";
         }

         ArrayList var5 = Lists.newArrayList((Object[])("Minecraft 1.8.8 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + Minecraft.theWorld.getDebugLoadedEntities(), Minecraft.theWorld.getProviderName(), "", String.format("XYZ: %.3f / %.5f / %.3f", this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ), String.format("Block: %d %d %d", var1.getX(), var1.getY(), var1.getZ()), String.format("Chunk: %d %d %d in %d %d %d", var1.getX() & 15, var1.getY() & 15, var1.getZ() & 15, var1.getX() >> 4, var1.getY() >> 4, var1.getZ() >> 4), String.format("Facing: %s (%s) (%.1f / %.1f)", var3, var4, MathHelper.wrapAngleTo180_float(var2.rotationYaw), MathHelper.wrapAngleTo180_float(var2.rotationPitch))));
         if (Minecraft.theWorld != null && Minecraft.theWorld.isBlockLoaded(var1)) {
            Chunk var6 = Minecraft.theWorld.getChunkFromBlockCoords(var1);
            var5.add("Biome: " + var6.getBiome(var1, Minecraft.theWorld.getWorldChunkManager()).biomeName);
            var5.add("Light: " + var6.getLightSubtracted(var1, 0) + " (" + var6.getLightFor(EnumSkyBlock.SKY, var1) + " sky, " + var6.getLightFor(EnumSkyBlock.BLOCK, var1) + " block)");
            DifficultyInstance var7 = Minecraft.theWorld.getDifficultyForLocation(var1);
            if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
               EntityPlayerMP var8 = this.mc.getIntegratedServer().getConfigurationManager().getPlayerByUUID(Minecraft.thePlayer.getUniqueID());
               if (var8 != null) {
                  var7 = var8.worldObj.getDifficultyForLocation(new BlockPos(var8));
               }
            }

            var5.add(String.format("Local Difficulty: %.2f (Day %d)", var7.getAdditionalDifficulty(), Minecraft.theWorld.getWorldTime() / 24000L));
         }

         if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
            var5.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
         }

         if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            BlockPos var9 = this.mc.objectMouseOver.getBlockPos();
            var5.add(String.format("Looking at: %d %d %d", var9.getX(), var9.getY(), var9.getZ()));
         }

         return var5;
      }
   }

   public GuiOverlayDebug(Minecraft var1) {
      this.mc = var1;
      this.fontRenderer = Minecraft.fontRendererObj;
   }

   static final class GuiOverlayDebug$1 {
      private static final String __OBFID = "CL_00001955";
      static final int[] field_178907_a = new int[EnumFacing.values().length];

      static {
         try {
            field_178907_a[EnumFacing.NORTH.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
         }

         try {
            field_178907_a[EnumFacing.SOUTH.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            field_178907_a[EnumFacing.WEST.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
         }

         try {
            field_178907_a[EnumFacing.EAST.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
         }

      }
   }
}
