package me.yarukon.hud.window.impl;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Wrapper;
import cn.hanabi.modules.modules.world.Teams;
import cn.hanabi.utils.Colors;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.value.Value;
import java.util.Iterator;
import me.yarukon.YRenderUtil;
import me.yarukon.hud.window.HudWindow;
import net.minecraft.entity.player.EntityPlayer;

@ObfuscationClass
public class WindowRadar extends HudWindow {
   public Value scale = new Value("HudWindow", "Radar Scale", 2.0, 0.0, 20.0, 0.1);

   public WindowRadar() {
      super("Radar", 5.0F, 25.0F, 100.0F, 100.0F, "Radar", "", 12.0F, 0.0F, 1.0F, true, 100.0F, 100.0F);
   }

   public void draw() {
      super.draw();
      float xOffset = this.x;
      float yOffset = this.y + this.draggableHeight;
      float playerOffsetX = (float)this.mc.thePlayer.posX;
      float playerOffSetZ = (float)this.mc.thePlayer.posZ;
      YRenderUtil.drawRectNormal(xOffset + (this.width / 2.0F - 0.5F), yOffset + 3.5F, xOffset + this.width / 2.0F + 0.5F, yOffset + this.height - 3.5F, 1358954495);
      YRenderUtil.drawRectNormal(xOffset + 3.5F, yOffset + (this.height / 2.0F - 0.5F), xOffset + this.width - 3.5F, yOffset + this.height / 2.0F + 0.5F, 1358954495);
      Iterator var5 = this.mc.theWorld.getLoadedEntityList().iterator();

      while(var5.hasNext()) {
         Object o = var5.next();
         if (o instanceof EntityPlayer) {
            EntityPlayer ent = (EntityPlayer)o;
            if (ent.isEntityAlive() && ent != this.mc.thePlayer && !ent.isInvisible() && !ent.isInvisibleToPlayer(this.mc.thePlayer)) {
               float pTicks = Wrapper.getTimer().renderPartialTicks;
               float posX = (float)((ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks - (double)playerOffsetX) * (double)((Double)this.scale.getValueState()).floatValue());
               float posZ = (float)((ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - (double)playerOffSetZ) * (double)((Double)this.scale.getValueState()).floatValue());
               int color = Teams.isOnSameTeam(ent) ? Colors.GREEN.c : Colors.RED.c;
               float cos = (float)Math.cos((double)this.mc.thePlayer.rotationYaw * 0.017453292519943295);
               float sin = (float)Math.sin((double)this.mc.thePlayer.rotationYaw * 0.017453292519943295);
               float rotY = -(posZ * cos - posX * sin);
               float rotX = -(posX * cos + posZ * sin);
               if (rotY > this.height / 2.0F - 5.0F) {
                  rotY = this.height / 2.0F - 5.0F;
               } else if (rotY < -(this.height / 2.0F) + 5.0F) {
                  rotY = -(this.height / 2.0F) + 5.0F;
               }

               if (rotX > this.width / 2.0F - 5.0F) {
                  rotX = this.width / 2.0F - 5.0F;
               } else if (rotX < -(this.width / 2.0F - 5.0F)) {
                  rotX = -(this.width / 2.0F - 5.0F);
               }

               RenderUtil.circle(xOffset + this.width / 2.0F + rotX, yOffset + this.height / 2.0F + rotY, 1.0F, color);
            }
         }
      }

   }
}
