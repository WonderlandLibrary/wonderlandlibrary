package cn.hanabi.modules.modules.player;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Wrapper;
import cn.hanabi.events.EventRender;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.MoveUtils;
import cn.hanabi.utils.NukerUtil;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.utils.pathfinder.PathUtils;
import cn.hanabi.utils.pathfinder.Vec3;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.BlockBed;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

@ObfuscationClass
public class TeleportBedFucker extends Mod {
   public BlockPos playerBed;
   public BlockPos fuckingBed;
   public ArrayList posList;
   public Value delay = new Value("TP2Bed", "Delay", 600.0, 200.0, 3000.0, 100.0);
   TimeHelper timer = new TimeHelper();
   private ArrayList path = new ArrayList();

   public TeleportBedFucker() {
      super("TP2Bed", Category.PLAYER);
   }

   public void onEnable() {
      try {
         this.posList = new ArrayList(NukerUtil.list);
         this.posList.sort((o1, o2) -> {
            double distance1 = this.getDistanceToBlock(o1);
            double distance2 = this.getDistanceToBlock(o2);
            return (int)(distance1 - distance2);
         });
         if (this.posList.size() < 3) {
            this.set(false);
         }

         ArrayList posListFor = new ArrayList(this.posList);
         int index = 1;
         Iterator var3 = posListFor.iterator();

         while(var3.hasNext()) {
            BlockPos kid = (BlockPos)var3.next();
            ++index;
            if (index % 2 == 1) {
               this.posList.remove(kid);
            }
         }

         this.playerBed = (BlockPos)this.posList.get(0);
         this.posList.remove(0);
         if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01)) {
            for(int i = 0; i < 49; ++i) {
               mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06249, mc.thePlayer.posZ, false));
               mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
            }

            mc.thePlayer.onGround = false;
            mc.thePlayer.jumpMovementFactor = 0.0F;
         }

         this.fuckingBed = (BlockPos)this.posList.get(0);
      } catch (Throwable var5) {
         this.set(false);
      }

   }

   @EventTarget
   public void onRender(EventRender e) {
   }

   protected void onDisable() {
      Wrapper.canSendMotionPacket = true;
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      Iterator var2 = this.posList.iterator();

      while(var2.hasNext()) {
         BlockPos pos = (BlockPos)var2.next();
         if (!(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockBed)) {
            PlayerUtil.tellPlayer("Destory!" + pos);
            this.posList.remove(pos);
            this.posList.sort((o1, o2) -> {
               double distance1 = this.getDistanceToBlock(o1);
               double distance2 = this.getDistanceToBlock(o2);
               return (int)(distance1 - distance2);
            });
            this.fuckingBed = (BlockPos)this.posList.get(0);
         }
      }

      if (mc.thePlayer.getDistance((double)this.fuckingBed.getX(), (double)this.fuckingBed.getY(), (double)this.fuckingBed.getZ()) < 4.0) {
         Wrapper.canSendMotionPacket = true;
         PlayerUtil.tellPlayer("Teleported! :3");
         this.set(false);
      }

      if (this.timer.isDelayComplete((Double)this.delay.getValueState())) {
         Vec3 topFrom = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
         Vec3 to = new Vec3((double)(this.fuckingBed.getX() + 1), (double)this.fuckingBed.getY(), (double)(this.fuckingBed.getZ() + 1));
         this.path = PathUtils.computePath(topFrom, to);
         if (mc.thePlayer.getDistance((double)this.fuckingBed.getX(), (double)this.fuckingBed.getY(), (double)this.fuckingBed.getZ()) > 4.0) {
            PlayerUtil.tellPlayer("Trying to teleport...");
            Wrapper.canSendMotionPacket = false;
            Iterator var4 = this.path.iterator();

            while(var4.hasNext()) {
               Vec3 pathElm = (Vec3)var4.next();
               mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
            }
         }

         this.timer.reset();
      }

      if (this.posList.size() == 0) {
         this.set(false);
      }

   }

   public double getDistanceToBlock(BlockPos pos) {
      return mc.thePlayer.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
   }
}
