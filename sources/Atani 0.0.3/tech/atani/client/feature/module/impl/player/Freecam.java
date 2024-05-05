package tech.atani.client.feature.module.impl.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.event.minecraft.player.movement.BlockPushEvent;
import tech.atani.client.listener.event.minecraft.world.CollisionBoxesEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.utility.interfaces.Methods;

@ModuleData(name = "Freecam", description = "Go through blocks", category = Category.PLAYER)
public class Freecam extends Module {

    @Listen
    public void onCollisionBoxes(CollisionBoxesEvent event) {
        if (Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
            return;

        event.setCancelled(true);
    }

    @Listen
    public void onPushBlocks(BlockPushEvent event) {
        if (Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
            return;

        event.setCancelled(true);
    }

    @Listen
    public void onPacket(PacketEvent event) {
        if(mc.thePlayer != null && mc.theWorld != null) {
            if(event.getPacket() instanceof C03PacketPlayer) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onEnable() {
        Methods.mc.thePlayer.capabilities.allowFlying = true;
        Methods.mc.thePlayer.capabilities.isFlying = true;
    }

    @Override
    public void onDisable() {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null) {
            return;
        }

        Methods.mc.thePlayer.capabilities.allowFlying = false;
        Methods.mc.thePlayer.capabilities.isFlying = false;
    }
}
