package koks.module.player;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.event.BlockDamageEvent;
import koks.event.PacketEvent;
import koks.event.UpdateEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.EnumFacing;

@Module.Info(name = "FastBreak", description = "You break fast blocks", category = Module.Category.PLAYER)
public class FastBreak extends Module {

    @Value(name = "Mode", modes = {"Vanilla", "Intave13GommeCookies"})
    String mode = "Vanilla";

    @Value(name = "BreakTime", minimum = 0, maximum = 1)
    double breakTime = 0.2;

    @Value(name = "Packets", minimum = 5, maximum = 150)
    int packets = 100;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        return switch (name) {
            case "BreakTime" -> mode.equalsIgnoreCase("Vanilla");
            case "Packets" -> mode.equalsIgnoreCase("Intave13GommeCookies");
            default -> super.isVisible(value, name);
        };
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof BlockDamageEvent) {
            if (mode.equalsIgnoreCase("Vanilla")) {
                if (getPlayerController().curBlockDamageMP >= breakTime) {
                    getPlayerController().isHittingBlock = false;
                    sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit));
                    getPlayerController().onPlayerDestroyBlock(mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit);
                    getPlayerController().curBlockDamageMP = 0.0F;
                    getPlayerController().stepSoundTickCounter = 0.0F;
                    getPlayerController().blockHitDelay = 5;
                }
            }
        }
        if (event instanceof final PacketEvent packetEvent) {
            final Packet<?> packet = packetEvent.getPacket();
            if (packetEvent.getType() == PacketEvent.Type.SEND)
                if (packet instanceof final C07PacketPlayerDigging digging) {
                    if (mode.equalsIgnoreCase("Intave13GommeCookies"))
                        if (digging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                            for (int i = 0; i < packets; i++)
                                sendPacketUnlogged(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, digging.getPosition(), digging.getFacing()));
                        }
                }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
