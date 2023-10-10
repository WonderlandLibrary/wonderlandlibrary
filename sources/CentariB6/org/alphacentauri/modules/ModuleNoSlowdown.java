package org.alphacentauri.modules;

import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventBlockBB;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventPacketSend;
import org.alphacentauri.management.events.EventPacketSent;
import org.alphacentauri.management.events.EventSlowdown;
import org.alphacentauri.management.events.EventSlowdown$Cause;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.BlockUtils;
import org.alphacentauri.modules.ModuleSprint;

public class ModuleNoSlowdown extends Module implements EventListener {
   private HashMap values = new HashMap();
   private Property blockBypass = new Property(this, "BypassBlocking", Boolean.valueOf(true));
   private Property sneakBypass = new Property(this, "BypassSneaking", Boolean.valueOf(true));
   private Property webBypass = new Property(this, "BypassCobWeb", Boolean.valueOf(false));
   private Property aacBypass = new Property(this, "BypassAACUse", Boolean.valueOf(false));

   public ModuleNoSlowdown() {
      super("NoSlowdown", "No more getting slowed down.", new String[]{"noslow", "noslowdown"}, Module.Category.Movement, 9566910);

      for(EventSlowdown$Cause cause : EventSlowdown$Cause.values()) {
         this.values.put(cause, new Property(this, cause.name(), Float.valueOf(cause != EventSlowdown$Cause.Water && cause != EventSlowdown$Cause.Lava?1.0F:0.0F)));
      }

   }

   public void setBypass(AntiCheat ac) {
      super.setBypass(ac);
      if(ac == AntiCheat.NCP) {
         ((Property)this.values.get(EventSlowdown$Cause.SoulSand)).value = Float.valueOf(1.0F);
         ((Property)this.values.get(EventSlowdown$Cause.Using)).value = Float.valueOf(1.0F);
         ((Property)this.values.get(EventSlowdown$Cause.Blocking)).value = Float.valueOf(1.0F);
         ((Property)this.values.get(EventSlowdown$Cause.Sneaking)).value = Float.valueOf(1.0F);
         ((Property)this.values.get(EventSlowdown$Cause.CobWeb)).value = Float.valueOf(0.8F);
         this.blockBypass.value = Boolean.valueOf(true);
         this.sneakBypass.value = Boolean.valueOf(true);
         this.webBypass.value = Boolean.valueOf(false);
         this.aacBypass.value = Boolean.valueOf(false);
      } else if(ac == AntiCheat.Spartan) {
         ((Property)this.values.get(EventSlowdown$Cause.SoulSand)).value = Float.valueOf(1.0F);
         ((Property)this.values.get(EventSlowdown$Cause.Using)).value = Float.valueOf(1.0F);
         ((Property)this.values.get(EventSlowdown$Cause.Blocking)).value = Float.valueOf(1.0F);
         ((Property)this.values.get(EventSlowdown$Cause.Sneaking)).value = Float.valueOf(1.0F);
         ((Property)this.values.get(EventSlowdown$Cause.CobWeb)).value = Float.valueOf(1.1F);
         this.blockBypass.value = Boolean.valueOf(false);
         this.sneakBypass.value = Boolean.valueOf(true);
         this.webBypass.value = Boolean.valueOf(true);
         this.aacBypass.value = Boolean.valueOf(false);
         ((ModuleSprint)AC.getModuleManager().get(ModuleSprint.class)).fake.value = Boolean.valueOf(true);
      } else if(ac == AntiCheat.AAC) {
         ((Property)this.values.get(EventSlowdown$Cause.SoulSand)).value = Float.valueOf(1.0F);
         ((Property)this.values.get(EventSlowdown$Cause.Using)).value = Float.valueOf(1.0F);
         ((Property)this.values.get(EventSlowdown$Cause.Blocking)).value = Float.valueOf(1.0F);
         ((Property)this.values.get(EventSlowdown$Cause.Sneaking)).value = Float.valueOf(0.0F);
         ((Property)this.values.get(EventSlowdown$Cause.CobWeb)).value = Float.valueOf(0.15F);
         this.blockBypass.value = Boolean.valueOf(false);
         this.sneakBypass.value = Boolean.valueOf(false);
         this.webBypass.value = Boolean.valueOf(false);
         this.aacBypass.value = Boolean.valueOf(true);
      }

   }

   public void onEvent(Event event) {
      if(event instanceof EventSlowdown) {
         ((EventSlowdown)event).setNoSlowDown(((Float)((Property)this.values.get(((EventSlowdown)event).getCause())).value).floatValue());
      } else if(event instanceof EventPacketSend) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         if(((EventPacketSend)event).getPacket() instanceof C03PacketPlayer && ((C03PacketPlayer)((EventPacketSend)event).getPacket()).isMoving()) {
            if(((Boolean)this.blockBypass.value).booleanValue() && player.isBlocking()) {
               player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }

            if(((Boolean)this.sneakBypass.value).booleanValue() && player.isSneaking()) {
               player.sendQueue.addToSendQueue(new C0BPacketEntityAction(player, net.minecraft.network.play.client.C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
         }
      } else if(event instanceof EventPacketSent) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         if(((EventPacketSent)event).getPacket() instanceof C03PacketPlayer && ((C03PacketPlayer)((EventPacketSent)event).getPacket()).isMoving()) {
            if(((Boolean)this.blockBypass.value).booleanValue() && player.isBlocking()) {
               player.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(player.getHeldItem()));
            }

            if(((Boolean)this.sneakBypass.value).booleanValue() && player.isSneaking()) {
               player.sendQueue.addToSendQueue(new C0BPacketEntityAction(player, net.minecraft.network.play.client.C0BPacketEntityAction.Action.START_SNEAKING));
            }
         }
      } else if(event instanceof EventBlockBB) {
         if(!((Boolean)this.webBypass.value).booleanValue()) {
            return;
         }

         Block block = ((EventBlockBB)event).getBlock();
         BlockPos pos = ((EventBlockBB)event).getPos();
         if(block instanceof BlockWeb) {
            ((EventBlockBB)event).setBb(new AxisAlignedBB((double)pos.getX() + block.minX, (double)pos.getY() + block.minY, (double)pos.getZ() + block.minZ, (double)pos.getX() + block.maxX, (double)pos.getY() + block.maxY, (double)pos.getZ() + block.maxZ));
         }
      } else if(event instanceof EventTick) {
         if(((Boolean)this.webBypass.value).booleanValue()) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(BlockUtils.getBlock(-0.1F).getBlock() instanceof BlockWeb && player.onGround) {
               player.jump();
            }
         }

         if(((Boolean)this.aacBypass.value).booleanValue()) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(player.isUsingItem() && player.hasMovementInput()) {
               if(player.onGround) {
                  player.motionY = 0.42D;
               } else {
                  player.motionY -= 0.42D;
               }
            }
         }
      }

   }
}
