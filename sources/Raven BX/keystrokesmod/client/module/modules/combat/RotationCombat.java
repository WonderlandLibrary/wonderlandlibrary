package keystrokesmod.client.module.modules.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class RotationCombat {
    private boolean isAttacking = false;

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            EntityPlayer player = event.player;

            if (player.isSwingInProgress) {
                // Player is in combat/hitting an enemy
                isAttacking = true;
            } else {
                isAttacking = false;
            }

            if (isAttacking) {
                // Increase player motion rotation
                handleCombatMotion(player);
            }
        }
    }
//if you are reading this . idk how the source got leaked but just know this iam very alone right now and i hope one day
//someone will read this just know this . it doesnt matter if you are black or white right or left just be a good person
    // world needs more people like you ;) dont forget me
    private void handleCombatMotion(EntityPlayer player) {
        // Rotate the player's motion by 45 degrees to the left and right
        float rotationYaw = player.rotationYaw;

        // Rotate right (clockwise)
        float rightRotation = rotationYaw - 45.0f;
        if (rightRotation < 0.0f) {
            rightRotation += 360.0f;
        }
        player.rotationYaw = rightRotation;

        // Rotate left (counterclockwise)
        float leftRotation = rotationYaw + 45.0f;
        if (leftRotation >= 360.0f) {
            leftRotation -= 360.0f;
        }
        player.rotationYaw = leftRotation;
    }
}
