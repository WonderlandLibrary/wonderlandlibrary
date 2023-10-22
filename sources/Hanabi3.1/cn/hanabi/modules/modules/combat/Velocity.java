package cn.hanabi.modules.modules.combat;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Wrapper;
import cn.hanabi.events.EventJump;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.gui.notifications.Notification;
import cn.hanabi.injection.interfaces.IS12PacketEntityVelocity;
import cn.hanabi.injection.interfaces.IS27PacketExplosion;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.ClientUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

@ObfuscationClass
public class Velocity extends Mod {
   public static final Value modes = (new Value("Velocity", "Mode", 0)).LoadValue(new String[]{"Cancel", "Packet", "AAC", "AAC4.4.0", "AAC4", "Intave", "RedeSky", "RedeSkyHVH", "RedeSkyPacket", "AAC5"});
   public Value x = new Value("Velocity", "Vertical", 0.0, -100.0, 100.0, 1.0);
   public Value y = new Value("Velocity", "Horizontal", 0.0, 0.0, 100.0, 1.0);
   public Value detect = new Value("Velocity", "Check KB", true);
   private final TimeHelper timer = new TimeHelper();
   private final TimeHelper calcTimer = new TimeHelper();
   private final TimeHelper knockBackTimer = new TimeHelper();
   boolean canVelo = false;
   boolean wtfBoolean = true;

   public Velocity() {
      super("Velocity", Category.COMBAT);
   }

   @EventTarget
   private void onPacket(EventPacket e) {
      S12PacketEntityVelocity veloPacket;
      if ((Boolean)this.detect.getValue() && e.getPacket() instanceof S12PacketEntityVelocity) {
         veloPacket = (S12PacketEntityVelocity)e.getPacket();
         if (veloPacket.getEntityID() != mc.thePlayer.getEntityId()) {
            return;
         }

         if (this.knockBackTimer.isDelayComplete(250L) && mc.thePlayer.ticksExisted > 60) {
            if (this.wtfBoolean && mc.thePlayer.hurtResistantTime == 0 && mc.thePlayer.velocityChanged) {
               ClientUtil.sendClientMessage("You may have been KB checked!", Notification.Type.WARNING);
               this.wtfBoolean = false;
               mc.thePlayer.addVelocity((double)veloPacket.getMotionX(), (double)veloPacket.getMotionY(), (double)veloPacket.getMotionZ());
            } else {
               this.wtfBoolean = false;
            }
         } else if (this.wtfBoolean && mc.thePlayer.hurtResistantTime > 0) {
            this.wtfBoolean = false;
         }

         e.setCancelled(true);
      }

      if (modes.isCurrentMode("Cancel")) {
         this.setDisplayName("Cancel");
         if (e.getPacket() instanceof S12PacketEntityVelocity || e.getPacket() instanceof S27PacketExplosion) {
            e.setCancelled(true);
         }
      } else if (modes.isCurrentMode("Packet")) {
         this.setDisplayName("Packet");
         if (e.getPacket() instanceof S12PacketEntityVelocity) {
            veloPacket = (S12PacketEntityVelocity)e.getPacket();
            if (veloPacket.getEntityID() == mc.thePlayer.getEntityId()) {
               ((IS12PacketEntityVelocity)veloPacket).setX((int)((double)veloPacket.getMotionX() * (Double)this.x.getValue() / 100.0));
               ((IS12PacketEntityVelocity)veloPacket).setY((int)((double)veloPacket.getMotionY() * (Double)this.y.getValue() / 100.0));
               ((IS12PacketEntityVelocity)veloPacket).setZ((int)((double)veloPacket.getMotionZ() * (Double)this.x.getValue() / 100.0));
            }
         }

         if (e.getPacket() instanceof S27PacketExplosion) {
            S27PacketExplosion packet = (S27PacketExplosion)e.getPacket();
            ((IS27PacketExplosion)packet).setX(packet.func_149149_c() * ((Double)this.x.getValue()).floatValue() / 100.0F);
            ((IS27PacketExplosion)packet).setY(packet.func_149144_d() * ((Double)this.y.getValue()).floatValue() / 100.0F);
            ((IS27PacketExplosion)packet).setZ(packet.func_149147_e() * ((Double)this.x.getValue()).floatValue() / 100.0F);
         }
      }

      Iterator var4;
      Entity entity;
      if (modes.isCurrentMode("RedeSky")) {
         veloPacket = null;
         if (e.getPacket() instanceof S12PacketEntityVelocity) {
            if (((S12PacketEntityVelocity)e.getPacket()).getEntityID() == mc.thePlayer.getEntityId()) {
               veloPacket = (S12PacketEntityVelocity)e.getPacket();
            }
         } else if (e.getPacket() instanceof S27PacketExplosion) {
            S27PacketExplosion explPacket = (S27PacketExplosion)e.getPacket();
            if (explPacket.func_149149_c() != 0.0F || explPacket.func_149144_d() != 0.0F || explPacket.func_149147_e() != 0.0F) {
               veloPacket = new S12PacketEntityVelocity(mc.thePlayer.getEntityId(), (double)explPacket.func_149149_c(), (double)explPacket.func_149144_d(), (double)explPacket.func_149147_e());
            }
         }

         if (veloPacket == null) {
            return;
         }

         boolean near = false;
         var4 = mc.theWorld.loadedEntityList.iterator();

         while(var4.hasNext()) {
            entity = (Entity)var4.next();
            if (!entity.equals(mc.thePlayer) && entity.getDistanceToEntity(mc.thePlayer) < 10.0F) {
               near = true;
               break;
            }
         }

         if (!near) {
            return;
         }

         if (veloPacket.getMotionX() == 0 && veloPacket.getMotionZ() == 0) {
            return;
         }

         e.setCancelled(true);
         if (mc.thePlayer.onGround) {
            mc.thePlayer.motionY = 0.0;
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.03, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
         }
      } else if (modes.isCurrentMode("RedeSkyHVH")) {
         if (e.getPacket() instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)e.getPacket()).getEntityID() == mc.thePlayer.getEntityId()) {
            Wrapper.getTimer().timerSpeed = 0.3F;
            this.canVelo = true;
            this.timer.reset();
         }
      } else if (modes.isCurrentMode("RedeSkyPacket") && e.getPacket() instanceof S12PacketEntityVelocity) {
         veloPacket = (S12PacketEntityVelocity)e.getPacket();
         if (veloPacket.getMotionY() <= 0 || veloPacket.getEntityID() != mc.thePlayer.getEntityId()) {
            return;
         }

         EntityLivingBase target = null;
         var4 = mc.theWorld.loadedEntityList.iterator();

         while(var4.hasNext()) {
            entity = (Entity)var4.next();
            if (entity instanceof EntityLivingBase && !entity.equals(mc.thePlayer)) {
               target = (EntityLivingBase)entity;
               break;
            }
         }

         if (target == null) {
            return;
         }

         mc.thePlayer.motionX = 0.0;
         mc.thePlayer.motionZ = 0.0;
         mc.thePlayer.motionY = (double)((float)veloPacket.getMotionY() / 8000.0F) * 1.0;
         e.setCancelled(true);
         if (this.timer.hasReached(500L)) {
            int count = 20;
            if (!this.timer.hasReached(800L)) {
               count = 5;
            } else if (!this.timer.hasReached(1200L)) {
               count = 8;
            }

            for(int i = 0; i < count; ++i) {
               mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
               mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
            }

            this.calcTimer.reset();
         }
      }

   }

   @EventTarget
   public void onPre(EventPreMotion e) {
      this.setDisplayName(modes.getModeAt(modes.getCurrentMode()));
      EntityPlayerSP var10000;
      if (modes.isCurrentMode("AAC4.4.0")) {
         this.setDisplayName("AAC4.4.0");
         if (!mc.thePlayer.onGround && mc.thePlayer.hurtResistantTime > 0) {
            var10000 = mc.thePlayer;
            var10000.motionX *= 0.6;
            var10000 = mc.thePlayer;
            var10000.motionZ *= 0.6;
         }
      } else if (modes.isCurrentMode("AAC4")) {
         this.setDisplayName("AAC4");
         if (mc.thePlayer.hurtTime == 9) {
            var10000 = mc.thePlayer;
            var10000.motionX *= 0.5;
            var10000 = mc.thePlayer;
            var10000.motionZ *= 0.5;
         }

         if (mc.thePlayer.hurtTime == 8) {
            var10000 = mc.thePlayer;
            var10000.motionX *= 0.4;
            var10000 = mc.thePlayer;
            var10000.motionZ *= 0.4;
         }

         if (mc.thePlayer.hurtTime == 7) {
            var10000 = mc.thePlayer;
            var10000.motionX *= 0.7;
            var10000 = mc.thePlayer;
            var10000.motionZ *= 0.7;
         }

         if (mc.thePlayer.hurtTime == 6) {
            var10000 = mc.thePlayer;
            var10000.motionX *= 0.3;
            var10000 = mc.thePlayer;
            var10000.motionZ *= 0.3;
         }

         if (mc.thePlayer.hurtTime == 5) {
            var10000 = mc.thePlayer;
            var10000.motionX *= 0.1;
            var10000 = mc.thePlayer;
            var10000.motionZ *= 0.1;
         }
      } else if (modes.isCurrentMode("Intave")) {
         this.setDisplayName("Intave");
         if (mc.thePlayer.hurtTime > 1 && mc.thePlayer.hurtTime < 10) {
            var10000 = mc.thePlayer;
            var10000.motionX *= 0.75;
            var10000 = mc.thePlayer;
            var10000.motionZ *= 0.75;
            if (mc.thePlayer.hurtTime < 4) {
               if (mc.thePlayer.motionY > 0.0) {
                  var10000 = mc.thePlayer;
                  var10000.motionY *= 0.9;
               } else {
                  var10000 = mc.thePlayer;
                  var10000.motionY *= 1.1;
               }
            }
         }
      } else if (modes.isCurrentMode("AAC")) {
         this.setDisplayName("AAC");
         if (mc.thePlayer.hurtTime != 0) {
            mc.thePlayer.onGround = true;
         }
      } else if (modes.isCurrentMode("RedeSkyHVH")) {
         if (this.timer.isDelayComplete(100L) && this.canVelo) {
            this.canVelo = false;
            Wrapper.getTimer().timerSpeed = 1.0F;
            var10000 = mc.thePlayer;
            var10000.motionX *= 0.6;
            var10000 = mc.thePlayer;
            var10000.motionZ *= 0.6;
         }
      } else if (modes.isCurrentMode("AAC5") && mc.thePlayer.hurtTime > 8) {
         var10000 = mc.thePlayer;
         var10000.motionX *= 0.6;
         var10000 = mc.thePlayer;
         var10000.motionZ *= 0.6;
      }

   }

   @EventTarget
   public void onJump(EventJump event) {
      boolean jump = true;
   }
}
