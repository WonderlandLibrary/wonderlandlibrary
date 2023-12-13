package org.alphacentauri.modules;

import java.util.ArrayList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.BlockPos;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventRender3D;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.RenderUtils;
import org.alphacentauri.modules.ModuleChestAura;
import org.lwjgl.opengl.GL11;

public class ModuleChestESP extends Module implements EventListener {
   private Property design = new Property(this, "Design", ModuleChestESP.Design.Block);

   public ModuleChestESP() {
      super("ChestESP", "Show\'s nearby chests", new String[]{"chestesp"}, Module.Category.Render, 7120331);
   }

   public void onEvent(Event event) {
      if(event instanceof EventRender3D) {
         double viewerPosX = AC.getMC().getRenderManager().viewerPosX;
         double viewerPosY = AC.getMC().getRenderManager().viewerPosY;
         double viewerPosZ = AC.getMC().getRenderManager().viewerPosZ;
         double line_lenght = 0.35D;
         ArrayList<BlockPos> dontRender = new ArrayList();
         Module module = AC.getModuleManager().get(ModuleChestAura.class);
         if(module.isEnabled()) {
            dontRender.addAll(((ModuleChestAura)module).clicked);
         }

         for(TileEntity tileEntity : AC.getMC().getWorld().loadedTileEntityList) {
            if(tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityEnderChest) {
               BlockPos pos = tileEntity.getPos();
               if(!dontRender.contains(pos)) {
                  if(this.design.value == ModuleChestESP.Design.Star) {
                     double x = -(viewerPosX - (double)pos.getX()) + 0.5D;
                     double y = -(viewerPosY - (double)pos.getY()) + 0.5D;
                     double z = -(viewerPosZ - (double)pos.getZ()) + 0.5D;
                     GL11.glPushMatrix();
                     GL11.glBlendFunc(770, 771);
                     GL11.glEnable(3042);
                     GL11.glLineWidth(1.0F);
                     GL11.glDisable(3553);
                     GL11.glDisable(2929);
                     GL11.glDepthMask(true);
                     GL11.glColor3f(1.0F, 1.0F, 1.0F);
                     GL11.glBegin(1);
                     GL11.glVertex3d(x - line_lenght, y, z);
                     GL11.glVertex3d(x + line_lenght, y, z);
                     GL11.glVertex3d(x, y + line_lenght, z);
                     GL11.glVertex3d(x, y - line_lenght, z);
                     GL11.glVertex3d(x, y, z + line_lenght);
                     GL11.glVertex3d(x, y, z - line_lenght);
                     GL11.glEnd();
                     GL11.glEnable(3553);
                     GL11.glEnable(2929);
                     GL11.glDepthMask(true);
                     GL11.glDisable(3042);
                     GL11.glPopMatrix();
                  } else {
                     RenderUtils.blockESP(pos, 11534127, true);
                  }
               }
            }
         }
      }

   }

   public static enum Design {
      Block,
      Star;
   }
}
