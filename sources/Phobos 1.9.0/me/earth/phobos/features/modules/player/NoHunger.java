package me.earth.phobos.features.modules.player;

import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoHunger extends Module {
    public Setting<Boolean> cancelSprint = register(new Setting("CancelSprint", Boolean.valueOf(true)));

    public NoHunger() {
        super("NoHunger", "Prevents you from getting Hungry", Module.Category.PLAYER, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = event.getPacket();
            packet.onGround = (mc.player.fallDistance >= 0.0F || mc.playerController.isHittingBlock);
        }
        if (this.cancelSprint.getValue().booleanValue() && event.getPacket() instanceof CPacketEntityAction) {
            CPacketEntityAction packet = event.getPacket();
            if (packet.getAction() == CPacketEntityAction.Action.START_SPRINTING || packet.getAction() == CPacketEntityAction.Action.STOP_SPRINTING)
                event.setCanceled(true);
        }
    }
}
