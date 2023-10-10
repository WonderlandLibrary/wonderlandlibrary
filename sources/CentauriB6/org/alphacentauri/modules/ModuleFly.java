package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventEntityHealthUpdate;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventPacketSend;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.MoveUtils;
import org.alphacentauri.management.util.Timer;
import org.alphacentauri.modules.ModuleDamage;

public class ModuleFly extends Module implements EventListener {
   public Property mode = new Property(this, "Mode", ModuleFly.Mode.Vanilla);
   private Property vanillaYSpeed = new Property(this, "VanillaYSpeed", Float.valueOf(0.3F));
   private Property vanillaFwdSpeed = new Property(this, "VanillaXZSpeed", Float.valueOf(0.5F));
   private Property aacYSet = new Property(this, "AAC1YValue", Double.valueOf(0.85D));
   private Property aacYSpeed = new Property(this, "AAC1SpeedValue", Float.valueOf(2.8F));
   private Property glideMotion = new Property(this, "GlideMotion", Float.valueOf(0.0F));
   private Property ncp2SpeedBoost = new Property(this, "NCP2SpeedBoost", Boolean.valueOf(false));
   private Timer timer = new Timer();
   private int counter = 0;

   public ModuleFly() {
      super("Fly", "Free like a bird", new String[]{"fly"}, Module.Category.Movement, 14659669);
   }

   public void setEnabledSilent(boolean enabled) {
      if(this.mode.value == ModuleFly.Mode.NCP2 && enabled) {
         Module dmg = AC.getModuleManager().get(ModuleDamage.class);
         AntiCheat org = dmg.getBypass();
         dmg.setBypass(AntiCheat.NCP);
         dmg.enable();
         dmg.setBypass(org);
      }

      super.setEnabledSilent(enabled);
   }

   public void setBypass(AntiCheat ac) {
      if(ac == AntiCheat.AAC) {
         this.mode.value = ModuleFly.Mode.AAC1;
      } else if(ac == AntiCheat.Vanilla) {
         this.mode.value = ModuleFly.Mode.Vanilla;
      }

      super.setBypass(ac);
   }

   public void onEvent(Event event) {
      if(this.mode.value == ModuleFly.Mode.AAC1) {
         if(event instanceof EventPacketSend) {
            if(AC.getMC().getPlayer().fallDistance > 4.0F && ((EventPacketSend)event).getPacket() instanceof C03PacketPlayer) {
               ((C03PacketPlayer)((EventPacketSend)event).getPacket()).onGround = true;
               AC.getMC().getPlayer().fallDistance = 0.0F;
               AC.getMC().getPlayer().motionY = ((Double)this.aacYSet.value).doubleValue();
            }
         } else if(event instanceof EventEntityHealthUpdate) {
            if(((EventEntityHealthUpdate)event).getEntity() instanceof EntityPlayerSP && ((EventEntityHealthUpdate)event).getHealthDiffrence() < 0.0F) {
               if(AC.getMC().getPlayer().onGround) {
                  return;
               }

               AC.getMC().getPlayer().hurtTime = AC.getMC().getPlayer().maxHurtTime = 25;
            }
         } else if(event instanceof EventTick) {
            if(!AC.getMC().getPlayer().onGround && !AC.getMC().getPlayer().isInLiquid()) {
               MoveUtils.setSpeed((double)(0.25F * (AC.getMC().getPlayer().hurtTime > 0?((Float)this.aacYSpeed.value).floatValue():1.0F)));
            }

            if(AC.getMC().getPlayer().fallDistance > 0.0F && (double)AC.getMC().getPlayer().fallDistance < 0.1D) {
               ;
            }
         }
      } else if(this.mode.value == ModuleFly.Mode.Gomme1) {
         if(event instanceof EventPacketSend) {
            if(AC.getMC().getPlayer().fallDistance > 5.0F && ((EventPacketSend)event).getPacket() instanceof C03PacketPlayer) {
               ((C03PacketPlayer)((EventPacketSend)event).getPacket()).onGround = true;
               AC.getMC().getPlayer().fallDistance = 0.0F;
               AC.getMC().getPlayer().motionY = ((Double)this.aacYSet.value).doubleValue();
            }
         } else if(event instanceof EventTick && !AC.getMC().getPlayer().onGround && !AC.getMC().getPlayer().isInLiquid()) {
            MoveUtils.setSpeed(0.25D);
         }
      } else if(this.mode.value == ModuleFly.Mode.Vanilla) {
         if(event instanceof EventTick) {
            GameSettings gameSettings = AC.getMC().gameSettings;
            float y = 0.0F;
            y = y + (gameSettings.keyBindJump.isKeyDown()?((Float)this.vanillaYSpeed.value).floatValue():0.0F);
            y = y - (gameSettings.keyBindSneak.isKeyDown()?((Float)this.vanillaYSpeed.value).floatValue():0.0F);
            if(this.getBypass() == AntiCheat.Spartan) {
               y *= AC.getRandom().nextFloat() / 10.0F + 0.9F;
            }

            AC.getMC().getPlayer().motionY = (double)y;
            MoveUtils.setSpeed((double)((Float)this.vanillaFwdSpeed.value).floatValue());
         }
      } else if(this.mode.value == ModuleFly.Mode.NCP1) {
         if(event instanceof EventTick && this.timer.hasMSPassed(485L)) {
            AC.getMC().getPlayer().jump();
            this.timer.reset();
         }
      } else if(this.mode.value == ModuleFly.Mode.NCP2) {
         if(event instanceof EventTick) {
            AC.getMC().getPlayer().motionY = (double)(-((Float)this.glideMotion.value).floatValue());
         } else if(event instanceof EventEntityHealthUpdate && ((Boolean)this.ncp2SpeedBoost.value).booleanValue() && ((EventEntityHealthUpdate)event).getEntity() == AC.getMC().getPlayer() && ((EventEntityHealthUpdate)event).getHealthDiffrence() < 0.0F) {
            MoveUtils.setSpeed(9.9D);
         }
      }

   }

   public static enum Mode {
      Vanilla,
      AAC1,
      NCP1,
      NCP2,
      Gomme1;
   }
}
