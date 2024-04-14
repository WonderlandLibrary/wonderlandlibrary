package de.violence.module.modules.PLAYER;

import de.violence.friend.FriendManager;
import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.modules.OTHER.AntiBots;
import de.violence.module.modules.OTHER.Teams;
import de.violence.module.ui.Category;
import de.violence.ui.BlickWinkel3D;
import de.violence.ui.Location3D;
import de.violence.utils.RotationHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class BowAimbot extends Module {
   public VSetting sFov = new VSetting("Fov", this, 5.0D, 360.0D, 5.0D, false);
   public VSetting aimThroughBlocks = new VSetting("Aim Through Blocks", this, false);
   public VSetting silent = new VSetting("Silent", this, false);

   public BowAimbot() {
      super("BowAimbot", Category.PLAYER);
   }

   public void onFrameRender() {
      EntityPlayer target = this.getNearest();
      if(target != null) {
         Location3D from = new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.6D, this.mc.thePlayer.posZ);
         Location3D toNormal = new Location3D(target.posX, target.posY + 1.0D, target.posZ);
         BlickWinkel3D bl = new BlickWinkel3D(from, toNormal);
         float yaw = (float)Math.abs((double)MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw) - bl.getYaw());
         if((double)yaw <= this.sFov.getCurrent()) {
            if(this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() == Items.bow) {
               if(this.mc.thePlayer.getItemInUseDuration() >= 5) {
                  this.updateLook(target);
                  super.onFrameRender();
               }
            }
         }
      }
   }

   public void updateLook(Entity target) {
      Location3D from = new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.6D, this.mc.thePlayer.posZ);
      Location3D toNormal = new Location3D(target.posX, target.posY + 1.0D, target.posZ);
      double movesAmount = from.distance(toNormal) + 8.0D;
      movesAmount /= 3.0D;
      double xOffSet = (target.posX - target.prevPosX) * movesAmount;
      double zOffSet = (target.posZ - target.prevPosZ) * movesAmount;
      Location3D to = new Location3D(toNormal.getX() + xOffSet, toNormal.getY(), toNormal.getZ() + zOffSet);
      BlickWinkel3D bl = new BlickWinkel3D(from, to);
      double bowUse = (double)this.mc.thePlayer.getItemInUseDuration();
      if(bowUse >= 20.0D) {
         this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, this.mc.thePlayer.getPosition(), EnumFacing.DOWN));
      }

      double pitchOffSet = (Math.abs(bl.getPitch()) - 90.0D) * from.distance(to) / (bowUse * 10.0D) / 2.5D;
      if(bl.getPitch() >= -90.0D && bl.getPitch() <= 90.0D && bl.getYaw() <= 999.0D && bl.getYaw() >= -999.0D) {
         if(this.silent.isToggled()) {
            RotationHandler.server_yaw = (float)bl.getYaw();
            RotationHandler.server_pitch = (float)(bl.getPitch() + pitchOffSet);
         } else {
            this.mc.thePlayer.rotationYaw = (float)bl.getYaw();
            this.mc.thePlayer.rotationPitch = (float)(bl.getPitch() + pitchOffSet);
         }

      }
   }

   private EntityPlayer getNearest() {
      Location3D p = new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
      EntityPlayer best = null;
      double distance = 50.0D;

      for(int i = 0; i < this.mc.theWorld.playerEntities.size(); ++i) {
         EntityPlayer ep = (EntityPlayer)this.mc.theWorld.playerEntities.get(i);
         if(!ep.equals(this.mc.thePlayer) && (!Module.getByName("Teams").isToggled() || !Teams.isInTeam(ep)) && (!Module.getByName("ClientFriends").isToggled() || FriendManager.getAliasOf(ep.getName()) == null) && (this.aimThroughBlocks.isToggled() || this.mc.thePlayer.canEntityBeSeen(ep)) && (!Module.getByName("AntiBots").isToggled() || AntiBots.canHit(ep))) {
            Location3D epl = new Location3D(ep.posX, ep.posY, ep.posZ);
            double newDis = epl.distance(p);
            if(newDis <= distance) {
               distance = newDis;
               best = ep;
            }
         }
      }

      return best;
   }
}
