package ru.smertnix.celestial.feature.impl.movement;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.feature.impl.combat.AttackAura;
import ru.smertnix.celestial.feature.impl.hud.Hud;
import ru.smertnix.celestial.ui.notification.NotificationMode;
import ru.smertnix.celestial.ui.notification.NotificationRenderer;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.math.TimerHelper;
import ru.smertnix.celestial.utils.movement.MovementUtils;
import ru.smertnix.celestial.utils.other.ChatUtils;

public class Speed extends Feature {
    public static float ticks = 0;
    public TimerHelper timerHelper = new TimerHelper();
    public static final ListSetting speedMode = new ListSetting("Mode", "Really World", () -> true, "Really World", "ReallyWorld2", "Matrix Damage", "Sunrise Damage");

    public Speed() {
        super("Speed", "Позволяет вам ускоряться", FeatureCategory.Movement);
        addSettings(speedMode);

    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = speedMode.getOptions();
       if (mode.equalsIgnoreCase("Really World")) {
    	   if (mc.player.isInWeb || mc.player.isOnLadder() || mc.player.isInLiquid()) {
               return;
           }

           if (mc.player.onGround && !mc.gameSettings.keyBindJump.pressed) {
               mc.player.jump();
           }
           if (mc.player.ticksExisted % 3 == 0) {
               mc.timer.timerSpeed = 1.3f;
           } else {
               mc.timer.timerSpeed = 1.f;
           }
           if (mc.player.motionY == -0.4448259643949201D) {
               mc.player.jumpMovementFactor = 0.05F;
               if(mc.player.ticksExisted % 2 == 0) {
                  mc.player.motionX *= 2.D;
                   mc.player.motionZ *= 2.D;
               } else {
                   MovementUtils.setMotion(MovementUtils.getSpeed() * 1 + (0.22f));

               }
           }
        } else if (mode.equalsIgnoreCase("ReallyWorld2")) {
    	   if (mc.player.isInWeb || mc.player.isOnLadder() || mc.player.isInLiquid()) {
               return;
           }
    	   BlockPos feet = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
           BlockPos newPos = new BlockPos(feet.getX(),feet.getY() - 0.1, feet.getZ());
           if (mc.world.getBlockState(newPos).getBlock() instanceof BlockAir) {
        	   mc.timer.timerSpeed = 1F;
               return;
           }
    	   if (!MovementUtils.isMoving()) {
    		   mc.timer.timerSpeed = 1F;
               return;
           }
    	   if (MovementUtils.isMoving()) {
    		   if (mc.player.onGround) {
    		   mc.player.jump();
    		   }
    		   if (mc.player.fallDistance <= 0.22) {
    		   mc.timer.timerSpeed = 1.5f;
    		   mc.player.jumpMovementFactor = 0.026523f;
    		   } else if ((double) mc.player.fallDistance < 1.25) {
    		   mc.timer.timerSpeed = 0.94f;
    		   } 
    	   }
        } else {
            if (MovementUtils.isMoving()) {
                if (mc.player.onGround) {
                    mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 9.5D / 24.5D, 0, Math.cos(MovementUtils.getAllDirection()) * 9.5D / 24.5D);
                    MovementUtils.strafe();
                } else if (mc.player.isInWater()) {
                    mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 9.5D / 24.5D, 0, Math.cos(MovementUtils.getAllDirection()) * 9.5D / 24.5D);
                    MovementUtils.strafe();
                } else if (!mc.player.onGround) {
                    mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 0.31D / 24.5D, 0, Math.cos(MovementUtils.getAllDirection()) * 0.31D / 24.5D);
                    MovementUtils.strafe();
                } else {
                    mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 0.005D * MovementUtils.getSpeed(), 0, Math.cos(MovementUtils.getAllDirection()) * 0.005 * MovementUtils.getSpeed());
                    MovementUtils.strafe();

                }
            }
        }
    }

    private int findarmor() {
        for (int i = 0; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getStack() != null && mc.player.inventoryContainer.getSlot(i).getStack().getUnlocalizedName().contains("chestplate")) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}