package de.violence.module.modules.RENDER;

import de.violence.module.Module;
import de.violence.module.ui.Category;
import de.violence.save.manager.FileManager;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public class BlockESP extends Module {
   public static ArrayList glow = new ArrayList();
   public static ArrayList blocks = new ArrayList();
   Thread updater = null;

   public BlockESP() {
      super("BlockESP", Category.RENDER);
   }

   public void updateESP() {
      if(blocks.isEmpty()) {
         glow.clear();
      } else {
         ArrayList newGlow = new ArrayList();

         for(int xoff = -59; xoff < 60; ++xoff) {
            for(int zoff = -59; zoff < 60; ++zoff) {
               for(int yoff = 0; yoff < 255; ++yoff) {
                  BlockPos block = new BlockPos(this.mc.thePlayer.posX + (double)xoff, (double)yoff, this.mc.thePlayer.posZ + (double)zoff);
                  if(blocks.contains(Integer.valueOf(Block.getIdFromBlock(this.mc.theWorld.getBlockState(block).getBlock())))) {
                     newGlow.add(block);
                  }
               }
            }
         }

         glow.clear();
         glow = newGlow;
      }
   }

   public void onEnable() {
      if(FileManager.toggledModules.containsValue("blockEsp_arrayList").booleanValue()) {
         ArrayList toSave = FileManager.toggledModules.getArrayList("blockEsp_arrayList");
         Iterator var3 = toSave.iterator();

         while(var3.hasNext()) {
            String ids = (String)var3.next();
            blocks.add(Integer.valueOf(Integer.parseInt(ids)));
         }
      }

      this.updater = new Thread(new Runnable() {
         public void run() {
            BlockESP.this.updateESP();

            while(BlockESP.this.isToggled()) {
               try {
                  Thread.sleep(5000L);
                  if(BlockESP.this.mc.theWorld != null && BlockESP.this.mc.theWorld.isRemote) {
                     BlockESP.this.updateESP();
                  }

                  ArrayList toSave = new ArrayList();
                  Iterator var3 = BlockESP.blocks.iterator();

                  while(var3.hasNext()) {
                     int ids = ((Integer)var3.next()).intValue();
                     toSave.add(String.valueOf(ids));
                  }

                  FileManager.toggledModules.setArrayList("blockEsp_arrayList", toSave);
                  FileManager.toggledModules.save();
               } catch (InterruptedException var4) {
                  ;
               }
            }

         }
      });
      this.updater.start();
      super.onEnable();
   }

   public void onDisable() {
      try {
         super.onDisable();
         this.updater.interrupt();
         this.updater = null;
      } catch (Exception var2) {
         ;
      }

   }

   public void onFrameRender() {
      Iterator var2 = ((ArrayList)glow.clone()).iterator();

      while(var2.hasNext()) {
         BlockPos pos = (BlockPos)var2.next();
         drawBox(pos);
      }

      super.onFrameRender();
   }

   public static void drawBox(BlockPos blockPos) {
      if(blockPos != null) {
         double var10000 = blockPos.getX();
         Minecraft.getMinecraft().getRenderManager();
         double x = var10000 - RenderManager.renderPosX;
         var10000 = blockPos.getY();
         Minecraft.getMinecraft().getRenderManager();
         double y = var10000 - RenderManager.renderPosY;
         var10000 = blockPos.getZ();
         Minecraft.getMinecraft().getRenderManager();
         double z = var10000 - RenderManager.renderPosZ;
         GL11.glEnable(3042);
         GL11.glLineWidth(1.0F);
         GL11.glColor4f(0.2F, 0.3F, 1.0F, 1.0F);
         GL11.glDisable(3553);
         GL11.glDisable(2929);
         GL11.glDepthMask(false);
         RenderGlobal.func_181561_a(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
         GL11.glEnable(3553);
         GL11.glEnable(2929);
         GL11.glDepthMask(true);
         GL11.glDisable(3042);
      }
   }
}
