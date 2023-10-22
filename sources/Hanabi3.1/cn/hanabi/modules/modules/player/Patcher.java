package cn.hanabi.modules.modules.player;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Wrapper;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventTick;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.utils.random.Random;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook.EnumFlags;

@ObfuscationClass
public class Patcher extends Mod {
   private final Value mode = (new Value("Patcher", "Mode", 0)).LoadValue(new String[]{"Hypixel", "AACv4LessFlag"});
   private final Value pong = new Value("Patcher", "Pong", true);
   byte[] uuid = UUID.randomUUID().toString().getBytes();
   int count;
   private double x;
   private double y;
   private double z;
   float cacheYaw;
   float cachePitch;
   private final Queue queue = new ConcurrentLinkedDeque();
   private final LinkedList list = new LinkedList();
   private int bypassValue = 0;
   private long lastTransaction = 0L;
   int biqiling;
   private int lastUid;
   private boolean checkReset;
   private boolean active;
   public boolean hasDisable;
   public TimeHelper timer = new TimeHelper();
   private final TimeHelper brust = new TimeHelper();
   private final ArrayList packets = new ArrayList();
   long delay = 150L;
   int invalid = 0;
   private boolean collect = false;
   private int serverPosPacket = 0;
   private final TimeHelper generation = new TimeHelper();
   private final TimeHelper tick = new TimeHelper();
   private final TimeHelper collecttimer = new TimeHelper();
   private final TimeHelper choke = new TimeHelper();
   private final TimeHelper giga = new TimeHelper();

   public Patcher() {
      super("Patcher", Category.PLAYER);
   }

   @EventTarget
   public void onChangeWorld(EventWorldChange e) {
   }

   @EventTarget
   public void onPre(EventTick e) {
      if (this.mode.isCurrentMode("Hypixel")) {
         this.hypixel((EventPacket)null);
      }

   }

   @EventTarget
   public void onPreUpdate(EventPreMotion e) {
      if (this.mode.isCurrentMode("Hypixel")) {
         if (mc.isSingleplayer()) {
            return;
         }

         if (!this.hasDisable && this.timer.isDelayComplete(2000L)) {
            e.setX(1993634.696969697);
            e.setY(1993634.696969697);
            e.setZ(1993634.696969697);
            e.setOnGround(true);
         }
      }

   }

   @EventTarget
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (this.mode.isCurrentMode("AACv4LessFlag")) {
         if (packet instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packetS08 = (S08PacketPlayerPosLook)packet;
            double x = packetS08.getX() - mc.thePlayer.posX;
            double y = packetS08.getY() - mc.thePlayer.posY;
            double z = packetS08.getZ() - mc.thePlayer.posZ;
            double diff = Math.sqrt(x * x + y * y + z * z);
            if (diff <= 8.0) {
               event.setCancelled(true);
               mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(packetS08.getX(), packetS08.getY(), packetS08.getZ(), packetS08.getYaw(), packetS08.getPitch(), true));
            }
         }
      } else if (this.mode.isCurrentMode("HypixelSlime")) {
         if (event.getPacket() instanceof C03PacketPlayer) {
            PlayerCapabilities capabilities = new PlayerCapabilities();
            capabilities.disableDamage = false;
            capabilities.isFlying = true;
            capabilities.allowFlying = false;
            capabilities.isCreativeMode = false;
            mc.getNetHandler().addToSendQueue(new C13PacketPlayerAbilities(capabilities));
         }
      } else if (this.mode.isCurrentMode("Hypixel")) {
         this.hypixel(event);
      }

   }

   public void onEnable() {
      super.onEnable();
      this.uuid = UUID.randomUUID().toString().getBytes();
      this.list.clear();
      if (mc.thePlayer.ticksExisted > 1) {
         PlayerUtil.tellPlayer("Login again to disable watchdog.");
      }

   }

   public void onDisable() {
   }

   private void checkUidVaild(EventPacket event) {
      if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
         C0FPacketConfirmTransaction C0F = (C0FPacketConfirmTransaction)event.getPacket();
         int windowId = C0F.getWindowId();
         int uid = C0F.getUid();
         if (windowId == 0 && uid < 0) {
            int predictedUid = this.lastUid - 1;
            if (!this.checkReset) {
               if (uid == predictedUid) {
                  if (!this.active) {
                     this.active = true;
                  }
               } else {
                  this.active = false;
               }
            } else {
               if (uid != predictedUid) {
                  this.active = false;
               }

               this.checkReset = false;
            }

            this.lastUid = uid;
         }
      }

   }

   public void hypixel(EventPacket event) {
      if (!mc.isSingleplayer()) {
         if (event != null) {
            if (event.getPacket() instanceof S07PacketRespawn) {
               this.hasDisable = false;
               this.timer.reset();
            }

            if (event.getPacket() instanceof S08PacketPlayerPosLook && mc.thePlayer != null && mc.theWorld != null) {
               double d0 = ((S08PacketPlayerPosLook)event.getPacket()).getX();
               double d1 = ((S08PacketPlayerPosLook)event.getPacket()).getY();
               double d2 = ((S08PacketPlayerPosLook)event.getPacket()).getZ();
               float f = ((S08PacketPlayerPosLook)event.getPacket()).getYaw();
               float f1 = ((S08PacketPlayerPosLook)event.getPacket()).getPitch();
               if (((S08PacketPlayerPosLook)event.getPacket()).func_179834_f().contains(EnumFlags.X)) {
                  d0 += mc.thePlayer.posX;
               } else {
                  mc.thePlayer.motionX = 0.0;
               }

               if (((S08PacketPlayerPosLook)event.getPacket()).func_179834_f().contains(EnumFlags.Y)) {
                  d1 += mc.thePlayer.posY;
               } else {
                  mc.thePlayer.motionY = 0.0;
               }

               if (((S08PacketPlayerPosLook)event.getPacket()).func_179834_f().contains(EnumFlags.Z)) {
                  d2 += mc.thePlayer.posZ;
               } else {
                  mc.thePlayer.motionZ = 0.0;
               }

               this.x = ((S08PacketPlayerPosLook)event.getPacket()).getX();
               this.y = ((S08PacketPlayerPosLook)event.getPacket()).getY();
               this.z = ((S08PacketPlayerPosLook)event.getPacket()).getZ();
               mc.thePlayer.setPositionAndRotation(d0, d1, d2, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
               if (!this.hasDisable && this.timer.isDelayComplete(2000L)) {
                  mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(1.3371337696969E7, 1.3371337696969E7, 1.3371337696969E7, true));
               } else {
                  mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(((S08PacketPlayerPosLook)event.getPacket()).getX(), ((S08PacketPlayerPosLook)event.getPacket()).getY(), ((S08PacketPlayerPosLook)event.getPacket()).getZ(), ((S08PacketPlayerPosLook)event.getPacket()).getYaw(), ((S08PacketPlayerPosLook)event.getPacket()).getPitch(), false));
               }

               event.setCancelled(true);
            }

            if (event.getPacket() instanceof S00PacketKeepAlive && this.hasDisable) {
               this.packets.add(event.getPacket());
               event.setCancelled(true);
            }

            if (event.getPacket() instanceof S32PacketConfirmTransaction && ((S32PacketConfirmTransaction)event.getPacket()).getWindowId() == 0 && ((S32PacketConfirmTransaction)event.getPacket()).getActionNumber() < 0) {
               if (!this.hasDisable) {
                  this.hasDisable = true;
                  this.brust.reset();
                  this.invalid = 0;
               }

               this.packets.add(event.getPacket());
               event.setCancelled(true);
            }

            if (event.getPacket() instanceof C03PacketPlayer && !((C03PacketPlayer)event.getPacket()).isMoving() && !((C03PacketPlayer)event.getPacket()).getRotating()) {
               event.setCancelled(true);
            }
         } else {
            if (mc.thePlayer == null || mc.theWorld == null) {
               return;
            }

            if (mc.currentScreen instanceof GuiDownloadTerrain) {
               mc.thePlayer.closeScreen();
            }

            if (!this.brust.isDelayComplete(this.delay)) {
               return;
            }

            this.resetPackets(mc.getNetHandler());
            if (this.delay > 450L) {
               this.delay = 250L;
            } else {
               this.delay += 25L;
            }

            this.brust.reset();
         }

      }
   }

   private void resetPackets(INetHandlerPlayClient netHandler) {
      if (this.packets.size() > 0) {
         synchronized(this.packets) {
            for(; this.packets.size() != 0; this.packets.remove(this.packets.get(0))) {
               try {
                  ((Packet)this.packets.get(0)).processPacket(netHandler);
                  if (this.packets.get(0) instanceof S32PacketConfirmTransaction) {
                     if (this.invalid > 14) {
                        mc.getNetHandler().getNetworkManager().sendPacket(new C0FPacketConfirmTransaction(1, ((S32PacketConfirmTransaction)this.packets.get(0)).getActionNumber(), true));
                        this.invalid = 0;
                        this.delay = 50L;
                     }

                     ++this.invalid;
                  }
               } catch (Exception var5) {
               }
            }
         }
      }

   }

   public void base(EventPacket event) {
      if (!mc.isSingleplayer()) {
         if (event != null) {
            if (event.getPacket() instanceof S07PacketRespawn) {
               this.queue.clear();
               this.collect = false;
               this.checkReset = false;
            }

            if (event.getPacket() instanceof S08PacketPlayerPosLook && this.collect && this.giga.isDelayComplete(2000L)) {
               this.lastTransaction = 6L;
               event.setCancelled(true);
            }

            if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
               this.lastTransaction = System.currentTimeMillis();
               if (!this.checkReset) {
                  this.checkReset = true;
               }

               if (this.collect) {
                  this.queue.add(new TimestampedPacket(event.getPacket(), System.currentTimeMillis()));
                  event.setCancelled(true);
               }
            }

            if (event.getPacket() instanceof C00PacketKeepAlive && this.collect) {
               this.queue.add(new TimestampedPacket(event.getPacket(), System.currentTimeMillis()));
               event.setCancelled(true);
            }

            if (event.getPacket() instanceof C03PacketPlayer) {
               if (!((C03PacketPlayer)event.getPacket()).isMoving() && !((C03PacketPlayer)event.getPacket()).getRotating()) {
                  event.setCancelled(true);
               }

               if (!event.isCancelled() && (this.collect || !this.checkReset)) {
                  this.queue.add(new TimestampedPacket(event.getPacket(), System.currentTimeMillis()));
                  event.setCancelled(true);
               }
            }
         } else {
            if (this.lastTransaction > 0L) {
               return;
            }

            if (this.generation.isDelayComplete((long)(250 + Random.nextInt(40, 70)))) {
               while(!this.queue.isEmpty()) {
                  Wrapper.sendPacketNoEvent(((TimestampedPacket)this.queue.poll()).packet);
               }

               this.generation.reset();
               this.collect = false;
            }

            if (this.tick.isDelayComplete(10000L)) {
               this.collect = true;
               this.generation.reset();
               this.tick.reset();
            }
         }

      }
   }

   private static class TimestampedPacket {
      private final Packet packet;
      private final long timestamp;

      public TimestampedPacket(Packet packet, long timestamp) {
         this.packet = packet;
         this.timestamp = timestamp;
      }
   }
}
