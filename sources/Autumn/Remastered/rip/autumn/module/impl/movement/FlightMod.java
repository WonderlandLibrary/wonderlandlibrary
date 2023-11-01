package rip.autumn.module.impl.movement;

import java.util.ArrayDeque;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S45PacketTitle;
import org.lwjgl.input.Keyboard;
import rip.autumn.annotations.Label;
import rip.autumn.core.Autumn;
import rip.autumn.events.packet.ReceivePacketEvent;
import rip.autumn.events.packet.SendPacketEvent;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.events.player.MoveEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Bind;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.module.option.impl.EnumOption;
import rip.autumn.notification.NotificationPublisher;
import rip.autumn.notification.NotificationType;
import rip.autumn.utils.MathUtils;
import rip.autumn.utils.MoveUtil;
import rip.autumn.utils.PlayerUtils;
import rip.autumn.utils.Stopwatch;
import rip.autumn.utils.pathfinding.CustomVec3;

@Bind("F")
@Label("Flight")
@Category(ModuleCategory.MOVEMENT)
@Aliases({"flight", "fly"})
public final class FlightMod extends Module {
   public final EnumOption mode;
   public final DoubleOption vanillaSpeed;
   private TargetStrafeMod targetStrafe;

   public FlightMod() {
      this.mode = new EnumOption("Mode", Mode.VANILLA);
      this.vanillaSpeed = new DoubleOption("Vanilla Speed", 5.0D, () -> {
         return this.mode.getValue() == FlightMod.Mode.VANILLA;
      }, 0.1D, 7.0D, 0.1D);
      this.setMode(this.mode);
      this.addOptions(this.mode, this.vanillaSpeed);
   }

   public static FlightMod getInstance() {
      return (FlightMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(FlightMod.class);
   }

   public void onEnable() {
      getPlayer().stepHeight = 0.0F;
      getPlayer().motionX = 0.0D;
      getPlayer().motionZ = 0.0D;
      if (this.targetStrafe == null) {
         this.targetStrafe = (TargetStrafeMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(TargetStrafeMod.class);
      }
   }

   public void onDisable() {
      mc.timer.timerSpeed = 1.0F;
      getPlayer().stepHeight = 0.625F;
      getPlayer().motionX = 0.0D;
      getPlayer().motionZ = 0.0D;
   }

   @Listener(MotionUpdateEvent.class)
   public void onMotionUpdate(MotionUpdateEvent event) {
      switch(mode.getValue().toString()) {
         case "VANILLA":
            mc.thePlayer.motionY = 0;

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
               mc.thePlayer.motionY = (double) vanillaSpeed.getValue();
            }

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
               mc.thePlayer.motionY = -(double)vanillaSpeed.getValue();
            }

            MoveUtil.setSpeed((Double) vanillaSpeed.getValue());
            break;
      }
   }

   public enum Mode {
      VANILLA;
   }
}
