package today.flux.module.implement.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import today.flux.event.TickEvent;
import today.flux.gui.hud.notification.Notification;
import today.flux.gui.hud.notification.NotificationManager;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.utility.ChatUtils;

public class HackerDetector extends Module {
    public HackerDetector() {
        super("HackerDetector", Category.World, false);
    }

    @EventTarget
    public void onUpdate(TickEvent e) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityPlayer) {
                if (entity == mc.thePlayer) continue;
                EntityPlayer player = (EntityPlayer) entity;

                /*
                if (entity.getName().contains("Suspect")) {
                    double y = (player.posY - (int)player.posY);
                    System.out.println(y);
                }*/


                // All Direction Sprint
                if (player.getSpeed() >= player.getBaseMoveSpeed() * 0.95 && (player.moveForward < 0.0F || player.moveForward == 0.0F && player.moveStrafing != 0.0F)) {
                    player.detect.sprint++;
                    ChatUtils.debug(player.getName() + " Omni Sprint - VL:" + player.detect.sprint);
                }

                // No Slow Checks
                if (player.isUsingItem() && player.onGround && player.hurtTime == 0 && !player.isPotionActive(Potion.jump) && player.getSpeed() >= player.getBaseMoveSpeed() * 0.9 && !mc.thePlayer.getHeldItem().getDisplayName().toLowerCase().contains("bow")) {
                    player.detect.noSlow++;
                    ChatUtils.debug(player.getName() + " No Slow - VL:" + player.detect.noSlow);
                }

              /*  // Fly Checks
                boolean isNotMoving = MoveUtils.isOnGround(player, 0.01) || player.getSpeed() < 0.1;
                double yOffset = (player.posY - (int)player.posY);
                boolean isFlying = (Math.abs(yOffset - 0.40625)) < 0.0005 || (Math.abs(yOffset - 0.75)) < 0.0005 || (Math.abs(yOffset - 0.3125)) < 0.0005 || (Math.abs(yOffset - 0.65625)) < 0.0005;

                if (isFlying && !isNotMoving && player.getSpeed() > player.getBaseMoveSpeed() * 3) {
                    player.detect.flight++;
                    ChatUtils.debug(player.getName() + " Fly - VL:" + player.detect.flight);
                }*/

                if (player.detect.flight >= 3 && !player.detect.isFlying) {
                    NotificationManager.show("Hacker Detector", player.getName() + " - Fly!", Notification.Type.WARNING);
                    player.detect.isFlying = true;
                }

                if (player.detect.sprint >= 5 && !player.detect.allDirectionSprint) {
                    NotificationManager.show("Hacker Detector", player.getName() + " - Omni Sprint!", Notification.Type.WARNING);
                    player.detect.allDirectionSprint = true;
                }

                if (player.detect.noSlow >= 5 && !player.detect.isNoSlow) {
                    NotificationManager.show("Hacker Detector", player.getName() + " - No Slow!", Notification.Type.WARNING);
                    player.detect.isNoSlow = true;
                }
            }
        }
    }

    public static boolean isHacker(EntityPlayer player) {
        return player.detect.isFlying || player.detect.allDirectionSprint || player.detect.isNoSlow;
    }
}