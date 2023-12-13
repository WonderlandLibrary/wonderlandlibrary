package cn.hanabi.modules.modules.player;

import cn.hanabi.events.EventPacket;
import cn.hanabi.injection.interfaces.IKeyBinding;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.TimeHelper;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import java.util.ArrayList;
import java.util.Iterator;
import javax.vecmath.Vector3f;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class Blink extends Mod {
   TimeHelper time = new TimeHelper();
   ArrayList packets = new ArrayList();

   public Blink() {
      super("Blink", Category.PLAYER);
      this.setState(false);
   }

   @EventTarget
   public void onPacket(EventPacket event) {
      if (event.getEventType() == EventType.SEND) {
         if (event.getPacket() instanceof C03PacketPlayer) {
            this.packets.add(event.getPacket());
            event.setCancelled(true);
         } else if (event.getPacket() instanceof C08PacketPlayerBlockPlacement || event.getPacket() instanceof C07PacketPlayerDigging || event.getPacket() instanceof C09PacketHeldItemChange || event.getPacket() instanceof C02PacketUseEntity) {
            this.packets.add(event.getPacket());
            event.setCancelled(true);
         }
      }

   }

   private void addPosition() {
      double x = mc.thePlayer.posX;
      double y = mc.thePlayer.posY;
      double z = mc.thePlayer.posZ;
      new Vector3f((float)x, (float)y, (float)z);
      if (mc.thePlayer.movementInput.moveForward == 0.0F && !((IKeyBinding)mc.gameSettings.keyBindJump).getPress() && mc.thePlayer.movementInput.moveStrafe != 0.0F) {
      }

   }

   public void onEnable() {
      EventManager.register(this);
      if (mc.thePlayer != null && mc.theWorld != null) {
         double x = mc.thePlayer.posX;
         double y = mc.thePlayer.posY;
         double z = mc.thePlayer.posZ;
         float yaw = mc.thePlayer.rotationYaw;
         float pitch = mc.thePlayer.rotationPitch;
         EntityOtherPlayerMP ent = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
         ent.inventory = mc.thePlayer.inventory;
         ent.inventoryContainer = mc.thePlayer.inventoryContainer;
         ent.setPositionAndRotation(x, y, z, yaw, pitch);
         ent.rotationYawHead = mc.thePlayer.rotationYawHead;
         mc.theWorld.addEntityToWorld(-1, ent);
      }

      this.packets.clear();
   }

   public void onDisable() {
      EventManager.unregister(this);
      mc.theWorld.removeEntityFromWorld(-1);
      Iterator var1 = this.packets.iterator();

      while(var1.hasNext()) {
         Packet packet = (Packet)var1.next();
         mc.thePlayer.sendQueue.addToSendQueue(packet);
         this.time.reset();
      }

      this.packets.clear();
   }
}
