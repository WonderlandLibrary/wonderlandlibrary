package koks.module.player;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.event.UpdateEvent;
import net.minecraft.network.play.client.C03PacketPlayer;

@Module.Info(name = "Regen", description = "You regenerate fast", category = Module.Category.PLAYER)
public class Regen extends Module {

    @Value(name = "Packets", minimum = 1, maximum = 100)
    int packets = 100;

    @Value(name = "inAir")
    boolean inAir = false;

    @Value(name = "Sneak")
    boolean sneak = false;

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof UpdateEvent) {
            if (getPlayer().getHealth() < getPlayer().getMaxHealth() && !getPlayer().isUsingItem() && (getPlayer().isSneaking() || !sneak) && (getPlayer().onGround || inAir) && !getPlayer().getFoodStats().needFood()) {
                for (int i = 0; i < packets; i++)
                    sendPacket(new C03PacketPlayer(true));
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
