package me.aquavit.liquidsense.module.modules.ghost;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.EventType;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0BPacketEntityAction;

@ModuleInfo(name = "WTap", description = "Can make you combo entity", category = ModuleCategory.GHOST)
public class WTap extends Module {
    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getEventType() == EventType.RECEIVE && mc.theWorld != null && mc.thePlayer != null && event.getPacket() instanceof C02PacketUseEntity) {
            final C02PacketUseEntity c02PacketUseEntity = (C02PacketUseEntity)event.getPacket();
            if (c02PacketUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK &&
                    c02PacketUseEntity.getEntityFromWorld(mc.theWorld) != mc.thePlayer && mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
                if (mc.thePlayer.isSprinting()) {
                    mc.thePlayer.setSprinting(false);
                    mc.thePlayer.setSprinting(true);
                }
                mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            }
        }
    }
}
